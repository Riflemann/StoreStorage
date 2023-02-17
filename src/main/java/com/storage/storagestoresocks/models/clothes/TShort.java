package com.storage.storagestoresocks.models.clothes;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class TShort extends clothes {

    int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TShort tShort = (TShort) o;
        return cotton == tShort.cotton && size == tShort.size && color == tShort.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, color, cotton);
    }
}
