package com.projecttango.roomerapp.ui;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.projecttango.DataStructure.ADF;

import com.projecttango.DataStructure.Point;
import com.projecttango.DataStructure.PointProperties;
import com.projecttango.DataStructure.PosCalculator;
import com.projecttango.DataStructure.RoomerDB;
import com.projecttango.Dijkstra.VectorGraph;
import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.RoomerRenderer;


import org.rajawali3d.math.vector.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Julian Dobrot on 01.06.2016.
 * This class represents the main dialog for selecting the building and the destination point for the
 * navigation.
 */
public class DestinationDialog extends DialogFragment {


    private static Button cancel;
    private static Button accept;
    private static ListView lstDestinations;
    private static AutoCompleteTextView srcDestination;

    /**
     * The List of the Points as point objects to get the real selected point for the navigation.
     */
    private ArrayList<Point> pointsDialog = new ArrayList<Point>();

    /**
     * The List of the Tags(names) of the destination points of the Building to display them in the ListView.
     */
    private ArrayList<String>pointsDialogTag  =  new ArrayList<String>();
    private ArrayList<String> adfList = new ArrayList<String>();
    private Point selectedPoint = null;
    private String selectedPointTag = null;
    private ArrayList<Point> allPoints;
    private ListView lstBuildings;
    private AutoCompleteTextView srcBuilding;
    private boolean isBuilding = true;
    private ArrayAdapter<String> adapterBuilding;
    private Button btnBuilding;
    private Button btnDestination;
    private boolean onBuilding;
    private LinearLayout linDestinations;
    private LinearLayout linBuilding;
    private ArrayAdapter<Point> adapterSrcDestination;
    private ArrayAdapter<String> adapterSrcBuilding;

    /**
     * The ArrayAdapter which holds the String tags of the destinations.
     */
    private ArrayAdapter<String> adapterDestinationTag;


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        accept.setEnabled(false);
        lstDestinations.clearChoices();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("DEBUGGER", "Create View");

        final View destinationDialogView = inflater.inflate(R.layout.destination_point_list, null);
        final RoomerMainActivity main = SetUpUI.getInstance(null).main;



        this.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        lstDestinations = (ListView) destinationDialogView.findViewById(R.id.lstDestination);
        srcDestination = (AutoCompleteTextView) destinationDialogView.findViewById(R.id.srcDestination);

        lstBuildings = (ListView) destinationDialogView.findViewById(R.id.lstBuilding);
        srcBuilding = (AutoCompleteTextView) destinationDialogView.findViewById(R.id.srcBuilding);

        linDestinations = (LinearLayout) destinationDialogView.findViewById(R.id.linDestination);
        linBuilding = (LinearLayout) destinationDialogView.findViewById(R.id.linBuilding);

        cancel = (Button) destinationDialogView.findViewById(R.id.cancel);
        accept = (Button) destinationDialogView.findViewById(R.id.accept);
        final Typeface robotoMedium = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");

        cancel.setTypeface(robotoMedium);
        accept.setTypeface(robotoMedium);


        ArrayList<String> fullUuidList = main.mTango.listAreaDescriptions();
        Collections.reverse(fullUuidList);
        for (String uuid : fullUuidList) {
            adfList.add(new String(main.mTango.loadAreaDescriptionMetaData(uuid).get("name")));
        }

        adapterDestinationTag = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice,pointsDialogTag);
        adapterBuilding = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, adfList);
        lstDestinations.setAdapter(adapterDestinationTag);
        adapterSrcDestination = new ArrayAdapter<Point>
                (getActivity(), android.R.layout.select_dialog_item, pointsDialog);
        srcDestination.setAdapter(adapterSrcDestination);
        lstBuildings.setAdapter(adapterBuilding);
        adapterSrcBuilding = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, adfList);
        srcBuilding.setAdapter(adapterSrcBuilding);

        selectedPoint = null;
        selectedPointTag = null;
        //srcDestination.setQueryHint("Search..");

        srcBuilding.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("DEBUGGER", srcBuilding.getText().toString());
                lstBuildings.setFilterText(srcBuilding.getText().toString());
                return false;
            }
        });


        //listener for the accept button
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the selected String from the destination ListView
                selectedPointTag = (String) lstDestinations.getAdapter().getItem(lstDestinations.getCheckedItemPosition());

                // search in the List with all destination Points for the point with the selected TAG from the list.
                for (Point p : pointsDialog) {
                    if (p.getTag().equals(selectedPointTag)) {
                        selectedPoint = p;
                        break;
                    }
                }

                // call the render path method with the selected point from the ListView
                renderPath();
                dismiss();


            }
        });

        lstDestinations.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                accept.setEnabled(lstDestinations.getCheckedItemPosition() > -1);
            }
        });

        lstBuildings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectADFFile(main, position);
            }
        });


        btnBuilding = (Button) destinationDialogView.findViewById(R.id.btnBuilding);

        btnDestination = (Button) destinationDialogView.findViewById(R.id.btnDestination);

        btnBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnBuildingTab(main);

            }
        });
        btnDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnDestinationTab(main);

            }
        });
        // listener for the cancel button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }

        });

        if (!onBuilding) clickOnDestinationTab(main);
        return destinationDialogView;
    }

    /**
     * This method loads the ADF, creeates DB, loads point list and connects them to the adapter.
     * @param main
     * @param position
     */
    private void selectADFFile(RoomerMainActivity main, int position) {
        String name = adapterBuilding.getItem(position);
        String uuid = null;
        for (String adfID : main.mTango.listAreaDescriptions()) {
            if (new String(main.mTango.loadAreaDescriptionMetaData(adfID).get("name")).equals(name)) {
                uuid = adfID;
                break;
            }
        }
        main.loadAreaDescription(uuid);

        RoomerDB db = new RoomerDB(main);
        ADF adf = db.getAdf(uuid);
        Log.d("DEBUGGER", "Adf: " + adf);
        try {
            ArrayList<Point> points = db.getAllPoints();
            Iterator<Point> i = points.iterator();
            while (i.hasNext()) {
                Point p = i.next();
                if (!p.getAdf().getBuilding().equals(adf.getBuilding())) i.remove();
            }
            Log.d("DEBUGGER", "Points: " + points);
            // Log.d("DEBUGGER", points +"");
            connectAdapter(points);
        } catch (Exception e) {
            e.printStackTrace();
        }

        clickOnDestinationTab(main);
    }

    public void show(FragmentManager manager, String tag, boolean onBuilding) {
        super.show(manager, tag);
        this.onBuilding = onBuilding;
        // Log.d("DEBUGGER" , onBuilding +"");
    }

    /**
     * This method performs all activities when the destination tab is selected.
     * @param main
     */
    public void clickOnDestinationTab(RoomerMainActivity main) {
        //  Log.d("DEBUGGER","onDestination");
        btnBuilding.setBackgroundColor(Color.TRANSPARENT);
        btnDestination.setBackgroundColor(main.getResources().getColor(R.color.light_blue_roomer));
        linDestinations.setVisibility(View.VISIBLE);
        linBuilding.setVisibility(View.INVISIBLE);
    }

    /**
     * This method performs all activities when the building tab is selected.
     * @param main
     */
    public void clickOnBuildingTab(RoomerMainActivity main) {
        // Log.d("DEBUGGER","onBuilding");
        btnDestination.setBackgroundColor(Color.TRANSPARENT);
        btnBuilding.setBackgroundColor(main.getResources().getColor(R.color.light_blue_roomer));
        linBuilding.setVisibility(View.VISIBLE);
        linDestinations.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {

        //Enable the the destination button to secure that the user can reopen the DestinationDialog
        if (Icon_Segment_Fragment.segDestinations != null) Icon_Segment_Fragment.segDestinations.setEnabled(true);
        super.onDismiss(dialog);
    }

    /**
     * This methos adds all destinations to the point list and connect the adapter to the list view.
     * @param list
     */
    public void connectAdapter(ArrayList<Point> list) {

        allPoints = list;
        pointsDialog.clear();

        for (Point p : list) {
            if (p.getProperties().get(PointProperties.type).equals(PointProperties.destination)) {
                Log.d("DEBUGGER", p.toString() + "Point");

                pointsDialogTag.add(p.getTag());
                pointsDialog.add(p);

            }
        }

        lstDestinations.setAdapter(adapterDestinationTag);

    }

    /**
     * This method renders the Path to the selected point.
     */

    public void renderPath() {


        RoomerRenderer mRenderer = SetUpUI.getInstance(null).getRenderer();



        ArrayList<Point> points = new ArrayList<Point>();

        for (Point p : allPoints) {

            Point newPoint = new Point(
                    (int) p.getId(),
                    PosCalculator.newPos(mRenderer.adf,p),
                    new HashMap<Point, Double>(),
                    p.getTag(),
                    p.getProperties(),
                    p.getAdf()
            );

            points.add(newPoint);
        }

        for (Point p : allPoints) {

            int index = points.indexOf(p);

            if(index > -1) {

                Point newPoint = points.get(index);

                for (Point n : p.getNeighbours().keySet()) {
                    int nIndex = points.indexOf(n);
                    if(nIndex > -1)
                        newPoint.addNeighbour(points.get(nIndex));
                }
            }
        }

        Point destpoint = selectedPoint;
        for (Point p : points) {
            if (p.equals(selectedPoint)) {
                destpoint = p;
                Log.d("DEBUGGER", "DestPoint: " + destpoint);
                break;
            }
        }


        Log.d("DEBUGGER", "BeforeGraph: " + points);
        final RoomerMainActivity main = SetUpUI.getInstance(null).main;
        main.Destination = destpoint.getTag();
        mRenderer.setPoints(
                points, destpoint
        );

    }

    @Override
    public String toString() {
        return "DestinationDialog{" +
                "pointsDialog=" + pointsDialog +
                ", selectedPoint=" + selectedPoint +
                '}';
    }


}
