package ru.terra.dms.db.repos;

import org.springframework.data.repository.CrudRepository;
import ru.terra.dms.db.entity.DmsObjectField;

public interface DmsObjectFieldRepo extends CrudRepository<DmsObjectField, String> {
}
