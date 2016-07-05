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

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("test","properties");

        Point p1 = pointDao.createPoint(new Vector3(1,2,3),properties,"test1",a,b);

        Point p2 = pointDao.createPoint(new Vector3(3,2,1),properties,"test2",a,b);

        Log.d("debug", "" + pointDao.getAllPoints());

        p1.setPosition(new Vector3(4,5,6));

        pointDao.updatePoint(p1,a,b);

        Log.d("debug", "" + pointDao.getAllPoints());

        pointDao.setEdge(p1,p2);

        Log.d("debug", "" + p1.getNeighbours());

        pointDao.deleteEdge(p1,p2);


        Log.d("debug", "" + p1.getNeighbours());

    }
}
