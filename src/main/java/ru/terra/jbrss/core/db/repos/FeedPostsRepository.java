package ru.terra.jbrss.core.db.repos;

import org.springframework.data.repository.CrudRepository;
import ru.terra.jbrss.core.db.entity.Feedposts;

public interface FeedPostsRepository extends CrudRepository<Feedposts, Integer> {
}
