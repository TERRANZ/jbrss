package ru.terra.jbrss.im.jabber;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.CommandsFactory;
import ru.terra.jbrss.im.core.IMType;
import ru.terra.jbrss.im.core.ServerInterface;

import javax.inject.Inject;

@Component
public class JabberIM extends ServerInterface {
    @Value("${jabber.server}")
    protected String jabberServer;
    @Value("${jabber.port}")
    protected Integer jabberPort;
    @Value("${jabber.user}")
    protected String jabberUser;
    @Value("${jabber.server}")
    protected String jabberPass;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private CommandsFactory commandsFactory = new CommandsFactory();
    private static XMPPConnection connection;

    @Inject
    public JabberIM() {
        try {
            ConnectionConfiguration config = new ConnectionConfiguration(jabberServer, jabberPort);
            connection = new XMPPConnection(config);
            connection.connect();
            connection.login(jabberUser, jabberPass);
            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
            connection.addPacketListener(new JabberPacketListener(), filter);
        } catch (XMPPException ex) {
            logger.error("Exception in jabber service", ex);
        }
    }

    @Override
    public void sendMessage(String contact, String message) {
        Message msg = new Message();
        msg.setTo(contact);
        msg.setBody(message);
        msg.setType(Message.Type.chat);
        connection.sendPacket(msg);
    }

    @Override
    protected IMType getType() {
        return IMType.JABBER;
    }

    private class JabberPacketListener implements PacketListener {
        @Override
        public void processPacket(Packet packet) {
            Message message = (Message) packet;
            if (message.getBody() != null) {
                String fromName = message.getFrom();
                logger.info("Message from " + fromName + " : " + message.getBody());

                String msg = message.getBody();
                String[] params = msg.split(" ");
                AbstractCommand cmd = commandsFactory.getCommand(params[0]);
                if (cmd != null)
                    try {
                        cmd.setContact(fromName);
                        cmd.setServerInterface(JabberIM.this);
                        cmd.doCmd(fromName, params);
                    } catch (Exception e) {
                        logger.error("Error while executing command", e);
                        sendMessage(fromName, "Exception while doing command, " + e.getMessage());
                    }
                if (!isContactExists(fromName))
                    sendMessage(fromName, "Hello, you are not registered, type reg");

            }
        }
    }
}
