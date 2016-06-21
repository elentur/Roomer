package com.projecttango.roomerapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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


    public static ImageButton segDestinations;
    public static ImageButton segChangeBuilding;
    public static ImageButton segFavorites;
    public static ImageButton segRestroom;
    public static ImageButton segEmergencyExit;

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

    public void setChangeBuildingListener(View.OnTouchListener listener) {
        this.listener = listener;

        if (segChangeBuilding !=null) segChangeBuilding.setOnTouchListener(listener);
    }

    public void setFavoritesListener(View.OnTouchListener listener) {
        this.listener = listener;

        if (segFavorites !=null) segFavorites.setOnTouchListener(listener);

    }

    public void setRestroomListener(View.OnTouchListener listener) {
        this.listener = listener;

        if (segRestroom !=null) segRestroom.setOnTouchListener(listener);

    }
    public void setEmergenyExitListener(View.OnTouchListener listener) {
        this.listener = listener;

        if (segEmergencyExit !=null) segEmergencyExit.setOnTouchListener(listener);

    }

    @Override
    public String toString() {
        return "Icon_Segment_Fragment{" +
                "listener=" + listener +
                '}';
    }
}





