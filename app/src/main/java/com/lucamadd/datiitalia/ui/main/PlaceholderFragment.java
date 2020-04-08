package com.lucamadd.datiitalia.ui.main;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lucamadd.datiitalia.R;
import com.lucamadd.datiitalia.StartActivity;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView casi_totali_italia = null;

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_start, container, false);
        casi_totali_italia = root.findViewById(R.id.casitotaliitalia);
        /*
        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
         */
        return root;
    }

    @SuppressWarnings("deprecation")
    public void getData(){
        //final TextView textView = getView().findViewById(R.id.casitotaliitalia);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url =getResources().getString(R.string.andamento_nazionale_latest);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //response = response.replace("[","").replace("]","");
                        Log.e("debug",response);
                        JsonParser jsonParser = new JsonParser();
                        JsonArray array = (JsonArray) jsonParser.parse(response);
                        Log.e("ds",((JSONObject) array.get(0)).get("user_id"));
                        //JsonObject jo = (JsonObject)jsonParser.parse(response);
                        //casi_totali_italia.setText(array.getAsJsonObject().get("totale_casi").toString() + " casi");
                        //Log.e("debug",jo.get("deceduti").toString());
                        //Log.e("tag",array.get(0).getAsString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                casi_totali_italia.setText("");
                Log.e("debug","errore nella richiesta");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}