package com.projecttango.roomerapp.ui.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;


/**
 * Created by Julian Dobrot on 10.06.2016.
 */
public class FavoritesOnTouchListener implements View.OnTouchListener {

    private final ImageButton imageButton;
    private final RoomerMainActivity main;

    public FavoritesOnTouchListener(ImageButton imageButton, RoomerMainActivity main) {
        this.imageButton = imageButton;
        this.main = main;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


            imageButton.setImageResource(R.drawable.thumb_button_segment3_red);


        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            imageButton.setImageResource(R.drawable.thumb_button_segment3_blu);

        }
        return false;
    }
}
