package com.projecttango.roomerapp.ui.listener;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.ui.Icon_Segment_Fragment;
import com.projecttango.roomerapp.ui.SetUpUI;


/**
 * This class represets the events of the favorites button.
 * This feature is not implemented yet. No usage except image and position changes for design.
 * Created by Julian Dobrot on 10.06.2016.
 */
public class FavoritesOnTouchListener implements View.OnTouchListener {

    private final ImageButton imageButton;
    private final RoomerMainActivity main;
    private final ThumbOnTouchListener thumbOnTouchListener;

    /**
     * This constructor creates a new favorite button with its parameters.
     * @param icon_segment_fragment
     * @param thumbOnTouchListener
     * @param main
     */
    public FavoritesOnTouchListener(Icon_Segment_Fragment icon_segment_fragment,ThumbOnTouchListener thumbOnTouchListener, RoomerMainActivity main) {

        this.imageButton = icon_segment_fragment.segFavorites;
        this.thumbOnTouchListener = thumbOnTouchListener;
        this.main = main;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            imageButton.setImageResource(R.drawable.thumb_button_segment3_red);
            thumbOnTouchListener.upaDateUI();

        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            imageButton.setImageResource(R.drawable.thumb_button_segment3_blu);

        }
        return false;
    }
}
