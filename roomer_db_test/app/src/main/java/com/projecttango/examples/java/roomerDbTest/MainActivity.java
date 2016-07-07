package com.projecttango.examples.java.roomerDbTest;

import android.app.Activity;
import android.os.Bundle;
import com.projecttango.DataStructure.*;
import org.rajawali3d.math.vector.Vector3;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        RoomerDB db = new RoomerDB(this);

        db.createADF("Haus1",new Vector3(0,0,0),"xyz");
        db.createADF("Haus1",new Vector3(0,0,0),"abc");

    }
}
