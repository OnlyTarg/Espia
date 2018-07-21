package com.pav.avdonin.functions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

public class AnotherFunctions {
    public String time (){
        DateFormat df = new SimpleDateFormat("dd.MM HH:mm");
        Date currenttime = Calendar.getInstance().getTime();
        String time = df.format(currenttime);
        return time;

    }
    public String timeWithSeconds(){
        DateFormat df = new SimpleDateFormat("dd.MM HH:mm:ss");
        Date currenttime = Calendar.getInstance().getTime();
        String time = df.format(currenttime);
        return time;

    }
    public void close(DataOutputStream dataout, DataInputStream datain, Socket socket) {
        try {
            dataout.close();
            datain.close();
            socket.close();

        } catch (IOException e) {
           e.printStackTrace();
        }


    }

}