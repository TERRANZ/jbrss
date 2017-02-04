
package ru.terra.jbrss.service.pojo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "feedses"
})
public class Embedded {

    @JsonProperty("feedses")
    private List<Feedse> feedses = null;

    @JsonProperty("feedses")
    public List<Feedse> getFeedses() {
        return feedses;
    }

    @JsonProperty("feedses")
    public void setFeedses(List<Feedse> feedses) {
        this.feedses = feedses;
    }

}
