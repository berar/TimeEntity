package com.aleksandarberar.timeentity;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class TestTimeEntity {

    public static class Person {

        private String name;
        private int age;
        private String state;

        public Person(String state, String name, int age) {
            this.state = state;
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            if (age != person.age) return false;
            if (state != null ? !state.equals(person.state) : person.state != null) return false;
            return name != null ? name.equals(person.name) : person.name == null;
        }

        @Override
        public int hashCode() {
            int result = state != null ? state.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + age;
            return result;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "state='" + state + '\'' +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    @Test
    public void testWithReflectionSimple() {

        Person initialState = new Person("Serbia", "Aca", 28);
        TimeEntity<Person, Person> timeEntity = new TimeEntity<>(initialState);

        Person change = new Person(null, null, 29);
        timeEntity.apply(change);

        Assert.assertEquals(new Person("Serbia", "Aca", 29), timeEntity.getFinalState());
    }

    @Test
    public void testWithTransitionFunction() {

        Person initialState = new Person("Serbia", "Aca", 28);
        TimeEntity<Person, Person> timeEntity = new TimeEntity<>(initialState, (a,b) -> {
            Person eventPerson = a.getEvent();
            return new Person(
                    eventPerson.state == null ? b.state : eventPerson.state,
                    eventPerson.name == null ? b.name : eventPerson.name, 
                    eventPerson.age
            );
        });

        Person change = new Person(null, null, 29);
        timeEntity.apply(change);

        Assert.assertEquals(new Person("Serbia", "Aca", 29), timeEntity.getFinalState());
    }

    @Test
    public void testAtMethod() throws Exception {

        Person initialState = new Person("Serbia", "Aca", 28);
        TimeEntity<Person, Person> timeEntity = new TimeEntity<>(initialState);

        Person change = new Person("Montenegro", null, 29);
        Thread.sleep(100);
        timeEntity.apply(change);
        LocalDateTime testDateTime = LocalDateTime.now();

        Thread.sleep(100);
        Person _2ndChange = new Person(null, null, 30);
        timeEntity.apply(_2ndChange);

        Assert.assertEquals(new Person("Montenegro", "Aca", 29), timeEntity.at(testDateTime));
    }

    @Test
    public void testAtMethodWhenAtIsBeforeCreation() throws Exception {

        LocalDateTime testDateTime = LocalDateTime.now();
        Thread.sleep(100);

        Person initialState = new Person("Serbia", "Aca", 28);
        TimeEntity<Person, Person> timeEntity = new TimeEntity<>(initialState);
        Assert.assertEquals(new Person("Serbia", "Aca", 28), timeEntity.at(testDateTime));
    }

    @Test
    public void testWhenMethodCaseWhenEventIsFound() throws Exception {

        Person initialState = new Person("Serbia", "Aca", 26);
        TimeEntity<Person, Person> timeEntity = new TimeEntity<>(initialState);

        Person change = new Person("Montenegro", null, 27);
        Thread.sleep(100);
        timeEntity.apply(change);
        LocalDateTime testDateTime = LocalDateTime.now();

        Thread.sleep(100);
        Person _2ndChange = new Person(null, null, 30);
        timeEntity.apply(_2ndChange);

        Assert.assertEquals(timeEntity.when(change).get().getNano(), testDateTime.getNano(), 100000000);
    }

    @Test
    public void testWhenMethodCaseWhenEventIsntFound() throws Exception {

        Person initialState = new Person("Serbia", "Aca", 28);
        TimeEntity<Person, Person> timeEntity = new TimeEntity<>(initialState);

        Person change = new Person("Montenegro", null, 31);
        timeEntity.apply(change);

        Assert.assertTrue(!timeEntity.when(new Person("Montenegro", null, 35)).isPresent());
    }
}
