package io.github.dashboard.models.widgets;

import com.google.gson.annotations.SerializedName;

public class WidgetParameters {

    public String name;

    @SerializedName("parameters")
    public WidgetGridParameters gridParameters;

    @Override
    public String toString() {
        return String.format("name: %s, parameters: %s", this.name, this.gridParameters != null ? this.gridParameters.toString() : "null");
    }
}
