package it.mahd.taxiadmin.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by salem on 04/05/16.
 */
public class Controllers {
    //public static final String url = "http://10.0.2.2:4004";
    public static final String url = "http://192.168.1.3:4004";
    public static final String url_login = url + "/loginAdmin";
    public static final String url_getAllTaxi = url + "/getAllTaxi";
    public static final String url_getOneTaxi = url + "/getOneTaxi";
    public static final String url_getAllPublicity = url + "/getAllPublicity";
    public static final String url_getOnePublicity = url + "/getOnePublicity";
    public static final String url_getAllMark = url + "/getAllMark";
    public static final String url_addTaxi = url + "/addTaxi";
    public static final String url_deleteTaxi = url + "/deleteTaxi";
    public static final String url_addPublicity = url + "/addPublicity";
    public static final String url_deletePublicity = url + "/deletePublicity";
    public static final String url_getAllService = url + "/getAllService";
    public static final String url_addService = url + "/addService";
    public static final String url_getAllReclamationAdmin = url + "/getAllReclamationAdmin";
    public static final String url_getAllMessage = url + "/getAllMessage";
    public static final String url_addMessageAdmin = url + "/addMessageAdmin";
    public static final String url_getAllBookNow = url + "/getAllBookNow";
    public static final String url_getAllBookAdvance = url + "/getAllBookAdvance";
    public static final String url_getAllClients = url + "/getAllClients";
    public static final String url_getAllDrivers = url + "/getAllDrivers";

    public static final String app = "AppTaxiAdmin";
    public static final String res = "res";
    public static final String response = "response";

    public static final String tag_key = "key";
    public static final String tag_id = "_id";
    public static final String tag_tokenDriver = "tokenDriver";
    public static final String tag_tokenClient = "tokenClient";
    public static final String tag_token = "token";
    public static final String tag_username = "username";
    public static final String tag_name = "name";
    public static final String tag_fname = "fname";
    public static final String tag_lname = "lname";
    public static final String tag_picture = "picture";
    public static final String tag_email = "email";
    public static final String tag_password = "password";
    public static final String tag_gender = "gender";
    public static final String tag_dateN = "dateN";
    public static final String tag_country = "country";
    public static final String tag_city = "city";
    public static final String tag_phone = "phone";
    public static final String tag_pt = "pt";
    public static final String tag_ptt = "ptt";
    public static final String tag_subject = "subject";
    public static final String tag_message = "message";
    public static final String tag_date = "date";
    public static final String tag_status = "status";
    public static final String tag_sender = "sender";
    public static final String tag_latitude = "latitude";
    public static final String tag_longitude = "longitude";
    public static final String tag_repeat = "repeat";
    public static final String tag_mon = "mon";
    public static final String tag_tue = "tue";
    public static final String tag_wed = "wed";
    public static final String tag_thu = "thu";
    public static final String tag_fri = "fri";
    public static final String tag_sat = "sat";
    public static final String tag_sun = "sun";
    public static final String tag_description = "description";
    public static final String tag_working = "working";
    public static final String tag_originLatitude = "originLatitude";
    public static final String tag_originLongitude = "originLongitude";
    public static final String tag_desLatitude = "desLatitude";
    public static final String tag_desLongitude = "desLongitude";
    //public static final String tag_validRoute = "validRoute";
    public static final String tag_stopBook = "stopBook";
    public static final String tag_pcourse = "pcourse";
    public static final String tag_ptake = "ptake";
    public static final String tag_preturn = "preturn";
    public static final String tag_mark = "mark";
    public static final String tag_model = "model";
    public static final String tag_serial = "serial";
    public static final String tag_places = "places";
    public static final String tag_luggages = "luggages";
    public static final String tag_period = "period";
    public static final String tag_price = "price";
    public static final String tag_category = "category";
    public static final String tag_value = "value";
    public static final String tag_me = "me";
    public static final String tag_nameClient = "nameClient";
    public static final String tag_nameDriver = "nameDriver";
    public static final String tag_dateBook = "dateBook";
    public static final String tag_notify = "notify";

    public Controllers() {}

    public boolean NetworkIsAvailable(Context cx) {
        ConnectivityManager manager = (ConnectivityManager) cx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }
}
