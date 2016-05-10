package it.mahd.taxiadmin.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.mahd.taxiadmin.Main;
import it.mahd.taxiadmin.R;
import it.mahd.taxiadmin.database.BookNowAdapterList;
import it.mahd.taxiadmin.database.BookNowDB;
import it.mahd.taxiadmin.database.UsersAdapterList;
import it.mahd.taxiadmin.database.UsersDB;
import it.mahd.taxiadmin.util.Calculator;
import it.mahd.taxiadmin.util.Controllers;
import it.mahd.taxiadmin.util.ServerRequest;

/**
 * Created by salem on 05/05/16.
 */
public class Users extends Fragment {
    SharedPreferences pref;
    Controllers conf = new Controllers();
    ServerRequest sr = new ServerRequest();

    private Spinner Choice_sp;
    private ListView BookList_lv;

    ArrayList<UsersDB> booknowDBList;
    private String type[] = {"Clients", "Drivers"};
    JSONArray loads = null;

    public Users() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book, container, false);
        ((Main) getActivity()).getSupportActionBar().setTitle(getString(R.string.users));
        pref = getActivity().getSharedPreferences(conf.app, Context.MODE_PRIVATE);

        BookList_lv = (ListView) v.findViewById(R.id.listBook);
        Choice_sp = (Spinner) v.findViewById(R.id.Choice_sp);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, type); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Choice_sp.setAdapter(spinnerArrayAdapter);

        Choice_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Choice_sp.getSelectedItem().toString().equals("Clients")) {
                    getClients();
                } else if (Choice_sp.getSelectedItem().toString().equals("Drivers")) {
                    getDrivers();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getClients();

        return v;
    }

    private void getClients() {
        if(conf.NetworkIsAvailable(getActivity())){
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            booknowDBList = new ArrayList<>();
            JSONObject json = sr.getJSON(conf.url_getAllClients, params);
            if(json != null){
                try{
                    if(json.getBoolean("res")) {
                        loads = json.getJSONArray("data");
                        for (int i = 0; i < loads.length(); i++) {
                            JSONObject c = loads.getJSONObject(i);
                            String id = c.getString(conf.tag_id);
                            String name = c.getString(conf.tag_fname) + " " + c.getString(conf.tag_lname);
                            String city = c.getString(conf.tag_city);
                            int[] tab = new Calculator().getAge(c.getString(conf.tag_dateN));
                            String date = tab[0] + "years, " + tab[1] + "month, " + tab[2] + "day";
                            UsersDB rec = new UsersDB(id, name, city, date);
                            booknowDBList.add(rec);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            UsersAdapterList adapter = new UsersAdapterList(getActivity(), booknowDBList, Users.this);
            BookList_lv.setAdapter(adapter);
        }else{
            Toast.makeText(getActivity(), R.string.networkunvalid, Toast.LENGTH_SHORT).show();
        }
    }

    private void getDrivers() {
        if(conf.NetworkIsAvailable(getActivity())){
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            booknowDBList = new ArrayList<>();
            JSONObject json = sr.getJSON(conf.url_getAllDrivers, params);
            if(json != null){
                try{
                    if(json.getBoolean("res")) {
                        loads = json.getJSONArray("data");
                        for (int i = 0; i < loads.length(); i++) {
                            JSONObject c = loads.getJSONObject(i);
                            String id = c.getString(conf.tag_id);
                            String name = c.getString(conf.tag_fname) + " " + c.getString(conf.tag_lname);
                            String city = c.getString(conf.tag_city);
                            int[] tab = new Calculator().getAge(c.getString(conf.tag_dateN));
                            String date = tab[0] + "years, " + tab[1] + "month, " + tab[2] + "day";
                            UsersDB rec = new UsersDB(id, name, city, date);
                            booknowDBList.add(rec);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            UsersAdapterList adapter = new UsersAdapterList(getActivity(), booknowDBList, Users.this);
            BookList_lv.setAdapter(adapter);
        }else{
            Toast.makeText(getActivity(), R.string.networkunvalid, Toast.LENGTH_SHORT).show();
        }
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
