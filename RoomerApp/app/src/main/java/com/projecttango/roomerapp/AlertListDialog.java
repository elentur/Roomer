package com.projecttango.roomerapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.atap.tangoservice.Tango;

/**
 * Created by Julian Dobrot on 29.05.2016.
 */
public class AlertListDialog extends DialogFragment {




    Tango mTango;
    Button dismiss;
    ListView listView;
    SearchView searchView;
    ArrayAdapter<String> adapter;
    String[] uuid = {"a","b","c","d"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list,null);

        getDialog().setTitle("ADF files on device");

        listView = (ListView) rootView.findViewById(R.id.lv);
        searchView = (SearchView) rootView.findViewById(R.id.searchView);
        dismiss = (Button) rootView.findViewById(R.id.dismiss);


        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,uuid);
        listView.setAdapter(adapter);

        searchView.setQueryHint("Search..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapter.getFilter().filter(s);
                return false;
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return rootView;

    }
}
