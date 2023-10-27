/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author thanhcanhit
 */
public enum BookOrigin {
    LOCAL(1),
    FOREIGN(2);

    private final int value;

    private BookOrigin(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
