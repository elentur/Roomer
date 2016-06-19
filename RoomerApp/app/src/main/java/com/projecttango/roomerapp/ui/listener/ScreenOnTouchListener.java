package com.projecttango.roomerapp.ui.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;


import com.projecttango.roomerapp.R;


/**
 * Created by Julian Dobrot on 19.06.2016.
 */
public class ScreenOnTouchListener implements View.OnTouchListener {

    private ImageButton thumbButton;
    private ThumbOnTouchListener thumbOnTouchListener;


    public ScreenOnTouchListener(ImageButton thumbButton, ThumbOnTouchListener thumbOnTouchListener) {

        this.thumbButton = thumbButton;
        this.thumbOnTouchListener = thumbOnTouchListener;

    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


            if (!thumbOnTouchListener.thumbButtonIsDisplayed){


                thumbButton.setImageResource(R.drawable.thumb_button_segment3_main);
                thumbButton.setX(1590);
                thumbButton.setY(600);
                thumbButton.animate().scaleX(1).scaleY(1).setDuration(1).setInterpolator(new LinearInterpolator());

            }
        }


        return false;
    }
}
