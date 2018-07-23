package com.pav.avdonin;

import com.pav.avdonin.clients.Client;

import com.pav.avdonin.server.Server;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.Properties;

//Created by CleBo on 19.01.2018.


public class Main extends JFrame {
    boolean infoSide;
    boolean makingChange;
    Properties properties = new Properties();

    String ipKPP="", ipKTP="", ipServer="",ip120="";
    String currentIP;

    public Main() {
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
        System.out.println(currentIP);
        if(properties.containsValue(currentIP)){
            if (currentIP.equals(ipKPP)){

                makingChange = Boolean.valueOf(properties.getProperty("makingChangeForKPP"));
                new Client("КПП-1",makingChange,infoSide);
            }
            if (currentIP.equals(ipKTP)){
                makingChange = Boolean.valueOf(properties.getProperty("makingChangeForKPP"));
                new Client("КПП-2(КТП)",makingChange,infoSide);
            }
            if (currentIP.equals(ipServer)){
                new Server("EspiaServer");
            }

        }
        else {
            if (currentIP.equals("127.0.0.1")){
                JOptionPane.showMessageDialog(null,"Компьютер не підключений до мережі. Перевірте підключення та спробуйте щє раз.");
            }
            else {
                makingChange = Boolean.valueOf(properties.getProperty("makingChangeForEspiaJL"));
                new Client("EspiaJL",makingChange,infoSide);
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Main main = new Main();
        }

}
