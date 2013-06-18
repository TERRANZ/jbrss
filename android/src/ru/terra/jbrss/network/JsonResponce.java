package ru.terra.jbrss.network;

/**
 * Created with IntelliJ IDEA.
 * User: terranz
 * Date: 07.06.13
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */
public class JsonResponce {
    public String json;
    public int code;

    public JsonResponce(String json) {
        this.json = json;
    }

    public JsonResponce() {
    }
}
