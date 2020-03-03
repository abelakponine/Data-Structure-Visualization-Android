package com.glido.cs991;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;
/* Stack Activity Class */
public class StackActivity extends AppCompatActivity {
    private Stack<String> stack = new Stack<>();
    /* OnCreate method for creating visible elements */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide action bar
        getSupportActionBar().hide();
        // adjust keyboard layout on key input
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_stack);

        final Button pushBtn = findViewById(R.id.push);
        final Button popBtn = findViewById(R.id.pop);
        final Button clearBtn = findViewById(R.id.clear);
        final Button closeBtn = findViewById(R.id.closeBtn);
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

        pushBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushStack();
            }
        });
        popBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popStack();
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearStack();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /* Method to push data to stack */
    public void pushStack(){
        final EditText input = findViewById(R.id.input);
        String inputValue = input.getText().toString().trim();
        // push stack
        if (stack.size() < 12 && !inputValue.isEmpty()) {
            stack.push(inputValue);
            // simulate element animation
            pushTextView(inputValue);
        }

        Toast.makeText(getApplicationContext(), "Stack size: "+stack.size(), Toast.LENGTH_SHORT).show();
    }

    /* Method to pop data from stack */
    public void popStack(){
        // pop stack
        if (stack.size() > 0) {
            popTextView();
        }
    }
    /* Method to clear the whole stack data */
    public void clearStack(){
        // clear stack
        clearStackView();
    }

    /* Set up push animation method */
    public void pushTextView(String value){
        // get input and parent layout elements' coordinates
        final RelativeLayout layout = findViewById(R.id.relativeLayout);
        final EditText input = findViewById(R.id.input);
        int[] location = new int[2];
        input.getLocationInWindow(location);
        // create new text view for animation
        final TextView E = new TextView(this);
        // set properties
        E.setId((int) 24);
        E.setText(value);
        input.setText("");
        E.setTextSize(18f);
        E.setTextColor(getResources().getColor(R.color.green));
        E.setTranslationX((float) location[0]);
        E.setTranslationY((float) location[1]);
        E.setVisibility(View.GONE);
        E.setAlpha(0f);
        // add to layout
        layout.addView(E, 1);
        E.setVisibility(View.VISIBLE);
        // animate
        E.animate().alpha(1f).setDuration(100).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // assign new position to animated element
                final ViewGroup textView;
                int determinant = 1;
                if (stack.size() < 7){
                    textView = (ViewGroup) findViewById(R.id.textView1).getParent();
                }
                else {
                    determinant = 7;
                    textView = (ViewGroup) findViewById(R.id.textView7).getParent();
                }
                final TextView targetView = (TextView) textView.getChildAt(stack.size()-determinant);
                int[] newLocation = new int[2];
                targetView.getLocationInWindow(newLocation);
                E.animate().translationX((float) newLocation[0]).translationY((float) newLocation[1])
                        .setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        // finalize animation
                        targetView.setText(E.getText());
                        layout.removeView(E);
                        input.setText("");
                    }
                });
            }
        });
    }

    /* set up pop animation method */
    public void popTextView(){
        final ViewGroup textView;
        int i = 1;
        int determinant = stack.size();
        if (determinant > 6){
            i = 7;
            textView = (ViewGroup) findViewById(R.id.textView7).getParent();
        }
        else {
            textView = (ViewGroup) findViewById(R.id.textView1).getParent();
        }
        final TextView targetView = (TextView) textView.getChildAt(determinant - i);

        // get value from target
        final RelativeLayout layout = findViewById(R.id.relativeLayout);
        final String value = targetView.getText().toString().trim();
        // get target location
        int[] location = new int[2];
        targetView.getLocationInWindow(location);
        // create new text view for animation
        final TextView E = new TextView(this);
        // set properties
        E.setId((int) 24);
        E.setText(value);
        targetView.setText("");
        E.setTextSize(18f);
        E.setTextColor(getResources().getColor(R.color.green));
        E.setTranslationX((float) location[0]);
        E.setTranslationY((float) location[1]);
        E.setVisibility(View.GONE);
        // add to layout
        layout.addView(E, 1);
        E.setVisibility(View.VISIBLE);
        // animate
        E.animate().translationY((float) location[1] - 80)
                .setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layout.removeView(E);
                stack.pop();
                Toast.makeText(getApplicationContext(), "Stack size: "+stack.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* set up clear animation */
    public void clearStackView(){
        int i;
        int determinant = 1;
        for (i = stack.size(); i > 0;){
            final ViewGroup textView;
            if (i > 6){
                determinant = 7;
                textView = (ViewGroup) findViewById(R.id.textView7).getParent();
            }
            else {
                determinant = 1;
                textView = (ViewGroup) findViewById(R.id.textView1).getParent();
            }
            final TextView targetView = (TextView) textView.getChildAt(i - determinant);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    targetView.setText("");
                }
            }, 500);
            i--;
            if (i < 1){
                stack.clear();
                Toast.makeText(getApplicationContext(), "Stack size: "+stack.size(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
