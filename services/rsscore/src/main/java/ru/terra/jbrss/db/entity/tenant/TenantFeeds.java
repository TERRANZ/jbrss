package ru.terra.jbrss.db.entity.tenant;

import ru.terra.jbrss.db.entity.BaseFeeds;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "feeds")
public class TenantFeeds extends BaseFeeds {
    public TenantFeeds() {
    }

    public TenantFeeds(Integer id, String feedname, String feedurl, Date updateTime) {
        super(id, feedname, feedurl, updateTime);
    }
}
