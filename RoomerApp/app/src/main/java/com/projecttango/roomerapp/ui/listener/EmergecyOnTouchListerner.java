package com.projecttango.roomerapp.ui.listener;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.StartActivity;
import com.projecttango.roomerapp.ui.Icon_Segment_Fragment;

/**
 * Created by Julian Dobrot on 10.06.2016.
 */
public class EmergecyOnTouchListerner implements View.OnTouchListener {

    private final ImageButton imageButton;
    private final RoomerMainActivity main;

    public EmergecyOnTouchListerner(Icon_Segment_Fragment icon_segment_fragment, RoomerMainActivity main) {
        this.imageButton = icon_segment_fragment.segEmergencyExit;
        this.main = main;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            main.setDebug(view);

            imageButton.setImageResource(R.drawable.thumb_button_segment2_red);

        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            imageButton.setImageResource(R.drawable.thumb_button_segment2_blu);


        }
        return false;
    }
}
