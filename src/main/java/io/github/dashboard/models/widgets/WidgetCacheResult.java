package io.github.dashboard.models.widgets;

public class WidgetCacheResult {

    public WidgetCacheResult(){

    }

    public WidgetCacheResult(boolean foundValid, String response){
        this.foundValid = foundValid;
        this.response = response;
    }

    public boolean foundValid;
    public String response;

}
