create table news_tags
(
    news_entity_id bigint not null,
    tags_id        bigint not null,
    primary key (news_entity_id, tags_id)
);
alter table if exists news_tags
    add constraint news_tags_news_fk
    foreign key (news_entity_id) references news;
alter table if exists news_tags
    add constraint news_tags_tag_fk
    foreign key (tags_id) references tags;