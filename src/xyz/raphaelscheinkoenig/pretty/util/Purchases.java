package xyz.raphaelscheinkoenig.pretty.util;


public enum Purchases {

    TWO_HOMES(0), FIVE_HOMES(1);

    private int ID;

    Purchases(int ID) {
        this.ID = ID;
    }

    public final int getID() {
        return ID;
    }

    public static Purchases getPurchase(int buyId) {
        for(Purchases p : Purchases.values()) {
            if(p.getID() == buyId)
                return p;
        }
        return null;
    }
}
