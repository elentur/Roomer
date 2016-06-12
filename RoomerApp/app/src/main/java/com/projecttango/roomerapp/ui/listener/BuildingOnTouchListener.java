package com.projecttango.roomerapp.ui.listener;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.atap.tangoservice.TangoErrorException;
import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.StartActivity;
import com.projecttango.roomerapp.ui.Icon_Segment_Fragment;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by Julian Dobrot on 10.06.2016.
 */
public class BuildingOnTouchListener implements View.OnTouchListener {


    private final ImageButton imageButton;
    private final RoomerMainActivity main;

    public BuildingOnTouchListener(Icon_Segment_Fragment icon_segment_fragment,RoomerMainActivity main) {

        this.imageButton = icon_segment_fragment.segChangeBuilding;
        this.main=main;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            imageButton.setImageResource(R.drawable.thumb_button_segment4_red);

        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            imageButton.setImageResource(R.drawable.thumb_button_segment4_blu);

            Intent intent = new Intent(view.getContext(), StartActivity.class);
            main.startActivity(intent);

        }

        return false;
    }


}
