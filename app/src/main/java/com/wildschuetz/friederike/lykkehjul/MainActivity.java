package com.wildschuetz.friederike.lykkehjul;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String EXTRA_TIMER_ARRAY = "com.wildschuetz.friederike.lykkehjul.EXTRA_TIMER_ARRAY";
    public static final String EXTRA_BOOLEAN_CHORD_GENERATOR = "com.wildschuetz.friederike.lykkehjul.EXTRA_BOOLEAN_CHORD_GENERATOR";
    public static final String EXTRA_INT_PIANO = "com.wildschuetz.friederike.lykkehjul.EXTRA_INT_PIANO";
    public static final String EXTRA_INT_OPTION = "com.wildschuetz.friederike.lykkehjul.EXTRA_INT_OPTION";
    public static final String EXTRA_BOOLEAN_PRACTICE = "com.wildschuetz.friederike.lykkehjul.EXTRA_BOOLEAN_PRACTICE";

    //countdown times in milliseconds for the different options, the initial countdown is here for all set to 15 seconds
    private long[] timersOptionOneOriginal = {15000, 90000, 120000, 150000, 180000, 210000, 240000};
    private long[] timersOptionTwoThreeQuarter = {15000, 67500, 90000, 112500, 135000, 157500, 180000};
    private long[] timersOptionThreeHalf = {15000, 45000, 60000, 75000, 90000, 105000, 120000};
    private long[] timersOptionFourDecreasing = {15000, 90000, 80000, 70000, 60000, 50000, 40000};
    private long[] timersTest = {15000, 5000, 10000, 7000, 9000, 6000, 25000};

    //times for the initial countdown is displayed in Spinner, the corresponding Strings are in strings.xml
    private long[] countdownTimes = {5000, 10000, 15000, 20000, 25000, 30000};

    //checkbox to make sure flight mode is selected
    private CheckBox flightModeChecker;
    //checkbox for the possibility to practice only the last part (to get a feel for the chord generator
    private CheckBox practiceChecker;
    //radio buttons for the timer options
    private RadioButton testRadioButton, optionOneRadioButton, optionTwoRadioButton, optionThreeRadioButton, optionFourRadioButton;
    //radio group for piano selection
    private RadioGroup pianoRadioGroup;

    // default selections
    private int selectedTimerOption = 1;
    private long[] selectedTimers = timersOptionOneOriginal;
    private long selectedCountdownTime = 15000;
    private boolean chordGeneratorIsSelected = false;
    private boolean practiceLastPartIsSelected = false;
    private int selectedPiano = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flightModeChecker = findViewById(R.id.flight_mode_checkbox);
        final Toast flightModeToast = Toast.makeText(MainActivity.this, getResources().getString(R.string.flight_mode_toast), Toast.LENGTH_SHORT);

        testRadioButton = findViewById(R.id.radio_test);
        testRadioButton.setVisibility(GONE);
        optionOneRadioButton = findViewById(R.id.radio_timer_one_original);
        optionTwoRadioButton = findViewById(R.id.radio_timer_two_three_quarter);
        optionThreeRadioButton = findViewById(R.id.radio_timer_three_half);
        optionFourRadioButton = findViewById(R.id.radio_timer_four_decreasing);

        //implement radio buttons due to nested layout
        
        testRadioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                testRadioButton.setChecked(true);
                optionOneRadioButton.setChecked(false);
                optionTwoRadioButton.setChecked(false);
                optionThreeRadioButton.setChecked(false);
                optionFourRadioButton.setChecked(false);
                selectedTimerOption = 0;
            }
        });

        optionOneRadioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                testRadioButton.setChecked(false);
                optionOneRadioButton.setChecked(true);
                optionTwoRadioButton.setChecked(false);
                optionThreeRadioButton.setChecked(false);
                optionFourRadioButton.setChecked(false);
                selectedTimerOption = 1;
            }
        });

        optionTwoRadioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                testRadioButton.setChecked(false);
                optionOneRadioButton.setChecked(false);
                optionTwoRadioButton.setChecked(true);
                optionThreeRadioButton.setChecked(false);
                optionFourRadioButton.setChecked(false);
                selectedTimerOption = 2;
            }
        });

        optionThreeRadioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                testRadioButton.setChecked(false);
                optionOneRadioButton.setChecked(false);
                optionTwoRadioButton.setChecked(false);
                optionThreeRadioButton.setChecked(true);
                optionFourRadioButton.setChecked(false);
                selectedTimerOption = 3;
            }
        });

        optionFourRadioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                testRadioButton.setChecked(false);
                optionOneRadioButton.setChecked(false);
                optionTwoRadioButton.setChecked(false);
                optionThreeRadioButton.setChecked(false);
                optionFourRadioButton.setChecked(true);
                selectedTimerOption = 4;
            }
        });

        //set text on all the text views
        TextView option1Timer1TextView = findViewById(R.id.option1_timer1);
        option1Timer1TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(1, 1));

        TextView option1Timer2TextView = findViewById(R.id.option1_timer2);
        option1Timer2TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(1, 2));

        TextView option1Timer3TextView = findViewById(R.id.option1_timer3);
        option1Timer3TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(1, 3));

        TextView option1Timer4TextView = findViewById(R.id.option1_timer4);
        option1Timer4TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(1, 4));

        TextView option1Timer5TextView = findViewById(R.id.option1_timer5);
        option1Timer5TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(1, 5));

        TextView option1Timer6TextView = findViewById(R.id.option1_timer6);
        option1Timer6TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(1, 6));


        TextView option2Timer1TextView = findViewById(R.id.option2_timer1);
        option2Timer1TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(2, 1));

        TextView option2Timer2TextView = findViewById(R.id.option2_timer2);
        option2Timer2TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(2, 2));

        TextView option2Timer3TextView = findViewById(R.id.option2_timer3);
        option2Timer3TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(2, 3));

        TextView option2Timer4TextView = findViewById(R.id.option2_timer4);
        option2Timer4TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(2, 4));

        TextView option2Timer5TextView = findViewById(R.id.option2_timer5);
        option2Timer5TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(2, 5));

        TextView option2Timer6TextView = findViewById(R.id.option2_timer6);
        option2Timer6TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(2, 6));


        TextView option3Timer1TextView = findViewById(R.id.option3_timer1);
        option3Timer1TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(3, 1));

        TextView option3Timer2TextView = findViewById(R.id.option3_timer2);
        option3Timer2TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(3, 2));

        TextView option3Timer3TextView = findViewById(R.id.option3_timer3);
        option3Timer3TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(3, 3));

        TextView option3Timer4TextView = findViewById(R.id.option3_timer4);
        option3Timer4TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(3, 4));

        TextView option3Timer5TextView = findViewById(R.id.option3_timer5);
        option3Timer5TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(3, 5));

        TextView option3Timer6TextView = findViewById(R.id.option3_timer6);
        option3Timer6TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(3, 6));


        TextView option4Timer1TextView = findViewById(R.id.option4_timer1);
        option4Timer1TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(4, 1));

        TextView option4Timer2TextView = findViewById(R.id.option4_timer2);
        option4Timer2TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(4, 2));

        TextView option4Timer3TextView = findViewById(R.id.option4_timer3);
        option4Timer3TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(4, 3));

        TextView option4Timer4TextView = findViewById(R.id.option4_timer4);
        option4Timer4TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(4, 4));

        TextView option4Timer5TextView = findViewById(R.id.option4_timer5);
        option4Timer5TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(4, 5));

        TextView option4Timer6TextView = findViewById(R.id.option4_timer6);
        option4Timer6TextView.setText(getTimerTextFromSelectedOptionAndTimerNumber(4, 6));


        //make the spinner work
        Spinner countdownSpinner = findViewById(R.id.countdown_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countdown_times, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countdownSpinner.setAdapter(adapter);
        countdownSpinner.setSelection(2);
        countdownSpinner.setOnItemSelectedListener(this);

        pianoRadioGroup = findViewById(R.id.piano_radio_group);
        practiceChecker = findViewById(R.id.practice_checkbox);


        //if flight mode is enabled (or at least if the user checked the flight mode checkbox),
        // the next button opens the summary activity
        Button nextButton = findViewById(R.id.next_button);
        // Set a click listener on next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the next button is clicked on.
            @Override
            public void onClick(View view) {

                if (flightModeChecker.isChecked()) {
                    practiceLastPartIsSelected = practiceChecker.isChecked();
                    openSummaryActivity();


                } else {
                    flightModeToast.show();
                }
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCountdownTime = countdownTimes[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * this method checks which radio buttons are checked
     */
    public void checkButton(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            //which timer option is done in implementation of radio buttons in onCreate

            //with or without chord generator
            case R.id.radio_yes:
                if (checked)
                    chordGeneratorIsSelected = true;
                pianoRadioGroup.setVisibility(View.VISIBLE);
                practiceChecker.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_no:
                if (checked)
                    chordGeneratorIsSelected = false;
                pianoRadioGroup.setVisibility(GONE);
                practiceChecker.setVisibility(GONE);
                break;

            //which piano
            case R.id.radio_piano1:
                if (checked)
                    selectedPiano = 1;
                break;
            case R.id.radio_piano2:
                if (checked)
                    selectedPiano = 2;
                break;
        }
    }

    /**
     * this method opens the summary activity and passes on the following Extras:
     * EXTRA_INT_OPTION which timer option was selected (test, 1, 2, 3, 4)
     * EXTRA_TIMER_ARRAY the array of timers that will be used
     * EXTRA_BOOLEAN_CHORD_GENERATOR if the user wants the chord generator for the final part
     * EXTRA_INT_PIANO the piano (1 or 2). will be needed to decide which chords to display in chord generator
     * EXTRA_BOOLEAN_PRACTICE if the user wants to practice only the last part
     */
    public void openSummaryActivity() {

        /* String text = "Timer selected: " + selectedTimerOption + "\n";
        text += "Countdown selected: " + selectedCountdownTime + " seconds" + "\n";
        text += "Chord generator selected: " + chordGeneratorIsSelected + "\n";
        if (chordGeneratorIsSelected) {
            text += "Piano selected: " + selectedPiano;
            text += "Practice only last part:" + practiceLastPartIsSelected;
        }
        Toast summary = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        summary.show();
        */

        generateTimerArray(selectedCountdownTime, selectedTimerOption);
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.putExtra(EXTRA_INT_OPTION, selectedTimerOption);
        intent.putExtra(EXTRA_TIMER_ARRAY, selectedTimers);
        intent.putExtra(EXTRA_BOOLEAN_CHORD_GENERATOR, chordGeneratorIsSelected);
        if (chordGeneratorIsSelected) {
            intent.putExtra(EXTRA_INT_PIANO, selectedPiano);
            intent.putExtra(EXTRA_BOOLEAN_PRACTICE, practiceLastPartIsSelected);
        }
        startActivity(intent);


    }

    /**
     * this method generates the timer array from the user's selection of timer option and countdown time
     *
     * @param selectedCountdownTime the user's selected time for the initial countdown
     * @param selectedTimerOption   the user's selected timer (original, 1/2 time...)
     */

    private void generateTimerArray(long selectedCountdownTime, int selectedTimerOption) {
        if (selectedTimerOption == 0) {
            selectedTimers = timersTest;
        } else if (selectedTimerOption == 1) {
            selectedTimers = timersOptionOneOriginal;
        } else if (selectedTimerOption == 2) {
            selectedTimers = timersOptionTwoThreeQuarter;
        } else if (selectedTimerOption == 3) {
            selectedTimers = timersOptionThreeHalf;

        } else if (selectedTimerOption == 4) {
            selectedTimers = timersOptionFourDecreasing;
        }
        selectedTimers[0] = selectedCountdownTime;
    }

    /**
     * this method creates a timer text String
     *
     * @param option      = the timer option the user selected (test, 1, 2, 3, 4)
     * @param timerNumber = the 1st, 2nd, 3rd... timer from the selected timer array
     * @return timer text String
     */
    public String getTimerTextFromSelectedOptionAndTimerNumber(int option, int timerNumber) {
        long timeInMillis = 0;
        if (option == 0) {
            timeInMillis = timersTest[timerNumber];
        } else if (option == 1) {
            timeInMillis = timersOptionOneOriginal[timerNumber];
        } else if (option == 2) {
            timeInMillis = timersOptionTwoThreeQuarter[timerNumber];
        } else if (option == 3) {
            timeInMillis = timersOptionThreeHalf[timerNumber];
        } else if (option == 4) {
            timeInMillis = timersOptionFourDecreasing[timerNumber];
        }

        return Common.getTimerTextFromLong(timeInMillis);

    }

}
