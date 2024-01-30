create table logs
(
    id      bigserial    not null,
    log     varchar(1000) not null,
    created timestamp(6) not null,
    primary key (id)
);