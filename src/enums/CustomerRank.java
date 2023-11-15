/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author Như Tâm
 */
public enum CustomerRank {
    NOTHING(0),
    SLIVER(1),
    GOLD(2),
    DIAMOND(3);
    
    private final int value;

    private CustomerRank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public boolean compare(int value) {
        return this.value == value;
    }

    public static CustomerRank fromInt(int value) {
        for (CustomerRank rank : values()) {
            if (rank.compare(value)) {
                return rank;
            }
        }
        return null;
    }
    
}
