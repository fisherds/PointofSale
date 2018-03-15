package edu.rosehulman.fisher.pointofsale;

import java.text.DateFormat;
import java.util.GregorianCalendar;

/**
 * Created by boutell on 11/14/2015.
 */
public class Item {
    public String name;
    public int quantity;
    public GregorianCalendar deliveryDate;

    public Item() {
        name = "Nothing";
        quantity = 0;
        deliveryDate = new GregorianCalendar();
    }

    public Item(String name, int quantity, GregorianCalendar deliveryDate) {
        this.name = name;
        this.quantity = quantity;
        this.deliveryDate = deliveryDate;
    }

    public static Item getDefaultItem() {
        return new Item("Earplugs", 5, new GregorianCalendar());
    }

    public String getDeliveryDateString() {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(deliveryDate.getTime());
                // DateFormat.getDateInstance().format(deliveryDate);
    }

    public long getDeliveryDateTime() {
        return deliveryDate.getTime().getTime();
    }
}
