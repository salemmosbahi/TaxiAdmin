package it.mahd.taxiadmin.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
 * Created by salem on 04/05/16.
 */
public class Login extends Fragment {
    SharedPreferences pref;
    ServerRequest sr = new ServerRequest();
    Controllers conf = new Controllers();

    private EditText Email_etxt, Password_etxt;
    private TextInputLayout Email_input, Password_input;
    private Button Login_btn, SignUp_btn;

    public Login() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login, container, false);
        ((Main) getActivity()).getSupportActionBar().setTitle(getString(R.string.login));
        pref = getActivity().getSharedPreferences(conf.app, Context.MODE_PRIVATE);

        Email_input = (TextInputLayout) rootView.findViewById(R.id.Username_input);
        Password_input = (TextInputLayout) rootView.findViewById(R.id.input_password);
        Email_etxt = (EditText) rootView.findViewById(R.id.Username_etxt);
        Password_etxt = (EditText) rootView.findViewById(R.id.password_etxt);
        Login_btn = (Button) rootView.findViewById(R.id.login_btn);

        Email_etxt.addTextChangedListener(new MyTextWatcher(Email_etxt));
        Password_etxt.addTextChangedListener(new MyTextWatcher(Password_etxt));

        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conf.NetworkIsAvailable(getActivity())) {
                    submitForm();
                } else {
                    Toast.makeText(getActivity(), R.string.networkunvalid, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void submitForm() {
        if (!validateEmail()) { return; }
        if (!validatePassword()) { return; }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("app", conf.app));
        params.add(new BasicNameValuePair(conf.tag_username, Email_etxt.getText().toString()));
        params.add(new BasicNameValuePair(conf.tag_password, Password_etxt.getText().toString()));
        JSONObject json = sr.getJSON(conf.url_login, params);
        if(json != null){
            try{
                String jsonstr = json.getString(conf.response);
                if(json.getBoolean(conf.res)) {
                    String token = json.getString(conf.tag_token);
                    String fname = json.getString(conf.tag_username);

                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString(conf.tag_token, token);
                    edit.putString(conf.tag_username, fname);
                    edit.commit();

                    RelativeLayout rl = (RelativeLayout) getActivity().findViewById(R.id.nav_header_container);
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View vi = inflater.inflate(R.layout.toolnav_drawer, null);
                    TextView tv = (TextView) vi.findViewById(R.id.usernameTool_txt);
                    tv.setText(fname);
                    rl.addView(vi);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container_body, new Home());
                    ft.addToBackStack(null);
                    ft.commit();
                }else{
                    Toast.makeText(getActivity(),jsonstr,Toast.LENGTH_SHORT).show();
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    private boolean validateEmail() {
        String email = Email_etxt.getText().toString().trim();
        if (email.isEmpty()) {
            Email_input.setError(getString(R.string.username_err));
            requestFocus(Email_etxt);
            return false;
        } else {
            Email_input.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (Password_etxt.getText().toString().trim().isEmpty()) {
            Password_input.setError(getString(R.string.password_err));
            requestFocus(Password_etxt);
            return false;
        } else {
            Password_input.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;
        private MyTextWatcher(View view) { this.view = view; }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.Username_etxt:
                    validateEmail();
                    break;
                case R.id.password_etxt:
                    validatePassword();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
