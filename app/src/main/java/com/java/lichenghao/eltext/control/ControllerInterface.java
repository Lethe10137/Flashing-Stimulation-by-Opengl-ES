package com.java.lichenghao.eltext.control;

public interface ControllerInterface {
    void setAppTitle(String title, int id);
    void setUpperText(String text, int id);
    void appendUpperText(Character character, int id);
    void setBeginTime(long beginTime, int id);
    void setRedBlock(int blockId, int id);
    void setRedPoint(int blockId, int id);
    void GLSwitch(boolean turnON, int id);
    void HideExtraViews(boolean hide, int id);
}
