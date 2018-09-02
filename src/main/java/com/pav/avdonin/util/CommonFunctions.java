package com.pav.avdonin.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

public class CommonFunctions {
    public static String getCurrentTime(){
        DateFormat df = new SimpleDateFormat("dd.MM HH:mm");
        Date currenttime = Calendar.getInstance().getTime();
        String time = df.format(currenttime);
        return time;

    }
    public static String getCurrentTimeWithSeconds(){
        DateFormat df = new SimpleDateFormat("dd.MM HH:mm:ss");
        Date currenttime = Calendar.getInstance().getTime();
        String time = df.format(currenttime);
        return time;

    }
    public static  void close(DataOutputStream dataout, DataInputStream datain, Socket socket) {
        try {
            dataout.close();
            datain.close();
            socket.close();

        } catch (IOException e) {
           e.printStackTrace();
        }


    }
    public static String getDayOfWeek(){
        Date currenttime = Calendar.getInstance().getTime();
        DateFormat format3=new SimpleDateFormat("EEEE");
        String finalDay1=format3.format(currenttime);
        return finalDay1;
    }

}