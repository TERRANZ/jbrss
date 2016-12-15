package ru.terra.jbrss.core.db.repos;

import org.springframework.data.repository.CrudRepository;
import ru.terra.jbrss.core.db.entity.Feedposts;

import java.util.List;

public interface FeedPostsRepository extends CrudRepository<Feedposts, Integer> {
    List<Feedposts> getPostsByFeedAndByDateSorted(Integer feedId);
}
