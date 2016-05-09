package it.mahd.taxiadmin.database;

/**
 * Created by salem on 09/05/16.
 */
public class ServiceDB {
    private String name;
    private int value;

    public ServiceDB(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
