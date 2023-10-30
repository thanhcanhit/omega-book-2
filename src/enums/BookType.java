/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author thanhcanhit
 */
public enum BookType {
    LOCAL(1),
    FOREIGN(2);

    private final int value;

    private BookType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean compare(int value) {
        return this.value == value;
    }

    public static BookType fromInt(int value) {
        for (BookType type : values()) {
            if (type.compare(value)) {
                return type;
            }
        }
        return null;
    }
}
