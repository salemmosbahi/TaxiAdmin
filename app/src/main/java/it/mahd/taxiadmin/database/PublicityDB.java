package it.mahd.taxiadmin.database;

/**
 * Created by salem on 05/05/16.
 */
public class PublicityDB {
    private String id, name, category, price, period, date;

    public PublicityDB(String id, String name, String category, String price, String period, String date) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.period = period;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
