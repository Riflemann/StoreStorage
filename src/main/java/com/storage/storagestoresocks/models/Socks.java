package com.storage.storagestoresocks.models;

import lombok.Data;

@Data
public class Socks {

    Size size;

    Color color;

    int cotton;



    public enum Size {

        SIZE_S(36.0),

        SIZE_M(38.0),

        SIZE_L(40.0);

        final double size;

        Size(double size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return "Размер носков: " + size;
        }
    }

    public enum Color {

        GREEN("Зеленый"),

        BLUE("Голубой"),

        WHITE("Белый");

        final String color;

        Color(String s) {
            this.color = s;
        }

        @Override
        public String toString() {
            return "Цвет носков: " + color;
        }
    }
}
