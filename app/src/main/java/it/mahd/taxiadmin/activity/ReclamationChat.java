package it.mahd.taxiadmin.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.mahd.taxiadmin.Main;
import it.mahd.taxiadmin.R;
import it.mahd.taxiadmin.util.ChatAdapter;
import it.mahd.taxiadmin.util.ChatMessage;
import it.mahd.taxiadmin.util.Controllers;
import it.mahd.taxiadmin.util.ServerRequest;

/**
 * Created by salem on 2/19/16.
 */
public class ReclamationChat extends Fragment implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    SharedPreferences pref;
    Controllers conf = new Controllers();
    ServerRequest sr = new ServerRequest();

    private SwipeRefreshLayout RefreshChat_swipe;
    private EditText Message_etxt;
    private FloatingActionButton sendButton;

    private String idRec;
    public static ArrayList chatlist;
    public static ChatAdapter chatAdapter;
    ListView Message_lv;
    JSONArray loads = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reclamation_chat, container, false);

        idRec = getArguments().getString(conf.tag_id);
        Message_etxt = (EditText) rootView.findViewById(R.id.message_etxt);
        Message_lv = (ListView) rootView.findViewById(R.id.msg_lv);
        sendButton = (FloatingActionButton) rootView.findViewById(R.id.send_btn);
        sendButton.setOnClickListener(this);

        Message_lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        Message_lv.setStackFromBottom(true);



        RefreshChat_swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshChat_swipe);
        RefreshChat_swipe.setOnRefreshListener(this);
        RefreshChat_swipe.post(new Runnable() {
                               public void run() {
                                   RefreshChat_swipe.setRefreshing(true);
                                   getAllMsg();
                               }
                           }
        );

        return rootView;
    }

    public void onRefresh() {
        getAllMsg();
    }

    private void getAllMsg() {
        if(conf.NetworkIsAvailable(getActivity())){
            RefreshChat_swipe.setRefreshing(true);
            chatlist = new ArrayList();
            chatAdapter = new ChatAdapter(getActivity(), chatlist);
            Message_lv.setAdapter(chatAdapter);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(conf.tag_id, idRec));
            JSONObject json = sr.getJSON(conf.url_getAllMessage, params);
            if(json != null) {
                try {
                    if (json.getBoolean("res")) {
                        loads = json.getJSONArray("data");
                        for (int i = 0; i < loads.length(); i++) {
                            JSONObject c = loads.getJSONObject(i);
                            Boolean sender = c.getBoolean(conf.tag_sender);
                            String message = c.getString(conf.tag_message);
                            String date = c.getString(conf.tag_date);
                            final ChatMessage chatMessage = new ChatMessage(message, date, sender);
                            chatAdapter.add(chatMessage);
                            chatAdapter.notifyDataSetChanged();
                        }
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
            RefreshChat_swipe.setRefreshing(false);
        }else{
            Toast.makeText(getActivity(), R.string.networkunvalid, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    public void sendTextMessage(View v) {
        String message = Message_etxt.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(conf.tag_id, idRec));
            params.add(new BasicNameValuePair(conf.tag_message, message));
            JSONObject json = sr.getJSON(conf.url_addMessageAdmin, params);
            if(json != null){
                try{
                    if(json.getBoolean("res")){
                        String date = json.getString(conf.tag_date);
                        final ChatMessage chatMessage = new ChatMessage(message, date, false);
                        Message_etxt.setText("");
                        chatAdapter.add(chatMessage);
                        chatAdapter.notifyDataSetChanged();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn:
                if(conf.NetworkIsAvailable(getActivity())){
                    sendTextMessage(v);
                }else{
                    Toast.makeText(getActivity(), R.string.networkunvalid, Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_body, new Reclamation());
        ft.addToBackStack(null);
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
