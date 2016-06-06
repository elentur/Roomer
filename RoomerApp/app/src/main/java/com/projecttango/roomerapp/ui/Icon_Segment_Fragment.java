package com.projecttango.roomerapp.ui;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.projecttango.roomerapp.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This Fragment holds the logic for the icon segments which are active when the thumb button
 * is touched.
 * Created by Julian Dobrot on 05.06.2016.
 */
public class Icon_Segment_Fragment extends Fragment {



    public ImageButton segDestinations;
    private ImageButton segChangeBuilding;
    private ImageButton segFavorites;
    private ImageButton segRestroom;
    private ImageButton segEmergencyExit;


    public Icon_Segment_Fragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View icon_segment_fragment = inflater.inflate(R.layout.icon_segment_fragment,null);

        segDestinations = (ImageButton) icon_segment_fragment.findViewById(R.id.all_destinations);


        segDestinations.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {



                return false;
            }
        });




        return icon_segment_fragment;
    }




}





