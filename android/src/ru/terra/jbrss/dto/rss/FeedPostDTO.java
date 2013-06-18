package ru.terra.jbrss.dto.rss;

import ru.terra.jbrss.dto.CommonDTO;

public class FeedPostDTO extends CommonDTO {
	public Integer id;
	public int feedId;
	public Long postdate;
	public String posttitle;
	public String postlink;
	public String posttext;
	public boolean isRead;

	@Override
	public String toString() {
		return "FeedPostDTO [postdate=" + postdate + ", posttitle=" + posttitle + ", postlink=" + postlink + ", posttext=" + posttext + "]";
	}

}
