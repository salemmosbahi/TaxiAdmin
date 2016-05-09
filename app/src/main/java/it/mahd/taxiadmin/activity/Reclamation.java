package it.mahd.taxiadmin.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import it.mahd.taxiadmin.database.ReclamationAdapterList;
import it.mahd.taxiadmin.database.ReclamationDB;
import it.mahd.taxiadmin.util.Controllers;
import it.mahd.taxiadmin.util.ServerRequest;

/**
 * Created by salem on 2/13/16.
 */
public class Reclamation extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SharedPreferences pref;
    Controllers conf = new Controllers();
    ServerRequest sr = new ServerRequest();

    private SwipeRefreshLayout Refresh_swipe;
    private ListView lv;
    ArrayList<ReclamationDB> reclamationDBListx;
    JSONArray loads = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reclamation, container, false);

        pref = getActivity().getSharedPreferences(conf.app, Context.MODE_PRIVATE);
        Refresh_swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_swipe);
        lv = (ListView) rootView.findViewById(R.id.listReclamation);
        Refresh_swipe.setOnRefreshListener(this);
        Refresh_swipe.post(new Runnable() {
                               public void run() {
                                   Refresh_swipe.setRefreshing(true);
                                   getAllReclamation();
                               }
                           }
        );

        return rootView;
    }

    public void onRefresh() {
        getAllReclamation();
    }

    private void getAllReclamation() {
        if(conf.NetworkIsAvailable(getActivity())){
            Refresh_swipe.setRefreshing(true);
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
            lv.setAdapter(adapter);
            Refresh_swipe.setRefreshing(false);
        }else{
            Toast.makeText(getActivity(), R.string.networkunvalid, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
