package com.projecttango.roomerapp.ui;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projecttango.DataStructure.Point;
import com.projecttango.Visualisation.Visualize;
import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;

import org.rajawali3d.math.vector.Vector3;

import java.util.ArrayList;

/**
 * Created by Marcus Bätz on 10.06.2016.
 */
public class RoomerUIThread implements Runnable {

    private final SetUpUI ui;
    private final RoomerMainActivity main;

    private int countRelocationPoints = 0;

    public RoomerUIThread(RoomerMainActivity main){
        this.main = main;
        ui = SetUpUI.getInstance(main);
    }

        @Override
        public void run() {
            if (main.mIsRelocalized) {
                ui.getTxtLocalized().setVisibility(View.INVISIBLE);
                //Show Distance
                int distance = 0;
                ArrayList<Point> points = main.mRenderer.vis.getPoints();
                for (int i = 1; i < points.size(); i++) {
                    distance += Vector3.distanceTo2(
                            points.get(i - 1).getPosition(),
                            points.get(i).getPosition());
                }
                Vector3 cp = new Vector3(
                        main.mRenderer.getCurrentCamera().getPosition().x,
                        main.mRenderer.getCurrentCamera().getPosition().y - 1,
                        main.mRenderer.getCurrentCamera().getPosition().z);
                if (!points.isEmpty()) distance += Vector3.distanceTo2(
                        main.mRenderer.vis.getPoints().get(0).getPosition(),
                        cp);

                TextView lblDistance = new TextView(main.getBaseContext());
                RelativeLayout relInfo = (RelativeLayout) main.findViewById(R.id.relInfo);
                relInfo.removeAllViews();
                relInfo.addView(lblDistance);
                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams) relInfo.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                relInfo.setLayoutParams(layoutParams);
                String s = distance + "m";
                lblDistance.setText(s);

                //On arrive destination
               if(main.mRenderer.destArrive) {
                   Toast.makeText(main, "You have arrived at your Destination.", Toast.LENGTH_LONG).show();
                   Log.d("DEBUGGER", "Arrived");
                   main.mRenderer.destArrive = false;
               }

            } else {
                if (countRelocationPoints > 4) countRelocationPoints = 0;
                String s = ".";
                for (int i = 0; i < countRelocationPoints; i += 2) {
                    s += ".";
                }
                countRelocationPoints++;
                final String showString = main.getString(R.string.tryToLocate) + s;
                ui.getTxtLocalized().setText(showString);
            }

            ui.getTxtFPS().setText("FPS: " + main.mRenderer.globalFPS);
        }


}
