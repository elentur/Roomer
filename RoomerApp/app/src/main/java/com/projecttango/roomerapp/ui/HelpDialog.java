package com.projecttango.roomerapp.ui;

import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.projecttango.roomerapp.R;


/**
 * This class represents the Help Dialog it shows basic information about the application.
 * Created by Julian Dobrot on 13.07.2016.
 */
public class HelpDialog extends DialogFragment {

    /**
     * The button to close the Help Dialog.
     */
    private Button close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final View helpDialogView = inflater.inflate(R.layout.help_dialog,null);
        final Typeface robotoMedium = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");

        close = (Button) helpDialogView.findViewById(R.id.close_help_dialog);
        close.setTypeface(robotoMedium);

        this.getDialog().setTitle("Information");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return helpDialogView;
    }
}
