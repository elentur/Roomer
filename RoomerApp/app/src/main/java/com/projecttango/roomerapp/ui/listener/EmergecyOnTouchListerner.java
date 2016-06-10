package com.projecttango.roomerapp.ui.listener;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.StartActivity;

/**
 * Created by Julian Dobrot on 10.06.2016.
 */
public class EmergecyOnTouchListerner implements View.OnTouchListener {

    private final ImageButton imageButton;
    private final RoomerMainActivity main;

    public EmergecyOnTouchListerner(ImageButton imageButton, RoomerMainActivity main) {
        this.imageButton = imageButton;
        this.main = main;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            imageButton.setImageResource(R.drawable.thumb_button_segment2_red);

        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            imageButton.setImageResource(R.drawable.thumb_button_segment2_blu);


        }
        return false;
    }
}
