package ru.terra.jbrss.core.db.repos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.terra.jbrss.core.db.entity.Feedposts;

import java.util.Date;
import java.util.List;

public interface FeedPostsRepository extends CrudRepository<Feedposts, Integer> {
    List<Feedposts> getPostsByFeedAndByDateSorted(Integer feedId);

    List<Feedposts> getPostsByFeedAndByDate(Integer feedId, Date d);

    List<Feedposts> findByUserid(Integer userid);

    List<Feedposts> findFeedpostsByFeedLimited(Integer feedId, Integer offset, Integer limit);

    List<Feedposts> findByFeedIdAndByIsRead(Integer feedId, Boolean isRead);

    List<Feedposts> findByFeedId(Integer feedId);

    @Query(value = "SELECT f FROM Feedposts f WHERE f.posttext LIKE :posttext OR f.posttitle LIKE :posttext")
    List<Feedposts> findByPosttextLike(String posttex);
}
