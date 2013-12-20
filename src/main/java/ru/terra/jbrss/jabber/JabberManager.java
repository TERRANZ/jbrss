package ru.terra.jbrss.jabber;

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
import ru.terra.jbrss.jabber.commands.AbstractCommand;
import ru.terra.jbrss.jabber.commands.CommandsFactory;

/**
 * Date: 19.12.13
 * Time: 15:05
 */
public class JabberManager {
    private CommandsFactory commandsFactory = new CommandsFactory();
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private JabberModel model = new JabberModel();
    private ServerInterface serverInterface = new ServerInterface() {
        @Override
        public void sendMessage(String contact, String message) {
            Message msg = new Message();
            msg.setTo(contact);
            msg.setBody(message);
            msg.setType(Message.Type.chat);
            connection.sendPacket(msg);
        }
    };

    public JabberManager() {
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
                if (cmd != null) try {
                    cmd.doCmd(fromName, params, serverInterface);
                } catch (Exception e) {
                    serverInterface.sendMessage(fromName, "Exception while doing command, " + e.getMessage());
                }
                if (model.isContactExists(fromName)) {
                    //serverInterface.sendMessage(fromName, "Hello registrant");
                } else {
                    serverInterface.sendMessage(fromName, "Hello, you are not registered, type reg");
                }
            }
        }
    }

    private static XMPPConnection connection;

    public void start() {
        try {
            ConnectionConfiguration config = new ConnectionConfiguration("terranz.ath.cx", 5222);
            connection = new XMPPConnection(config);
            connection.connect();
            connection.login("jabberrssbot", "jbrss");
            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
            connection.addPacketListener(new JabberPacketListener(), filter);
        } catch (XMPPException ex) {
            logger.error("Exception in jabber service", ex);
        }
    }
}
