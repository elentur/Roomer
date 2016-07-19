package com.projecttango.roomerapp.ui.listener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.projecttango.DataStructure.Point;
import com.projecttango.Dijkstra.VectorGraph;
import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.ui.DestinationDialog;
import com.projecttango.roomerapp.ui.Icon_Segment_Fragment;
import com.projecttango.roomerapp.ui.SetUpUI;

import org.rajawali3d.math.vector.Vector3;

/**
 * This class handles the events of the destination button.
 * Created by Marcus Bätz on 10.06.2016.
 */
public class DestOnTouchListener implements View.OnTouchListener {


    /**
     * The button representing the destination button.
     */
    private final ImageButton imageButton;
    private final RoomerMainActivity main;

    /**
     * The button representing the thumb button to call the update ui method.
     */
    private final ThumbOnTouchListener thumbOnTouchListener;

    /**
     * This constructor creates a new DestOnTouchListener with params of the relationships.
     * @param icon_segment_fragment
     * @param thumbOnTouchListener
     * @param main
     */
    public DestOnTouchListener(Icon_Segment_Fragment icon_segment_fragment,ThumbOnTouchListener thumbOnTouchListener, RoomerMainActivity main) {

        this.imageButton = icon_segment_fragment.segDestinations;
        this.thumbOnTouchListener = thumbOnTouchListener;
        this.main=main;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            imageButton.setImageResource(R.drawable.thumb_button_segment5_red);
            // resets the timer on the removing animation of the entire thumb menu
            thumbOnTouchListener.upaDateUI();


        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            imageButton.setImageResource(R.drawable.thumb_button_segment5_blu);

            //Disable button to secure that the DestinationDialog not will be opened again before closing it.
            imageButton.setEnabled(false);

            main.mRenderer.clear=true;
            DestinationDialog dialog = SetUpUI.getInstance(main).getDestinationDialog();
            dialog.show(main.getFragmentManager(), "Ziele",false);
            main.firstTimeloaded = true;
            if (main.points.size() > 0) {
                dialog.connectAdapter(main.points);
            }
        }
        return false;
    }


}
