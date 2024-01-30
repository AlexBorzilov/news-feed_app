create table logs
(
    id         bigserial     not null,
    method     varchar(10),
    uri        varchar(255),
    status     int,
    exception  varchar(255),
    created    timestamp(6)  not null,
    primary key (id)
);