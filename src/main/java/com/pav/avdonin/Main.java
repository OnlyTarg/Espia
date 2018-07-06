package com.pav.avdonin;

import com.pav.avdonin.clients.Client;
import com.pav.avdonin.clients.Client120;
import com.pav.avdonin.clients.EspiaJL;
import com.pav.avdonin.server.Server;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.Properties;

/**
 * Created by CleBo on 19.01.2018.
 */
public class Main extends JFrame {
    Properties properties = new Properties();

    String ipKPP="", ipKTP="", ipServer="",ip120="";
    String currentIP;

    public Main() {
        try {
            properties.load(getClass().getResourceAsStream("/settings.properties"));
            currentIP = Inet4Address.getLocalHost().getHostAddress();
            ipServer = properties.getProperty("ipServer");
            ipKPP = properties.getProperty("ipKPP");
            ipKTP = properties.getProperty("ipKTP");
            ip120 = properties.getProperty("ip120");

        }catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Помилка при зчитуванні файла конфігурацій");
            e.printStackTrace();
        }
        System.out.println(currentIP);
        if(properties.containsValue(currentIP)){
            if (currentIP.equals(ipKPP)){
                new Client("КПП-1");
            }
            if (currentIP.equals(ipKTP)){
                new Client("КПП-2(КТП)");
            }
            if (currentIP.equals(ipServer)){
                new Server("ЦУС(121)");
            }
            if (currentIP.equals(ip120)){
                new Client120("ЦУС(120)");
            }
        }
        else {
            if (currentIP.equals("127.0.0.1")){
                JOptionPane.showMessageDialog(null,"Компьютер не підключений до мережі. Перевірте підключення та спробуйте щє раз.");
            }
            else {
                new EspiaJL();
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Main main = new Main();
        }

}