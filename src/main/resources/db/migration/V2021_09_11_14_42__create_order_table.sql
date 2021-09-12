create table buyer_order
(
    id                    bigint AUTO_INCREMENT PRIMARY KEY,
    user_id               bigint                             not null,
    acceptance_order_time datetime null,
    status                varchar(100)                       not null,
    created_time          datetime default CURRENT_TIMESTAMP null,
    updated_time          datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create table buyer_order_item
(
    id                    bigint AUTO_INCREMENT PRIMARY KEY,
    order_id              bigint                             not null,
    food_preparation_time int                                not null,
    price                 decimal(18, 6)                     not null,
    quantity              int                                not null,
    created_time          datetime default CURRENT_TIMESTAMP null,
    updated_time          datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create table message_history
(
    id           bigint AUTO_INCREMENT PRIMARY KEY,
    type         varchar(50)                        not null,
    content      varchar(100)                       not null,
    status       varchar(50)                        not null,
    created_time datetime default CURRENT_TIMESTAMP null,
    updated_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);
