package demo.person;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.NotFoundException;

@Singleton
public class PersonDatabase {

    private List<Person> persons;

    @PostConstruct
    public void init() {
        persons = Arrays.asList(
                new Person("Volodymyr"), 
                new Person("Janis"), 
                new Person("Sooraj"), 
                new Person("Naresh"), 
                new Person("Jevgeni"), 
                new Person("Naresh"), 
                new Person("Igor"), 
                new Person("Petersen"));
    }

    public Person[] currentList() {
        return persons.toArray(new Person[0]);
    }

    public Person getPerson(int id) {
        if (id < persons.size()) {
            return persons.get(id);
        }

        throw new NotFoundException("Person with id \"" + id + "\" not found.");
    }
}
