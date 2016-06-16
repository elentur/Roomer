package com.projecttango.roomerapp.ui;

import android.widget.ImageButton;
import android.widget.TextView;

import com.google.atap.tango.ux.TangoUx;
import com.google.atap.tango.ux.TangoUxLayout;
import com.projecttango.roomerapp.Exceptions.RoomerUxExceptionEventListener;
import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.RoomerRenderer;
import com.projecttango.roomerapp.ui.listener.ThumbOnTouchListener;

/**
 * Created by Marcus Bätz on 09.06.2016.
 * Handles all initialisations from the UI
 */
public class SetUpUI {
    private TextView txtLocalized;
    private TextView txtFPS;
    private final RoomerMainActivity main;
    private static SetUpUI instance;
    private ImageButton thumbButton;
    private final DestinationDialog destinationDialog;




    private SetUpUI(RoomerMainActivity main){
        this.main = main;
        txtLocalized = (TextView) main.findViewById(R.id.txtLocalized);
        txtFPS = (TextView)main.findViewById(R.id.txtFPS);
        destinationDialog = new DestinationDialog();
        setupThumbButton();

    }
    public static SetUpUI getInstance(RoomerMainActivity main){
        if(instance==null) instance = new SetUpUI(main);
        return instance;
    }
    public void diconnectUI(){

    }

    /**
     * Sets up TangoUX layout and sets its listener.
     */
    public TangoUx setupTangoUxAndLayout() {
        TangoUxLayout uxLayout = (TangoUxLayout) main.findViewById(R.id.layout_tango);
        TangoUx tangoUx = new TangoUx(main);
        tangoUx.setLayout(uxLayout);
        tangoUx.setUxExceptionEventListener(new RoomerUxExceptionEventListener(main.TAG));
        return tangoUx;
    }


    public TextView getTxtLocalized() {
        return txtLocalized;
    }

    public TextView getTxtFPS() {
        return txtFPS;
    }

    private void setupThumbButton(){
        thumbButton = (ImageButton) main.findViewById(R.id.thumb_button);

       if(thumbButton != null) thumbButton.setOnTouchListener(new ThumbOnTouchListener(main));
    }

    public DestinationDialog getDestinationDialog(){
        return destinationDialog;
    }

    public RoomerRenderer getRenderer(){
        return main.mRenderer;
    }


}
