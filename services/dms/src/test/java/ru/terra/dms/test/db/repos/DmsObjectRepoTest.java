package ru.terra.dms.test.db.repos;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.terra.dms.db.entity.DmsObject;
import ru.terra.dms.db.repos.DmsObjectRepo;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DmsObjectRepoTest {
    @Autowired
    private DmsObjectRepo repo;

    @Test
    public void persistTest() {
        repo.save(new DmsObject("awd", Lists.newArrayList()));
    }
}
