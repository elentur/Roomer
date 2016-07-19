package com.projecttango.Visualisation;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by Marcus Bätz on 13.07.2016.
 */
public class ToastHandler extends Handler {

    public ToastHandler(final Context context, final String text, final int time){
        super(Looper.getMainLooper());

        this.post(new Runnable() {
                         @Override
                         public void run() {
                             Toast.makeText(context, text,
                                     time).show();
                         }
                     }
        );
    }


}
