package com.projecttango.roomerapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.atap.tangoservice.Tango;
import com.google.atap.tangoservice.TangoAreaDescriptionMetaData;
import com.google.atap.tangoservice.TangoConfig;
import com.google.atap.tangoservice.TangoErrorException;


import java.util.ArrayList;
import java.util.Collections;

import com.projecttango.utils.Constants;

public class StartActivity extends Activity {
    private Tango mTango;
    private ListView lstView;
    private ArrayAdapter<String> adapter;

    ArrayList<String> fullUuidList;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTango = new Tango(StartActivity.this, new Runnable() {
            // Pass in a Runnable to be called from UI thread when Tango is ready,
            // this Runnable will be running on a new thread.
            // When Tango is ready, we can call Tango functions safely here only
            // when there is no UI thread changes involved.
            @Override
            public void run() {
                try {
                    TangoConfig config;
                    config = mTango.getConfig(TangoConfig.CONFIG_TYPE_DEFAULT);

                    mTango.connect(config);
                   fullUuidList = mTango.listAreaDescriptions();
                    Collections.reverse(fullUuidList);
                    ArrayList<String> adfNames = new ArrayList<String>();
                    for(String uuid: fullUuidList){
                        adfNames.add(new String(mTango.loadAreaDescriptionMetaData(uuid).get("name")));
                    }
                    adapter = new ArrayAdapter<String>(StartActivity.this,
                            android.R.layout.simple_list_item_single_choice,adfNames);
                    lstView = (ListView)findViewById(R.id.listView);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lstView.setAdapter(adapter);
                            lstView.setItemChecked(0, true);
                        }
                    });
                } catch (TangoErrorException e) {
                    Log.e("OnStart", getString(R.string.exception_tango_error), e);
                } catch (SecurityException e) {
                    Log.e("OnStart", getString(R.string.permission_motion_tracking), e);
                }

            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        startActivityForResult(
                Tango.getRequestPermissionIntent(Tango.PERMISSIONTYPE_ADF_LOAD_SAVE), 0);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void start(View view){
        mTango.disconnect();

        Intent i = new Intent(this, RoomerMainActivity.class);
        i.putExtra("uuid",fullUuidList.get(lstView.getCheckedItemPosition()));
        startActivity(i);
        passData();


    }

    /**
     * This method can be used to pass data to other activities, like used data...At the moment the name of the selected
     * adf is saved in the prefs.
     */
    private void passData() {
        SharedPreferences.Editor editor = getSharedPreferences(Constants.ROOMER_PREFS, MODE_PRIVATE).edit();

        String name = getName(fullUuidList.get(lstView.getCheckedItemPosition()));
        editor.putString( "name",name);
        editor.commit();

    }

    /**
     * This method returns the name of a passed adf file. The name is the saved string given by the user when saving the ADF
     * in this case, not the name of the adf in the array.
      * @param uuid The ADF we want the name from.
     * @return The name of the ADF
     */
    public String getName(String uuid) {

        TangoAreaDescriptionMetaData metadata = new TangoAreaDescriptionMetaData();
        metadata = mTango.loadAreaDescriptionMetaData(uuid);
        byte[] nameBytes = metadata.get(TangoAreaDescriptionMetaData.KEY_NAME);
        if (nameBytes != null) {
            String name = new String(nameBytes);

            return name;
        }
        return "byteArrempty";
    }



}
