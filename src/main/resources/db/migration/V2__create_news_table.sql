create table news
(
    id          bigserial not null,
    user_id     uuid,
    description varchar(255),
    image       varchar(255),
    title       varchar(255),
    primary key (id)
);
alter table if exists news
    add constraint news_user_fk
    foreign key (user_id) references users;