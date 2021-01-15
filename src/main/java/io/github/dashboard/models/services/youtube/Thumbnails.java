package io.github.dashboard.models.services.youtube;

import com.google.gson.annotations.SerializedName;

public class Thumbnails {
    @SerializedName("default")
    public Picture def;
    public Picture medium;
    public Picture high;
}
