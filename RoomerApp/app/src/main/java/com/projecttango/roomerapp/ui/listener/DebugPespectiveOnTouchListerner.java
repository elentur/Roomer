package com.projecttango.roomerapp.ui.listener;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.ui.Icon_Segment_Fragment;

/**
 * This class handles the events on the Debug button. It shows all points with their relationships.
 * Created by Julian Dobrot on 10.06.2016.
 */
public class DebugPespectiveOnTouchListerner implements View.OnTouchListener {

    /**
     * This button represets the Debug button
     */
    private final ImageButton imageButton;

    /**
     * instance of the main.
     */
    private final RoomerMainActivity main;

    /**
     * The thumb button instance.
     */
    private final ThumbOnTouchListener thumbOnTouchListener;

    /**
     * This constructor creates a new DebugPerspectiveOnTouchLister with its parameters.
     * @param icon_segment_fragment
     * @param thumbOnTouchListener
     * @param main
     */

    public DebugPespectiveOnTouchListerner(Icon_Segment_Fragment icon_segment_fragment, ThumbOnTouchListener thumbOnTouchListener, RoomerMainActivity main) {

        this.imageButton = icon_segment_fragment.segEmergencyExit;
        this.thumbOnTouchListener = thumbOnTouchListener;
        this.main = main;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            Log.d("DEBUGGER", "debug");
            main.setDebug(view);

            imageButton.setImageResource(R.drawable.thumb_button_segment2_red);
            thumbOnTouchListener.upaDateUI();

        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            imageButton.setImageResource(R.drawable.thumb_button_segment2_blu);


        }
        return false;
    }
}
