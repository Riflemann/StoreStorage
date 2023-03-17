package com.storage.storagestoresocks.models.clothes.enums;

public enum TypeClothes {

    TSHIRT("Футболка"),
    SHIRTS("Рубашка"),
    SWEATSHIRT("Толстовка"),
    BLAZER("Пиджак"),
    SOCKS("Носки"),
    SHORTS("Шорты");

    final String typeClothes;

    TypeClothes(String typeClothes) {
        this.typeClothes = typeClothes;
    }
}
