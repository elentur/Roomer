package com.projecttango.roomerapp.ui;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projecttango.DataStructure.Point;
import com.projecttango.Visualisation.ToastHandler;
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

    private static int countRelocationPoints = 0;

    public RoomerUIThread(RoomerMainActivity main){
        this.main = main;
        ui = SetUpUI.getInstance(main);
    }

        @Override
        public void run() {
            if (main.mIsRelocalized) {
                ui.getTxtLocalized().setVisibility(View.INVISIBLE);
                //Show Distance
                if(!main.mRenderer.destArrive && !main.Destination.equals("")){


                    //On arrive destination

                }if (main.mRenderer.destArrive) {
                    Toast.makeText(main, "You have arrived at your Destination.", Toast.LENGTH_LONG).show();
                    Log.d("DEBUGGER", "Arrived");
                    main.mRenderer.vis.clear(main.mRenderer.getCurrentScene());
                    main.mRenderer.destArrive=false;
                }
            } else {

                ui.getTxtLocalized().setVisibility(View.VISIBLE);
                if (countRelocationPoints > 4) {
                    countRelocationPoints = 0;
                }
                String s = ".";
                for (int i = 0; i < countRelocationPoints; i += 2) {
                    s += ".";
                }
                countRelocationPoints++;

                final String showString = main.getString(R.string.tryToLocate) + s;
                ui.getTxtLocalized().setText(showString);
            }

            ui.getTxtFPS().setText("FPS: " + main.mRenderer.globalFPS);
            try {
                if (main.mRenderer.vis.changeADF) {
                    new ToastHandler(
                            main,
                            "neues adf geladen: " + main.mRenderer.vis.adf.getName()
                            , Toast.LENGTH_LONG);
                    main.mRenderer.vis.changeADF = false;
                    main.loadAreaDescription(main.mRenderer.vis.adf.getUuid());


                }
            }catch(Exception e){

            }
            if(main.mRenderer != null && main.mRenderer.vis !=null && main.mRenderer.adf!=null){
            int distance = main.mRenderer.vis.getDistance(main.mRenderer.getCurrentCamera());

            TextView lblDistance = new TextView(main.getBaseContext());
            lblDistance.setTextSize(32);
            lblDistance.setTextColor(Color.BLACK);
            RelativeLayout relInfo = (RelativeLayout) main.findViewById(R.id.relInfo);
            relInfo.removeAllViews();
            relInfo.addView(lblDistance);
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) relInfo.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            relInfo.setLayoutParams(layoutParams);
            String s = "Destination: " + main.Destination + "  " + distance + "m"
                    + " Nextpoint: " + main.mRenderer.vis.nextPoint + " ADF: " + main.mRenderer.adf.getName()
                    + " SetNextPoint: " + main.mRenderer.nextPoint;
            lblDistance.setText(s);
                }
        }




}
