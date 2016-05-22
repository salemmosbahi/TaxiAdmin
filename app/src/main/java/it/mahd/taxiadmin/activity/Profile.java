package it.mahd.taxiadmin.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.mahd.taxiadmin.Main;
import it.mahd.taxiadmin.R;
import it.mahd.taxiadmin.util.Calculator;
import it.mahd.taxiadmin.util.Controllers;
import it.mahd.taxiadmin.util.ServerRequest;

/**
 * Created by salem on 2/13/16.
 */
public class Profile extends Fragment {
    SharedPreferences pref;
    ServerRequest sr = new ServerRequest();
    Controllers conf = new Controllers();

    private TextView Username_txt, City_txt, Age_txt, Email_txt, Phone_txt;
    private TextView DateN_txt;
    private EditText Phone_etxt;
    private ImageView Picture_iv;

    private String id, account, fname, lname, gender, dateN, country, city, email, phone, picture;


    public Profile() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile, container, false);
        ((Main) getActivity()).getSupportActionBar().setTitle(getString(R.string.profile));

        pref = getActivity().getSharedPreferences(conf.app, Context.MODE_PRIVATE);
        id = getArguments().getString(conf.tag_id);
        account = getArguments().getString("account");

        Username_txt = (TextView) rootView.findViewById(R.id.username_txt);
        Age_txt = (TextView) rootView.findViewById(R.id.age_txt);
        City_txt = (TextView) rootView.findViewById(R.id.city_txt);
        Email_txt = (TextView) rootView.findViewById(R.id.email_txt);
        Phone_txt = (TextView) rootView.findViewById(R.id.phone_txt);
        Picture_iv = (ImageView) rootView.findViewById(R.id.picture_iv);

        if(conf.NetworkIsAvailable(getActivity())){
            findUser();
        }else{
            Toast.makeText(getActivity(), R.string.networkunvalid, Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    public void findUser() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(conf.tag_id, id));
        params.add(new BasicNameValuePair("account", account));
        JSONObject json = sr.getJSON(conf.url_profileAdmin, params);
        if(json != null){
            try{
                if(json.getBoolean("res")) {
                    fname = json.getString(conf.tag_fname);
                    lname = json.getString(conf.tag_lname);
                    gender = json.getString(conf.tag_gender);
                    dateN = json.getString(conf.tag_dateN);
                    country = json.getString(conf.tag_country);
                    city = json.getString(conf.tag_city);
                    email = json.getString(conf.tag_email);
                    phone = json.getString(conf.tag_phone);
                    picture = json.getString(conf.tag_picture);
                    Username_txt.setText(fname + " " + lname);
                    int[] tab = new Calculator().getAge(dateN);
                    Age_txt.setText(tab[0] + "years, " + tab[1] + "month, " + tab[2] + "day");
                    City_txt.setText(gender + " from " + country + ", lives in " + city);
                    Email_txt.setText(email);
                    Phone_txt.setText(phone);
                    if (picture.equals("")) {
                        Picture_iv.setBackgroundResource(R.mipmap.ic_profile_p);
                    } else {
                        byte[] imageAsBytes = Base64.decode(picture.getBytes(), Base64.DEFAULT);
                        Picture_iv.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), R.string.serverunvalid, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_body, new Users());
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
