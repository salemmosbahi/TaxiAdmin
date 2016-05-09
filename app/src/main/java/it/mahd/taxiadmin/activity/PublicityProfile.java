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
 * Created by salem on 05/05/16.
 */
public class PublicityProfile extends Fragment {
    SharedPreferences pref;
    Controllers conf = new Controllers();
    ServerRequest sr = new ServerRequest();

    private TextView Name_txt, Category_txt, Price_txt, Period_txt, Date_txt;
    private Button Delete_btn;
    private String idPub, name, category, price, period, date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.taxi_profile, container, false);
        ((Main) getActivity()).getSupportActionBar().setTitle(getString(R.string.taxi_profile));
        pref = getActivity().getSharedPreferences(conf.app, Context.MODE_PRIVATE);

        idPub = getArguments().getString(conf.tag_id);
        name = getArguments().getString(conf.tag_name);
        category = getArguments().getString(conf.tag_category);
        price = getArguments().getString(conf.tag_price);
        period = getArguments().getString(conf.tag_period);
        date = getArguments().getString(conf.tag_date);

        Name_txt = (TextView) rootView.findViewById(R.id.Model_txt);
        Category_txt = (TextView) rootView.findViewById(R.id.Serial_txt);
        Price_txt = (TextView) rootView.findViewById(R.id.Places_txt);
        Period_txt = (TextView) rootView.findViewById(R.id.Luggages_txt);
        Date_txt = (TextView) rootView.findViewById(R.id.Date_txt);
        Delete_btn = (Button) rootView.findViewById(R.id.Delete_btn);

        Name_txt.setText(name);
        Category_txt.setText(category);
        Price_txt.setText(price + " $");
        Period_txt.setText(period);
        Date_txt.setText(date);

        Delete_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(conf.tag_id, idPub));
                JSONObject json = sr.getJSON(conf.url_deletePublicity, params);
                if(json != null){
                    try {
                        Toast.makeText(getActivity(), json.getString(conf.response), Toast.LENGTH_SHORT).show();
                        if(json.getBoolean(conf.res)) {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.container_body, new Publicity());
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_body, new Publicity());
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
