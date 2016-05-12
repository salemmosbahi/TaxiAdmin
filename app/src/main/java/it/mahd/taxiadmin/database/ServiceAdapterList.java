package it.mahd.taxiadmin.database;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import it.mahd.taxiadmin.R;
import it.mahd.taxiadmin.activity.PublicityProfile;
import it.mahd.taxiadmin.activity.ServiceProfile;
import it.mahd.taxiadmin.util.Controllers;
import it.mahd.taxiadmin.util.ServerRequest;

/**
 * Created by salem on 09/05/16.
 */
public class ServiceAdapterList extends BaseAdapter {
    Controllers conf = new Controllers();
    LayoutInflater inflater;
    Context contxt;
    List<ServiceDB> data;

    //Dialog dialog;
    Fragment fragment;

    public ServiceAdapterList(Context contxt, List<ServiceDB> data, Fragment fragment) {
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
            v = inflater.inflate(R.layout.service_list, null);
            holder.Name_txt = (TextView) v.findViewById(R.id.Name_txt);
            holder.Value_txt = (TextView) v.findViewById(R.id.Value_txt);
            holder.Row_relative = (RelativeLayout) v.findViewById(R.id.row_rl);
            v.setTag(holder);
        } else {
            holder = (TaxiHolder) v.getTag();
        }

        holder.Name_txt.setText(data.get(position).getName());
        holder.Value_txt.setText(data.get(position).getValue() + "");

        holder.Row_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                Fragment fr = new ServiceProfile();
                FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString(conf.tag_id, data.get(position).getId());
                args.putString(conf.tag_name, data.get(position).getName());
                args.putInt(conf.tag_value, data.get(position).getValue());
                fr.setArguments(args);
                ft.replace(R.id.container_body, fr);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return v;
    }

    static class TaxiHolder {
        TextView Name_txt, Value_txt;
        RelativeLayout Row_relative;
    }
}
