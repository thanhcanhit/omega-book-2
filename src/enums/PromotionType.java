/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author KienTran
 */
public enum PromotionType {
    PERCENT(0), PRICE(1);
    
    private final int value;

    private PromotionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public boolean compare(int value) {
        return this.value == value;
    }

    public static PromotionType fromInt(int value) {
        for (PromotionType type : values()) {
            if (type.compare(value)) {
                return type;
            }
        }
        return null;
    }
}
