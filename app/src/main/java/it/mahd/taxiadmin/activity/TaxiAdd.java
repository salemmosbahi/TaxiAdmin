package it.mahd.taxiadmin.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
 * Created by salem on 04/05/16.
 */
public class TaxiAdd extends Fragment {
    SharedPreferences pref;
    Controllers conf = new Controllers();
    ServerRequest sr = new ServerRequest();

    private ArrayList<String> MarkList;
    private ArrayAdapter<String> markAdapter;
    private JSONArray marks = null;

    private Spinner Mark_sp, Place_sp, Luggage_sp;
    private EditText Model_etxt, Serial1_etxt, Serial2_etxt;
    private TextInputLayout Model_input, Serial1_input, Serial2_input;
    private TextView Date_txt;
    private String idTaxi, model, serial, places, luggages, date;
    private int year, month, day;
    private Button Add_btn, Empty_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.taxi_add, container, false);
        ((Main) getActivity()).getSupportActionBar().setTitle(getString(R.string.taxi_add));
        pref = getActivity().getSharedPreferences(conf.app, Context.MODE_PRIVATE);

        Model_input = (TextInputLayout) rootView.findViewById(R.id.Model_input);
        Model_etxt = (EditText) rootView.findViewById(R.id.Model_etxt);
        Serial1_input = (TextInputLayout) rootView.findViewById(R.id.Serial1_input);
        Serial1_etxt = (EditText) rootView.findViewById(R.id.Serial1_etxt);
        Serial2_input = (TextInputLayout) rootView.findViewById(R.id.Serial2_input);
        Serial2_etxt = (EditText) rootView.findViewById(R.id.Serial2_etxt);
        Place_sp = (Spinner) rootView.findViewById(R.id.Place_sp);
        Mark_sp = (Spinner) rootView.findViewById(R.id.Mark_sp);
        Luggage_sp = (Spinner) rootView.findViewById(R.id.Luggage_sp);
        Date_txt = (TextView) rootView.findViewById(R.id.Date_txt);
        Add_btn = (Button) rootView.findViewById(R.id.Add_btn);
        Empty_btn = (Button) rootView.findViewById(R.id.Empty_btn);

        Calendar d = new Calculator().getCurrentTime();
        year = d.get(Calendar.YEAR);
        month  = d.get(Calendar.MONTH);
        day  = d.get(Calendar.DAY_OF_MONTH);

        Date_txt.setText(new StringBuilder().append(year).append("/").append(month + 1).append("/").append(day));
        Date_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), R.style.MyMaterialDesignTheme, dateSetListener, year, month, day).show();
            }
        });

        MarkList = new ArrayList<String>();
        List<NameValuePair> cityParams = new ArrayList<NameValuePair>();
        cityParams.add(new BasicNameValuePair(conf.tag_name, pref.getString(conf.tag_country, "")));
        JSONObject jsonx = sr.getJSON(conf.url_getAllMark, cityParams);
        if (jsonx != null) {
            try {
                if (jsonx.getBoolean(conf.res)) {
                    marks = jsonx.getJSONArray("data");
                    for (int i = 0; i < marks.length(); i++) {
                        JSONObject t = marks.getJSONObject(i);
                        MarkList.add(t.getString(conf.tag_name));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), R.string.serverunvalid,Toast.LENGTH_LONG).show();
        }
        markAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,MarkList);
        markAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Mark_sp.setAdapter(markAdapter);

        Empty_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                emptyForm();
            }
        });

        Add_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (conf.NetworkIsAvailable(getActivity())) {
                    addForm();
                } else {
                    Toast.makeText(getActivity(),R.string.networkunvalid,Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void emptyForm() {
        Model_etxt.setText("");
    }

    private void addForm() {
        if (!validateModel()) { return; }
        if (!validateSerial1()) { return; }
        if (!validateSerial2()) { return; }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(conf.tag_mark, Mark_sp.getSelectedItem().toString()));
        params.add(new BasicNameValuePair(conf.tag_model, Model_etxt.getText().toString()));
        params.add(new BasicNameValuePair(conf.tag_serial, Serial1_etxt.getText().toString() + "/" + Serial2_etxt.getText().toString()));
        params.add(new BasicNameValuePair(conf.tag_places, Place_sp.getSelectedItem().toString()));
        params.add(new BasicNameValuePair(conf.tag_luggages, Luggage_sp.getSelectedItem().toString()));
        params.add(new BasicNameValuePair(conf.tag_date, Date_txt.getText().toString()));
        JSONObject json = sr.getJSON(conf.url_addTaxi, params);
        if (json != null) {
            try{
                String response = json.getString(conf.response);
                if(json.getBoolean(conf.res)) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.container_body, new Taxi());
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

    private boolean validateModel() {
        if(Model_etxt.getText().toString().trim().isEmpty()) {
            Model_input.setError(getString(R.string.model_err));
            requestFocus(Model_etxt);
            return false;
        } else {
            Model_input.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateSerial1() {
        if(Serial1_etxt.getText().toString().trim().isEmpty()) {
            Serial1_input.setError(getString(R.string.serial_err));
            requestFocus(Serial1_etxt);
            return false;
        } else {
            Serial1_input.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateSerial2() {
        if(Serial2_etxt.getText().toString().trim().isEmpty()) {
            Serial2_input.setError(getString(R.string.serial_err));
            requestFocus(Serial2_etxt);
            return false;
        } else {
            Serial2_input.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            Date_txt.setText(new StringBuilder().append(year).append("/").append(month + 1).append("/").append(day));
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.container_body, new Taxi());
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
