package io.github.dashboard.models.services.github.repos;

import com.google.gson.annotations.SerializedName;

public class Repository {
    public String name;
    public String description;
    public String html_url;
    public String language;
    public String clone_url;
    @SerializedName("private")
    public boolean priv;
}
