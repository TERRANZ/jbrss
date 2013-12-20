package ru.terra.jbrss;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import org.apache.log4j.BasicConfigurator;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.WebappContext;
import ru.terra.jbrss.db.controllers.FeedsJpaController;
import ru.terra.jbrss.jabber.JabberManager;
import ru.terra.server.config.Config;
import ru.terra.server.constants.ConfigConstants;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

public class Main {
    protected static HttpServer startServer() throws IOException {
        String url = "http://" + Config.getConfig().getValue(ConfigConstants.SERVER_ADDR, ConfigConstants.SERVER_ADDR_DEFAULT);
        URI uri = UriBuilder.fromUri(url).port(8080).build();
        WebappContext context = new WebappContext("context");
        HttpServer webserver = GrizzlyServerFactory.createHttpServer(uri);
        context.deploy(webserver);
        return webserver;
    }

    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        HttpServer httpServer = startServer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Feeds in DB: " + new FeedsJpaController().count());
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new JabberManager().start();
            }
        }).start();
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
