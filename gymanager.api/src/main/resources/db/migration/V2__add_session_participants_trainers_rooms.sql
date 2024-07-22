create table if not exists "rooms"
(
    "id" uuid not null
        constraint "PK_rooms"
            primary key,
    "name" text not null,
    "gym_id" uuid not null,
    "max_daily_sessions" integer not null,
    "session_ids_by_date" jsonb not null,
    "schedule_calendar" jsonb,
    "schedule_id" uuid
);

create table if not exists "sessions"
(
    "id" uuid not null
        constraint "PK_sessions"
            primary key,
    "session_category" text not null,
    "date" date not null,
    "time_start" time not null,
    "time_end" time not null,
    "name" text not null,
    "description" text not null,
    "max_participants" integer not null,
    "room_id" uuid not null,
    "trainer_id" uuid not null
);

create table if not exists "session_reservations"
(
    "id" uuid not null
        constraint "PK_session_reservations"
            primary key,
    "session_id" uuid not null
        constraint "FK_session_reservations_sessions_id"
            references "sessions"
            on delete cascade,
    "participant_id" uuid not null
);

create index if not exists "IX_session_reservations_session_id"
    on "session_reservations" ("session_id");

create table if not exists "trainers"
(
    "id" uuid not null
        constraint "PK_trainers"
            primary key,
    "user_id" uuid not null,
    "session_ids" uuid[] not null,
    "schedule_calendar" jsonb,
    "schedule_id" uuid
);

create table if not exists "participants"
(
    "id" uuid not null
        constraint "PK_participants"
            primary key,
    "user_id" uuid not null,
    "session_ids" uuid[] not null,
    "schedule_calendar" text,
    "schedule_id" uuid
);
