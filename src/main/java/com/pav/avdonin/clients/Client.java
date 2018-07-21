package com.pav.avdonin.clients; /**
 * Created by CleBo on 07.12.2017.
 */

import com.google.gson.Gson;
//import com.pav.avdonin.Main;
import com.google.gson.GsonBuilder;
import com.pav.avdonin.functions.*;
import com.pav.avdonin.visual.FlashingLight;
import com.pav.avdonin.media.Music;
import com.pav.avdonin.visual.Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by CleBo on 23.11.2017.
 */
public class Client extends JFrame{
    AnotherFunctions functions = new AnotherFunctions();
    StatusButtons statusButtons;
    Music music = new Music();
    int hash=0;
    Properties properties = new Properties();
    String name;
    String currentIP;
    Frames mainframe;
    boolean isAllowed;
    boolean infoSide;
    boolean makingChange;
    InetAddress ipAddress;
    Socket socket;
    DataInputStream datain;
    DataOutputStream dataout;

    JLabel connectionStatus= new JLabel("Статус соединения");
    Point point = null;

    public Client(String name, Point point)  {
        this.point=point;
        try {
            properties.load(getClass().getResourceAsStream("/settings.properties"));
        } catch (IOException e) {
           e.printStackTrace();
        }

        this.name = name;
        createClient();
        mainframe.name = name;
        readData();
        functions.close(dataout,datain,socket);
    }

    public Client(String name)  {

        this.name = name;
        try {
            properties.load(getClass().getResourceAsStream("/settings.properties"));
        } catch (IOException e) {
           e.printStackTrace();
        }

        createClient();
        mainframe.name = name;
        readData();
        functions.close(dataout,datain,socket);

    }






    private void createClient() {



        isAllowed = false;
        boolean isConnected = false;
        //Создаю клиент
        try {
            currentIP =  Inet4Address.getLocalHost().getHostAddress();
            hash = Thread.currentThread().hashCode()*(int)(Math.random()*999)+0;
            int serverPort = Integer.valueOf(properties.getProperty("port"));
            String address = properties.getProperty("ipServer"); //10.244.1.121    localhost
            while(!isConnected) {

                connectionStatus.setForeground(Color.BLACK);
                connectionStatus.setText("З'єднання......");
                ipAddress = InetAddress.getByName(address);
                try{
                socket = new Socket(ipAddress, serverPort);
                    socket.setSoTimeout(30000);
                //socket = new Socket(ipAddress, serverPort);
                    isConnected=true;

                   // Gson gson = new GsonBuilder().create();
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(StatusButtons.class, new StatusButtonsDeserializer())
                            .create();
                    String msg="";
                    int checker = 0;
                    datain = new DataInputStream(socket.getInputStream());
                    dataout = new DataOutputStream(socket.getOutputStream());
                    dataout.writeUTF("candidate_"+currentIP+"_"+hash);
                    dataout.flush();
                    mainframe = new Frames();
                    mainframe.name = name;
                    //mainframe.setVisible(true);

                    while (checker!=1) {
                        System.out.println("Point 1");
                        try {
                            msg = datain.readUTF();
                            //System.out.println(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (msg.length() > 40) {

                            statusButtons = gson.fromJson(msg, StatusButtons.class);

                            mainframe.mainButtons=statusButtons.mainButtons;

                            mainframe.timeButtons = statusButtons.timeButtons;
                            mainframe.placeButtons = statusButtons.placeButtons;
                            mainframe.listOfPersons = statusButtons.listOfPersons;
                            checker = 1;
                            mainframe.createWindow(name,true);
                            mainframe.createJButtonsArraysForClients(true,mainframe.listOfPersons, statusButtons);
                            for (int i = 0; i <mainframe.mainButtons.length ; i++) {
                                mainframe.mainButtons[i].addActionListener(new ActListeners().OnlineListener(mainframe.mainButtons[i],
                                        mainframe.timeButtons[i],mainframe.placeButtons[i]));
                            }
                           // mainframe.setVisible(true);
                        }
                    }
                    System.out.println("tyt1");


                }catch (Exception e){
                    e.printStackTrace();

                   /* e.printStackTrace();*/
                    //connectionStatus.setText("");
                    for (int i = 30; i >= 0; i--) {
                        connectionStatus.setText("З'єднання "+i);
                        connectionStatus.setForeground(Color.RED);
                        Thread.currentThread().sleep(1000);
                    }
                }

           }





            Thread.currentThread().sleep(500);
            connectionStatus.setForeground(Color.GREEN);
            connectionStatus.setBounds(32,490,200,30);
            connectionStatus.setText("Підключено");
            //Прием состояния кнопок (цвет кнопок) сервера на момент подключения



        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement [] stack = e.getStackTrace();
            connectionStatus.setForeground(Color.RED);
            connectionStatus.setBounds(15,490,200,30);
            connectionStatus.setText("Помилка (код 01)");
            functions.close(dataout,datain,socket);

        }


    }

    private void restart(){
        try {
            Thread.currentThread().sleep(3000);
            point = mainframe.frame.getLocation();
            mainframe.frame.dispose();
            new Client (name,point);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    public void readData()  {

        //Метод принимает данные от сервера
        String value = "";
        SwitchButton switchButton = new SwitchButton();
        while(true){

            /*Массив с значениями с входящего потока:
                values[0] - положение кнопки по вертекали
                values[1] - цвет (green;red)
                values[2] - имя (КПП-1; КПП-2(КТП))
                values[3] - время
                */

            try{
                switchButton.determineButton(value,socket,isAllowed,datain,dataout,mainframe,0);
            }catch (SocketException e){
                StackTraceElement [] stack = e.getStackTrace();
                connectionStatus.setForeground(Color.RED);
                connectionStatus.setBounds(15,490,200,30);
                connectionStatus.setText("Помилка (код 02)");
                try {
                    Thread.currentThread().sleep(2000);
                } catch (InterruptedException e1) {
                    e.printStackTrace();
                }
                mainframe.frame.dispose();
                functions.close(dataout,datain,socket);
                if(isAllowed){
                    restart();
                }

                break;


            }catch (IllegalArgumentException e){
                StackTraceElement [] stack = e.getStackTrace();

                // DO NOTHING

            }catch (Exception e) {
                e.printStackTrace();
                connectionStatus.setForeground(Color.RED);
                connectionStatus.setBounds(15,490,200,30);
                connectionStatus.setText("Помилка (код 03)");
                functions.close(dataout,datain,socket);
                restart();
                break;
            }

        }




    }



    public static void main(String[] args) throws IOException {
        Client c = new Client("КПП-2(КТП)");
    }






       /* public ActionListener OnlineListener (JButton b,JButton binfo,JButton bwho) {
        //Создаю слушатель, такой же как на сервере.
        //Изменяет состояние кнопок и передает инфо на сервер
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(null,"Кнопка нажата");
                music.soundClick();
                //JOptionPane.showMessageDialog(null,"Онлайн слушатель");
                try {

                    if (b.getBackground().equals(Color.RED)) {

                        binfo.setText(time());
                        b.setBackground(Color.GREEN);
                        bwho.setText(name);
                        music.soundZvonok();

                        //JOptionPane.showMessageDialog(null,"Данные изменены");
                        dataout.writeUTF(b.getY()+"_green" + "_"+name+"_"+time());
                        dataout.flush();
                       // JOptionPane.showMessageDialog(null,"Данные отправлены");
                    }
                    else {
                        binfo.setText(time());
                        b.setBackground(Color.RED);
                        bwho.setText(name);
                        music.soundDoor();
                        //JOptionPane.showMessageDialog(null,"Данные изменены");
                        dataout.writeUTF(b.getY()+"_red"+ "_"+name+"_"+time());
                        dataout.flush();
                        //JOptionPane.showMessageDialog(null,"Данные отправлены");
                    }
                } catch (IOException e1) {
                    StackTraceElement [] stack = e1.getStackTrace();
                    logger.log(Level.INFO,e1.toString()+"\r\n"+stack[0]+"\r\n");
                   *//* e1.printStackTrace();*//*
                    functions.close(dataout,datain,socket);
                    *//*JOptionPane.showMessageDialog(null,"Помилка при передачі даних до серверу");*//*
                }
            }
        };
        return actionListener;
    }
*/
}
