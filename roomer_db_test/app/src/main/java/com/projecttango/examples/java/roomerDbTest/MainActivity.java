package com.projecttango.examples.java.roomerDbTest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.projecttango.DataStructure.*;
import org.rajawali3d.math.vector.Vector3;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        RoomerDB db = new RoomerDB(this);

        Building b = db.createBuilding("Haus");

        db.setBuilding(b);
        db.setAdf(db.createADF(new Vector3(0,0,0),"xyz"));

        HashMap<String, Object> properties = new HashMap<String, Object>();

        properties.put("type", "navigation");

        Point p1 = db.createPoint(new Vector3(1,2,3),properties,"test1");
        Point p2 = db.createPoint(new Vector3(2,3,4),properties,"test2");
        Point p3 = db.createPoint(new Vector3(3,4,5),properties,"test3");
        Point p4 = db.createPoint(new Vector3(4,5,6),properties,"test4");
        Point p5 = db.createPoint(new Vector3(5,6,7),properties,"test5");

        db.createEdge(p1,p2);
        db.createEdge(p2,p3);
        db.createEdge(p3,p4);
        db.createEdge(p4,p5);
        db.createEdge(p5,p1);

        List<Point> points = db.getAllPoints();

        Log.d("DEBUGGER_ALL",points.toString());

        for(Point p : points){
            Log.d("DEBUGGER_P",p.toString());
            Log.d("DEBUGGER_N",p.getNeighbours().toString());
        }

        db.deleteBuilding(b);

    }
}
