package com.java.lichenghao.eltext.control;

import android.util.Log;

import com.java.lichenghao.eltext.KeyBoardData;
import com.java.lichenghao.eltext.control.ControllerInterface;

public class StopController extends Controller{
    public StopController(ControllerInterface controllerInterface, int id) {
        super(controllerInterface, id);
    }

    @Override
    public void run() {
        controllerInterface.HideExtraViews(false, id);
        controllerInterface.GLSwitch(false, id);
        controllerInterface.setUpperText("", id);
        controllerInterface.setAppTitle("", id);
        controllerInterface.setRedBlock(-1, id);
        controllerInterface.setRedBlock(-1, id);
    }
}
