package ru.terra.dms.test.db.service;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.terra.dms.db.service.DmsObjectSerivce;

@SpringBootTest
@RunWith(SpringRunner.class)
@Ignore
public class DmsObjectServiceTest {
    @Autowired
    private DmsObjectSerivce serivce;
}
