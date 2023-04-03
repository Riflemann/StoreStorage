create table if not exists transactions_rep
(
    id              integer AUTO_INCREMENT PRIMARY KEY,
    typeTransaction varchar   not null,
    typeClothes     varchar   not null,
    brand_clothes   varchar   ,
    createTime      timestamp not null,
    clothesQuantity integer   not null
);
create table if not exists Clothes_rep
(
    id              integer AUTO_INCREMENT PRIMARY KEY,
    transactions_id int     not null references transactions_rep (id),
    type_Clothes    varchar not null,
    brand           varchar ,
    model           varchar,
    gender          varchar ,
    size            varchar not null,
    color           varchar not null,
    cotton          integer,
    quantity        integer not null
);


