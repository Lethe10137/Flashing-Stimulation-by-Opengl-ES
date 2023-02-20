package com.java.lichenghao.eltext.control;

import android.util.Log;

import com.java.lichenghao.eltext.KeyBoardData;
import com.java.lichenghao.eltext.control.ControllerInterface;

public class TestController extends Controller{
    public TestController(ControllerInterface controllerInterface, int id){
        super(controllerInterface, id);
    }
    @Override
    public void run(){
        controllerInterface.HideExtraViews(true, id);
        controllerInterface.GLSwitch(true, id);
        controllerInterface.setUpperText("", id);
        controllerInterface.setRedBlock(-1, id);
        controllerInterface.setRedPoint(-1, id);
        controllerInterface.setAppTitle("Testing", id);
        controllerInterface.setBeginTime(System.nanoTime() + 100_000_000, id);
    }
}
