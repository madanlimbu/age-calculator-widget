package com.madan.madan.ageing;

public interface Theme {
    int getOpacity();
    void setOpacity(int opacity);
    void setForegroundColor(String foregroundColor);
    void setBackgroundColor(String backgroundColor);
    String getBackgroundColor();
    String getForegroundColor();
}
