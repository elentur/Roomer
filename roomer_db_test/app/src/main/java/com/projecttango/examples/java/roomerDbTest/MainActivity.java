package com.projecttango.examples.java.roomerDbTest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.projecttango.DataStructure.*;
import org.rajawali3d.math.vector.Vector3;

import java.sql.SQLException;
import java.util.HashMap;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        BuildingsDataSource buildingDao = new BuildingsDataSource(this);
        try {
            buildingDao.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Building b = buildingDao.createBuilding("Test");


        ADFDataSource adfDao = new ADFDataSource(this);
        try {
            adfDao.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ADF a = adfDao.createADF("adfTest", b);

        PointsDataSource pointDao = new PointsDataSource(this);
        try {
            pointDao.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pointDao.createPoint(new Vector3(1,2,3),new HashMap<Point, Double>(),"test1",a,b);

        pointDao.createPoint(new Vector3(3,2,1),new HashMap<Point, Double>(),"test2",a,b);

        Log.d("debug", "" + pointDao.getAllPoints());


    }
}
