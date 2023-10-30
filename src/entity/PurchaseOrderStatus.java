
package entity;

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
