package com.madan.madan.ageing;

public class WidgetTheme implements Theme {
    int widgetOpacity = 100;
    String foregroundColor = "#ffffff";
    String backgroundColor = "#55000000";

    public WidgetTheme() {

    }

    public WidgetTheme(String foregroundColor, String backgroundColor, int widgetOpacity) {
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.widgetOpacity = widgetOpacity;
    }

    @Override
    public int getOpacity() {
        return this.widgetOpacity;
    }

    @Override
    public void setOpacity(int opacity) {
        this.widgetOpacity = opacity;
    }

    @Override
    public void setForegroundColor(String foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    @Override
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public String getBackgroundColor() {
        return this.backgroundColor;
    }

    @Override
    public String getForegroundColor() {
        return this.foregroundColor;
    }
}
