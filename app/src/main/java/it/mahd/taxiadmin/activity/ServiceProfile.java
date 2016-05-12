package it.mahd.taxiadmin.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import it.mahd.taxiadmin.util.Controllers;
import it.mahd.taxiadmin.util.ServerRequest;

/**
 * Created by salem on 12/05/16.
 */
public class ServiceProfile extends Fragment {
    SharedPreferences pref;
    Controllers conf = new Controllers();
    ServerRequest sr = new ServerRequest();

    private TextView Name_txt, Value_txt;
    private Button Delete_btn, Edit_btn;
    private String idSer, name;
    private int value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.taxi_profile, container, false);
        ((Main) getActivity()).getSupportActionBar().setTitle(getString(R.string.service_profile));
        pref = getActivity().getSharedPreferences(conf.app, Context.MODE_PRIVATE);

        idSer = getArguments().getString(conf.tag_id);
        name = getArguments().getString(conf.tag_name);
        value = getArguments().getInt(conf.tag_value);

        Name_txt = (TextView) rootView.findViewById(R.id.Model_txt);
        Value_txt = (TextView) rootView.findViewById(R.id.Places_txt);
        Delete_btn = (Button) rootView.findViewById(R.id.Delete_btn);
        Edit_btn = (Button) rootView.findViewById(R.id.Edit_btn);
        Edit_btn.setVisibility(View.VISIBLE);

        Name_txt.setText(name);
        Value_txt.setText(value + "");

        Delete_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(conf.tag_id, idSer));
                JSONObject json = sr.getJSON(conf.url_deleteService, params);
                if(json != null){
                    try {
                        Toast.makeText(getActivity(), json.getString(conf.response), Toast.LENGTH_SHORT).show();
                        if(json.getBoolean(conf.res)) {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.container_body, new Services());
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Edit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fr = new ServiceAdd();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString(conf.tag_id, idSer);
                fr.setArguments(args);
                ft.replace(R.id.container_body, fr);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_body, new Services());
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
