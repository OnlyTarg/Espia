package com.pav.avdonin.clients; /**
 * Created by CleBo on 07.12.2017.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pav.avdonin.functions.*;
import com.pav.avdonin.logger.Logging;
import com.pav.avdonin.visual.Frames;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Properties;

public class Client extends JFrame{

    Socket socket;
    InetAddress ipAddress;
    Properties properties = new Properties();
    JLabel connectionStatus= new JLabel("Статус соединения");
    boolean statusOfLogger=false;
    boolean infoSide,makingChange;
    public static boolean isAllowed;
    StatusButtons statusButtons;
    int hash=0;
    int serverPort;
    String name,currentIP,address;

    Frames mainframe;
    Rectangle jLabelPossition;
    public static  Logging logging;
    public static DataInputStream datain;
    public static DataOutputStream dataout;

    public Client(String name, boolean makingChange, boolean infoSide)  {
        this.makingChange = makingChange;
        this.infoSide = infoSide;
        this.name = name;
        loadPropertiesOptions();
        createClientLogger();
        createClient();
        startDataExchange();
    }

    private void loadPropertiesOptions() {
        try {
            properties.load(getClass().getResourceAsStream("/settings.properties"));
            serverPort = Integer.valueOf(properties.getProperty("port"));
            address = properties.getProperty("ipServer");
            if(properties.getProperty("logger").equals("true"))statusOfLogger=true;
        } catch (IOException e) {
            logging.writeExeptionToLogger(e,statusOfLogger,Thread.currentThread());
           e.printStackTrace();
        }
    }

    private void createClientLogger() {
        if (logging == null) {
            logging = new Logging();
            logging.createLogger();
        }
    }

    private void createClient() {

        isAllowed = false;
        boolean isConnected = false;
        try {
            currentIP =  Inet4Address.getLocalHost().getHostAddress();
            hash = (int)(Math.random()*9999)+0;
            ipAddress = InetAddress.getByName(address);
            connectToServerAndCreateGUI(isConnected);
            connectionStatus.setForeground(Color.GREEN);
            connectionStatus.setText("Підключено");
        } catch (Exception e) {
            logging.writeExeptionToLogger(e,statusOfLogger,Thread.currentThread());
            e.printStackTrace();
            connectionStatus.setForeground(Color.RED);
            connectionStatus.setText("Помилка (код 01)");
            new AnotherFunctions().close(dataout,datain,socket);

        }


    }

    private void connectToServerAndCreateGUI(boolean isConnected) throws InterruptedException {
        while(!isConnected) {

            try {
                connectToServer();
                isConnected=true;

                int checker = 0;
                while (checker!=1) {
                    String msg="";
                    try {
                        msg = datain.readUTF();
                    } catch (IOException e) {
                        logging.writeExeptionToLogger(e,statusOfLogger,Thread.currentThread());
                        e.printStackTrace();
                    }

                    if (msg.length() > 40) {
                        checker = 1;
                        readSrarusOfButtonsFromServer(msg);
                        mainframe.createWindow(name,infoSide);
                        mainframe.createJButtonsArraysForClients(infoSide,mainframe.listOfPersons, statusButtons);
                        if(mainframe.listOfPersons.size()<10){
                            jLabelPossition = new Rectangle(20,6+(60*mainframe.listOfPersons.size()),200,30);
                        }else {
                            jLabelPossition = new Rectangle(20,6+(600),200,30);
                        }
                        fillingJLabelProperties();

                        if(makingChange==true) {
                            for (int i = 0; i < mainframe.mainButtons.length; i++) {
                                mainframe.mainButtons[i].addActionListener(new ActListeners().OnlineListenerForServer(mainframe.name, mainframe.mainButtons[i],
                                        mainframe.timeButtons[i], mainframe.placeButtons[i]));
                            }
                        }

                    }
                }

            }catch (Exception e){
                logging.writeExeptionToLogger(e,statusOfLogger,Thread.currentThread());
                e.printStackTrace();
                new Frames().tryToConnect();
            }

       }
    }

    private void connectToServer() throws IOException {
        socket = new Socket(ipAddress, serverPort);
        socket.setSoTimeout(30000);
        datain = new DataInputStream(socket.getInputStream());
        dataout = new DataOutputStream(socket.getOutputStream());
        dataout.writeUTF("candidate_"+currentIP+"_"+hash);
        dataout.flush();
        mainframe = new Frames();
        mainframe.name = name;
    }

    private void readSrarusOfButtonsFromServer(String msg) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(StatusButtons.class, new StatusButtonsDeserializer())
                .create();
        statusButtons = gson.fromJson(msg, StatusButtons.class);
        mainframe.mainButtons=statusButtons.mainButtons;
        mainframe.timeButtons = statusButtons.timeButtons;
        mainframe.placeButtons = statusButtons.placeButtons;
        mainframe.listOfPersons = statusButtons.listOfPersons;
    }

    private void fillingJLabelProperties() {
        connectionStatus.setBounds(jLabelPossition);
        connectionStatus.setFont(new Font("Times new Roman",Font.BOLD,20));
        connectionStatus.setForeground(Color.BLACK);
        connectionStatus.setText("З'єднання......");
        mainframe.frame.add(connectionStatus);
    }

    private void restart(){
        try {
            Thread.currentThread().sleep(3000);
            mainframe.boundsPoint = mainframe.frame.getLocation();
            mainframe.frame.dispose();
            new Client (name,makingChange,infoSide);
            new Frames().tryToConnect();
        } catch (Exception e1) {
            logging.writeExeptionToLogger(e1,statusOfLogger,Thread.currentThread());
            e1.printStackTrace();
        }
    }

    public void startDataExchange()  {
        SwitchButton switchButton = new SwitchButton();
        while(true){
            /*Массив с значениями с входящего потока:
                values[0] - положение кнопки по вертекали
                values[1] - цвет (green;red)
                values[2] - имя (КПП-1; КПП-2(КТП))
                values[3] - время
                */
            try{
                switchButton.determineButton(null,null,null,socket, datain,dataout,mainframe,0);
            }catch (SocketException e){
                logging.writeExeptionToLogger(e,statusOfLogger,Thread.currentThread());
                connectionStatus.setForeground(Color.RED);
                connectionStatus.setText("Помилка (код 02)");
                try {
                    Thread.currentThread().sleep(2000);
                } catch (InterruptedException e1) {
                    e.printStackTrace();
                    logging.writeExeptionToLogger(e,statusOfLogger,Thread.currentThread());
                }
                mainframe.frame.dispose();
                new AnotherFunctions().close(dataout,datain,socket);
                if(isAllowed==true){
                    restart();
                }
                break;
            }catch (IllegalArgumentException e){
                logging.writeExeptionToLogger(e,statusOfLogger,Thread.currentThread());
                // DO NOTHING
            }catch (Exception e) {
                logging.writeExeptionToLogger(e,statusOfLogger,Thread.currentThread());
                e.printStackTrace();
                connectionStatus.setForeground(Color.RED);
                connectionStatus.setText("Помилка (код 03)");
                new AnotherFunctions().close(dataout,datain,socket);
                restart();
                break;
            }
        }
    }

    public static void main(String[] args) {
        Client c = new Client("КПП-2(КТП)",true,false);
    }







}
