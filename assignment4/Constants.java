package com.example.assignment4;

/**
 * Created by Dream Team.
 *
 * Class containing constants for the foreground service.
 */

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.example.assignment4.action.main";
        public static String STARTFOREGROUND_ACTION = "com.example.assignment4.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.example.assignment4.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}
