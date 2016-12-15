package ru.terra.jbrss.core;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Date: 20.12.13
 * Time: 15:34
 */
public class ClassSearcher<T> {

    @SuppressWarnings("unchecked")
    public List<T> load(String packageName, Class annotaion) {
        List<T> ret = new ArrayList<>();
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(annotaion);
        for (Class<?> c : annotated) {
            Object o = null;
            try {
                o = c.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (o != null)
                ret.add((T) o);
        }
        return ret;
    }
}