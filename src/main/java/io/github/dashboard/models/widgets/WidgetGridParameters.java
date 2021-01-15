package io.github.dashboard.models.widgets;

public class WidgetGridParameters {

    public int x;
    public int y;
    public int w;
    public int h;

    @Override
    public String toString() {
        return String.format("x: %s, y: %s, w: %s, h: %s", this.x, this.y, this.w, this.h);
    }

}
