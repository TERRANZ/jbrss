package ru.terra.jbrss.db.repos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.terra.jbrss.db.entity.Feedposts;

import java.util.Date;
import java.util.List;

public interface FeedPostsRepository extends CrudRepository<Feedposts, Integer> {
    @Query(value = "SELECT f.* FROM feedposts f WHERE f.feed_id = ?1 ORDER BY f.postdate DESC LIMIT 1", nativeQuery = true)
    List<Feedposts> getPostsByFeedAndByDateSorted(Integer feedId);

    @Query("SELECT f FROM Feedposts f WHERE f.feedId = ?1 AND f.postdate >= ?2 ORDER BY f.postdate DESC")
    List<Feedposts> getPostsByFeedAndByDate(Integer feedId, Date d);

    @Query(value = "SELECT f.* FROM feedposts f WHERE f.feed_id = ?1 ORDER BY f.postdate DESC LIMIT ?2, ?3", nativeQuery = true)
    List<Feedposts> findFeedpostsByFeedLimited(Integer feedId, Integer offset, Integer limit);

    List<Feedposts> findByFeedIdAndIsRead(Integer feedId, Boolean isRead);

    @Query(value = "SELECT f FROM Feedposts f WHERE f.posttext LIKE :posttext OR f.posttitle LIKE :posttext")
    List<Feedposts> findByPosttextLike(String posttex);

    @Query(value = "select count(id) from feedposts where feed_id=?1", nativeQuery = true)
    Integer countByFeedId(Integer feedId);

    List<Feedposts> findByFeedId(Integer feedId);
}
