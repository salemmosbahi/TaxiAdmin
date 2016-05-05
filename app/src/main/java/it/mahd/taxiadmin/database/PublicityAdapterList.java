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
import it.mahd.taxiadmin.activity.Publicity;
import it.mahd.taxiadmin.activity.PublicityProfile;
import it.mahd.taxiadmin.activity.TaxiProfile;
import it.mahd.taxiadmin.util.Controllers;

/**
 * Created by salem on 05/05/16.
 */
public class PublicityAdapterList extends BaseAdapter {
    Controllers conf = new Controllers();
    LayoutInflater inflater;
    Context contxt;
    List<PublicityDB> data;

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
            v = inflater.inflate(R.layout.taxi_list, null);
            holder.Name_txt = (TextView) v.findViewById(R.id.Model_txt);
            holder.Period_txt = (TextView) v.findViewById(R.id.Serial_txt);
            holder.Date_txt = (TextView) v.findViewById(R.id.Date_txt);
            holder.Row_relative = (RelativeLayout) v.findViewById(R.id.row_rl);
            v.setTag(holder);
        } else {
            holder = (TaxiHolder) v.getTag();
        }

        holder.Name_txt.setText(data.get(position).getName());
        holder.Period_txt.setText(data.get(position).getPeriod());
        holder.Date_txt.setText(data.get(position).getDate());

        holder.Row_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                Fragment fr = new PublicityProfile();
                FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString(conf.tag_id, data.get(position).getId());
                args.putString(conf.tag_mark, data.get(position).getName());
                args.putString(conf.tag_model, data.get(position).getPeriod());
                args.putString(conf.tag_serial, data.get(position).getPrice());
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
        TextView Name_txt, Period_txt, Date_txt;
        RelativeLayout Row_relative;
    }
}
