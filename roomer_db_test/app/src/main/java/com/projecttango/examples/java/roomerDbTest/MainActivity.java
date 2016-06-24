package com.projecttango.examples.java.roomerDbTest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.j256.ormlite.dao.Dao;
import com.projecttango.DataStructure.DatabaseHelper;
import com.projecttango.DataStructure.NavigationPoint;
import com.projecttango.DataStructure.Point;
import org.rajawali3d.math.vector.Vector3;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {

    LinearLayout ll;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DatabaseHelper helper  = new DatabaseHelper(this);
        Dao<Point, Integer> pointDao = null;
        try {
            pointDao = helper.getPointDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        NavigationPoint p = new NavigationPoint(new Vector3(0,0,0),new HashMap<Point, Double>(),"test1");

        try {
            pointDao.create(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        p = new NavigationPoint(new Vector3(0,0,0),new HashMap<Point, Double>(),"test2");
        try {
            pointDao.create(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        p = new NavigationPoint(new Vector3(0,0,0),new HashMap<Point, Double>(),"test3");
        try {
            pointDao.create(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        p = new NavigationPoint(new Vector3(0,0,0),new HashMap<Point, Double>(),"test4");
        try {
            pointDao.create(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        p = new NavigationPoint(new Vector3(0,0,0),new HashMap<Point, Double>(),"test5");
        try {
            pointDao.create(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        p = new NavigationPoint(new Vector3(0,0,0),new HashMap<Point, Double>(),"test6");
        try {
            pointDao.create(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ll = (LinearLayout) findViewById(R.id.linearlayout);

        try {
            final List<Point> points = pointDao.queryForAll();

            Log.d("POINT_DB", "" + points.size());

            for(Point point : points){
                editText = new EditText(this);
                editText.setHint(point.toString());
                ll.addView(editText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
