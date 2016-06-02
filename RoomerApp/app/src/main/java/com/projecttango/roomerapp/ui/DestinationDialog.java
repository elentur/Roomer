package com.projecttango.roomerapp.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.projecttango.DataStructure.DestinationPoint;
import com.projecttango.DataStructure.Point;
import com.projecttango.roomerapp.R;
import java.util.ArrayList;

/**
 * Created by Julian Dobrot on 01.06.2016.
 * This class represents a list view dialog of all destinations of the loaded adf file.
 * Is is possible to filter them with search strings.
 * With the selected point the navigation will be calculated. So the value have to be passed by the
 * DestinationDialog.
 *
 */
public class DestinationDialog extends DialogFragment implements DialogInterface.OnClickListener {


    private static Button cancel;
    private static Button accept;
    private static ListView destinationPoints;
    private static SearchView searchView;
    private static ArrayAdapter<Point> adapter;
    private ArrayList<Point> points = new ArrayList<Point>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final View destinationDialogView = inflater.inflate(R.layout.destination_point_list, null);

        getDialog().setTitle("Ziele in ihrer Umgebung");

        destinationPoints = (ListView) destinationDialogView.findViewById(R.id.lv);
        searchView = (SearchView) destinationDialogView.findViewById(R.id.searchView);
        cancel = (Button) destinationDialogView.findViewById(R.id.dismiss);
        accept = (Button) destinationDialogView.findViewById(R.id.accept);

        adapter = new ArrayAdapter<Point>(getActivity(), android.R.layout.select_dialog_singlechoice,points);
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

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Point checkedItem = (Point) destinationPoints.getAdapter().getItem(destinationPoints.getCheckedItemPosition());



                //TODO here the selected item have to be saved before closing the view.
                dismiss();
                Toast.makeText(getActivity(), "selected"+ checkedItem, Toast.LENGTH_SHORT).show();
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

    /**
     * This method fills the point list with direction points.
     * @param list
     */
    public void connectAdapter(ArrayList<Point> list) {

        points.clear();

        for (Point p : list){
            if (p instanceof DestinationPoint){
                points.add(p);
            }

        }


    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        this.dismiss();

    }
}
