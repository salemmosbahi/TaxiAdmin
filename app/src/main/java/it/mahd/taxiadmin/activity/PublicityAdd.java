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
import it.mahd.taxiadmin.database.PublicityAdapterList;
import it.mahd.taxiadmin.database.PublicityDB;
import it.mahd.taxiadmin.util.Calculator;
import it.mahd.taxiadmin.util.Controllers;
import it.mahd.taxiadmin.util.ServerRequest;

/**
 * Created by salem on 05/05/16.
 */
public class PublicityAdd extends Fragment {
    SharedPreferences pref;
    Controllers conf = new Controllers();
    ServerRequest sr = new ServerRequest();

    private Spinner Period_sp;
    private EditText Name_etxt, Category_etxt, Price_etxt;
    private TextInputLayout Name_input, Category_input, Price_input;
    private TextView Date_txt;
    JSONArray loads = null;
    private ArrayAdapter<String> periodArray;
    private String type[] = {"6 month", "1 year", "5 years"};
    private int year, month, day;
    private String idPub;
    private Button Add_btn, Empty_btn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.publicity_add, container, false);
        ((Main) getActivity()).getSupportActionBar().setTitle(getString(R.string.publicity_add));
        pref = getActivity().getSharedPreferences(conf.app, Context.MODE_PRIVATE);

        if (getArguments() != null) {
            idPub = getArguments().getString(conf.tag_id);
        }

        Name_input = (TextInputLayout) rootView.findViewById(R.id.Name_input);
        Name_etxt = (EditText) rootView.findViewById(R.id.Name_etxt);
        Category_input = (TextInputLayout) rootView.findViewById(R.id.Category_input);
        Category_etxt = (EditText) rootView.findViewById(R.id.Category_etxt);
        Price_input = (TextInputLayout) rootView.findViewById(R.id.Price_input);
        Price_etxt = (EditText) rootView.findViewById(R.id.Price_etxt);
        Period_sp = (Spinner) rootView.findViewById(R.id.Period_sp);
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

        periodArray = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, type); //selected item will look like a spinner set from XML
        periodArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Period_sp.setAdapter(periodArray);

        if (idPub != null) {
            Add_btn.setText(R.string.edit);
            getPub();
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
        Category_etxt.setText("");
        Price_etxt.setText("");
        Period_sp.setSelection(0);
    }

    private void editForm() {
        if (!validateName()) { return; }
        if (!validateCategory()) { return; }
        if (!validatePrice()) { return; }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(conf.tag_id, idPub));
        params.add(new BasicNameValuePair(conf.tag_name, Name_etxt.getText().toString()));
        params.add(new BasicNameValuePair(conf.tag_category, Category_etxt.getText().toString()));
        params.add(new BasicNameValuePair(conf.tag_price, Price_etxt.getText().toString()));
        params.add(new BasicNameValuePair(conf.tag_period, Period_sp.getSelectedItem().toString()));
        params.add(new BasicNameValuePair(conf.tag_date, Date_txt.getText().toString()));
        JSONObject json = sr.getJSON(conf.url_editPublicity, params);
        if (json != null) {
            try{
                String response = json.getString(conf.response);
                if(json.getBoolean(conf.res)) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.container_body, new Publicity());
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

    private void getPub() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(conf.tag_id, idPub));
        JSONObject json = sr.getJSON(conf.url_getPublicityById, params);
        if(json != null){
            try{
                if(json.getBoolean(conf.res)) {
                    loads = json.getJSONArray("data");
                    if(loads.length() != 0){
                        JSONObject c = loads.getJSONObject(0);
                        Name_etxt.setText(c.getString(conf.tag_name));
                        Category_etxt.setText(c.getString(conf.tag_category));
                        Price_etxt.setText(c.getString(conf.tag_price));
                        Period_sp.setSelection(periodArray.getPosition(c.getString(conf.tag_period)));
                        Date_txt.setText(c.getString(conf.tag_date));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addForm() {
        if (!validateName()) { return; }
        if (!validateCategory()) { return; }
        if (!validatePrice()) { return; }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(conf.tag_name, Name_etxt.getText().toString()));
        params.add(new BasicNameValuePair(conf.tag_category, Category_etxt.getText().toString()));
        params.add(new BasicNameValuePair(conf.tag_price, Price_etxt.getText().toString()));
        params.add(new BasicNameValuePair(conf.tag_period, Period_sp.getSelectedItem().toString()));
        params.add(new BasicNameValuePair(conf.tag_date, Date_txt.getText().toString()));
        JSONObject json = sr.getJSON(conf.url_addPublicity, params);
        if (json != null) {
            try{
                String response = json.getString(conf.response);
                if(json.getBoolean(conf.res)) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.container_body, new Publicity());
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

    private boolean validateCategory() {
        if(Category_etxt.getText().toString().trim().isEmpty()) {
            Category_input.setError(getString(R.string.category_err));
            requestFocus(Category_etxt);
            return false;
        } else {
            Category_input.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePrice() {
        if(Price_etxt.getText().toString().trim().isEmpty()) {
            Price_input.setError(getString(R.string.price_err));
            requestFocus(Price_etxt);
            return false;
        } else {
            Price_input.setErrorEnabled(false);
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
        ft.replace(R.id.container_body, new Publicity());
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
