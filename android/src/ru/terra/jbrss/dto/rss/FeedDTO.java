package ru.terra.jbrss.dto.rss;

public class FeedDTO extends CommonDTO {
	public Integer id = 0;
	public String feedname = "";
	public String feedurl = "";
	public Long updateTime = 0L;

	public Integer unread = 0;

	@Override
	public String toString() {
		return "FeedDTO [id=" + id + ", feedname=" + feedname + ", feedurl=" + feedurl + ", updateTime=" + updateTime + "]";
	}

}
