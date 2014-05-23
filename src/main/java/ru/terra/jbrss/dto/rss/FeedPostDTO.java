package ru.terra.jbrss.dto.rss;

import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.server.dto.CommonDTO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FeedPostDTO extends CommonDTO {
    public Integer id;
    public int feedId;
    public Long postdate;
    public String posttitle;
    public String postlink;
    public String posttext;
    public boolean isRead;

    public FeedPostDTO(Feedposts parent) {
        this.id = parent.getId();
        this.feedId = parent.getFeedId();
        this.postdate = parent.getPostdate().getTime();
        this.posttitle = parent.getPosttitle();
        this.postlink = parent.getPostlink();
        this.posttext = parent.getPosttext();
        this.isRead = parent.isRead();
    }

    public FeedPostDTO() {
    }
}
