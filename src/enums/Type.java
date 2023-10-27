/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author thanhcanhit
 */
public enum Type {
    BOOK(1),
    STATIONERY(2);

    private final int value;

    private Type(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
