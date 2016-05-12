package it.mahd.taxiadmin.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.mahd.taxiadmin.Main;
import it.mahd.taxiadmin.R;
import it.mahd.taxiadmin.util.Calculator;
import it.mahd.taxiadmin.util.Controllers;
import it.mahd.taxiadmin.util.ServerRequest;

/**
 * Created by salem on 09/05/16.
 */
public class ServiceAdd extends Fragment {
    SharedPreferences pref;
    Controllers conf = new Controllers();
    ServerRequest sr = new ServerRequest();

    private Spinner Value_sp;
    private EditText Name_etxt;
    private TextInputLayout Name_input;
    private Button Add_btn, Empty_btn;
    JSONArray loads = null;
    private String idSer;
    private ArrayAdapter<String> valueArray;
    private String type[] = {"1", "2", "3", "4", "5"};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.service_add, container, false);
        ((Main) getActivity()).getSupportActionBar().setTitle(getString(R.string.service_add));
        pref = getActivity().getSharedPreferences(conf.app, Context.MODE_PRIVATE);

        if (getArguments() != null) {
            idSer = getArguments().getString(conf.tag_id);
        }

        Name_input = (TextInputLayout) rootView.findViewById(R.id.Name_input);
        Name_etxt = (EditText) rootView.findViewById(R.id.Name_etxt);
        Value_sp = (Spinner) rootView.findViewById(R.id.Value_sp);
        Add_btn = (Button) rootView.findViewById(R.id.Add_btn);
        Empty_btn = (Button) rootView.findViewById(R.id.Empty_btn);

        valueArray = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, type); //selected item will look like a spinner set from XML
        valueArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Value_sp.setAdapter(valueArray);

        if (idSer != null) {
            Add_btn.setText(R.string.edit);
            getSer();
        }

        Empty_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                emptyForm();
            }
        });

        Add_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (conf.NetworkIsAvailable(getActivity())) {
                    if (Add_btn.getText().toString().equals(getString(R.string.add))) {
                        addForm();
                    } else if (Add_btn.getText().toString().equals(getString(R.string.edit))) {
                        editForm();
                    }
                } else {
                    Toast.makeText(getActivity(),R.string.networkunvalid,Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void emptyForm() {
        Name_etxt.setText("");
        Value_sp.setSelection(0);
    }

    private void editForm() {
        if (!validateName()) { return; }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(conf.tag_id, idSer));
        params.add(new BasicNameValuePair(conf.tag_name, Name_etxt.getText().toString()));
        params.add(new BasicNameValuePair(conf.tag_value, Value_sp.getSelectedItem().toString()));
        JSONObject json = sr.getJSON(conf.url_editService, params);
        if (json != null) {
            try{
                String response = json.getString(conf.response);
                if(json.getBoolean(conf.res)) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.container_body, new Services());
                    ft.commit();
                }
                Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
            }catch(JSONException e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), R.string.serverunvalid,Toast.LENGTH_SHORT).show();
        }
    }

    private void getSer() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(conf.tag_id, idSer));
        JSONObject json = sr.getJSON(conf.url_getServiceById, params);
        if(json != null){
            try{
                if(json.getBoolean(conf.res)) {
                    loads = json.getJSONArray("data");
                    if(loads.length() != 0){
                        JSONObject c = loads.getJSONObject(0);
                        Name_etxt.setText(c.getString(conf.tag_name));
                        Value_sp.setSelection(valueArray.getPosition(c.getString(conf.tag_value)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addForm() {
        if (!validateName()) { return; }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(conf.tag_name, Name_etxt.getText().toString()));
        params.add(new BasicNameValuePair(conf.tag_value, Value_sp.getSelectedItem().toString()));
        JSONObject json = sr.getJSON(conf.url_addService, params);
        if (json != null) {
            try{
                String response = json.getString(conf.response);
                if(json.getBoolean(conf.res)) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.container_body, new Services());
                    ft.commit();
                }
                Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
            }catch(JSONException e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), R.string.serverunvalid,Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateName() {
        if(Name_etxt.getText().toString().trim().isEmpty()) {
            Name_input.setError(getString(R.string.name_err));
            requestFocus(Name_etxt);
            return false;
        } else {
            Name_input.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.container_body, new Services());
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
