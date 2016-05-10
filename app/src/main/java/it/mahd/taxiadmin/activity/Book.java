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
import it.mahd.taxiadmin.database.BookAdvanceDB;
import it.mahd.taxiadmin.database.BookNowAdapterList;
import it.mahd.taxiadmin.database.BookNowDB;
import it.mahd.taxiadmin.database.ReclamationAdapterList;
import it.mahd.taxiadmin.database.ReclamationDB;
import it.mahd.taxiadmin.util.Controllers;
import it.mahd.taxiadmin.util.ServerRequest;

/**
 * Created by salem on 05/05/16.
 */
public class Book extends Fragment {
    SharedPreferences pref;
    Controllers conf = new Controllers();
    ServerRequest sr = new ServerRequest();

    private Spinner Choice_sp;
    private ListView BookList_lv;

    ArrayList<BookNowDB> booknowDBList;
    private String type[] = {"Book Now","Book Advance"};
    JSONArray loads = null;

    public Book() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book, container, false);
        ((Main) getActivity()).getSupportActionBar().setTitle(getString(R.string.books));
        pref = getActivity().getSharedPreferences(conf.app, Context.MODE_PRIVATE);

        BookList_lv = (ListView) v.findViewById(R.id.listBook);
        Choice_sp = (Spinner) v.findViewById(R.id.Choice_sp);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, type); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Choice_sp.setAdapter(spinnerArrayAdapter);

        Choice_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Choice_sp.getSelectedItem().toString().equals("Book Now")) {
                    getBookNow();
                } else if (Choice_sp.getSelectedItem().toString().equals("Book Advance")) {
                    getBookAdvance();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getBookNow();

        return v;
    }

    private void getBookNow() {
        if(conf.NetworkIsAvailable(getActivity())){
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            booknowDBList = new ArrayList<>();
            JSONObject json = sr.getJSON(conf.url_getAllBookNow, params);
            if(json != null){
                try{
                    if(json.getBoolean("res")) {
                        loads = json.getJSONArray("data");
                        for (int i = 0; i < loads.length(); i++) {
                            JSONObject c = loads.getJSONObject(i);
                            String id = c.getString(conf.tag_id);
                            String nameClient = c.getString(conf.tag_nameClient);
                            String nameDriver = c.getString(conf.tag_nameDriver);
                            String value = c.getString(conf.tag_value);
                            String date = c.getString(conf.tag_date);
                            BookNowDB rec = new BookNowDB(id, nameClient, nameDriver, value, date);
                            booknowDBList.add(rec);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            BookNowAdapterList adapter = new BookNowAdapterList(getActivity(), booknowDBList, Book.this);
            BookList_lv.setAdapter(adapter);
        }else{
            Toast.makeText(getActivity(), R.string.networkunvalid, Toast.LENGTH_SHORT).show();
        }
    }

    private void getBookAdvance() {
        if(conf.NetworkIsAvailable(getActivity())){
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            booknowDBList = new ArrayList<>();
            JSONObject json = sr.getJSON(conf.url_getAllBookAdvance, params);
            if(json != null){
                try{
                    if(json.getBoolean("res")) {
                        loads = json.getJSONArray("data");
                        for (int i = 0; i < loads.length(); i++) {
                            JSONObject c = loads.getJSONObject(i);
                            String id = c.getString(conf.tag_id);
                            String nameClient = c.getString(conf.tag_username);
                            String nameDriver = c.getString(conf.tag_nameDriver);
                            String value = (c.getBoolean(conf.tag_repeat)) ? "repeat" : "not repeat";
                            String date = c.getString(conf.tag_dateBook);
                            BookNowDB rec = new BookNowDB(id, nameClient, nameDriver, value, date);
                            booknowDBList.add(rec);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            BookNowAdapterList adapter = new BookNowAdapterList(getActivity(), booknowDBList, Book.this);
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
