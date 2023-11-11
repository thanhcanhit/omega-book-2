/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author Như Tâm
 */
public enum PromotionRankCustomer {
    NOTHING(0),
    SLIVER(1),
    GOLD(2),
    DIAMOND(3);
    
    private final int value;

    private PromotionRankCustomer(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public boolean compare(int value) {
        return this.value == value;
    }

    public static PromotionRankCustomer fromInt(int value) {
        for (PromotionRankCustomer rank : values()) {
            if (rank.compare(value)) {
                return rank;
            }
        }
        return null;
    }
    
}
