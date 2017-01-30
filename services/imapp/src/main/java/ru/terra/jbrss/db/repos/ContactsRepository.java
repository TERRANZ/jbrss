
package ru.terra.jbrss.db.repos;

import org.springframework.data.repository.CrudRepository;
import ru.terra.jbrss.db.entity.Contact;

public interface ContactsRepository extends CrudRepository<Contact, Integer> {
}
