package io.github.dashboard.models.widgets;

import com.google.gson.annotations.SerializedName;

public class UserWidget {

    public int id;
    public String name;
    public String route;
    @SerializedName("parameters")
    public WidgetGridParameters gridParameters;
    public String url_parameters;

    @Override
    public String toString() {
        return String.format("id: %s, name: %s, route: %s, parameters: %s, url_parameters: %s", this.id, this.name, this.route, this.gridParameters != null ? this.gridParameters.toString() : "null", this.url_parameters);
    }

}
