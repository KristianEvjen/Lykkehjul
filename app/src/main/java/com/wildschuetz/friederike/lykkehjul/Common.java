package com.wildschuetz.friederike.lykkehjul;

import android.widget.TextView;

class Common {

    /**
     * @param timeInMillis a time value in milliseconds
     * @return a String that displays minutes and seconds 00:00
     */

    static String getTimerTextFromLong(long timeInMillis) {
        int minutes = (int) timeInMillis / 60000;
        int seconds = (int) timeInMillis % 60000 / 1000;
        String timerText;
        timerText = "" + minutes;
        timerText += ":";
        if (seconds < 10) {
            timerText += "0";
        }
        timerText += seconds;

        return timerText;

    }

    /**
     * @param optionTextView TextView that displays which timers the user has chosen
     * @param option         int for the option the user has chosen
     */

    static void setTextFromOption(TextView optionTextView, int option) {
        if (option == 0) {
            optionTextView.setText(R.string.test);
        } else if (option == 1) {
            optionTextView.setText(R.string.option_one_original);
        } else if (option == 2) {
            optionTextView.setText(R.string.option_two_three_quarter);
        } else if (option == 3) {
            optionTextView.setText(R.string.option_three_half);
        } else if (option == 4) {
            optionTextView.setText(R.string.option_four_decreasing);
        }

    }
}
