package ru.terra.jbrss.core.db.repos;

import org.springframework.data.repository.CrudRepository;
import ru.terra.jbrss.core.db.entity.Contact;

public interface ContactsRepository extends CrudRepository<Contact, Integer> {
}
