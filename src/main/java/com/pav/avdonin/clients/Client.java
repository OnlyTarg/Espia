package com.pav.avdonin.clients; /**
 * Created by CleBo on 07.12.2017.
 */

import com.google.gson.Gson;
//import com.pav.avdonin.Main;
import com.google.gson.GsonBuilder;
import com.pav.avdonin.functions.*;
import com.pav.avdonin.visual.Frames;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Properties;

/**
 * Created by CleBo on 23.11.2017.
 */
public class Client extends JFrame{
    AnotherFunctions functions = new AnotherFunctions();
    StatusButtons statusButtons;
    int hash=0;
    Properties properties = new Properties();
    String name;
    String currentIP;
    Frames mainframe;
    public static boolean isAllowed;
    boolean infoSide;
    boolean makingChange;
    InetAddress ipAddress;
    Socket socket;
    Rectangle jLabelPossition;
    public static DataInputStream datain;
    public static DataOutputStream dataout;

    JLabel connectionStatus= new JLabel("Статус соединения");



    public Client(String name, boolean makingChange, boolean infoSide)  {
        this.makingChange = makingChange;
        this.infoSide = infoSide;
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
                           // mainframe.setVisible(true);
                        }
                    }
                    System.out.println("tyt1");


                }catch (Exception e){

                    e.printStackTrace();
                    new Frames().tryToConnect();

                   /* e.printStackTrace();*/
                    //connectionStatus.setText("");

                }

           }





            Thread.currentThread().sleep(500);
            connectionStatus.setForeground(Color.GREEN);
            //connectionStatus.setBounds(32,490,200,30);
            connectionStatus.setText("Підключено");
            //Прием состояния кнопок (цвет кнопок) сервера на момент подключения



        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement [] stack = e.getStackTrace();
            connectionStatus.setForeground(Color.RED);
            //connectionStatus.setBounds(jLabelPossition);
            connectionStatus.setText("Помилка (код 01)");
            functions.close(dataout,datain,socket);

        }


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
            System.out.println("at method restart");
            Thread.currentThread().sleep(3000);
            mainframe.boundsPoint = mainframe.frame.getLocation();

            mainframe.frame.dispose();

            new Client (name,makingChange,infoSide);
            System.out.println("before method try");
            new Frames().tryToConnect();
            System.out.println("after method try");

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
                switchButton.determineButton(value,socket, datain,dataout,mainframe,0);
            }catch (SocketException e){
                StackTraceElement [] stack = e.getStackTrace();
                connectionStatus.setForeground(Color.RED);
                connectionStatus.setText("Помилка (код 02)");
                try {
                    Thread.currentThread().sleep(2000);
                } catch (InterruptedException e1) {
                    e.printStackTrace();
                }
                mainframe.frame.dispose();
                functions.close(dataout,datain,socket);
                System.out.println(isAllowed);

                if(isAllowed==true){
                    restart();
                }


                break;


            }catch (IllegalArgumentException e){
                StackTraceElement [] stack = e.getStackTrace();

                // DO NOTHING

            }catch (Exception e) {
                e.printStackTrace();
                connectionStatus.setForeground(Color.RED);
                connectionStatus.setText("Помилка (код 03)");
                functions.close(dataout,datain,socket);
                restart();
                break;
            }

        }




    }



    public static void main(String[] args) throws IOException {
        Client c = new Client("КПП-2(КТП)",true,false);
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
                        bwho.setText(name);
                        b.setBackground(Color.GREEN);

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
