package com.storage.storagestoresocks.models;

import com.storage.storagestoresocks.models.clothes.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    TypeTransaction typeTransaction;
    String typeClothes;
    String brandClothes;
    LocalDateTime createTime;
    int clothesQuantity;

}
