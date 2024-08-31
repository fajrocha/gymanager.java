# Gymanager

# Introduction

Small project I started to mess around both with _Java_ and _Domain Driven Design_ (DDD) concepts. Shout out to 
[Amichai Mantinbad's DDD course](https://dometrain.com/course/getting-started-domain-driven-design-ddd/), where I 
learnt some of the concepts applied here.

It is a basic _REST API_ which would aim to provide the backend for a gym manager service where users could 
register their gyms as gym owners. Then, other users could participate as either trainers or 
participants of the various gym training sessions. For storage a simple _PostgreSQL DB_ is used.

# Framework and key libraries

- [Spring Boot 3](https://spring.io/projects/spring-boot);
- [Spring Modulith](https://spring.io/projects/spring-modulith);
- [PipelinR](https://github.com/sizovs/PipelinR), very similar to the _.NET_ famous
[MediatR](https://github.com/jbogard/MediatR) which I use quite a lot (since I do mainly _.NET_ at work) to implement 
a CQRS(ish) pattern where each request is separated in its own **command/query** and respective **handler**.
- [jOOQ](https://www.jooq.org/) for data access.
- [Flyway](https://github.com/flyway/flyway) for database migrations.
- [Spock Framework](https://spockframework.org/) for unit and integration testing.
- [Test Containers](https://testcontainers.com/) for integration testing.

# The Ubiquitous Language 💬

## Users

- A `user` can `create` an `admin profile`.
- An `admin` can have an `subscription` on the platform by `subscribing`.
- A `user` can `create` a `trainer profile`.
- A `trainer` can `add` `sessions` across several `gyms`.
- A `user` can `create` a `participant profile`.
- A `participant` can make a `session reservation` to participate in a `session`.
- A `participant` can `cancel` their `session reservation`.

## Subscriptions

- A `subscription` can be of type `Free`, `Starter` or `Pro`.
- A `subscription` has a `maximum number` of `gyms`, `rooms` and `sessions` depending on the `subscription` type.

## Gyms

- A `gym` can have multiple `rooms`.
- A `gym` has a `subscription` attached which will define how many `rooms` it can have. 
- A `gym` can `support` multiple `session categories` (like pilates, functional).
- A `gym` can have multiple `trainers`.

## Rooms

- Several `sessions` can be `reserved` in a `room` by `trainers`.
- A `room` has a maximum number of `daily sessions` depending on the `subscription`.

## Sessions

- `Trainer` can add a `session` for a given `gym`. 
- There should be only one `trainer` for a given `session`.
- `Sessions` should have a `maximum number` of `participants` set by the `trainer`.
- `Session` have a `category` (like pilates or functional), which must be available on the `gym` where they are added.
- Participants can make a `session reservation` to participate.

# Invariants 📝

## Users

### Admins 

- An `admin` cannot have more than one `active subscription`.

### Trainers

- A `trainer` cannot `reserve` overlapping `sessions`.

### Participants

- A `participant` cannot `reserve` overlapping `sessions`.

## Subscriptions

- A `subscription` cannot have more `gyms` than the `subscription` allows:
    - `Free`: 1.
    - `Starter`: 3.
    - `Pro`: Infinite.
- A `subscription` cannot allow more `rooms` than the `subscription` allows:
    - `Free`: 3.
    - `Starter`: 10.
    - `Pro`: Infinite.
- A `subscription` cannot allow more `sessions` than the `subscription` allows:
    - `Free`: 3.
    - `Starter`: 10.
    - `Pro`: Infinite.

## Gyms

- A `gym` cannot have more `rooms` than the `subscription` allows.
- A `gym` only support certain `session categories`.

## Rooms

- A `room` cannot have more `sessions` than the `subscription` allows.
- A `room` cannot have two or more overlapping `sessions`.

## Sessions

- A `session` cannot contain more than the maximum number of `participants` set by the `trainer`.
- A `session reservation` cannot be `cancelled` less than 24h before the `session` `start time`.

# Bounded Contexts 🚧

With the several **domain models** identified, the solution was divided into 3 **bounded contexts**:
- **User Management**: responsible for the `users` management (registration, login), which can be `trainers`, 
`participants` and/or `admins`.
- **Gym Management**: responsible for `admins`, `subscriptions`, `gyms` management and communicating any downstream 
request of `room` to the **Session Management** bounded context.
- **Session Management**: management of `rooms`, `sessions`, `trainers`, `participants` and `session reservations`. It
should have a `gym` model only to fetch the supported `session categories` supported by a gym where the `trainer`
intends to add a `session`.

![screenshot](./docs/resources/bounded_contexts.png)

# Design Choices 🧱

## Architecture 

Each **bounded context** is separated on its own **package**. Any shared code is under a `common` **package**.

Each **bounded context** follows the _Clean Architecture_. Some main points:
- **API**: presentation layer of all **bounded contexts**, with the controllers and any middleware like exception 
handlers.
- **Application**: application logic resides here. Hard rule to not mention any implementations, 
the data could come virtually from anywhere, so no data access/ORM libraries references (like _JPA_, _JDBC_ or in this 
case _jOOQ_). All abstractions (`gateways`) of the **Infrastructure** layer are defined with interfaces here (but no
implementations!) and should only reference **Domain** layer models.
- **Infrastructure**: implementations of the abstractions (`gateways`) defined in the **Application** layer, mainly 
repositories. Here resides the references to data access/ORM libraries, in this case _jOOQ_. Other stuff like the 
token generation services (for security) implementations are also defined here.
- **Domain**: all business logic resides here and by extension all **domain models**. An effort was made to make them 
**rich domain models** to encapsulate the business logic on the model.

## Eventual Consistency

Instead of a **transactional consistency** for each request, an **eventual consistency** approach was implemented. 
Meaning, when something interesting from a business perspective occurs, instead of updating all relevant entities in a
single transaction, the response is given to the client immediately and further changes will occur on the background.

While this brings further complexity, it improves performance and allows to customize what happens when certain changes
within and between the **bounded contexts** fail. Any failed event will go to the database and will be republished 
either on restart or on a scheduled job. To simplify, _Spring Modulith_ out of the box features were used.

Mainly two type of events are used, **domain events** (within the **bounded context**) and **integration events**
(between **bounded contexts**).

### Domain Events

An example of a **domain event** is when adding a `gym`: 

- The request should cause the `gym` to be added to the `subscription`. If it succeeds, it returns a `201` code 
response to the client right away, and it will trigger an **event**.

This **event** should lead to (on the background):

- The `gym` is created in whatever external dependency (in this case a DB). 

So the happy path would look something like this:

![screenshot](./docs/resources/create_gym_happy.png)

For errors, either it would fail right away on the `subscription` update, returning a `500` code response (it would not
trigger an event):

![screenshot](./docs/resources/create_gym_error_request.png)

or the `subscription` updates but the `gym` is not created, in which case it will retry periodically:

![screenshot](./docs/resources/create_gym_error_events.png)

### Integration Events

An example of an **integration event** would be when creating a new `session`. When a `session` is created:

- The request should cause the respective `room` schedule to be updated. If it succeeds it would return a `201` code 
response right away, and it should trigger an **event**. 

This **event** should lead to (all on the background):

- The new `session` should be created (within the **Session Management** bounded context).
- The `trainer` schedule should be updated with the new `session` (within the **Session Management** bounded context).
- The `gym` should be updated with the `trainer`, in case the `trainer` is not registered there (notification from 
the **Session Management** to the **Gym Management** bounded context).

Since there is an interaction between the **Session Management** and the **Gym Management** bounded contexts this could 
be considered an **integration event**.

It would look something like below. Note that the event handlers are parallel, they should not wait on 
one another like the sequence diagram would indicate (perhaps a different diagram type is warranted here!).

![screenshot](./docs/resources/create_session_request.png)

