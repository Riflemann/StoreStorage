-- delete from Clothes_rep;

insert into transactions_rep (typeTransaction,
                              typeClothes,
                              brand_clothes,
                              createTime,
                              clothesQuantity)
values ('INCOMING', 'SOCKS', 'Nike', '2023-03-02', 1500);

insert into Clothes_rep (transactions_id, type_Clothes, brand, model, gender, size, color, cotton, quantity)
values (1, 'SOCKS', 'Nike', 'test', 'MALE','SIZE_M', 'BLUE', 50, 500);
insert into Clothes_rep (transactions_id, type_Clothes, brand, model, gender, size, color, cotton, quantity)
values (1, 'SOCKS', 'Nike', 'test', 'FEMALE','SIZE_S', 'BLUE', 50, 500);
insert into Clothes_rep (transactions_id, type_Clothes, brand, model, gender, size, color, cotton, quantity)
values (1, 'SOCKS', 'Nike', 'test', 'UNISEX','SIZE_L', 'BLUE', 50, 500);


