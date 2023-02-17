package com.storage.storagestoresocks.models.clothes;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Socks extends Clothes {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Socks socks = (Socks) o;
        return cotton == socks.cotton && size == socks.size && color == socks.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, color, cotton);
    }
}
