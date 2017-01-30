
package ru.terra.jbrss.db.repos;

import org.springframework.data.repository.CrudRepository;
import ru.terra.jbrss.db.entity.Contact;

import java.util.List;

public interface ContactsRepository extends CrudRepository<Contact, Integer> {
    Contact findByContactAndType(String contact, String type);
    List<Contact> findByUserId(Integer userId);
}
