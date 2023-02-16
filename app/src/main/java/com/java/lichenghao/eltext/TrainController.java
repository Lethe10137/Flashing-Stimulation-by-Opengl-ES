package com.java.lichenghao.eltext;

import android.util.Log;

public class TrainController extends Thread{
    Controller controller;
    public TrainController(Controller controller){
        this.controller = controller;
    }

    @Override
    public void run(){
        controller.HideExtraViews(true);
        controller.GLSwitch(true);
        controller.setAppTitle("Training...");
        controller.setUpperText("");
        for(int i = 0; i < 40; i++){
            if(this.interrupted()){
                Log.d("trainController","interrupted");
                break;
            }
            controller.setBeginTime(System.nanoTime()+500_000_000);
            controller.setRedBlock(i);
            controller.setRedPoint(-1);
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("trainController","interrupted");
                break;
            }
            controller.setRedBlock(-1);
            controller.setRedPoint(i);
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("trainController","interrupted");
                break;
            }
            controller.appendUpperText(KeyBoardData.buttons[i].charAt(0));
        }
        controller.setRedPoint(-1);
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        controller.HideExtraViews(false);
        controller.setAppTitle("");
        controller.GLSwitch(false);
    }
}
