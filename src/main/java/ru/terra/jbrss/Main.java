package ru.terra.jbrss;

import ru.terra.jbrss.db.controllers.FeedsJpaController;
import ru.terra.jbrss.jabber.JabberManager;
import ru.terra.server.ServerBoot;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new ServerBoot().start();
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
