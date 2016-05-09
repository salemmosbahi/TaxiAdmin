package it.mahd.taxiadmin.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.mahd.taxiadmin.Main;
import it.mahd.taxiadmin.R;
import it.mahd.taxiadmin.database.BookAdvanceDB;
import it.mahd.taxiadmin.database.BookNowDB;
import it.mahd.taxiadmin.database.ReclamationAdapterList;
import it.mahd.taxiadmin.database.ReclamationDB;
import it.mahd.taxiadmin.util.Controllers;

/**
 * Created by salem on 05/05/16.
 */
public class Book extends Fragment {
    SharedPreferences pref;
    Controllers conf = new Controllers();

    private Spinner Choice_sp;
    private ListView BookList_lv;

    ArrayList<BookNowDB> booknowDBList;
    ArrayList<BookAdvanceDB> bookadvanceDBList;
    private String type[] = {"Book Now","Book Advance"};

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


        return v;
    }

    private void getBook(String str) {
        if(conf.NetworkIsAvailable(getActivity())){
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            reclamationDBListx = new ArrayList<>();
            JSONObject json = sr.getJSON(conf.url_getAllReclamationAdmin, params);
            if(json != null){
                try{
                    if(json.getBoolean("res")) {
                        loads = json.getJSONArray("data");
                        for (int i = 0; i < loads.length(); i++) {
                            JSONObject c = loads.getJSONObject(i);
                            String id = c.getString(conf.tag_id);
                            String subject = c.getString(conf.tag_subject);
                            String date = c.getString(conf.tag_date);
                            Boolean status = c.getBoolean(conf.tag_status);
                            Boolean me = c.getBoolean(conf.tag_me);
                            ReclamationDB rec = new ReclamationDB(id, subject, date, status, me);
                            reclamationDBListx.add(rec);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ReclamationAdapterList adapter = new ReclamationAdapterList(getActivity(), reclamationDBListx, Reclamation.this);
            BookList_lv.setAdapter(adapter);
        }else{
            Toast.makeText(getActivity(), R.string.networkunvalid, Toast.LENGTH_SHORT).show();
        }
    }
}
