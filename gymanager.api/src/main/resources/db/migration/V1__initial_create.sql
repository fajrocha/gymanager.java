create table if not exists "admins"
(
    "id" uuid not null
        constraint "PK_admins"
            primary key,
    "user_id" uuid not null,
    "subscription_id" uuid
);

create table if not exists "gyms"
(
    "id" uuid not null
        constraint "PK_gyms"
            primary key,
    "name" text not null,
    "subscription_id" uuid not null,
    "max_rooms" integer not null,
    "room_ids" uuid[] not null,
    "trainer_ids" uuid[] not null
);

create table if not exists "subscriptions"
(
    "id" uuid not null
        constraint "PK_subscriptions"
            primary key,
    "subscription_type" text not null,
    "admin_id" uuid not null,
    "gym_ids" uuid[] not null,
    "max_gyms" integer not null
);

create table if not exists "users"
(
    "id" uuid not null
        constraint "PK_users"
            primary key,
    "first_name" text not null,
    "last_name" text not null,
    "email" text not null,
    "admin_id" uuid,
    "trainer_id" uuid,
    "participant_id" uuid,
    "password_hash" text not null
);