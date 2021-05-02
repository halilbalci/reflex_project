package com.example.reflex_projec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;



public class GameActivity extends AppCompatActivity implements Observer {
    private static Handler HANDLER;
    private ConstraintLayout constraintLayout;
    private Observable reflexGameObservable;
    private boolean isThreadWorking;
    private TextView gameText;
    private TextView highestScoreTV;
    private ReflexGame reflexGame;
    @Override
    protected void onPause() {
        super.onPause();
        if(HANDLER!=null){
            HANDLER.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sh = getSharedPreferences(getString(R.string.mypref), MODE_PRIVATE);
        highestScoreTV.setText(getString(R.string.highestScore)+ String.valueOf(sh.getLong("hScore",Long.MAX_VALUE)));
        reflexGame.setHighestScore(sh.getLong("hScore",Long.MAX_VALUE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //initialize attributes
        isThreadWorking =false;
        constraintLayout =findViewById(R.id.gameLayout);
        reflexGameObservable = ReflexGame.getInstance();
        reflexGame= (ReflexGame) reflexGameObservable;
        reflexGameObservable.addObserver(this);
        gameText = findViewById(R.id.gameText);
        HANDLER = new Handler();
        highestScoreTV = findViewById(R.id.highestScore);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        reflexGameObservable.deleteObserver(this);
    }

    private void changeBackgroundColor(int color){
        constraintLayout.setBackgroundColor(getColor(color));
    }
    //screen onClick triggers the playGame
    public void playGame(View view){
        if(isThreadWorking){//second tab
            secondTab();
        }else {//first tab
            firstTab();
        }
    }
    //TimerTask can not change textView directly. Delegate the function to change textView.
    private void showClickNowInTextView(){
        gameText.setText(R.string.clickNow);
    }
    //first tap to the screen
    private void firstTab(){
        long delay = reflexGame.waitms();
        changeBackgroundColor(R.color.white);
        isThreadWorking = true;
        HANDLER.postDelayed(bgColorTask,delay);
        gameText.setText(R.string.ready);
    }
    //wait random ms to turn screen green.
    private Runnable bgColorTask= new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //change bg to green, store Current time to changedColorTime, show click now!
                    changeBackgroundColor(R.color.green);
                    reflexGame.setChangedColorDateCurrentTime();
                    showClickNowInTextView();
                }
            });
        }
    };
    //second tap to the screen
    private void secondTab(){
        reflexGame.setPressedDateCurrentTime(); //time of user interaction
        long dif = reflexGame.getDifferenceMs(); //user score
        if(dif>0){ //user pressed correctly
            changeBackgroundColor(R.color.white);
            gameText.setText(getString(R.string.playerClicked)+" " +dif + " "+ getString(R.string.ms) +"\n"+getString(R.string.start));
        }else{//user pressed early.
            //cancel thread
            HANDLER.removeCallbacks(bgColorTask);
            changeBackgroundColor(R.color.red);
            gameText.setText(getString(R.string.early)+"\n"+getString(R.string.start));
        }
        isThreadWorking = false;
        reflexGame.resetDates();
    }

    //if highest score update, the changes will displayed and sharedPreferences will updated..
    @Override
    public void update(Observable o, Object arg) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.mypref),MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        if(o instanceof ReflexGame){
            ReflexGame reflexGame = (ReflexGame) o;
            highestScoreTV.setText(getString(R.string.highestScore)+String.valueOf(reflexGame.getHighestScore()));
            myEdit.putLong("hScore",reflexGame.getHighestScore());
            myEdit.commit(); //put new value to sharedPreferences
        }

    }
}