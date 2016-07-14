package com.projecttango.roomerapp.ui.listener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.ui.DestinationDialog;
import com.projecttango.roomerapp.ui.HelpDialog;
import com.projecttango.roomerapp.ui.Icon_Segment_Fragment;
import com.projecttango.roomerapp.ui.SetUpUI;

/**
 * This Class represents the Listener for the events and actions occur when the help button is clicked.
 * Created by Julian Dobrot on 10.06.2016.
 */
public class HelpOnTouchListener implements View.OnTouchListener {
    private final ImageButton imageButton;
    private final RoomerMainActivity main;
    private final ThumbOnTouchListener thumbOnTouchListener;


    public HelpOnTouchListener(Icon_Segment_Fragment icon_segment_fragment, ThumbOnTouchListener thumbOnTouchListener, RoomerMainActivity main) {

        this.imageButton = icon_segment_fragment.segRestroom ;
        this.thumbOnTouchListener = thumbOnTouchListener;
        this.main = main;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            imageButton.setImageResource(R.drawable.thumb_button_segment1_red);
            thumbOnTouchListener.upaDateUI();
            HelpDialog helpDialog = SetUpUI.getInstance(main).getHelpDialog();
            helpDialog.show(main.getFragmentManager(),"help");

        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            imageButton.setImageResource(R.drawable.thumb_button_segment1_blu);

        }

        return false;
    }
}
