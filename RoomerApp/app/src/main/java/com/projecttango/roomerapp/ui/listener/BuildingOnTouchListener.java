package com.projecttango.roomerapp.ui.listener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.ui.Icon_Segment_Fragment;



/**
 * Created by Julian Dobrot on 10.06.2016.
 */
public class BuildingOnTouchListener implements View.OnTouchListener {


    private final ImageButton imageButton;
    private final RoomerMainActivity main;
    private final ThumbOnTouchListener thumbOnTouchListener;

    public BuildingOnTouchListener(Icon_Segment_Fragment icon_segment_fragment, ThumbOnTouchListener thumbOnTouchListener, RoomerMainActivity main) {

        this.imageButton = icon_segment_fragment.segChangeBuilding;
        this.thumbOnTouchListener = thumbOnTouchListener;
        this.main=main;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            imageButton.setImageResource(R.drawable.thumb_button_segment4_red);
            imageButton.setX(1595);
            imageButton.setY(418);

            thumbOnTouchListener.upaDateUI();

        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            imageButton.setImageResource(R.drawable.thumb_button_segment44_blu);
            imageButton.setX(1645);
            imageButton.setY(462);

        }

        return false;
    }


}
