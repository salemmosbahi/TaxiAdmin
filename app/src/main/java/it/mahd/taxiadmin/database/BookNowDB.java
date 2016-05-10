package it.mahd.taxiadmin.database;

/**
 * Created by salem on 09/05/16.
 */
public class BookNowDB {
    private String id, nameClient, nameDriver, value, date;

    public BookNowDB(String id, String nameClient, String nameDriver, String value, String date) {
        this.id = id;
        this.nameClient = nameClient;
        this.nameDriver = nameDriver;
        this.value = value;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getNameDriver() {
        return nameDriver;
    }

    public void setNameDriver(String nameDriver) {
        this.nameDriver = nameDriver;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
