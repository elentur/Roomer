package com.projecttango.DataStructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.projecttango.tangoutils.R;

import org.rajawali3d.math.vector.Vector3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by marcu_000 on 24.05.2016.
 */
public class RoomerDB extends SQLiteOpenHelper {

    private final String adf;
    public final String CREATE_TABLE;
    private boolean isCreating;
    public RoomerDB(Context context, String  adf) {
        super(context, "roomer_"+ adf +".db", null, 1);
        this.adf = adf;
        CREATE_TABLE = "CREATE TABLE Points (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "iSNAV STRING, TAG TEXT, POSX REAL, POSY REAL, POSZ REAL, NEIGHBOURS TEXT);";
        isCreating=true;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DEBUGGER", "Try to create DB");
        Log.d("DEBUGGER", CREATE_TABLE);
        try {
            db.execSQL(CREATE_TABLE);
            Log.d("DEBUGGER","Table created");
        }catch (SQLiteException e){
            Log.e("DEBUGGER" , e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(Point p){
        long rowID = -1;

        try{

            SQLiteDatabase db = getWritableDatabase();
            if(isCreating) {
                db.execSQL("DROP TABLE IF EXISTS Points");
                onCreate(db);
                isCreating=false;
            }
            Log.d("DEBUGGER","Pfad: " + db.getPath());
            ContentValues ct = new ContentValues();
            ct.put("ISNAV",(p instanceof NavigationPoint)+"");
            ct.put("TAG",p.getTag());
            ct.put("POSX",p.getPosition().x);
            ct.put("POSY",p.getPosition().y);
            ct.put("POSZ",p.getPosition().z);
            rowID = db.insert("Points",null,ct);
            //Cursor c = db.query("Points",null,null,null,null,null,"ID DESC");

        }catch (SQLiteException e){
                Log.e("DEBUGGER" , e.getMessage());
        }


    }
    public void update(ArrayList<Point> list){
        long rowID = -1;

        try{
            SQLiteDatabase db = getWritableDatabase();
            for(Point p: list){
                int id = list.indexOf(p)+1;
                String idN ="";
                for (Point n : p.getNeighbours().keySet()){
                    idN = idN + (list.indexOf(n)+1)+";";
                }

                ContentValues ct = new ContentValues();
                ct.put("NEIGHBOURS", idN);
                rowID = db.update("Points",ct,"ID="+id,null);
            }
            Cursor c = db.rawQuery("select * from Points",null);
            Log.d("DEBUGGER", Arrays.toString(c.getColumnNames()));
            if (c.moveToFirst()) {

                while (!c.isAfterLast()) {
                    String tag = c.getString(c.getColumnIndex("TAG"));
                    int id = c.getInt(c.getColumnIndex("ID"));
                    double posx = c.getDouble(c.getColumnIndex("POSX"));
                    double posy = c.getDouble(c.getColumnIndex("POSY"));
                    double posz = c.getDouble(c.getColumnIndex("POSZ"));
                    String neighbours = c.getString(c.getColumnIndex("NEIGHBOURS"));
                    Log.d("DEBUGGER",id + " " + tag + " " + posx + " " + posy + " "+ posz + " " +neighbours);
                    c.moveToNext();

                }
            }
        }catch (SQLiteException e){

        }


    }

    public void exportDB(Context context) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/Exam Creator");

        if(!direct.exists())
        {
            if(direct.mkdir())
            {
               Log.d("DEBUGGER", "Directory is created");
            }

        }

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String  currentDBPath= "/data/" + context.getPackageName() +"/databases/roomer_"+ adf +".db";
                String backupDBPath  = "/roomerDBBackups/roomer_"+ adf +".db";

                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                Log.d("DEBUGGER", "BackupPath: " + backupDB.toString());
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(context, backupDB.toString(),
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG)
                    .show();

            Log.e("DEBUGGER",  e.toString());

        }
    }

    public void importDB(Context context) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/Exam Creator");

        if(!direct.exists())
        {
            if(direct.mkdir())
            {
                Log.d("DEBUGGER", "Directory is created");
            }

        }

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data  = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String  currentDBPath= "/data/" + context.getPackageName() +"/databases/roomer_"+ adf +".db";;
                String backupDBPath  = "/roomerDBBackups/roomer_"+ adf +".db";
                File  backupDB= new File(data, currentDBPath);
                File currentDB  = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(context, backupDB.toString(),
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    public ArrayList<Point> loadPoints(){
        ArrayList<Point> points = new ArrayList<Point>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("select * from Points", null);
            if (c.moveToFirst()) {

                while (!c.isAfterLast()) {
                    String isNav = c.getString(c.getColumnIndex("iSNAV"));
                    String tag = c.getString(c.getColumnIndex("TAG"));
                    double posx = c.getDouble(c.getColumnIndex("POSX"));
                    double posy = c.getDouble(c.getColumnIndex("POSY"));
                    double posz = c.getDouble(c.getColumnIndex("POSZ"));
                    Point point;
                    if(isNav.equals("true")){
                        point = new NavigationPoint(new Vector3(posx,posy,posz),null,tag);
                    }else{
                        point = new DestinationPoint(new Vector3(posx,posy,posz),null,tag);
                    }
                    points.add(point);
                    c.moveToNext();
                }
            }
            if (c.moveToFirst()) {

                while (!c.isAfterLast()) {
                    int id = c.getInt(c.getColumnIndex("ID"));
                    String n = c.getString(c.getColumnIndex("NEIGHBOURS"));
                    String[] ids = n.split(";");
                    for(String s: ids){
                        int i = Integer.parseInt(s);
                        points.get(id-1).addNeighhbour(points.get(i-1));
                    }
                    c.moveToNext();
                }
            }
        }catch  (SQLiteException e){
                Log.e("DEBUGGER", e.getMessage());
        }
        return points;
    }
}
