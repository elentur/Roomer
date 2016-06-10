package com.projecttango.roomerapp.ui.listener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.ui.SetUpUI;

/**
 * Created by Julian Dobrot on 10.06.2016.
 */
public class RestroomOnTouchListener implements View.OnTouchListener {
    private ImageButton imageButton;
    private RoomerMainActivity main;

    public RestroomOnTouchListener(ImageButton imageButton, RoomerMainActivity main) {
        this.imageButton = imageButton;
        this.main = main;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            imageButton.setImageResource(R.drawable.thumb_button_segment1_red);

        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            imageButton.setImageResource(R.drawable.thumb_button_segment1_blu);

        }

        return false;
    }
}
