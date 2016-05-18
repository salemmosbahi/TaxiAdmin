package it.mahd.taxiadmin.database;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.mahd.taxiadmin.R;
import it.mahd.taxiadmin.util.Controllers;
import it.mahd.taxiadmin.util.ServerRequest;

/**
 * Created by salem on 10/05/16.
 */
public class UsersAdapterList extends BaseAdapter {
    Controllers conf = new Controllers();
    LayoutInflater inflater;
    Context contxt;
    List<UsersDB> data;

    Fragment fragment;

    public UsersAdapterList(Context contxt, List<UsersDB> data, Fragment fragment) {
        this.contxt = contxt;
        this.data = data;
        this.fragment = fragment;
    }

    @Override
    public int getCount() { return data.size(); }

    @Override
    public Object getItem(int position) { return data.get(position); }

    @Override
    public long getItemId(int position) { return data.indexOf(getItem(position)); }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TaxiHolder holder = new TaxiHolder();
        if (v == null) {
            inflater = (LayoutInflater) contxt.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.users_list, null);
            holder.Name_txt = (TextView) v.findViewById(R.id.Name_txt);
            holder.City_txt = (TextView) v.findViewById(R.id.City_txt);
            holder.Date_txt = (TextView) v.findViewById(R.id.Date_txt);
            holder.Row_rl = (RelativeLayout) v.findViewById(R.id.row_rl);
            v.setTag(holder);
        } else {
            holder = (TaxiHolder) v.getTag();
        }

        holder.Name_txt.setText(data.get(position).getName());
        holder.City_txt.setText(data.get(position).getCity());
        holder.Date_txt.setText(data.get(position).getDate());
        holder.Row_rl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("account", data.get(position).getAccount()));
                params.add(new BasicNameValuePair(conf.tag_token, data.get(position).getId()));
                JSONObject json = new ServerRequest().getJSON(conf.url_disableAccountAdmin, params);
                if (json != null) {
                    try {
                        if(json.getBoolean("res")) {
                            Toast.makeText(contxt, json.getString(conf.response), Toast.LENGTH_SHORT).show();
                        }
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        return v;
    }

    static class TaxiHolder {
        TextView Name_txt, City_txt, Date_txt;
        RelativeLayout Row_rl;
    }
}
