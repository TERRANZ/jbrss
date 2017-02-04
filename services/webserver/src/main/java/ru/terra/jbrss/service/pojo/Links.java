
package ru.terra.jbrss.service.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "self",
    "feeds"
})
public class Links {

    @JsonProperty("self")
    private Self self;
    @JsonProperty("feeds")
    private Feeds feeds;

    @JsonProperty("self")
    public Self getSelf() {
        return self;
    }

    @JsonProperty("self")
    public void setSelf(Self self) {
        this.self = self;
    }

    @JsonProperty("feeds")
    public Feeds getFeeds() {
        return feeds;
    }

    @JsonProperty("feeds")
    public void setFeeds(Feeds feeds) {
        this.feeds = feeds;
    }

}
