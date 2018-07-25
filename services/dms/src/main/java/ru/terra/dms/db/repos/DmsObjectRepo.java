package ru.terra.dms.db.repos;

import org.springframework.data.repository.CrudRepository;
import ru.terra.dms.db.entity.DmsObject;
import java.util.List;

public interface DmsObjectRepo extends CrudRepository<DmsObject, String> {
    @Override
    List<DmsObject> findAll();
}
