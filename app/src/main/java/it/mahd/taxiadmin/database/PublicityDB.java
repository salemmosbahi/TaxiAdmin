package it.mahd.taxiadmin.database;

/**
 * Created by salem on 05/05/16.
 */
public class PublicityDB {
    private String id, name, period, price, date;

    public PublicityDB(String id, String name, String period, String price, String date) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.price = price;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPeriod() {
        return period;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
