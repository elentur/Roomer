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

        Point p1 = new Point(new Vector3(1,2,3),new HashMap<Point, Double>(),"test1");
        p1.setProperty("navigation", true);

        Point p2 = new Point(new Vector3(3,2,1),new HashMap<Point, Double>(),"test2");
        p2.setProperty("navigation", true);

        p1.addNeighhbour(p2);
        p2.addNeighhbour(p1);

        try {
            pointDao = helper.getPointDao();
            pointDao.create(p1);
            pointDao.create(p2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ll = (LinearLayout) findViewById(R.id.linearlayout);

        try {
            final List<Point> points = pointDao.queryForAll();
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
