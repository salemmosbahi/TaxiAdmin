package it.mahd.taxiadmin.database;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
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
import it.mahd.taxiadmin.activity.ReclamationChat;
import it.mahd.taxiadmin.util.Controllers;

/**
 * Created by salem on 3/24/16.
 */
public class ReclamationAdapterList extends BaseAdapter {
    Controllers conf = new Controllers();
    LayoutInflater inflater;
    Context contxt;
    List<ReclamationDB> data;
    Fragment fragment;

    public ReclamationAdapterList(Context contxt, List<ReclamationDB> data, Fragment fragment) {
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
        ReclamationHolder holder = new ReclamationHolder();
        if (v == null) {
            inflater = (LayoutInflater) contxt.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.reclamation_list, null);
            holder.Subject_txt = (TextView) v.findViewById(R.id.subject_txt);
            holder.Date_txt = (TextView) v.findViewById(R.id.date_txt);
            holder.Row_relative = (RelativeLayout) v.findViewById(R.id.row_rl);
            v.setTag(holder);
        } else {
            holder = (ReclamationHolder) v.getTag();
        }
        holder.Subject_txt.setText(data.get(position).getSubject());
        holder.Date_txt.setText(data.get(position).getDate());
        if (data.get(position).getStatus() && data.get(position).getMe()) {
            holder.Subject_txt.setTypeface(null, Typeface.BOLD_ITALIC);
            holder.Date_txt.setTypeface(null, Typeface.BOLD_ITALIC);
        }

        holder.Row_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                Fragment fr = new ReclamationChat();
                FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString(conf.tag_id, data.get(position).getId());
                fr.setArguments(args);
                ft.replace(R.id.container_body, fr);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return v;
    }

    class ReclamationHolder {
        TextView Subject_txt;
        TextView Date_txt;
        RelativeLayout Row_relative;
    }
}
