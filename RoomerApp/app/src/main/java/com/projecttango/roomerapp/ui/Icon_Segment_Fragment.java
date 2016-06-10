package com.projecttango.roomerapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.projecttango.roomerapp.R;


/**
 * This Fragment holds the logic for the icon segments which are active when the thumb button
 * is touched.
 * Created by Julian Dobrot on 05.06.2016.
 */
public class Icon_Segment_Fragment extends Fragment {

    private Handler handler;



    public ImageButton segDestinations;
    private ImageButton segChangeBuilding;
    private ImageButton segFavorites;
    private ImageButton segRestroom;
    private ImageButton segEmergencyExit;

    private View.OnTouchListener listener = null;


    public Icon_Segment_Fragment(){
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View icon_segment_fragment = inflater.inflate(R.layout.icon_segment_fragment,null);

        segDestinations = (ImageButton) icon_segment_fragment.findViewById(R.id.all_destinations);
        segChangeBuilding = (ImageButton) icon_segment_fragment.findViewById(R.id.change_building);
        segFavorites = (ImageButton) icon_segment_fragment.findViewById(R.id.show_favorites);
        segRestroom = (ImageButton) icon_segment_fragment.findViewById(R.id.restrooms);
        segEmergencyExit = (ImageButton) icon_segment_fragment.findViewById(R.id.emergencyExit);

       if(listener != null) segDestinations.setOnTouchListener(listener);


        return icon_segment_fragment;
    }


    public void setDestinationDialogListener(View.OnTouchListener listener){
        this.listener =listener;
        if(segDestinations != null) segDestinations.setOnTouchListener(listener);
    }


}





