package ru.terra.jbrss.controller;

import com.sun.jersey.api.core.HttpContext;
import com.sun.mail.util.MailSSLSocketFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.jbrss.constants.URLConstants;
import ru.terra.jbrss.util.Pair;
import ru.terra.server.config.Config;
import ru.terra.server.controller.AbstractResource;
import ru.terra.server.dto.SimpleDataDTO;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * User: Vadim Korostelev
 * Date: 20.09.13
 * Time: 14:18
 */
@Path(URLConstants.DoJson.ErrorReports.REPORT)
public class ErrorReportController extends AbstractResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String mailServer = Config.getConfig().getValue("errorreport.mail.server", null);
    private String mailPort = Config.getConfig().getValue("errorreport.mail.server.port", "465");
    private String mailUser = Config.getConfig().getValue("errorreport.mail.user", null);
    private String mailPass = Config.getConfig().getValue("errorreport.mail.pass", null);
    private String mailTo = Config.getConfig().getValue("errorreport.mail.to", "jbrss@terranz.ath.cx");
    private String mailFrom = Config.getConfig().getValue("errorreport.mail.from", "jbrss@terranz.ath.cx");

    @POST
    @Path(URLConstants.DoJson.ErrorReports.DO_REPORT + "/{uid}")
    public SimpleDataDTO<Boolean> get(@Context HttpContext hc, @PathParam("uid") final String uid) {
        logger.info("Error reported!");
//        final StringBuilder sb = new StringBuilder();
        if (mailServer != null && mailUser != null && mailPass != null) {
            final List<Pair<String, String>> values = new ArrayList<>();
            for (String param : hc.getRequest().getFormParameters().keySet()) {
                values.add(new Pair<>(param, hc.getRequest().getFormParameters().getFirst(param)));
//                sb.append(param);
//                sb.append(" : ");
//                sb.append(hc.getRequest().getFormParameters().getFirst(param));
//                sb.append("\n");
//                sb.append("================================================================");
            }
//            logger.info("Received error log: " + sb.toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    logger.info("Starting error reporter thread");
                    try {
                        String json = new ObjectMapper().writeValueAsString(values);
                        sendError(uid, json);
                        File crashFolder = new File("crashez");
                        if (!crashFolder.exists())
                            crashFolder.mkdirs();
                        PrintWriter printWriter = new PrintWriter(new File("crashez/" + new Date().getTime() + ".json"), Charset.forName("UTF-8").name());
                        printWriter.write(json);
                        printWriter.close();
                    } catch (IOException e) {
                        logger.error("Unable to process json", e);
                    }
                }
            }).start();
        }
        return new SimpleDataDTO<>(true);
    }

    public void sendError(String app, String error) {
        logger.info("sending error");
        try {
            Properties props = new Properties();

            MailSSLSocketFactory socketFactory= new MailSSLSocketFactory();
            socketFactory.setTrustAllHosts(true);
            props.put("mail.imaps.ssl.socketFactory", socketFactory);
            props.put("mail.transport.protocol", "smtps");
            props.put("mail.smtps.auth", "true");
            props.put("mail.smtps.ssl.checkserveridentity", "false");
            props.put("mail.smtps.ssl.trust", "*");

            Session mailSession = Session.getDefaultInstance(props);
            mailSession.setDebug(true);
            Transport transport = mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject("Error report from " + app + " android client");
            message.setContent(error, "text/plain");

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
            message.addFrom(new InternetAddress[]{new InternetAddress(mailFrom)});

            transport.connect(mailServer, Integer.parseInt(mailPort), mailUser, mailPass);

            transport.sendMessage(message,
                    message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (Exception e) {
            logger.error("error while sending email", e);
        }
    }
}
