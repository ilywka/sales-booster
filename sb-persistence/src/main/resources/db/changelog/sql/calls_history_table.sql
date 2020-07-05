create table calls_history
(
    id bigserial PRIMARY KEY,
    number varchar(50) not null,
    type varchar(50) not null,
    duration_seconds integer not null
);