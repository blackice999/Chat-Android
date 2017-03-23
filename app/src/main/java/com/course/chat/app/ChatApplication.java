package com.course.chat.app;

import android.app.Application;

/**
 * Created by vhernest on 26/11/15.
 */
public class ChatApplication extends Application {

    private static ChatApplication instance;

    public static ChatApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }
}
