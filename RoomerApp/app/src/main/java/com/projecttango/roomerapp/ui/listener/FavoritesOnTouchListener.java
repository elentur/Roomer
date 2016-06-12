package com.projecttango.roomerapp.ui.listener;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.ui.Icon_Segment_Fragment;


/**
 * Created by Julian Dobrot on 10.06.2016.
 */
public class FavoritesOnTouchListener implements View.OnTouchListener {

    private final ImageButton imageButton;
    private final RoomerMainActivity main;

    public FavoritesOnTouchListener(Icon_Segment_Fragment icon_segment_fragment, RoomerMainActivity main) {
        this.imageButton = icon_segment_fragment.segFavorites;
        this.main = main;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("DEBUGGER","fav_down");


            imageButton.setImageResource(R.drawable.thumb_button_segment3_red);


        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Log.d("DEBUGGER","fav_up");

            imageButton.setImageResource(R.drawable.thumb_button_segment3_blu);

        }
        return false;
    }
}
