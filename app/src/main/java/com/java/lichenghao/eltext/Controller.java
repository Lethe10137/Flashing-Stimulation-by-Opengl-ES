package com.java.lichenghao.eltext;

public interface Controller {
    void setAppTitle(String title);
    void setUpperText(String text);
    void appendUpperText(Character character);
    void setBeginTime(long beginTime);
    void setRedBlock(int blockId);
    void setRedPoint(int blockId);
    void GLSwitch(boolean turnON);
    void HideExtraViews(boolean hide);
}
