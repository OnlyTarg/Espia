package com.pav.avdonin.server;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pav.avdonin.functions.AnotherFunctions;
import com.pav.avdonin.functions.StatusButtons;
import com.pav.avdonin.functions.StatusButtonsSerializer;
import com.pav.avdonin.functions.SwitchButton;
import com.pav.avdonin.media.Music;
import com.pav.avdonin.sql.SQL;
import com.pav.avdonin.visual.FlashingLight;
import com.pav.avdonin.visual.Frames;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.apache.commons.io.FileUtils;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConnectionPoint extends Thread {

    Music music = new Music();
    AnotherFunctions functions = new AnotherFunctions();
    int ID =0;
    Frames mainframe;



    private Socket socket;
    public DataOutputStream dataout;
    public DataInputStream datain;

    @Override
    public String toString() {
        return socket.getInetAddress().toString();
    }

    public ConnectionPoint(Socket socket,Frames mainframe){
        this.mainframe = mainframe;

        this.socket = socket;
        //Thread.currentThread().setName("asa");
        //System.out.println(currentThread());

    }

    private void readData() throws IOException {
        //Метод для принятия данных от клиента
        String value = "";
        SwitchButton switchButton = new SwitchButton();

        while(true) {


            try {
                switchButton.determineButton(value,socket,false,datain,dataout,mainframe,ID);
                Server.statusButtons.writeStatusOFButtons();

            } catch (SocketException e){
                e.printStackTrace();
                Server.listOfClients.remove(currentThread());
               /* logger.log(Level.INFO,Thread.currentThread().getName()+" disconnected\r\n");
                listOfClients.remove(currentThread());
                sql.exitFromSession(Thread.currentThread().getName(), timeWithSeconds(),ID,"");*/
                FileUtils.writeStringToFile(Server.client,"","UTF8");
                for (int i = 0; i <Server.listOfClients.size() ; i++) {
                        FileUtils.writeStringToFile(Server.client, Server.listOfClients.get(i).toString() + " " + Server.mapallowedClients.get(Server.listOfClients.get(i).toString().substring(1)) + "\r\n", "UTF8", true);
                }
                mainframe.countClients.setText("Кількість клієнтів - " +Server.listOfClients.size());
                close();
                break;
            } catch(Exception e){
                e.printStackTrace();
                StackTraceElement [] stack = e.getStackTrace();
                Server.listOfClients.remove(currentThread());
                FileUtils.writeStringToFile(Server.client,"","UTF8");
                for (int i = 0; i <Server.listOfClients.size() ; i++) {
                    System.out.println("checking2 "+i);
                    FileUtils.writeStringToFile(Server.client,Server.listOfClients.get(i).toString()+" "+Server.mapallowedClients.get(Server.listOfClients.get(i).toString().substring(1))+"\r\n","UTF8",true);
                }
                mainframe.countClients.setText("Кількість клієнтів - " +Server.listOfClients.size());
                close();
                break;
            }
        }
    }




    @Override
    public void run() {
        try {

            datain = new DataInputStream(socket.getInputStream());
            dataout = new DataOutputStream(socket.getOutputStream());
            mainframe.countClients.setText("Кількість клієнтів - " +Server.listOfClients.size());
            //отправка состояния кнопок клиентам
            System.out.println("test");
            //Gson gson = new Gson();
           // Gson gson = new GsonBuilder().create();
            //Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            StatusButtons statusButtons = new StatusButtons(mainframe.mainButtons,mainframe.timeButtons,
                    mainframe.placeButtons,mainframe.listOfPersons);
            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(StatusButtons.class, new StatusButtonsSerializer());
            Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

            dataout.writeUTF(gson.toJson(statusButtons));
            dataout.flush();
            //System.out.println(gson.toJson(statusButtons));



            CheckingSignal checkingSignal = new CheckingSignal(this);
            checkingSignal.setDaemon(true);
            checkingSignal.start();
            readData();
            checkingSignal.interrupt();


        } catch(SocketException e){
            e.printStackTrace();
            StackTraceElement [] stack = e.getStackTrace();
           // logger.log(Level.INFO,Thread.currentThread().getName()+" disconnected \r\n");
        } catch (IOException e){
            e.printStackTrace();
            StackTraceElement [] stack = e.getStackTrace();
            //logger.log(Level.INFO,e.toString()+"\r\n"+stack[0]+"\r\n"+Thread.currentThread()+" disconnected \r\n");
            //JOptionPane.showMessageDialog(null,"Помилка при створенні з'єднання з клієнтом "+Thread.currentThread());
        }

    }

    public void close() {
        try {
            dataout.close();
            datain.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
            StackTraceElement [] stack = e.getStackTrace();
            //logger.log(Level.INFO,e.toString()+"\r\n"+stack[0]+"\r\n"+Thread.currentThread()+" disconnected \r\n");
            JOptionPane.showMessageDialog(null,"Помилка при зикритті потоків. ");
        }


    }




    private class CheckingSignal extends Thread{
        ConnectionPoint connectionPoint;
        public CheckingSignal(ConnectionPoint connection){
            this.connectionPoint = connection;
        }

        @Override
        public void run() {
            this.setName(connectionPoint.getName()+"(signal)");
            while(!Thread.currentThread().isInterrupted()){
                try {
                    connectionPoint.dataout.writeUTF("Connection test");
                    connectionPoint.dataout.flush();
                    Thread.currentThread().sleep(10000);

                } catch (IOException e) {
                    e.printStackTrace();
                    //StackTraceElement [] stack = e.getStackTrace();
                    //logger.log(Level.INFO,e.toString()+"\r\n"+stack[0]+"\r\n"+Thread.currentThread()+" disconnected \r\n");
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //logger.log(Level.INFO,Thread.currentThread().getName()+" shutdown, becouse main Thread was closed \r\n");
                    // System.out.println(Thread.currentThread().getName()+" shutdown, becouse main Thread was closed");
                    break;
                }

            }

        }
    }


}