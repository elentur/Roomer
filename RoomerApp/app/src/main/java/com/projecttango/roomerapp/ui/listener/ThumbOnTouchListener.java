package com.projecttango.roomerapp.ui.listener;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.Toast;

import com.projecttango.roomerapp.R;
import com.projecttango.roomerapp.RoomerMainActivity;
import com.projecttango.roomerapp.ui.Icon_Segment_Fragment;

/**
 * Created by Marcus Bätz on 10.06.2016.
 *
 */
public class ThumbOnTouchListener implements View.OnTouchListener {

    private final FragmentManager fragmentManager;
    private final RoomerMainActivity main;

    public static boolean countClicks = false;
    private final ImageButton thumbButton;
    private Handler handler = new Handler();
    public static boolean thumbButtonIsDisplayed=true;

    public ThumbOnTouchListener(RoomerMainActivity main){
        this.main=main;
        fragmentManager = main.getFragmentManager();
        thumbButton = (ImageButton) main.findViewById(R.id.thumb_button);
    }


        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            FragmentTransaction transaction = fragmentManager.beginTransaction();

            Icon_Segment_Fragment icon_segment_fragment = new Icon_Segment_Fragment();

            icon_segment_fragment.setDestinationDialogListener(new DestOnTouchListener(icon_segment_fragment,this, main));
            icon_segment_fragment.setChangeBuildingListener(new BuildingOnTouchListener(icon_segment_fragment, this, main));
            icon_segment_fragment.setFavoritesListener(new FavoritesOnTouchListener(icon_segment_fragment,this, main ));
            icon_segment_fragment.setEmergenyExitListener(new EmergecyOnTouchListerner(icon_segment_fragment,this, main));
            icon_segment_fragment.setRestroomListener(new RestroomOnTouchListener(icon_segment_fragment,this, main));

            if (motionEvent.getAction()==MotionEvent.ACTION_DOWN) {

                upaDateUI();

                if (!countClicks){
                    transaction.add(R.id.fragment_holder, icon_segment_fragment);
                    transaction.commit();
                    thumbButton.isActivated();
                    thumbButton.setImageResource(R.drawable.thumb_button_segment4_main);
                    countClicks = true;
                    return true;
                }
                if (countClicks) {

                    clearFragment();
                    fragmentManager.beginTransaction().remove(
                            main.getFragmentManager().findFragmentById(R.id.fragment_holder)).commit();
                    thumbButton.setImageResource(R.drawable.thumb_button_segment3_main);
                    countClicks = false;
                    return true;
                }
            }
            return false;
        }

    public void clearFragment() {
        fragmentManager.beginTransaction().remove(
                main.getFragmentManager().findFragmentById(R.id.fragment_holder)).commit();
        thumbButton.setImageResource(R.drawable.thumb_button_segment3_main);
        countClicks = false;
    }


    /**
     * This method is called when the thumb button is touched. It holds a Thread which is
     * called after a specific time.u
     */
    public void upaDateUI() {
        handler.removeCallbacks(updateTimeTask);
        handler.postDelayed(updateTimeTask,10000);
    }
    private Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            update();

        }
    };

    /**
     * This method performs all activities inside of the runnable called by the updateUI method.
     *
     */
    private void update() {

        thumbButton.setImageResource(R.drawable.knob);
        thumbButton.setX(1700);
        thumbButton.setY(700);


        createAnimation(Icon_Segment_Fragment.segDestinations,-83,265);
        createAnimation(Icon_Segment_Fragment.segChangeBuilding,50,225);
        createAnimation(Icon_Segment_Fragment.segFavorites,145,145);
        createAnimation(Icon_Segment_Fragment.segEmergencyExit,225,0);
        createAnimation(Icon_Segment_Fragment.segRestroom,258,-100);

    }

    /**
     * This method creates the animations for the imagebuttons.
     * @param imgBtn
     * @param xValue
     * @param yValue
     */
    private void createAnimation(ImageButton imgBtn, float xValue, float yValue) {

        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Icon_Segment_Fragment.segDestinations.setVisibility(View.INVISIBLE);
                Icon_Segment_Fragment.segChangeBuilding.setVisibility(View.INVISIBLE);
                Icon_Segment_Fragment.segEmergencyExit.setVisibility(View.INVISIBLE);
                Icon_Segment_Fragment.segFavorites.setVisibility(View.INVISIBLE);
                Icon_Segment_Fragment.segRestroom.setVisibility(View.INVISIBLE);

                thumbButton.animate().scaleX(2/3).scaleY(2/3).setDuration(150).setInterpolator(new AccelerateInterpolator());
                ScreenOnTouchListener.onOff=true;
                thumbButtonIsDisplayed=false;
                countClicks=false;


            }

            @Override
            public void onAnimationRepeat(Animation animation) {


            }

        };

        Animation animation = new TranslateAnimation(0,xValue,0,yValue);
        animation.setDuration(800);
        animation.setInterpolator(new LinearInterpolator());
        animation.setAnimationListener(animationListener);
        imgBtn.startAnimation(animation);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThumbOnTouchListener that = (ThumbOnTouchListener) o;

        if (countClicks != that.countClicks) return false;
        if (fragmentManager != null ? !fragmentManager.equals(that.fragmentManager) : that.fragmentManager != null)
            return false;
        if (main != null ? !main.equals(that.main) : that.main != null) return false;
        return thumbButton != null ? thumbButton.equals(that.thumbButton) : that.thumbButton == null;

    }

    @Override
    public int hashCode() {
        int result = fragmentManager != null ? fragmentManager.hashCode() : 0;
        result = 31 * result + (main != null ? main.hashCode() : 0);
        result = 31 * result + (countClicks ? 1 : 0);
        result = 31 * result + (thumbButton != null ? thumbButton.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ThumbOnTouchListener{" +
                "fragmentManager=" + fragmentManager +
                ", main=" + main +
                ", countClicks=" + countClicks +
                ", thumbButton=" + thumbButton +
                '}';
    }
}
