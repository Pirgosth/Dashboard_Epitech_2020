package io.github.dashboard.models;

import java.util.ArrayList;
import java.util.List;

public class FormInputResult {

    private int localAccountId;
    public boolean success;
    public List<InputField> fields = new ArrayList<>();

    public int getLocalAccountId() {
        return localAccountId;
    }

    public void setLocalAccountId(int localAccountId) {
        this.localAccountId = localAccountId;
    }
}
