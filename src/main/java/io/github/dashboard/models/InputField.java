package io.github.dashboard.models;

public class InputField {

    public InputField(){

    }

    public InputField(String name, boolean validity, String cause){
        this.name = name;
        this.validity = validity;
        this.cause = cause;
    }

    public String name;
    public boolean validity;
    public String cause;

}
