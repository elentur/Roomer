package com.projecttango.roomerapp.ui.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;


import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;


/**
 * This class represents the events of the screen interactions.
 * The thumbmenu appeas and disappears when touching the screen.
 * Created by Julian Dobrot on 19.06.2016.
 */
public class ScreenOnTouchListener implements View.OnTouchListener {

    private final RoomerMainActivity main;

    /**
     * The thumb button.
     */
    private ImageButton thumbButton;

    /**
     * The thumbOnListener instance.
     */
    private ThumbOnTouchListener thumbOnTouchListener;

    /**
     * The state of the thumb button.
     */
    public static boolean onOff = false;

    /**
     * This constructor represents a new ScreenOnTouchListener.
     * @param thumbButton
     * @param thumbOnTouchListener
     * @param main
     */
    public ScreenOnTouchListener(ImageButton thumbButton, ThumbOnTouchListener thumbOnTouchListener, RoomerMainActivity main) {

        this.thumbButton = thumbButton;
        this.thumbOnTouchListener = thumbOnTouchListener;
        this.main = main;

    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (onOff) {
            thumbButton.setVisibility(View.VISIBLE);
            thumbButton.setEnabled(true);
            onOff=false;
        } else if (!onOff) {

            if (thumbOnTouchListener.countClicks){
                thumbOnTouchListener.clearFragment();

            }
            thumbButton.setVisibility(View.INVISIBLE);
            thumbButton.setEnabled(false);
            onOff=true;
            thumbOnTouchListener.countClicks=false;
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {


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
