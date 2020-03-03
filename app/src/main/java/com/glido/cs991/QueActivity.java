package com.glido.cs991;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.LinkedList;
import java.util.Queue;
/* Queue Activity Class */
public class QueActivity extends AppCompatActivity {
    private Queue<String> queue = new LinkedList<>();
    /* OnCreate method for creating visible elements */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide action bar
        getSupportActionBar().hide();
        // adjust keyboard layout on key input
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_que);

        final Button enQueueBtn = findViewById(R.id.enQueue);
        final Button deQueueBtn = findViewById(R.id.deQueue);
        final Button clearBtn = findViewById(R.id.clear);
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

        enQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enQueue();
            }
        });
        deQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deQueue();
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearQueue();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /* Method to queue data to list */
    public void enQueue(){
        final EditText input = findViewById(R.id.input);
        String inputValue = input.getText().toString().trim();
        // queue data
        if (queue.size() < 12 && !inputValue.isEmpty()) {
            queue.offer(inputValue);
            // simulate element animation
            queueTextView(inputValue);
        }

        Toast.makeText(getApplicationContext(), "Queue size: "+queue.size(), Toast.LENGTH_SHORT).show();
    }

    /* Method to dequeue data from list */
    public void deQueue(){
        // pop stack
        if (queue.size() > 0) {
            deQueueTextView();
        }
    }
    /* Method to clear the whole stack data */
    public void clearQueue(){
        // clear stack
        clearQueueView();
    }

    /* Set up data offer animation method */
    public void queueTextView(String value){
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
                if (queue.size() < 7){
                    textView = (ViewGroup) findViewById(R.id.textView1).getParent();
                }
                else {
                    determinant = 7;
                    textView = (ViewGroup) findViewById(R.id.textView7).getParent();
                }
                final TextView targetView = (TextView) textView.getChildAt(queue.size()-determinant);
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

    /* set up remove data animation method */
    public void deQueueTextView(){
        final ViewGroup textView = (ViewGroup) findViewById(R.id.textView1).getParent();
        final int index = 5;
        final int determinant = queue.size();
        final TextView targetView = (TextView) textView.getChildAt(0);

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
                // move target to back
                textView.removeView(targetView);
                textView.addView(targetView, index);
                // animate second row if queue size is greater than 5
                if (determinant > 5){
                    final ViewGroup textView2 = (ViewGroup) findViewById(R.id.textView7).getParent();
                    final TextView targetView2 = (TextView) textView2.getChildAt(0);
                    String targetValue = targetView2.getText().toString();
                    targetView.setText(targetValue);
                    targetView2.setText("");
                    queue.poll();
                    // move target2 to back
                    textView2.removeView(targetView2);
                    textView2.addView(targetView2, index);
                }
                else {
                    queue.poll();
                }
                Toast.makeText(getApplicationContext(), "Queue size: "+queue.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* set up clear animation method */
    public void clearQueueView(){
        int i;
        int determinant = 1;
        for (i = queue.size(); i > 0;){
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
                queue.clear();
                Toast.makeText(getApplicationContext(), "Queue size: "+queue.size(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
