package com.projecttango.roomerapp.ui;

import android.view.MotionEvent;
import android.view.View;

import com.projecttango.DataStructure.Point;
import com.projecttango.Dijkstra.VectorGraph;
import com.projecttango.roomerapp.RoomerMainActivity;

import org.rajawali3d.math.vector.Vector3;

/**
 * Created by Marcus Bätz on 10.06.2016.
 */
public class DestOnTouchListener implements View.OnTouchListener {

    private final RoomerMainActivity main;
    public DestOnTouchListener(RoomerMainActivity main) {
        this.main=main;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {


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
