package com.java.lichenghao.eltext.control;

import android.util.Log;

import com.java.lichenghao.eltext.KeyBoardData;
import com.java.lichenghao.eltext.control.ControllerInterface;

public class TrainController extends Controller{

    public TrainController(ControllerInterface controllerInterface, int id){
        super(controllerInterface, id);
    }

    @Override
    public void run(){
        controllerInterface.HideExtraViews(true, id);
        controllerInterface.GLSwitch(true, id);
        controllerInterface.setAppTitle("Training...", id);
        controllerInterface.setUpperText("", id);
        for(int i = 0; i < 40; i++){
            if(stop){
                Log.d("trainController","interrupted");
                return;
            }
            controllerInterface.setBeginTime(System.nanoTime()+500_000_000, id);
            controllerInterface.setRedBlock(i, id);
            controllerInterface.setRedPoint(-1, id);
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("trainController","interrupted");
                return;
            }
            controllerInterface.setRedBlock(-1, id);
            controllerInterface.setRedPoint(i, id);
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("trainController","interrupted");
                return;
            }
            controllerInterface.appendUpperText(KeyBoardData.buttons[i].charAt(0), id);
        }
        controllerInterface.setRedPoint(-1, id);
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("trainController","interrupted");
            return;
        }
        controllerInterface.HideExtraViews(false, id);
        controllerInterface.setAppTitle("", id);
        controllerInterface.GLSwitch(false, id);
    }
}
