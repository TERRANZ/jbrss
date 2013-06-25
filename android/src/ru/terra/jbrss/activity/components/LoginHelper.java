package ru.terra.jbrss.activity.components;

import android.content.Context;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

/**
 * Created with IntelliJ IDEA.
 * User: terranz
 * Date: 25.06.13
 * Time: 13:16
 * To change this template use File | Settings | File Templates.
 */
@ContextSingleton
public class LoginHelper {
    Context context;

    @Inject
    public LoginHelper(Context context) {
        this.context = context;
    }

    public boolean login() throws TooManyAttemptsException {
        return false;
    }
}
