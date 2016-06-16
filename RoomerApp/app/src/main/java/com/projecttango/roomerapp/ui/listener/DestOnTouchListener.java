package com.projecttango.roomerapp.ui.listener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.projecttango.DataStructure.Point;
import com.projecttango.Dijkstra.VectorGraph;
import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.ui.Icon_Segment_Fragment;
import com.projecttango.roomerapp.ui.SetUpUI;

import org.rajawali3d.math.vector.Vector3;

/**
 * Created by Marcus Bätz on 10.06.2016.
 */
public class DestOnTouchListener implements View.OnTouchListener {



    private final ImageButton imageButton;
    private final RoomerMainActivity main;
    public DestOnTouchListener(Icon_Segment_Fragment icon_segment_fragment, RoomerMainActivity main) {

        this.imageButton = icon_segment_fragment.segDestinations;
        this.main=main;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {



        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            imageButton.setImageResource(R.drawable.thumb_button_segment5_red);


        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            imageButton.setImageResource(R.drawable.thumb_button_segment5_blu);

            //Disable button to secure that the DestinationDialog not will be opened again before closing it.
            imageButton.setEnabled(false);

            main.mRenderer.clear=true;
            SetUpUI.getInstance(main).getDestinationDialog().show(main.getFragmentManager(), "Ziele");
            main.firstTimeloaded = true;
            if (main.points.size() > 0) {
                SetUpUI.getInstance(main).getDestinationDialog().connectAdapter(main.points);
            }
        }
        return false;
    }


}
