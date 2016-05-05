package it.mahd.taxiadmin.database;

/**
 * Created by salem on 04/05/16.
 */
public class TaxiDB {
    private String idTaxi, mark, model, serial, places, luggages, date;

    public TaxiDB(String idTaxi, String mark, String model, String serial, String places, String luggages, String date) {
        this.idTaxi = idTaxi;
        this.mark = mark;
        this.model = model;
        this.serial = serial;
        this.places = places;
        this.luggages = luggages;
        this.date = date;
    }

    public String getIdTaxi() {
        return idTaxi;
    }

    public String getMark() {
        return mark;
    }

    public String getModel() {
        return model;
    }

    public String getSerial() {
        return serial;
    }

    public String getPlaces() {
        return places;
    }

    public String getLuggages() {
        return luggages;
    }

    public String getDate() {
        return date;
    }

    public void setIdTaxi(String idTaxi) {
        this.idTaxi = idTaxi;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public void setLuggages(String luggages) {
        this.luggages = luggages;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
