package it.mahd.taxiadmin.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.mahd.taxiadmin.Main;
import it.mahd.taxiadmin.R;
import it.mahd.taxiadmin.database.TaxiAdapterList;
import it.mahd.taxiadmin.database.TaxiDB;
import it.mahd.taxiadmin.util.Controllers;
import it.mahd.taxiadmin.util.ServerRequest;

/**
 * Created by salem on 04/05/16.
 */
public class Taxi extends Fragment {
    SharedPreferences pref;
    Controllers conf = new Controllers();
    ServerRequest sr = new ServerRequest();

    private EditText Search_etxt;
    private TextInputLayout Search_input;
    private ListView lv;
    ArrayList<TaxiDB> taxiDBList;
    JSONArray loads = null;

    public Taxi() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.taxi, container, false);
        ((Main) getActivity()).getSupportActionBar().setTitle(getString(R.string.taxi));
        pref = getActivity().getSharedPreferences(conf.app, Context.MODE_PRIVATE);

        Search_input = (TextInputLayout) rootView.findViewById(R.id.Search_input);
        Search_etxt = (EditText) rootView.findViewById(R.id.Search_etxt);
        lv = (ListView) rootView.findViewById(R.id.listTaxi);
        lv.setTextFilterEnabled(true);

        FloatingActionButton AddTaxi_btn = (FloatingActionButton) rootView.findViewById(R.id.Add_btn);
        AddTaxi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container_body, new TaxiAdd());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        FloatingActionButton SearchTaxi_btn = (FloatingActionButton) rootView.findViewById(R.id.Search_btn);
        SearchTaxi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Search_etxt.getText().toString().equals("")) {
                    getTaxi();
                } else {
                    getOneTaxi();
                }
            }
        });

        if (conf.NetworkIsAvailable(getActivity())) {
            getTaxi();
        } else {
            Toast.makeText(getActivity(), R.string.networkunvalid, Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    private void getOneTaxi() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(conf.tag_serial, Search_etxt.getText().toString()));
        taxiDBList = new ArrayList<>();
        JSONObject json = sr.getJSON(conf.url_getOneTaxi, params);
        if(json != null){
            try{
                if(json.getBoolean(conf.res)) {
                    loads = json.getJSONArray("data");
                    if(loads.length() != 0){
                        for (int i = 0; i < loads.length(); i++) {
                            JSONObject c = loads.getJSONObject(i);
                            String idTaxi = c.getString(conf.tag_id);
                            String mark = c.getString(conf.tag_mark);
                            String model = c.getString(conf.tag_model);
                            String serial = c.getString(conf.tag_serial);
                            String places = String.valueOf(c.getInt(conf.tag_places));
                            String luggages = String.valueOf(c.getString(conf.tag_luggages));
                            String date = c.getString(conf.tag_date);
                            String pub = String.valueOf(c.getString(conf.tag_pub));
                            String dateExp = c.getString(conf.tag_dateExp);
                            TaxiDB taxi = new TaxiDB(idTaxi, mark, model, serial, places, luggages, date, pub, dateExp);
                            taxiDBList.add(taxi);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        TaxiAdapterList adapter = new TaxiAdapterList(getActivity(), taxiDBList, Taxi.this);
        lv.setAdapter(adapter);
    }

    private void getTaxi() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        taxiDBList = new ArrayList<>();
        JSONObject json = sr.getJSON(conf.url_getAllTaxi, params);
        if(json != null){
            try{
                if(json.getBoolean(conf.res)) {
                    loads = json.getJSONArray("data");
                    if(loads.length() != 0){
                        for (int i = 0; i < loads.length(); i++) {
                            JSONObject c = loads.getJSONObject(i);
                            String idTaxi = c.getString(conf.tag_id);
                            String mark = c.getString(conf.tag_mark);
                            String model = c.getString(conf.tag_model);
                            String serial = c.getString(conf.tag_serial);
                            String places = String.valueOf(c.getInt(conf.tag_places));
                            String luggages = String.valueOf(c.getString(conf.tag_luggages));
                            String date = c.getString(conf.tag_date);
                            String pub = String.valueOf(c.getString(conf.tag_pub));
                            String dateExp = c.getString(conf.tag_dateExp);
                            TaxiDB taxi = new TaxiDB(idTaxi, mark, model, serial, places, luggages, date, pub, dateExp);
                            taxiDBList.add(taxi);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        TaxiAdapterList adapter = new TaxiAdapterList(getActivity(), taxiDBList, Taxi.this);
        lv.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_body, new Home());
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
