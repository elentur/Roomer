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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.projecttango.DataStructure.DestinationPoint;
import com.projecttango.DataStructure.Point;
import com.projecttango.DataStructure.RoomerDB;
import com.projecttango.Dijkstra.VectorGraph;
import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.RoomerRenderer;


import org.rajawali3d.math.vector.Vector3;

import java.util.ArrayList;
import java.util.Collections;

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
    private static ListView lstDestinations;
    private static AutoCompleteTextView srcDestination;
    private static ArrayAdapter<Point> adapter;
    private ArrayList<Point> pointsDialog = new ArrayList<Point>();
    private ArrayList<String> adfList = new ArrayList<String>();
    private DestinationPoint selectedPoint = null;
    private ArrayList<Point> allPoints;
    private ListView lstBuildings;
    private AutoCompleteTextView srcBuilding;
    private boolean isBuilding =true;
    private ArrayAdapter<String> adapterBuilding;
    private Button btnBuilding;
    private Button btnDestination;
    private boolean onBuilding;
    private LinearLayout linDestinations;
    private LinearLayout linBuilding;


    @Override
    public void onResume() {
        super.onResume();

        accept.setEnabled(false);
        lstDestinations.clearChoices();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("DEBUGGER", "Create View");

        final View destinationDialogView = inflater.inflate(R.layout.destination_point_list, null);
        final RoomerMainActivity main = SetUpUI.getInstance(null).main;


        setTitel(main);


        lstDestinations = (ListView) destinationDialogView.findViewById(R.id.lstDestination);
        srcDestination = (AutoCompleteTextView) destinationDialogView.findViewById(R.id.srcDestination);

        lstBuildings = (ListView) destinationDialogView.findViewById(R.id.lstBuilding);
        srcBuilding = (AutoCompleteTextView) destinationDialogView.findViewById(R.id.srcBuilding);

        linDestinations = (LinearLayout) destinationDialogView.findViewById(R.id.linDestination);
        linBuilding = (LinearLayout) destinationDialogView.findViewById(R.id.linBuilding);

        cancel = (Button) destinationDialogView.findViewById(R.id.cancel);
        accept = (Button) destinationDialogView.findViewById(R.id.accept);
        Typeface robotoMedium = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");

        cancel.setTypeface(robotoMedium);
        accept.setTypeface(robotoMedium);


        ArrayList<String> fullUuidList =main.mTango.listAreaDescriptions();
        Collections.reverse(fullUuidList);
        for(String uuid: fullUuidList){
            adfList.add(new String(main.mTango.loadAreaDescriptionMetaData(uuid).get("name")));
        }

        adapter = new ArrayAdapter<Point>(getActivity(), android.R.layout.select_dialog_singlechoice,pointsDialog);
        adapterBuilding = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice,adfList);
        lstDestinations.setAdapter(adapter);
        ArrayAdapter<Point> adapterSrcDestination = new ArrayAdapter<Point>
                (getActivity(),android.R.layout.select_dialog_item, pointsDialog);
        srcDestination.setAdapter(adapterSrcDestination);
        lstBuildings.setAdapter(adapterBuilding);
        ArrayAdapter<String> adapterSrcBuilding = new ArrayAdapter<String>
                (getActivity(),android.R.layout.select_dialog_item, adfList);
        srcBuilding.setAdapter(adapterSrcBuilding);

        selectedPoint = null;
        //srcDestination.setQueryHint("Search..");

srcBuilding.setOnEditorActionListener(new TextView.OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Log.d("DEBUGGER",srcBuilding.getText().toString() );
        lstBuildings.setFilterText(srcBuilding.getText().toString());
        return false;
    }
});


        //listener for the accept button
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                selectedPoint = (DestinationPoint) lstDestinations.getAdapter().getItem(lstDestinations.getCheckedItemPosition());
                //setSelectedPoint(selectedPoint);
                renderPath();
                dismiss();


            }
        });

        lstDestinations.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                accept.setEnabled(lstDestinations.getCheckedItemPosition()>-1);
            }
        });

        lstBuildings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectADFFile(main,position);
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

    private void selectADFFile(RoomerMainActivity main, int position) {
        String name  = adapterBuilding.getItem(position);
        String uuid =null;
        for(String adfID : main.mTango.listAreaDescriptions()){
            if(new String(main.mTango.loadAreaDescriptionMetaData(adfID).get("name")).equals(name) ){
                uuid=adfID;
                break;
            }
        }
        main.loadAreaDescription(uuid);
        RoomerDB db  =new RoomerDB(main,uuid);
        try {
            db.importDB(main.getBaseContext());
            ArrayList<Point> points =  db.loadPoints();
           // Log.d("DEBUGGER", points +"");
            connectAdapter(points);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitel(main);
        clickOnDestinationTab(main);
    }

    private void setTitel(RoomerMainActivity main) {
        if (main.adf!=null) {
            getDialog().setTitle("Ziele in "+main.adf);
        } else {
            getDialog().setTitle("Ziele in ihrer Umgebung ");
        }
    }

    public void show(FragmentManager manager, String tag, boolean onBuilding) {
        super.show(manager, tag);
        this.onBuilding = onBuilding;
       // Log.d("DEBUGGER" , onBuilding +"");
    }

    public void clickOnDestinationTab(RoomerMainActivity main) {
      //  Log.d("DEBUGGER","onDestination");
        btnBuilding.setBackgroundColor(Color.TRANSPARENT);
        btnDestination.setBackgroundColor(main.getResources().getColor(R.color.light_blue_roomer));
        linDestinations.setVisibility(View.VISIBLE);
        linBuilding.setVisibility(View.INVISIBLE);
    }

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
       if(Icon_Segment_Fragment.segDestinations != null) Icon_Segment_Fragment.segDestinations.setEnabled(true);
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
              //  Log.d("DEBUGGER", p.getClass().getSimpleName());
                pointsDialog.add(p);
            }
        }
       // Log.d("DEBUGGER", lstDestinations.getAdapter().getCount() +"");
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
