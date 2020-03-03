package com.glido.cs991;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/* Class for Recursive Reverse */
public class RecursiveReverse extends AppCompatActivity {
    private int sec = 300;
    /* OnCreate event method for creating visible elements */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide action bar
        getSupportActionBar().hide();
        // adjust keyboard layout on key input
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_recursive_reverse);

        final Button rrbtn = findViewById(R.id.reverse);
        final EditText input = findViewById(R.id.input);
        final TextView display = findViewById(R.id.display);
        final TextView result = findViewById(R.id.displayResult);
        final Button closeBtn = findViewById(R.id.closeBtn2);
        final RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);

        // hide soft keyboard of screen touch
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        // onclick event for recursive reverse
        rrbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String value = input.getText().toString().trim();
                final String[] array = value.split("");
                int length = array.length;
                String newValue = "";

                display.setText("");
                // reset time
                setSec(500);
                // loop through data length
                for (int i=0;i<length;i++){
                    delayRun(array[i], display);
                }
                // rebuild data
                delayRun("\n*** Rebuilding Data ***\n", display);
                for (int j=length-1;j>0;j--){
                    newValue += array[j];
                    delayRun(newValue, display);
                }
                // clear display and show result
                input.setText("");
                result.setText("");
                delayRun(newValue, result);
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /* Method to set timer */
    public void setSec(int sec) {
        this.sec = sec;
    }
    /* Method to animate (delay) display of results */
    public void delayRun(final String data, final TextView view){

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.append(data+"\n");
            }
        }, sec);
        setSec(sec+300);
    }
}
