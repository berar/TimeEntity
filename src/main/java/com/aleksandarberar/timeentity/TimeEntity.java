package com.aleksandarberar.timeentity;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;

public class TimeEntity<T, S> {

    public final UUID identity;

    public final T initialState;
    protected final List<TimeEntityEvent<S>> stateTransitions;
    private T finalState;

    private BiFunction<TimeEntityEvent<S>, T, T> stateTransitionFunction = null;

    public TimeEntity(T initialState, BiFunction<TimeEntityEvent<S>, T, T> stateTransitionFunction) {
        this(initialState);
        this.stateTransitionFunction = stateTransitionFunction;
    }

    public TimeEntity(T initialState) {
        this.identity = UUID.randomUUID();
        this.initialState = initialState;
        this.stateTransitions = new ArrayList<>();
        this.finalState = this.initialState;
    }

    public T at(LocalDateTime localDateTime) { return null; }

    // equals preko identity

    // equals finalState metod

    public T getFinalState() {
        return this.finalState;
    }

    public T apply(S stateTransition) {
        TimeEntityEvent<S> event = TimeEntityEvent.create(stateTransition,
                LocalDateTime.now(), new TimeEntityDefaultUser());
        this.stateTransitions.add(event);
        return stateTransitionFunction == null ? this.applyReflection(event) : this.applyFunction(event);
    }

    private T applyFunction(TimeEntityEvent<S> event) {
        this.finalState = stateTransitionFunction.apply(event, this.finalState);
        return this.finalState;
    }

    private T applyReflection(TimeEntityEvent<S> event) {
        assert event.getEvent().getClass().getTypeName()
                .equals(this.initialState.getClass().getTypeName());

        S e = event.getEvent();
        Field[] fields = e.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                Object value = field.get(e);
                field.setAccessible(accessible);
                if (value == null)
                    continue;

                Field fField = this.finalState.getClass().getDeclaredField(field.getName());
                fField.setAccessible(true);
                fField.set(this.finalState, value);
                fField.setAccessible(accessible);
            } catch (IllegalAccessException | NoSuchFieldException ex) { }
        }

        return this.finalState;
    }
}
