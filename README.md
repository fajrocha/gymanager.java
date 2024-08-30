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

# Architecture 🧱

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

# Eventual Consistency 🔔

[TBD]