package com.java.lichenghao.eltext.control;


public class Controller extends Thread{
    protected ControllerInterface controllerInterface;

    public Controller(ControllerInterface controllerInterface, int id){
        this.controllerInterface = controllerInterface;
        this.id = id;
    }
    protected volatile boolean stop = false;
    protected int id;

    @Override
    public void run(){
        return;
    }

    public void stopThread(){
        stop = true;
        interrupt();
    }
}
