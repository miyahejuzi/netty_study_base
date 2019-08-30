package cn.orange.apachethrift.example;

import cn.orange.apachethrift.generated.DataException;
import cn.orange.apachethrift.generated.Person;
import cn.orange.apachethrift.generated.PersonService;
import org.apache.thrift.TException;

/**
 * @author kz
 * @date 2019/8/30
 */
public class PersonServiceImpl implements PersonService.Iface {


    @Override
    public Person getPersonByName(String name) throws DataException, TException {
        Person person = new Person();
        person.setName("person");
        person.setAge(18);
        person.setDescription("description");

        return person;
    }

    @Override
    public void savePerson(Person person) throws DataException, TException {
        System.out.println("save the person information = " + person);
    }
}
