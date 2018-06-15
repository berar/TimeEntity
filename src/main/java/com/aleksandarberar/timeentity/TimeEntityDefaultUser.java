package com.aleksandarberar.timeentity;

public class TimeEntityDefaultUser implements TimeEntityUser {

    private static final String DEFAULT = "DEFAULT";

    @Override
    public String getName() {
        return DEFAULT;
    }
}
