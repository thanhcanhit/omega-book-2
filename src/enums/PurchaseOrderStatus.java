/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author Như Tâm
 */
public enum PurchaseOrderStatus {

    /**
     *
     */
    PENDING(0), SUCCESS(1), CANCEL(2);
    
    private final int value;

    private PurchaseOrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
