package com.aleksandarberar.timeentity;

import org.junit.Assert;
import org.junit.Test;

public class TestTimeEntity {

    public static class Person {

        private String id;
        private String name;
        private int age;

        public Person(String id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            if (age != person.age) return false;
            if (id != null ? !id.equals(person.id) : person.id != null) return false;
            return name != null ? name.equals(person.name) : person.name == null;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + age;
            return result;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    @Test
    public void testWithReflectionSimple() {
        Person initialState = new Person("1", "Aca", 28);
        TimeEntity<Person, Person> someone = new TimeEntity<>(initialState);

        Person change = new Person(null, null, 29);
        someone.apply(change);

        Assert.assertEquals(new Person("1", "Aca", 29), someone.getFinalState());
    }
}
