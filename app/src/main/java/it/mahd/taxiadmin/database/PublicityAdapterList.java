package it.mahd.taxiadmin.database;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import it.mahd.taxiadmin.activity.Publicity;
import it.mahd.taxiadmin.activity.PublicityProfile;
import it.mahd.taxiadmin.activity.TaxiProfile;
import it.mahd.taxiadmin.util.Controllers;
import it.mahd.taxiadmin.util.ServerRequest;

/**
 * Created by salem on 05/05/16.
 */
public class PublicityAdapterList extends BaseAdapter {
    Controllers conf = new Controllers();
    ServerRequest sr = new ServerRequest();
    LayoutInflater inflater;
    Context contxt;
    List<PublicityDB> data;

    //Dialog dialog;
    Fragment fragment;

    public PublicityAdapterList(Context contxt, List<PublicityDB> data, Fragment fragment) {
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
            v = inflater.inflate(R.layout.publicity_list, null);
            holder.Name_txt = (TextView) v.findViewById(R.id.Name_txt);
            holder.Category_txt = (TextView) v.findViewById(R.id.Category_txt);
            holder.Price_txt = (TextView) v.findViewById(R.id.Price_txt);
            holder.Period_txt = (TextView) v.findViewById(R.id.Period_txt);
            holder.Date_txt = (TextView) v.findViewById(R.id.Date_txt);
            holder.Row_relative = (RelativeLayout) v.findViewById(R.id.row_rl);
            v.setTag(holder);
        } else {
            holder = (TaxiHolder) v.getTag();
        }

        holder.Name_txt.setText(data.get(position).getName());
        holder.Category_txt.setText(data.get(position).getCategory());
        holder.Price_txt.setText(data.get(position).getPrice());
        holder.Period_txt.setText(data.get(position).getPeriod());
        holder.Date_txt.setText(data.get(position).getDate());

        /*holder.Row_relative.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialog = new Dialog(contxt, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog);
                dialog.setCancelable(true);
                TextView Service_txt;
                Button Delete_btn, Cancel_btn;
                Service_txt = (TextView) dialog.findViewById(R.id.Service_txt);
                Service_txt.setText(R.string.publicity);
                Cancel_btn = (Button) dialog.findViewById(R.id.Cancel_btn);
                Cancel_btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Delete_btn = (Button) dialog.findViewById(R.id.Delete_btn);
                Delete_btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(conf.tag_id, data.get(position).getId()));
                        JSONObject json = sr.getJSON(conf.url_deletePublicity, params);
                        if(json != null){
                            try {
                                Toast.makeText(contxt, json.getString(conf.response), Toast.LENGTH_SHORT).show();
                                if(json.getBoolean(conf.res)) {
                                    new Publicity().getPub();
                                    dialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });*/

        holder.Row_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                Fragment fr = new PublicityProfile();
                FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString(conf.tag_id, data.get(position).getId());
                args.putString(conf.tag_name, data.get(position).getName());
                args.putString(conf.tag_category, data.get(position).getPeriod());
                args.putString(conf.tag_price, data.get(position).getPrice());
                args.putString(conf.tag_period, data.get(position).getPrice());
                args.putString(conf.tag_date, data.get(position).getDate());
                fr.setArguments(args);
                ft.replace(R.id.container_body, fr);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return v;
    }

    static class TaxiHolder {
        TextView Name_txt, Category_txt, Price_txt, Period_txt, Date_txt;
        RelativeLayout Row_relative;
    }
}
