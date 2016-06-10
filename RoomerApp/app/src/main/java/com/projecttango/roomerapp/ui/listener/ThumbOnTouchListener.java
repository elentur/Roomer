package com.projecttango.roomerapp.ui.listener;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.ui.Icon_Segment_Fragment;
import com.projecttango.roomerapp.ui.listener.DestOnTouchListener;

/**
 * Created by Marcus Bätz on 10.06.2016.
 */
public class ThumbOnTouchListener implements View.OnTouchListener {
    private final FragmentManager fragmentManager;
    private final RoomerMainActivity main;
    private boolean countClicks = false;
    private final ImageButton thumbButton;

    public ThumbOnTouchListener(RoomerMainActivity main){
        this.main=main;
        fragmentManager  = main.getFragmentManager();
        thumbButton =(ImageButton) main.findViewById(R.id.thumb_button);
    }


        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Icon_Segment_Fragment icon_segment_fragment = new Icon_Segment_Fragment();
            icon_segment_fragment.setDestinationDialogListener(new DestOnTouchListener(icon_segment_fragment.segDestinations,main));
            icon_segment_fragment.setChangeBuildingListener(new BuildingOnTouchListener(icon_segment_fragment.segChangeBuilding,main));
            if (motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                if (countClicks==false){
                    transaction.add(R.id.fragment_holder, icon_segment_fragment);
                    transaction.commit();
                    thumbButton.isActivated();
                    thumbButton.setImageResource(R.drawable.thumb_button_segment4_main);
                    countClicks = true;
                    return true;
                }
                if (countClicks = true) {
                    fragmentManager.beginTransaction().remove(
                            main.getFragmentManager().findFragmentById(R.id.fragment_holder)).commit();
                    thumbButton.setImageResource(R.drawable.thumb_button_segment3_main);
                    countClicks = false;
                    return true;
                }
            }
            return false;
        }
}
