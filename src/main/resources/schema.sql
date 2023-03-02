
create table if not exists transactions_rep
(
    id           integer PRIMARY KEY,
    typeTransaction varchar not null,
    typeClothes         varchar not null,
    createTime        timestamp not null,
    clothesQuantity       integer not null
);
create table if not exists Clothes_rep
(
    id           integer AUTO_INCREMENT PRIMARY KEY,
    transactions_id int not null references transactions_rep(id),
    type_Clothes varchar not null,
    size         varchar not null,
    color        varchar not null,
    cotton       integer not null,
    quantity     integer not null
);


