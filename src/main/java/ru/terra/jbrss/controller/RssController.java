package ru.terra.jbrss.controller;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.json.JSONWithPadding;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.jbrss.constants.RssErrorConstants;
import ru.terra.jbrss.constants.URLConstants;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.entity.User;
import ru.terra.jbrss.dto.rss.FeedDTO;
import ru.terra.jbrss.dto.rss.FeedPostDTO;
import ru.terra.jbrss.model.RssModel;
import ru.terra.server.constants.ErrorConstants;
import ru.terra.server.controller.AbstractResource;
import ru.terra.server.dto.ListDTO;
import ru.terra.server.dto.SimpleDataDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Path(URLConstants.DoJson.Rss.RSS)
@Produces({"application/x-javascript", "application/json", "application/xml"})
public class RssController extends AbstractResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private RssModel rssModel = new RssModel();

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_LIST)
    public JSONWithPadding getList(@QueryParam("callback") String callback,
                                   @Context HttpContext hc) {
        logger.info("Loading list of feeds");
        User user = (User) getCurrentUser(hc);
        ListDTO<FeedDTO> ret = new ListDTO<>();
        if (user != null) {
            List<Feeds> feeds;
            feeds = rssModel.getFeeds(user.getId());
            if (feeds != null && feeds.size() > 0) {
                List<FeedDTO> data = new ArrayList<>();
                for (Feeds f : feeds)
                    data.add(new FeedDTO(f));
                ret.setData(data);
            }
            return new JSONWithPadding(ret, callback);
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return new JSONWithPadding(ret, callback);
        }
    }

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_ADD)
    public JSONWithPadding add(@QueryParam("callback") String callback,
                               @Context HttpContext hc, @QueryParam("url") String url) {
        logger.info("Adding new feed");
        User user = (User) getCurrentUser(hc);
        SimpleDataDTO<Boolean> ret = new SimpleDataDTO<>(false);
        if (user != null) {
            try {
                rssModel.addFeed(user, url);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                ret.errorMessage = e.getLocalizedMessage();
                ret.errorCode = RssErrorConstants.ERR_UNABLE_TO_ADD_RSS;
                return new JSONWithPadding(ret, callback);
            }
            return new JSONWithPadding(new SimpleDataDTO<>(true), callback);
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return new JSONWithPadding(ret, callback);
        }
    }

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_GET_UNREAD)
    public JSONWithPadding getUnread(@QueryParam("callback") String callback,
                                     @Context HttpContext hc, @QueryParam("timestamp") long timestamp) {
        ListDTO<FeedPostDTO> ret = new ListDTO<>();
        logger.info("Getting unread feeds");
        User user = (User) getCurrentUser(hc);
        if (user != null) {
            List<Feeds> feeds;
            feeds = rssModel.getFeeds(user.getId());
            if (feeds != null && feeds.size() > 0) {
                List<Feedposts> posts = new ArrayList<>();
                List<FeedPostDTO> dtos = new ArrayList<>();
                for (Feeds feed : feeds) {
                    posts.addAll(rssModel.getNewUserPosts(user.getId(), feed, new Date(timestamp)));
                }
                if (posts != null && posts.size() > 0) {
                    for (Feedposts fp : posts)
                        dtos.add(new FeedPostDTO(fp));
                    ret.setData(dtos);
                }
            }
            logger.info(ret.getSize().toString());
            return new JSONWithPadding(ret, callback);
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return new JSONWithPadding(ret, callback);
        }
    }

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_MARK_READ)
    public JSONWithPadding markRead(@QueryParam("callback") String callback,
                                    @Context HttpContext hc, @QueryParam("read") Boolean read) {
        Integer feed = null;
        if (getParameter(hc, "feed") != null)
            feed = Integer.parseInt(getParameter(hc, "feed"));
        Integer post = null;
        if (getParameter(hc, "post") != null)
            post = Integer.parseInt(getParameter(hc, "post"));
        Boolean all = null;
        if (getParameter(hc, "all") != null)
            all = Boolean.getBoolean(getParameter(hc, "all"));
        logger.info("Marking read");
        User user = (User) getCurrentUser(hc);
        SimpleDataDTO<Boolean> ret = new SimpleDataDTO<>(true);
        if (user != null) {
            if (feed != null) {
                rssModel.setFeedRead(feed, read);
                return new JSONWithPadding(ret, callback);
            } else if (post != null) {
                rssModel.setPostRead(post, read);
                return new JSONWithPadding(ret, callback);
            } else if (all) {
                rssModel.setAllRead(user.getId());
                return new JSONWithPadding(ret, callback);
            } else {
                ret.data = false;
                return new JSONWithPadding(ret, callback);
            }
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return new JSONWithPadding(ret, callback);
        }
    }

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_UPDATE)
    public JSONWithPadding update(@QueryParam("callback") String callback,
                                  @Context HttpContext hc) {
        logger.info("Updating feeds");
        User user = (User) getCurrentUser(hc);
        SimpleDataDTO<Integer> ret = new SimpleDataDTO<>(0);
        if (user != null) {
            List<Feeds> feeds;
            feeds = rssModel.getFeeds(user.getId());
            Integer updated = 0;
            if (feeds != null && feeds.size() > 0) {
                for (Feeds f : feeds)
                    try {
                        updated += rssModel.updateFeed(f);
                    } catch (Exception e) {
                        logger.error("Error while updating feed " + f.getFeedurl(), e);
                    }
            }
            ret.data = updated;
            return new JSONWithPadding(ret, callback);
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return new JSONWithPadding(ret, callback);
        }
    }

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_DEL)
    public JSONWithPadding delete(@QueryParam("callback") String callback,
                                  @Context HttpContext hc, @QueryParam("id") Integer id) {
        logger.info("Delete feed");
        User user = (User) getCurrentUser(hc);
        SimpleDataDTO<Boolean> ret = new SimpleDataDTO<>(true);
        if (user != null) {
            rssModel.removeFeed(id);
            return new JSONWithPadding(ret, callback);
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return new JSONWithPadding(ret, callback);
        }
    }

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_GET_FEED)
    public JSONWithPadding getFeed(@QueryParam("callback") String callback,
                                   @Context HttpContext hc, @QueryParam("id") Integer id, @QueryParam("count") Integer count) {
        ListDTO<FeedPostDTO> ret = new ListDTO<>();
        logger.info("Getting feed");
        User user = (User) getCurrentUser(hc);
        if (user != null) {
            List<Feedposts> posts = new ArrayList<>();
            List<FeedPostDTO> dtos = new ArrayList<>();
            posts.addAll(rssModel.getFeedPosts(id, 0, count, false));
            if (posts != null && posts.size() > 0) {
                for (Feedposts fp : posts)
                    dtos.add(new FeedPostDTO(fp));
                ret.setData(dtos);
            }
            return new JSONWithPadding(ret, callback);
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return new JSONWithPadding(ret, callback);
        }
    }

    @POST
    @Path(URLConstants.DoJson.Rss.RSS_DO_SEARCH)
    public JSONWithPadding doSearch(@QueryParam("callback") String callback,
                                    @Context HttpContext hc, @FormParam("val") String val) {
        ListDTO<FeedPostDTO> ret = new ListDTO<>();
        if (val == null)
            val = getParameter(hc, "val");
        logger.info("Searching feeds by value " + val);
        User user = (User) getCurrentUser(hc);
        if (user != null) {
            List<Feedposts> posts = new ArrayList<>();
            List<FeedPostDTO> dtos = new ArrayList<>();
            posts.addAll(rssModel.search(val));
            if (posts != null && posts.size() > 0) {
                for (Feedposts fp : posts)
                    dtos.add(new FeedPostDTO(fp));
                ret.setData(dtos);
            }
            return new JSONWithPadding(ret, callback);
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return new JSONWithPadding(ret, callback);
        }
    }

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_GET_POST)
    public JSONWithPadding doGetPost(@QueryParam("callback") String callback,
                                     @Context HttpContext hc, @QueryParam("id") Integer id) {
        FeedPostDTO ret = new FeedPostDTO();
        User user = (User) getCurrentUser(hc);
        if (user != null)
            try {
                return new JSONWithPadding(new FeedPostDTO(rssModel.getPost(id)), callback);
            } catch (Exception e) {
                ret.errorCode = ErrorConstants.ERR_INTERNAL_EXCEPTION;
                ret.errorMessage = e.getMessage();
                logger.error("Unable to get feed post", e);
                return new JSONWithPadding(ret, callback);
            }
        else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return new JSONWithPadding(ret, callback);
        }
    }

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_EXPORT_FOR_USER)
    public Response doExport(@Context HttpContext hc) {

        User user = (User) getCurrentUser(hc);
        if (user != null) {
            try {
                List<FeedPostDTO> dtos = new ArrayList<>();
                for (Feeds feeds : rssModel.getFeeds(user.getId())) {
                    for (Feedposts fp : rssModel.getFeedPosts(feeds.getId(), 0, 0, true)) {
                        FeedPostDTO dto = new FeedPostDTO(fp);
                        dto.posttext = Base64.getEncoder().encodeToString(dto.posttext.getBytes(Charset.defaultCharset()));
                        dto.posttitle = Base64.getEncoder().encodeToString(dto.posttitle.getBytes(Charset.defaultCharset()));
                        dtos.add(dto);
                    }
                }
                Response response = Response
                        .ok()
                        .entity(new ObjectMapper().writeValueAsBytes(dtos))
                        .build();
                return response;
            } catch (Exception e) {
                logger.error("Unable to get feed post", e);
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path(URLConstants.DoJson.Rss.RSS_IMPORT_FOR_USER)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response doImport(@Context HttpContext hc,
                             @FormDataParam("file") InputStream uploadedInputStream,
                             @FormDataParam("file")
                             FormDataContentDisposition fileDetail) throws IOException {
        List<FeedPostDTO> dtos = new ObjectMapper().readValue(uploadedInputStream, new TypeReference<List<FeedPostDTO>>() {
        });
        for (FeedPostDTO dto : dtos) {
            Feedposts post = new Feedposts();
            post.setPosttitle(new String(Base64.getDecoder().decode(dto.posttitle), Charset.defaultCharset()));
            post.setPostlink(dto.postlink);
            post.setPosttext(new String(Base64.getDecoder().decode(dto.posttext), Charset.defaultCharset()));
            post.setPostdate(new Date(dto.postdate));
            post.setFeedId(dto.feedId);
            post.setUpdated(new Date());
            try {
                rssModel.insertPost(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Response.ok().build();
    }

}
