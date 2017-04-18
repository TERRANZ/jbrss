package ru.terra.jbrss.db.repos.nontenant;

import org.springframework.stereotype.Repository;
import ru.terra.jbrss.db.entity.nontenant.NonTenantFeeds;
import ru.terra.jbrss.db.repos.FeedsRepository;

/**
 * Created by Vadim_Korostelev on 4/18/2017.
 */
@Repository
public interface NonTenantFeedsRepository extends FeedsRepository<NonTenantFeeds> {
}
