create table if not exists Clothes_rep (
    id integer AUTO_INCREMENT PRIMARY KEY,
    type_Clothes varchar not null,
    size varchar not null,
    color varchar not null,
    cotton integer  not null,
    quantity integer not null
);
