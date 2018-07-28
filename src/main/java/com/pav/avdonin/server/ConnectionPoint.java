package com.pav.avdonin.server;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pav.avdonin.functions.AnotherFunctions;
import com.pav.avdonin.functions.StatusButtons;
import com.pav.avdonin.functions.StatusButtonsSerializer;
import com.pav.avdonin.functions.SwitchButton;
import com.pav.avdonin.sql.SQL;
import com.pav.avdonin.visual.Frames;
import org.apache.commons.io.FileUtils;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ConnectionPoint extends Thread {


    public  long ID =0;
    public  String reason = "";
    Frames mainframe;



    private Socket socket;
    public DataOutputStream dataout;
    public DataInputStream datain;
    public SQL sql = new SQL();
    @Override
    public String toString() {
        return socket.getInetAddress().toString().substring(1);
    }

    public ConnectionPoint(Socket socket,Frames mainframe){
        this.mainframe = mainframe;
        this.socket = socket;


    }

    private void readData() throws IOException {
        //Метод для принятия данных от клиента
        String value = "";
        SwitchButton switchButton = new SwitchButton();

        while(true) {


            try {
                String ip = Server.listOfClients.get(Server.listOfClients.size() - 1).toString();
                switchButton.determineButton(this, ip,Server.mapallowedClients.get(ip),socket, datain,dataout,mainframe,ID);
                Server.statusButtons.writeStatusOFButtons();

            } catch (SocketException e){
                e.printStackTrace();
                Server.listOfClients.remove(currentThread());
             /*   logger.log(Level.INFO,Thread.currentThread().getName()+" disconnected\r\n");
                listOfClients.remove(currentThread());*/
               /* new SQL.exitFromSession(Thread.currentThread().getName(), AnotherFunctions.timeWithSeconds(),ID,"");*/
                System.out.println("ID after exiting = " + ID);

                if(reason.equals("press exit")){
                sql.exitFromSession(Thread.currentThread().getName(), AnotherFunctions.timeWithSeconds(),ID,reason);
                }else sql.exitFromSession(Thread.currentThread().getName(), AnotherFunctions.timeWithSeconds(),ID,e.getMessage());


                FileUtils.writeStringToFile(Server.client,"","UTF8");
                for (int i = 0; i <Server.listOfClients.size() ; i++) {
                        FileUtils.writeStringToFile(Server.client, Server.listOfClients.get(i).toString() + " " + Server.mapallowedClients.get(Server.listOfClients.get(i).toString().substring(1)) + "\r\n", "UTF8", true);
                }
                mainframe.jLabel.setText("Кількість клієнтів - " +Server.listOfClients.size());
                close();
                break;
            } catch(Exception e){
                e.printStackTrace();
                StackTraceElement [] stack = e.getStackTrace();
                Server.listOfClients.remove(currentThread());
                FileUtils.writeStringToFile(Server.client,"","UTF8");
                for (int i = 0; i <Server.listOfClients.size() ; i++) {
                    FileUtils.writeStringToFile(Server.client,Server.listOfClients.get(i).toString()+" "+Server.mapallowedClients.get(Server.listOfClients.get(i).toString().substring(1))+"\r\n","UTF8",true);
                }
                mainframe.jLabel.setText("Кількість клієнтів - " +Server.listOfClients.size());
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
            mainframe.jLabel.setText("Кількість клієнтів - " +Server.listOfClients.size());
            //отправка состояния кнопок клиентам

            StatusButtons statusButtons = new StatusButtons(mainframe.mainButtons,mainframe.timeButtons,
                    mainframe.placeButtons,mainframe.listOfPersons);
            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(StatusButtons.class, new StatusButtonsSerializer());
            Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

            dataout.writeUTF(gson.toJson(statusButtons));
            dataout.flush();




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
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }

            }

        }
    }


}