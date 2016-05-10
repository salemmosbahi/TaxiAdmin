package it.mahd.taxiadmin.database;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import it.mahd.taxiadmin.R;
import it.mahd.taxiadmin.util.Controllers;

/**
 * Created by salem on 09/05/16.
 */
public class BookNowAdapterList extends BaseAdapter {
    Controllers conf = new Controllers();
    LayoutInflater inflater;
    Context contxt;
    List<BookNowDB> data;

    Fragment fragment;

    public BookNowAdapterList(Context contxt, List<BookNowDB> data, Fragment fragment) {
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
            v = inflater.inflate(R.layout.book_list, null);
            holder.NameCL_txt = (TextView) v.findViewById(R.id.NameCL_txt);
            holder.NameDR_txt = (TextView) v.findViewById(R.id.NameDR_txt);
            holder.Value_txt = (TextView) v.findViewById(R.id.Value_txt);
            holder.Date_txt = (TextView) v.findViewById(R.id.Date_txt);
            v.setTag(holder);
        } else {
            holder = (TaxiHolder) v.getTag();
        }

        holder.NameCL_txt.setText("Client : " + data.get(position).getNameClient());
        holder.NameDR_txt.setText("Driver : " + data.get(position).getNameDriver());
        holder.Value_txt.setText(data.get(position).getValue());
        holder.Date_txt.setText(data.get(position).getDate());

        return v;
    }

    static class TaxiHolder {
        TextView NameCL_txt, NameDR_txt, Value_txt, Date_txt;
    }
}
