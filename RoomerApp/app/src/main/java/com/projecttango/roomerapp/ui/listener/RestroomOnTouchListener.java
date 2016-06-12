package com.projecttango.roomerapp.ui.listener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.ui.Icon_Segment_Fragment;
import com.projecttango.roomerapp.ui.SetUpUI;

/**
 * Created by Julian Dobrot on 10.06.2016.
 */
public class RestroomOnTouchListener implements View.OnTouchListener {
    private ImageButton imageButton;
    private RoomerMainActivity main;

    public RestroomOnTouchListener(Icon_Segment_Fragment icon_segment_fragment, RoomerMainActivity main) {
        this.imageButton = icon_segment_fragment.segRestroom ;
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
