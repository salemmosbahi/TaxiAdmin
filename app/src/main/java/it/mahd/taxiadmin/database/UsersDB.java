package it.mahd.taxiadmin.database;

/**
 * Created by salem on 10/05/16.
 */
public class UsersDB {
    private String id, name, city, date, account;

    public UsersDB(String id, String name, String city, String date, String account) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.date = date;
        this.account = account;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
