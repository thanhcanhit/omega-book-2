/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author thanhcanhit
 */
public enum StationeryType {
    PEN(1),
    SCHOOL_SUPPLIES(2),
    OFFICE_SUPPLIES(3),
    DRAWING_TOOLS(4),
    PAPER_PRODUCTS(5),
    OTHER_PRODUCTS(6);

    private final int value;

    private StationeryType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
