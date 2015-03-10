package ru.terra.jbrss;

import ru.terra.jbrss.db.controllers.FeedsJpaController;
import ru.terra.jbrss.engine.UsersEngine;
import ru.terra.jbrss.jabber.JabberManager;
import ru.terra.jbrss.rss.UpdateRssEngine;
import ru.terra.server.ServerBoot;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Feeds in DB: " + new FeedsJpaController().count());
                try {
                    for (Integer uid : new UsersEngine().getUsers())
                        UpdateRssEngine.getInstance().scheduleUpdatingForUser(uid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new JabberManager().start();
            }
        }).start();
        new ServerBoot().start();
    }
}
