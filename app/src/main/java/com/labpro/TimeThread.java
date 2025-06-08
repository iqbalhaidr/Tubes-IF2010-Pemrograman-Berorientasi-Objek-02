package com.labpro;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class TimeThread extends Thread{
    //    Date date;
//    Time time;
    private Label timeLabel;
    public TimeThread(Label timeLabel){
        this.timeLabel = timeLabel;
        setDaemon(true);
    }

    @Override
    public void run(){
        while(true){
            try{
                long ctime = System.currentTimeMillis();
                long hour = ((ctime / 3600000) + 7) % 24;
                long minute = (ctime / 60000) % 60;
                long second = (ctime / 1000) % 60;

                String timeStr = String.format("%02d:%02d:%02d", hour, minute, second);

                Platform.runLater(() -> timeLabel.setText(timeStr));
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(e);
                break;
            }

        }
    }
}