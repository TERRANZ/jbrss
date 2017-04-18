package ru.terra.jbrss.db.entity.nontenant;

import ru.terra.jbrss.db.entity.base.BaseFeeds;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "feeds")
public class NonTenantFeeds extends BaseFeeds {
    public NonTenantFeeds() {
    }

    public NonTenantFeeds(Integer id, String feedname, String feedurl, Date updateTime) {
        super(id, feedname, feedurl, updateTime);
    }
}
