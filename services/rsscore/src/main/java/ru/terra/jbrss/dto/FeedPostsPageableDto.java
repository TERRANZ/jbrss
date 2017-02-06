package ru.terra.jbrss.dto;

import java.util.List;

public class FeedPostsPageableDto {
    private List<FeedPostDto> posts;
    private Integer all;

    public List<FeedPostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<FeedPostDto> posts) {
        this.posts = posts;
    }

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

    public FeedPostsPageableDto(List<FeedPostDto> posts, Integer all) {
        this.posts = posts;
        this.all = all;
    }
}
