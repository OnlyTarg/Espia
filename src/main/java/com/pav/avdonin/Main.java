package com.pav.avdonin;

import com.pav.avdonin.clients.Client;
import com.pav.avdonin.server.Server;
import com.pav.avdonin.util.Names;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.Properties;

//Created by CleBo on 19.01.2018.

public class Main extends JFrame {
    boolean infoSide;
    boolean makingChange;
    Properties properties;
    String ipKPP="", ipKTP="", ipServer="", currentIP;

    public Main() {
        properties = new Properties();
        setNamesFromProperties();
        chooseServerOrClient();
    }

    private void chooseServerOrClient() {
        if(properties.containsValue(currentIP)){
            if (currentIP.equals(ipKPP)){

                makingChange = Boolean.valueOf(properties.getProperty("makingChangeForKPP"));
                new Client(Names.KPP.getValue(),makingChange,infoSide);
            }
            if (currentIP.equals(ipKTP)){
                makingChange = Boolean.valueOf(properties.getProperty("makingChangeForKPP"));
                new Client(Names.KTP.getValue(),makingChange,infoSide);
            }
            if (currentIP.equals(ipServer)){
                Server server = new Server(Names.ESPIA_SERVER.getValue());
                server.startServer();
            }

        }
        else {
            if (currentIP.equals("127.0.0.1")){
                JOptionPane.showMessageDialog(null,"Компьютер не підключений до мережі. Перевірте підключення та спробуйте щє раз.");
            }
            else {
                System.out.println("tut");
                makingChange = Boolean.valueOf(properties.getProperty("makingChangeForEspiaJL"));
                new Client(Names.ESPIA_JL.getValue(),makingChange,infoSide);
            }
        }
    }
    private void setNamesFromProperties() {
        try {
            properties.load(getClass().getResourceAsStream("/settings.properties"));
            currentIP = Inet4Address.getLocalHost().getHostAddress();
            infoSide = Boolean.valueOf(properties.getProperty("infoSide"));
            ipServer = properties.getProperty("ipServer");
            ipKPP = properties.getProperty("ipKPP");
            ipKTP = properties.getProperty("ipKTP");


        }catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Помилка при зчитуванні файла конфігурацій");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();

    }


}
