package com.example.reflex_projec;

import android.content.SharedPreferences;

import java.util.Date;
import java.util.Observable;
import java.util.Random;

public class ReflexGame extends Observable {
    private long highestScore;
    private Date changedColorDate;
    private Date pressedDate;
    private TimeManager timeManager;
    private static ReflexGame INSTANCE = null;

    public ReflexGame() {
        this.timeManager = new TimeManager();
    }

    public static ReflexGame getInstance(){
        if(INSTANCE==null){
            INSTANCE = new ReflexGame();
        }
        return INSTANCE;
    }

    public void resetDates(){
        setChangedColorDate(null);
        setPressedDate(null);
    }

    public long getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(long highestScore) {
        this.highestScore = highestScore;
        setChanged();
        notifyObservers();
    }

    public long waitms(){
        Random r = new Random();
        return r.nextInt(10)*650;
    }

    public void setChangedColorDate(Date changedColorDate) {
        this.changedColorDate = changedColorDate;
    }

    public void setChangedColorDateCurrentTime() {
        setChangedColorDate(timeManager.getCurrentTime());
    }

    public void setPressedDate(Date pressedDate) {
        this.pressedDate = pressedDate;
    }

    public void setPressedDateCurrentTime() {
        setPressedDate(timeManager.getCurrentTime());
    }

    //pressedDate or changedColor have not been initialize yet, then return 0
    public long getDifferenceMs(){
        if(pressedDate!=null && changedColorDate !=null){
            long score = pressedDate.getTime() - changedColorDate.getTime();
            if((score > 0) && (score < highestScore)){
                setHighestScore(score);
                //store to sharedPreferences
            }
            return score;
        }
        return 0;
    }

}
