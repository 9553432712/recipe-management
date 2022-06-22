
    create table ingredient (
       id bigint not null,
        name varchar(255),
        primary key (id)
    );

    create table recipe (
       id bigint not null,
        is_veg boolean,
        name varchar(255),
        serving_count bigint,
        sufficient_for integer not null,
        primary key (id)
    );

    create table recipe_ingredient (
       ingredient_id bigint not null,
        recipe_id bigint not null,
        quantity varchar(255),
        primary key (ingredient_id, recipe_id)
    );

create sequence hibernate_sequence start with 1 increment by 1;

    alter table recipe_ingredient
       add constraint FKgu1oxq7mbcgkx5dah6o8geirh
       foreign key (recipe_id)
       references recipe;

    alter table recipe_ingredient
       add constraint FK9b3oxoskt0chwqxge0cnlkc29
       foreign key (ingredient_id)
       references ingredient;