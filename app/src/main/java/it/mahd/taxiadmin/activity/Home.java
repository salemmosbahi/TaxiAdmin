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

import it.mahd.taxiadmin.Main;
import it.mahd.taxiadmin.R;
import it.mahd.taxiadmin.util.Controllers;

/**
 * Created by salem on 04/05/16.
 */
public class Home extends Fragment {
    SharedPreferences pref;
    Controllers conf = new Controllers();

    private Button Taxi_btn, Publicity_btn, Users_btn, Services_btn, Books_btn, Reclamations_btn;

    public Home() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home, container, false);
        ((Main) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
        pref = getActivity().getSharedPreferences(conf.app, Context.MODE_PRIVATE);

        Taxi_btn = (Button) v.findViewById(R.id.Taxi_btn);
        Taxi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFragment(new Taxi());
            }
        });

        Publicity_btn = (Button) v.findViewById(R.id.Publicity_btn);
        Publicity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFragment(new Publicity());
            }
        });

        Users_btn = (Button) v.findViewById(R.id.Users_btn);
        Users_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFragment(new Users());
            }
        });

        Services_btn = (Button) v.findViewById(R.id.Services_btn);
        Services_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFragment(new Services());
            }
        });

        Books_btn = (Button) v.findViewById(R.id.Books_btn);
        Books_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFragment(new Book());
            }
        });

        Reclamations_btn = (Button) v.findViewById(R.id.Reclamations_btn);
        Reclamations_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFragment(new Reclamation());
            }
        });

        return  v;
    }

    private void goFragment(Fragment fr) {
        if (pref.getString(conf.tag_token, "").equals("")) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.container_body, new Login());
            ft.commit();
        } else {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.container_body, fr);
            ft.commit();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
        android.os.Process.killProcess(android.os.Process.myPid());
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
