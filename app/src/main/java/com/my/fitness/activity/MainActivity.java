package com.my.fitness.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.my.fitness.R;
import com.my.fitness.controller.Controller;
import com.my.fitness.fragments.BlankFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.startButton)
    Button startButton;
    @Bind(R.id.skipButton)
    Button skipButton;
    @Bind(R.id.timerValue)
    TextView timerValue;
    @Bind(R.id.video_view)
    VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private Controller controller;
    public final String MYPREFERENCES = "my_preferences";
    public final String REP_KEY = "repetition_count";

    private BlankFragment blankFragment;
    private final String BLANK_FRAGMENT = "blank_fragment";
    private boolean skipped;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d("controller", "exc:" + getResources().getString(R.string.duration_in_sec));
        //Log.d("controller","2vexc:"+getResources().getString(R.string.duration_in_sec));
        long excerciseDuration = Long.parseLong(getResources().getString(R.string.duration_in_sec));
        blankFragment = (BlankFragment) getSupportFragmentManager()
                .findFragmentByTag(BLANK_FRAGMENT);
        if (blankFragment == null) {
            blankFragment = new BlankFragment();
            getSupportFragmentManager().beginTransaction().add(blankFragment, BLANK_FRAGMENT).commit();
            controller = new Controller(excerciseDuration);
            blankFragment.setController(controller);
        } else {
            controller = blankFragment.getController();
        }

        controller.setSetValues(new SetValues() {
            @Override
            public void setTimerValueToUI(String timeString) {
                timerValue.setText(timeString);
                if (timeString.trim().equals("00:00:00")) {
                    enterRepetionCount(true);
                }
            }
        });
        showDialog();
        if (mediaControls == null)
            mediaControls = new MediaController(this);
        try {
            myVideoView.setMediaController(mediaControls);
            myVideoView.setVideoURI(controller.getVideoUri(getPackageName()));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        myVideoView.requestFocus();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                progressDialog.dismiss();
                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {
                    myVideoView.pause();
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        controller.stopTimerThread();
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @OnClick(R.id.startButton)
    public void startButtonClickHandler() {
        if (startButton.getText().toString().trim().equals(getResources().getString(R.string.startButtonLabel))) {
            controller.handleStart();
            startButton.setText(getResources().getString(R.string.pauseButtonLabel));
        } else {
            controller.handlePause();
            startButton.setText(getResources().getString(R.string.startButtonLabel));
        }
    }

    @OnClick(R.id.skipButton)
    public void skipButtonClickHandler() {
        skipped=true;
        enterRepetionCount(false);
    }

    @Override
    public void onBackPressed() {
        if(skipped){
            super.onBackPressed();
        }else{
            enterRepetionCount(true);
        }

    }

    private void enterRepetionCount(final boolean finishActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Repetetion Count");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int repetions = 0;
                if (input.getText().toString().trim().length() != 0)
                    repetions = Integer.parseInt(input.getText().toString().trim());
                SharedPreferences sharedpreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedpreferences.edit();
                ed.putInt(REP_KEY, repetions);
                ed.commit();
                controller.stopTimerThread();
                if (finishActivity) {
                    MainActivity.this.finish();
                } else {
                    dialog.cancel();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void showDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.loading_video));
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        blankFragment.setPosition(myVideoView.getCurrentPosition());
        myVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = blankFragment.getPosition();
        myVideoView.seekTo(position);
    }

}
