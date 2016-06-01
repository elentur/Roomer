package com.projecttango.roomerapp.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.projecttango.roomerapp.R;

/**
 * Created by Julian Dobrot on 01.06.2016.
 * This class represents a list view dialog of all destinations of the loaded adf file.
 * Is is possible to filter them with search strings.
 * With the selected point the navigation will be calculated. So the value have to be passed by the
 * DestinationDialog.
 *
 */
public class DestinationDialog extends DialogFragment {


    private static Button cancel;
    private static ListView destinationPoints;
    private static SearchView searchView;
    private static ArrayAdapter<String> adapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View destinationDialogView = inflater.inflate(R.layout.destination_point_list, null);

        getDialog().setTitle("Ziele in ihrer Umgebung");

        destinationPoints = (ListView) destinationDialogView.findViewById(R.id.lv);
        searchView = (SearchView) destinationDialogView.findViewById(R.id.searchView);
        cancel = (Button) destinationDialogView.findViewById(R.id.dismiss);


        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        destinationPoints.setAdapter(adapter);

        searchView.setQueryHint("Search..");


        //listener for the search view
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


        // listener for the cancel button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }


        });

        return destinationDialogView;
    }
}
