package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;
import ru.terra.jbrss.shared.dto.FeedPostDto;

import java.util.List;

@IMCommand(value = "read", help = "Read feed command, sytax: read feed_id page perpage. To get feed id send info command")
public class ReadCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, List<String> params) {
        if (!serverInterface.isContactExists(contact)) {
            sendMessage("Not authorized");
            return false;
        } else {
            Integer page = 1;
            Integer perPage = 5;
            Integer targetFeed = Integer.parseInt(params.get(0));
            try {
                page = Integer.parseInt(params.get(1));
            } catch (Exception e) {
            }
            try {
                perPage = Integer.parseInt(params.get(2));
            } catch (Exception e) {
            }
            List<FeedPostDto> fps = serverInterface.getFeedPosts(serverInterface.getUserId(contact), targetFeed, page, perPage);
            if (fps != null)
                fps.forEach(fp -> sendMessage(fp.getPosttitle() + " : " + fp.getPosttext()));
            else
                sendMessage("Empty feed");
            return true;
        }
    }
}
