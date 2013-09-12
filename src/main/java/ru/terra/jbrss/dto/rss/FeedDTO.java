package ru.terra.jbrss.dto.rss;

import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.server.dto.CommonDTO;

public class FeedDTO extends CommonDTO
{
	public Integer id = 0;
	public String feedname = "";
	public String feedurl = "";
	public Long updateTime = 0L;

	public FeedDTO()
	{
	}

	public FeedDTO(Feeds parent)
	{
		this.id = parent.getId();
		this.feedname = parent.getFeedname();
		this.feedurl = parent.getFeedurl();
		this.updateTime = parent.getUpdateTime().getTime();
	}
}
