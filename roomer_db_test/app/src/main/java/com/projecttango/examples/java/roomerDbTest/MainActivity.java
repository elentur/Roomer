package com.projecttango.examples.java.roomerDbTest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.j256.ormlite.dao.Dao;
import com.projecttango.DataStructure.DatabaseHelper;
import com.projecttango.DataStructure.Point;
import com.projecttango.DataStructure.Point2Point;
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

        Dao<Point2Point, Integer> neighbourDao = null;

        Point p1 = new Point(new Vector3(1,2,3),new HashMap<Point, Double>(),"test1");
        p1.setProperty("navigation", true);

        Point p2 = new Point(new Vector3(3,2,1),new HashMap<Point, Double>(),"test2");
        p2.setProperty("navigation", true);

        p1.addNeighhbour(p2);
        p2.addNeighhbour(p1);

        try {
            pointDao = helper.getPointDao();
            neighbourDao = helper.getPoint2PointDao();
            pointDao.create(p1);
            pointDao.create(p2);

            for(Point n : p1.getNeighbours().keySet()){
                try {
                    neighbourDao.create(new Point2Point(p1, n));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            for(Point n : p2.getNeighbours().keySet()){
                Log.d("GETNEIGHBOUR_DB", "" + n);
                Point2Point neighbour = new Point2Point(p2, n);
                Log.d("Point2Point", "" + neighbour);
                try {
                    neighbourDao.create(neighbour);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ll = (LinearLayout) findViewById(R.id.linearlayout);

        try {
            final List<Point> points = pointDao.queryForAll();

            Log.d("POINTS_DB", "" + points.size());

            for(Point point : points){
                Log.d("POINT_DB", "" + point);

                editText = new EditText(this);
                editText.setHint(point.toString());
                ll.addView(editText);
            }

            final List<Point2Point> neighbour = neighbourDao.queryForAll();
            Log.d("NEIGHBOUR_DB", "" + neighbour.size());
            Log.d("NEIGHBOUR_DB", "" + neighbour.toString());



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
