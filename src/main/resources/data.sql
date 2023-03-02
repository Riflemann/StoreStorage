-- delete from Clothes_rep;

insert into transactions_rep (id,
                              typeTransaction,
                              typeClothes,
                              createTime,
                              clothesQuantity
                              )
values (1, 'INCOMING', 'SOCKS', '2023-03-02',1500);

insert into Clothes_rep (transactions_id, type_Clothes, size, color, cotton, quantity)
values (1, 'SOCKS', 'SIZE_M', 'BLUE', 50, 500);
insert into Clothes_rep (transactions_id, type_Clothes, size, color, cotton, quantity)
values (1, 'SOCKS', 'SIZE_M', 'BLUE', 50, 500);
insert into Clothes_rep (transactions_id, type_Clothes, size, color, cotton, quantity)
values (1, 'SOCKS', 'SIZE_M', 'BLUE', 50, 500);


