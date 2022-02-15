create table incorrect_answer
(
    id          bigint not null,
    answer      varchar(255),
    question_id bigint not null,
    primary key (id)
) engine=InnoDB;


create table question
(
    id             bigint not null,
    correct_answer integer,
    difficulty     varchar(255),
    question       varchar(255),
    primary key (id)
) engine=InnoDB;

alter table incorrect_answer
    add constraint FKl1mrjtkar4tmxa1rso17s19c1 foreign key (question_id) references question (id);