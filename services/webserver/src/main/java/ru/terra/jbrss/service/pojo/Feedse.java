
package ru.terra.jbrss.service.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "userid",
    "feedname",
    "feedurl",
    "updateTime",
    "_links"
})
public class Feedse {

    @JsonProperty("userid")
    private Integer userid;
    @JsonProperty("feedname")
    private String feedname;
    @JsonProperty("feedurl")
    private String feedurl;
    @JsonProperty("updateTime")
    private String updateTime;
    @JsonProperty("_links")
    private Links links;

    @JsonProperty("userid")
    public Integer getUserid() {
        return userid;
    }

    @JsonProperty("userid")
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    @JsonProperty("feedname")
    public String getFeedname() {
        return feedname;
    }

    @JsonProperty("feedname")
    public void setFeedname(String feedname) {
        this.feedname = feedname;
    }

    @JsonProperty("feedurl")
    public String getFeedurl() {
        return feedurl;
    }

    @JsonProperty("feedurl")
    public void setFeedurl(String feedurl) {
        this.feedurl = feedurl;
    }

    @JsonProperty("updateTime")
    public String getUpdateTime() {
        return updateTime;
    }

    @JsonProperty("updateTime")
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @JsonProperty("_links")
    public Links getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(Links links) {
        this.links = links;
    }

}
