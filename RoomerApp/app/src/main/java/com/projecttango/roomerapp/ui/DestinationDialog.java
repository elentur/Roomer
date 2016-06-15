package com.projecttango.roomerapp.ui;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import com.projecttango.DataStructure.DestinationPoint;
import com.projecttango.DataStructure.Point;
import com.projecttango.Dijkstra.VectorGraph;
import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.RoomerRenderer;


import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Julian Dobrot on 01.06.2016.
 * This class represents a list view dialog of all destinations of the loaded adf file.
 * Is is possible to filter them with search strings.
 * With the selected point the navigation will be calculated.
 *
 */
public class DestinationDialog extends DialogFragment  {


    private static Button cancel;
    private static Button accept;
    private static ListView destinationPoints;
    private static SearchView searchView;
    private static ArrayAdapter<Point> adapter;
    private ArrayList<Point> pointsDialog = new ArrayList<Point>();
    private DestinationPoint selectedPoint = null;
    private ArrayList<Point> allPoints;


    @Override
    public void onResume() {
        super.onResume();

        accept.setEnabled(false);
        destinationPoints.clearChoices();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final View destinationDialogView = inflater.inflate(R.layout.destination_point_list, null);


        if (RoomerMainActivity.adf!=null) {
            getDialog().setTitle("Ziele in "+RoomerMainActivity.adf);
        } else {
            getDialog().setTitle("Ziele in ihrer Umgebung ");
        }


        destinationPoints = (ListView) destinationDialogView.findViewById(R.id.lv);
        searchView = (SearchView) destinationDialogView.findViewById(R.id.searchView);
        cancel = (Button) destinationDialogView.findViewById(R.id.dismiss);
        accept = (Button) destinationDialogView.findViewById(R.id.accept);

        adapter = new ArrayAdapter<Point>(getActivity(), android.R.layout.select_dialog_singlechoice,pointsDialog);
        destinationPoints.setAdapter(adapter);
        selectedPoint = null;
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

        //listener for the accept button
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                selectedPoint = (DestinationPoint) destinationPoints.getAdapter().getItem(destinationPoints.getCheckedItemPosition());
                //setSelectedPoint(selectedPoint);
                renderPath();
                dismiss();


            }
        });

        destinationPoints.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                accept.setEnabled(destinationPoints.getCheckedItemPosition()>-1);
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



    @Override
    public void onDismiss(DialogInterface dialog) {

        //Enable the the destination button to secure that the user can reopen the DestinationDialog
        Icon_Segment_Fragment.segDestinations.setEnabled(true);
        super.onDismiss(dialog);
    }

    public DestinationPoint getSelectedPoint() {
        return selectedPoint;
    }

    public void setSelectedPoint(DestinationPoint selectedPoint) {
        this.selectedPoint = selectedPoint;
    }

    /**
     * This method fills the point list with direction points.
     * @param list
     */
    public void connectAdapter(ArrayList<Point> list) {
        allPoints = list;
        pointsDialog.clear();

        for (Point p : list){
            if (p instanceof DestinationPoint){
                pointsDialog.add(p);
            }
        }
    }

    /**
     * This method renders the Path to the selected point.
     */

    public void renderPath(){

        {
            Point destpoint =selectedPoint;
                for (Point p : allPoints) {
                    if (p.equals(selectedPoint)) {
                        destpoint = p;
                        break;
                    }
                }
            RoomerRenderer mRenderer = SetUpUI.getInstance(null).getRenderer();
                Vector3 pos = new Vector3(mRenderer.getCurrentCamera().getPosition().x,
                        mRenderer.getCurrentCamera().getPosition().y - 1,
                        mRenderer.getCurrentCamera().getPosition().z);
                mRenderer.setPoints(
                        VectorGraph.getPath(pos,
                                destpoint,
                                allPoints)
                );
            }
        }

    @Override
    public String toString() {
        return "DestinationDialog{" +
                "pointsDialog=" + pointsDialog +
                ", selectedPoint=" + selectedPoint +
                '}';
    }

}
