package com.wildschuetz.friederike.lykkehjul;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.wildschuetz.friederike.lykkehjul.MainActivity.EXTRA_BOOLEAN_CHORD_GENERATOR;
import static com.wildschuetz.friederike.lykkehjul.MainActivity.EXTRA_BOOLEAN_PRACTICE;
import static com.wildschuetz.friederike.lykkehjul.MainActivity.EXTRA_INT_OPTION;
import static com.wildschuetz.friederike.lykkehjul.MainActivity.EXTRA_INT_PIANO;
import static com.wildschuetz.friederike.lykkehjul.MainActivity.EXTRA_TIMER_ARRAY;

public class SummaryActivity extends AppCompatActivity {
    public static final String EXTRA_COUNTER_INT = "com.wildschuetz.friederike.lykkehjul.EXTRA_COUNTER_INT";
    private long[] timers;
    private boolean chordGeneratorIsSelected = false;
    private boolean practiceLastPartIsSelected = false;
    private int piano = 1;
    private int option = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent intent = getIntent();
        timers = intent.getLongArrayExtra(EXTRA_TIMER_ARRAY);
        chordGeneratorIsSelected = intent.getBooleanExtra(EXTRA_BOOLEAN_CHORD_GENERATOR, false);
        option = intent.getIntExtra(EXTRA_INT_OPTION, 1);
        if(chordGeneratorIsSelected) {
            piano = intent.getIntExtra(EXTRA_INT_PIANO, 1);
            practiceLastPartIsSelected=intent.getBooleanExtra(EXTRA_BOOLEAN_PRACTICE, false);
        }

        //set all the texts in category layout

        TextView categoryTextView0 = findViewById(R.id.countdown_text_view);
        String text0 = getString(R.string.countdown);
        categoryTextView0.setText(text0);

        TextView categoryTextView1 = findViewById(R.id.part_one_text_view);
        String text1 = "1. " + getString(R.string.tempo_part_one) + " (" + getString(R.string.dynamics_part_one) + ")";
        categoryTextView1.setText(text1);

        TextView categoryTextView2 = findViewById(R.id.part_two_text_view);
        String text2 = "2. " + getString(R.string.tempo_part_two) + " (" + getString(R.string.dynamics_part_two) + ")";
        categoryTextView2.setText(text2);

        TextView categoryTextView3 = findViewById(R.id.part_three_text_view);
        String text3 = "3. " + getString(R.string.tempo_part_three) + " (" + getString(R.string.dynamics_part_three) + ")";
        categoryTextView3.setText(text3);

        TextView categoryTextView4 = findViewById(R.id.part_four_text_view);
        String text4 = "4. " + getString(R.string.tempo_part_four) + " (" + getString(R.string.dynamics_part_four) + ")";
        categoryTextView4.setText(text4);

        TextView categoryTextView5 = findViewById(R.id.part_five_text_view);
        String text5 = "5. " + getString(R.string.tempo_part_five) + " (" + getString(R.string.dynamics_part_five) + ")";
        categoryTextView5.setText(text5);

        TextView categoryTextView6 = findViewById(R.id.part_six_text_view);
        String text6 = "6. " + getString(R.string.tempo_part_six) + " (" + getString(R.string.dynamics_part_six) + ")";
        categoryTextView6.setText(text6);

        TextView chordGeneratorTextView = findViewById(R.id.chord_generator_text_view);
        chordGeneratorTextView.setText(getString(R.string.chord_generator));

        TextView pianoTextView = findViewById(R.id.piano_text_view);
        pianoTextView.setText(getString(R.string.piano));


        //set all the texts in selections layout
        TextView timer0TextView = findViewById(R.id.timer0_text_view);
        timer0TextView.setText(getTimerTextFromPosition(0));

        TextView timer1TextView = findViewById(R.id.timer1_text_view);
        timer1TextView.setText(getTimerTextFromPosition(1));

        TextView timer2TextView = findViewById(R.id.timer2_text_view);
        timer2TextView.setText(getTimerTextFromPosition(2));

        TextView timer3TextView = findViewById(R.id.timer3_text_view);
        timer3TextView.setText(getTimerTextFromPosition(3));

        TextView timer4TextView = findViewById(R.id.timer4_text_view);
        timer4TextView.setText(getTimerTextFromPosition(4));

        TextView timer5TextView = findViewById(R.id.timer5_text_view);
        timer5TextView.setText(getTimerTextFromPosition(5));

        TextView timer6TextView = findViewById(R.id.timer6_text_view);
        timer6TextView.setText(getTimerTextFromPosition(6));

        TextView yesNoTextView = findViewById(R.id.yes_no_text_view);
        if (chordGeneratorIsSelected) {
            yesNoTextView.setText(R.string.yes);

            pianoTextView.setVisibility(View.VISIBLE);

            TextView lastColon = findViewById(R.id.last_colon);
            lastColon.setVisibility(View.VISIBLE);

            TextView pianoPartTextView = findViewById(R.id.piano_part_text_view);
            pianoPartTextView.setVisibility(View.VISIBLE);
            pianoPartTextView.setText("" + piano);
        }
        else {
            yesNoTextView.setText(R.string.no);
        }

        if (practiceLastPartIsSelected) {

            LinearLayout part1To5Layout = findViewById(R.id.part_one_to_five_layout);
            part1To5Layout.setVisibility(View.GONE);

            LinearLayout colon1To5Layout = findViewById(R.id.colon_one_to_five_layout);
            colon1To5Layout.setVisibility(View.GONE);

            LinearLayout timer1To5Layout = findViewById(R.id.timer_one_to_five_layout);
            timer1To5Layout.setVisibility(View.GONE);
        }

        //the start button opens the timer activity
        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimerActivity();

            }
        });

    }

    //this method opens the timer activity and passes on a counter int in addition to the extras from the main activity
    private void openTimerActivity() {
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra(EXTRA_TIMER_ARRAY, timers);
        intent.putExtra(EXTRA_BOOLEAN_CHORD_GENERATOR, chordGeneratorIsSelected);
        intent.putExtra(EXTRA_INT_OPTION, option);
        int counter = 0;
        intent.putExtra(EXTRA_COUNTER_INT, counter);
        if (chordGeneratorIsSelected) {
            intent.putExtra(EXTRA_INT_PIANO, piano);
            intent.putExtra(EXTRA_BOOLEAN_PRACTICE, practiceLastPartIsSelected);
        }
        startActivity(intent);


    }

    //this method gets a timer text String from a position in a timer array
    public String getTimerTextFromPosition(int position) {
        long timeInMillis = timers[position];
        return Common.getTimerTextFromLong(timeInMillis);

    }
}
