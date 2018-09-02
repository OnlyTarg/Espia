package com.pav.avdonin.logger;

import sun.util.logging.resources.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging {
    public static Logger logger = Logger.getLogger(Logging.class.getName());

    public void createLogger() {
        SimpleFormatter txtFormatter = new SimpleFormatter();
        FileHandler fh = null;
        try {
            fh = new FileHandler("logFile.txt", 200000, 1, true);
            fh.setFormatter(txtFormatter);
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeExeptionToLogger(Exception e, boolean statusOfLogger, Thread thread) {

        if (statusOfLogger == true) {
            logger.log(Level.SEVERE, "Exeption: " + e.toString() +
                    "\r\n" + "Place : " + e.getStackTrace()[0] + "\r\n" +
                    "Client : " + thread.getName() + "\r\n");
        }
    }
}