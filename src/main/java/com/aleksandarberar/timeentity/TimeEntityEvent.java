package com.aleksandarberar.timeentity;

import java.time.LocalDateTime;

public class TimeEntityEvent<T> {

    private final T event;
    private final LocalDateTime timeStamp;
    private final TimeEntityUser user;

    public TimeEntityEvent(T event, LocalDateTime timeStamp, TimeEntityUser user) {
        this.event = event;
        this.timeStamp = timeStamp;
        this.user = user;
    }

    public static <T> TimeEntityEvent<T> create(T state, LocalDateTime timeStamp, TimeEntityUser user) {
        return new TimeEntityEvent<>(state, timeStamp, user);
    }

    public T getEvent() {
        return event;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public TimeEntityUser getUser() {
        return user;
    }
}