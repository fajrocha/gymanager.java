# Gymanager 💪

# Introduction

# Ubiquitous Language 💬
## Admins

- A `user` can `create` an `admin profile`.
- An `admin` can have an `active subscription`.
- An `active subscription` can be of type `Free`, `Starter` or `Pro`.
- A `subscription` can have multiple and a `maximum number` of `gyms` depending on the `active subscription` type.

## Trainers

- A `user` can `create` a `trainer profile`.
- A `trainer` can `teach` sessions across `gyms` and `subscriptions`. 

## Participants

- A `user` can `create` a `participant profile`.
- `Participants` can `reserve` a `spot` in a `session`.
- A `session` takes place in a `room`.
- A `session` has a single `trainer` and a `maximum number` of `participants`.
- A `gym` can have multiple `rooms`.

# Invariants 📝

## Sessions

- A `session` cannot contain more than the maximum number of `participants`.

## Gyms

- A `gym` cannot have more `rooms` than the `subscription` allows.

## Rooms

- A `room` cannot have more `sessions` than the `subscription` allows.
- A `room` cannot have two or more overlapping sessions.

## Subscriptions

- A `subscription` cannot have more `gyms` than the `subscription` allows.

## Trainers

- A `trainer` cannot `teach` overlapping `sessions`.

## Participants

- A `participant` cannot `reserve` overlapping `sessions`.

## Admins

- An `admin` cannot have more than one `active subscription`.