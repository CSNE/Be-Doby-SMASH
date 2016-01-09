package managers;

import interfaces.OnTimeUpdateListener;

import java.util.ArrayList;


public class TimeManager {
    private static TimeManager inst = new TimeManager();

    public static TimeManager getInstance() {
        return inst;
    }

    private TimeManager() {

        pause();
    }


    ArrayList<OnTimeUpdateListener> otuls=new ArrayList<>();
    boolean playing;

    public void addOnTimeUpdateListener(OnTimeUpdateListener otul){
        this.otuls.add(otul);
    }


    private long currentSystemTime,startSystemTime;
    private double startAnimationTime, currentAnimationTime, animationLength=5;





    public void setTime(double animTime){
        startSystemTime=System.currentTimeMillis();
        startAnimationTime=animTime;
        currentAnimationTime=animTime;
        for (OnTimeUpdateListener otul:otuls) otul.updateTime();
    }
    public void updateTime(){
        //System.out.println("t: "+getCurrentAnimationTime());
        if (playing) {
            currentSystemTime = System.currentTimeMillis();
            for (OnTimeUpdateListener otul:otuls) otul.updateTime();
            currentAnimationTime=(currentSystemTime-startSystemTime)/1000.0+startAnimationTime;
        }
    }
    public double getCurrentAnimationTime(){
        return currentAnimationTime%animationLength;
    }
    public void setAnimationLength(double length){
        this.animationLength=length;
        for (OnTimeUpdateListener otul:otuls) otul.updateTime();
    }
    public double getAnimationLength(){
        return this.animationLength;
    }

    public void pause(){
        playing=false;
    }
    public void play(){
        setTime(getCurrentAnimationTime());
        playing=true;
    }
    public void playFromStart(){
        setTime(0);
        playing=true;
    }
    public void stop(){
        setTime(0);
        playing=false;
    }
    public void togglePlayState(){
        if (playing) pause();
        else play();
    }
    public boolean isPlaying(){
        return playing;
    }
}
