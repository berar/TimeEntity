package com.aleksandarberar.timeentity;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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

    public T at(LocalDateTime localDateTime) {
        List<TimeEntityEvent<S>> earlierStateTransitions = this.stateTransitions.stream()
                .filter(s -> s.getTimeStamp().isBefore(localDateTime))
                .collect(Collectors.toList());

        TimeEntity<T, S> resultTimeEntity = new TimeEntity<>(initialState);
        earlierStateTransitions.forEach(t -> resultTimeEntity.apply(t.getEvent()));
        return resultTimeEntity.finalState;
    }

    public Optional<LocalDateTime> when(S event) {
        return this.stateTransitions.stream()
                .filter(s -> s.getEvent().equals(event))
                .findFirst()
                .map(a -> a.getTimeStamp());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeEntity<?, ?> that = (TimeEntity<?, ?>) o;

        return identity != null ? identity.equals(that.identity) : that.identity == null;
    }

    @Override
    public int hashCode() {
        return identity != null ? identity.hashCode() : 0;
    }

    // equals finalState metod

    // create interface or abstract class and factory method

    // in introduction, note that this is a raw implementation with no further dependencies, except junit which is a standard development dependency for unit testing.
    // uml diagrams
    // implementation chapters, should be subsections per unit test or per method of the interface
    // further work, 1) the "who" aspect, possible methods, creationUser, lastModifier. 2) database design for event sourcing

    public T getFinalState() {
        return this.finalState;
    }
}
