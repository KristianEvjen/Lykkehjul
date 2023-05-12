package com.wildschuetz.friederike.lykkehjul;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import static com.wildschuetz.friederike.lykkehjul.MainActivity.EXTRA_INT_OPTION;
import static com.wildschuetz.friederike.lykkehjul.MainActivity.EXTRA_INT_PIANO;
import static com.wildschuetz.friederike.lykkehjul.MainActivity.EXTRA_TIMER_ARRAY;
import static com.wildschuetz.friederike.lykkehjul.R.drawable;
import static com.wildschuetz.friederike.lykkehjul.R.id;
import static com.wildschuetz.friederike.lykkehjul.R.layout;
import static com.wildschuetz.friederike.lykkehjul.R.string;

public class ChordGeneratorActivity extends AppCompatActivity {
    int option;
    int piano;
    long[] timers;
    int pickedImage = 0, lastPicked = 0;
    Random r;
    Integer[] piano1Images = {
            drawable.a01,
            drawable.a02,
            drawable.a03,
            drawable.a04,
            drawable.a05,
            drawable.a06,
            drawable.a07,
            drawable.a08,
            drawable.a09,
            drawable.a10,
            drawable.a11,
            drawable.a12,
            drawable.b01,
            drawable.b02,
            drawable.b03,
            drawable.b04,
            drawable.b05,
            drawable.b06,
            drawable.b07,
            drawable.b08,
            drawable.b09,
            drawable.b10,
            drawable.b11,
            drawable.b12
    };
    Integer[] piano2Images = {
            drawable.c01,
            drawable.c02,
            drawable.c03,
            drawable.c04,
            drawable.c05,
            drawable.c06,
            drawable.c07,
            drawable.c08,
            drawable.c09,
            drawable.c10,
            drawable.c11,
            drawable.c12,
            drawable.d01,
            drawable.d02,
            drawable.d03,
            drawable.d04,
            drawable.d05,
            drawable.d06,
            drawable.d07,
            drawable.d08,
            drawable.d09,
            drawable.d10,
            drawable.d11,
            drawable.d12
    };
    private TextView timerText;
    private ImageView chordImageView;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_chord_generator);

        //keep the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        timers = intent.getLongArrayExtra(EXTRA_TIMER_ARRAY);
        option = intent.getIntExtra(EXTRA_INT_OPTION, 1);
        piano = intent.getIntExtra(EXTRA_INT_PIANO, 1);

        //set tempo, dynamic and option text (option text = which timer option the user has selected in main activity)
        TextView tempoTextView = findViewById(id.tempo_marking_text);
        tempoTextView.setText(string.tempo_part_six);

        TextView dynamicsTextView = findViewById(id.dynamic_marking_text);
        dynamicsTextView.setText(string.dynamics_part_six);

        TextView optionTextView = findViewById(id.option_text);
        Common.setTextFromOption(optionTextView, option);

        //make the Timer work
        timerText = findViewById(id.countdown_text_small);
        timeLeftInMilliseconds = timers[6];
        updateTimerText();
        startTimer();

        //set a random image on Image View depending on chosen piano
        r = new Random();
        chordImageView = findViewById(id.chord_image_view);

        if (piano == 1) {
            pickedImage = r.nextInt(piano1Images.length);
            chordImageView.setImageResource(piano1Images[pickedImage]);
        } else {
            pickedImage = r.nextInt(piano2Images.length);
            chordImageView.setImageResource(piano2Images[pickedImage]);
        }

        //avoid 2x same image in a row
        lastPicked = pickedImage;

        //make new image appear on click
        chordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (piano == 1) {
                    //remove duplicates
                    do {
                        pickedImage = r.nextInt(piano1Images.length);
                    } while (pickedImage == lastPicked);
                    lastPicked = pickedImage;
                    //display random image
                    chordImageView.setImageResource(piano1Images[pickedImage]);
                } else {
                    do {
                        pickedImage = r.nextInt(piano2Images.length);
                    } while (pickedImage == lastPicked);
                    lastPicked = pickedImage;
                    //display random image
                    chordImageView.setImageResource(piano2Images[pickedImage]);

                }
            }


        });

        //test Button to go to next activity
        Button testButton = findViewById(id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextActivity();
            }
        });
    }

    private void updateTimerText() {
        timerText.setText(Common.getTimerTextFromLong(timeLeftInMilliseconds));
    }

    public void startTimer() {
        endTime = System.currentTimeMillis() + timeLeftInMilliseconds;
        if (timeLeftInMilliseconds > 0) {
            countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMilliseconds = millisUntilFinished;
                    updateTimerText();
                }

                @Override
                public void onFinish() {
                    openNextActivity();
                }
            }.start();
        }
    }

    private void openNextActivity() {
        Intent intent = new Intent(this, FinishedActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getString(string.exit))
                .setMessage(getString(string.exit_question_chord_generator))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        Intent intent = new Intent(ChordGeneratorActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).create().show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("millisLeft", timeLeftInMilliseconds);
        outState.putLong("endTime", endTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        timeLeftInMilliseconds = savedInstanceState.getLong("millisLeft");
        endTime = savedInstanceState.getLong("endTime");
        timeLeftInMilliseconds = endTime - System.currentTimeMillis();
        startTimer();
    }
}
