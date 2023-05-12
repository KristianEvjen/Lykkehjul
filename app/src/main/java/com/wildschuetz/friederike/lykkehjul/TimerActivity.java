package com.wildschuetz.friederike.lykkehjul;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.wildschuetz.friederike.lykkehjul.MainActivity.EXTRA_BOOLEAN_CHORD_GENERATOR;
import static com.wildschuetz.friederike.lykkehjul.MainActivity.EXTRA_BOOLEAN_PRACTICE;
import static com.wildschuetz.friederike.lykkehjul.MainActivity.EXTRA_INT_OPTION;
import static com.wildschuetz.friederike.lykkehjul.MainActivity.EXTRA_INT_PIANO;
import static com.wildschuetz.friederike.lykkehjul.MainActivity.EXTRA_TIMER_ARRAY;
import static com.wildschuetz.friederike.lykkehjul.SummaryActivity.EXTRA_COUNTER_INT;

public class TimerActivity extends AppCompatActivity {
    private long[] timers;
    private boolean chordGeneratorIsSelected = false;
    private boolean practiceLastPartIsSelected = false;
    private int option = 1;
    private int piano = 1;
    private int counter;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        //keep the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //String arrays for tempo and dynamic strings from string.xml, that will be set on the TextViews depending on the counter
        String[] tempos = {getString(R.string.countdown), getString(R.string.tempo_part_one), getString(R.string.tempo_part_two), getString(R.string.tempo_part_three), getString(R.string.tempo_part_four), getString(R.string.tempo_part_five), getString(R.string.tempo_part_six)};
        String[] dynamics = {"", getString(R.string.dynamics_part_one), getString(R.string.dynamics_part_two), getString(R.string.dynamics_part_three), getString(R.string.dynamics_part_four), getString(R.string.dynamics_part_five), getString(R.string.dynamics_part_six)};

        //get variables from summary activity
        Intent intent = getIntent();
        timers = intent.getLongArrayExtra(EXTRA_TIMER_ARRAY);
        option = intent.getIntExtra(EXTRA_INT_OPTION, 1);
        chordGeneratorIsSelected = intent.getBooleanExtra(EXTRA_BOOLEAN_CHORD_GENERATOR, false);
        if (chordGeneratorIsSelected) {
            piano = intent.getIntExtra(EXTRA_INT_PIANO, 1);
            practiceLastPartIsSelected = intent.getBooleanExtra(EXTRA_BOOLEAN_PRACTICE, false);

        }
        counter = intent.getIntExtra(EXTRA_COUNTER_INT, 0);

        /*String timerString = "Timers: \n";
        for (int i = 0; i<timers.length; i++) {
            timerString+=timers[i] + "\n";
        }
        timerString+= "Counter: " + counter + "\n";
        timerString += "Chord generator: " + chordGeneratorIsSelected + "\n";
        if(chordGeneratorIsSelected) {
            timerString += "Piano: " + piano;
        }
        Toast t = Toast.makeText(this, timerString, Toast.LENGTH_LONG);
        t.show();
        */

        //set tempo, dynamic and option text (option text = which timer option the user has selected in main activity)
        TextView tempoTextView = findViewById(R.id.tempo_marking_text);
        tempoTextView.setText(tempos[counter]);

        TextView dynamicsTextView = findViewById(R.id.dynamic_marking_text);
        dynamicsTextView.setText(dynamics[counter]);

        TextView optionTextView = findViewById(R.id.option_text);
        Common.setTextFromOption(optionTextView, option);
        timeLeftInMilliseconds = timers[counter];
        updateTimerText();

        //start the timer
        startTimer();

        //test button to go to next activity

        Button testButton = findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                openNextActivity();
            }
        });


    }

    private void updateTimerText() {
        TextView timerText = findViewById(R.id.countdown_text);
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
                    counter++;
                    countDownTimer.cancel();
                    openNextActivity();
                }
            }.start();
        }
    }

    private void openNextActivity() {
        if ((counter == 6 && chordGeneratorIsSelected) || (counter == 1 && practiceLastPartIsSelected)) {
            Intent intent = new Intent(this, ChordGeneratorActivity.class);
            intent.putExtra(EXTRA_INT_OPTION, option);
            intent.putExtra(EXTRA_INT_PIANO, piano);
            intent.putExtra(EXTRA_TIMER_ARRAY, timers);
            startActivity(intent);
        } else if (counter == 7) {
            Intent intent = new Intent(this, FinishedActivity.class);
            startActivity(intent);
        } else {

            Intent intent = new Intent(this, TimerActivity.class);
            intent.putExtra(EXTRA_TIMER_ARRAY, timers);
            intent.putExtra(EXTRA_INT_OPTION, option);
            intent.putExtra(EXTRA_BOOLEAN_CHORD_GENERATOR, chordGeneratorIsSelected);
            intent.putExtra(EXTRA_COUNTER_INT, counter);
            if (chordGeneratorIsSelected) {
                intent.putExtra(EXTRA_INT_PIANO, piano);
            }
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.exit))
                .setMessage(getString(R.string.exit_question_timer))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        Intent intent = new Intent(TimerActivity.this, MainActivity.class);
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
        outState.putInt("counter", counter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        timeLeftInMilliseconds = savedInstanceState.getLong("millisLeft");
        counter = savedInstanceState.getInt("counter");
        endTime = savedInstanceState.getLong("endTime");
        timeLeftInMilliseconds = endTime - System.currentTimeMillis();
        startTimer();
    }

/*
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", timeLeftInMilliseconds);
        editor.putLong("endTime", endTime);
        editor.putInt("counter", counter);

        editor.apply();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        counter = prefs.getInt("counter", 0);
        timeLeftInMilliseconds = prefs.getLong("millisLeft", timers[counter]);
        updateTimerText();

        endTime = prefs.getLong("endTime", 0);
        timeLeftInMilliseconds = endTime - System.currentTimeMillis();

        if (timeLeftInMilliseconds < 0) {
            timeLeftInMilliseconds = 0;
            updateTimerText();
        } else {
            startTimer();
        }
    }

*/
}
