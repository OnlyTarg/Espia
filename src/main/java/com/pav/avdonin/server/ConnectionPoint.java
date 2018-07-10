package com.pav.avdonin.server;
import com.google.gson.Gson;
import com.pav.avdonin.media.Music;
import com.pav.avdonin.sql.SQL;
import com.pav.avdonin.visual.FlashingLight;
import com.pav.avdonin.visual.Frames;
import org.apache.commons.io.FileUtils;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ConnectionPoint extends Thread {
    transient public List listOfClients = Collections.synchronizedList(new ArrayList<Server.ConnectionPoint>());
    File client = new File("clients.txt");
    ArrayList<String> allowedClients;
    HashMap<String,String > mapallowedClients;
    Music music = new Music();
    int hash=0;
    Frames mainframe = new Frames("EspiaServer", true);


    private Socket socket;
    DataOutputStream dataout;
    DataInputStream datain;

    @Override
    public String toString() {
        return socket.getInetAddress().toString();
    }

    public ConnectionPoint(Socket socket){
        this.socket = socket;
        //Thread.currentThread().setName("asa");
        //System.out.println(currentThread());

    }

    private void readData() throws IOException {
        //Метод для принятия данных от клиента
        String value = "";

        while(true) {

            try {
                value = datain.readUTF();
                String[] values = value.split("_");
                //System.out.println(value);

                /*Массив с значениями с входящего потока:
                values[0] - положение кнопки по вертекали
                values[1] - цвет (green;red)
                values[2] - имя (КПП-1; КПП-2(КТП))
                values[3] - время
                */
                switch (values[0]) {
                    //case "value" - положение кнопок
                    case "10*10":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[0],mainframe.timeButtons[0],mainframe.placeButtons[0]);
                        break;
                    case "10*70":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[1],mainframe.timeButtons[1],mainframe.placeButtons[1]);
                        break;
                    case "10*130":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[2],mainframe.timeButtons[2],mainframe.placeButtons[2]);
                        break;
                    case "10*190":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[3],mainframe.timeButtons[3],mainframe.placeButtons[3]);
                        break;
                    case "10*250":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[4],mainframe.timeButtons[4],mainframe.placeButtons[4]);
                        break;
                    case "10*310":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[5],mainframe.timeButtons[5],mainframe.placeButtons[5]);
                        break;
                    case "10*370":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[6],mainframe.timeButtons[6],mainframe.placeButtons[6]);
                        break;
                    case "10*430":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[7],mainframe.timeButtons[7],mainframe.placeButtons[7]);
                        break;
                    case "10*490":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[8],mainframe.timeButtons[8],mainframe.placeButtons[8]);
                        break;
                    case "10*550":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[9],mainframe.timeButtons[9],mainframe.placeButtons[9]);
                        break;
                    case "230*10":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[11],mainframe.timeButtons[11],mainframe.placeButtons[11]);
                        break;
                    case "230*70":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[12],mainframe.timeButtons[12],mainframe.placeButtons[12]);
                        break;
                    case "230*130":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[13],mainframe.timeButtons[13],mainframe.placeButtons[13]);
                        break;
                    case "230*190":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[14],mainframe.timeButtons[14],mainframe.placeButtons[14]);
                        break;
                    case "230*250":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[15],mainframe.timeButtons[15],mainframe.placeButtons[15]);
                        break;
                    case "230*310":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[16],mainframe.timeButtons[16],mainframe.placeButtons[16]);
                        break;
                    case "230*370":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[17],mainframe.timeButtons[17],mainframe.placeButtons[17]);
                        break;
                    case "230*430":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[18],mainframe.timeButtons[18],mainframe.placeButtons[18]);
                        break;
                    case "230*490":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[7],mainframe.timeButtons[7],mainframe.placeButtons[7]);
                        break;
                    case "230*550":
                        switchButton(values[1],values[2],values[3],
                                mainframe.mainButtons[7],mainframe.timeButtons[7],mainframe.placeButtons[7]);
                        break;

                    case "candidate":
                        try {
                            hash = Integer.parseInt(values[2]);
                            //String ip = socket.getRemoteSocketAddress().toString().substring(1,socket.getRemoteSocketAddress().toString().indexOf(":"));
                          //  sql.addEntering(dayOfWeek(),this.getName(),mapallowedClients.get(this.getName()), timeWithSeconds(),hash);
                        } catch (Exception e) {
                            e.printStackTrace();
                            e.printStackTrace();
                        }
                        if(values.length==3 && mapallowedClients.containsKey(values[1])){
                            dataout.writeUTF("isAllowed_"+"YES");
                            dataout.flush();
                        }
                        else {
                            dataout.writeUTF("isAllowed_"+"NO");
                            dataout.flush();
                        }
                        break;
                    case "exiting":
                        if(hash==0){
                            SQL.goodExitInformer(77777);
                        }else {
                            SQL.goodExitInformer(hash);
                        }
                        break;
                }
                writeStatusOFButtons();
            } catch (SocketException e){
                e.printStackTrace();
               /* logger.log(Level.INFO,Thread.currentThread().getName()+" disconnected\r\n");
                listOfClients.remove(currentThread());
                sql.exitFromSession(Thread.currentThread().getName(), timeWithSeconds(),hash,"");*/
                FileUtils.writeStringToFile(client,"","UTF8");
                for (int i = 0; i <listOfClients.size() ; i++) {
                    FileUtils.writeStringToFile(client,listOfClients.get(i).toString()+" "+mapallowedClients.get(listOfClients.get(i).toString().substring(1))+"\r\n","UTF8",true);
                }
                mainframe.countClients.setText("Кількість клієнтів - " +listOfClients.size());
                close();
                break;
            } catch(Exception e){
                e.printStackTrace();
                StackTraceElement [] stack = e.getStackTrace();
                //logger.log(Level.INFO,e.toString()+"\r\n"+stack[0]+"\r\n"+Thread.currentThread()+" disconnected\r\n");
                listOfClients.remove(currentThread());
                //sql.exitFromSession(timeWithSeconds(),Thread.currentThread().getName(),hash,"");
                FileUtils.writeStringToFile(client,"","UTF8");
                for (int i = 0; i <listOfClients.size() ; i++) {
                    FileUtils.writeStringToFile(client,listOfClients.get(i).toString()+" "+mapallowedClients.get(listOfClients.get(i).toString().substring(1))+"\r\n","UTF8",true);
                }
                mainframe.countClients.setText("Кількість клієнтів - " +listOfClients.size());
                close();
                break;
            }
        }
    }


    private void switchButton(String color, String name, String when, JButton b, JButton binfo, JButton bwho) throws IOException {
        //метод решающий какую строку отправлять
        new FlashingLight(b).start();
        if (color.equals("green")) {
            if (b.getBackground().equals(Color.GREEN)) {
                //DONOTHING
            }
            if (b.getBackground().equals(Color.RED)) {

                b.setBackground(Color.GREEN);
                binfo.setText(time());
                bwho.setText(name);
                music.soundZvonok();
            }

        }
        if (color.equals("red")) {
            if(b.getBackground().equals(Color.GREEN)){
                b.setBackground(Color.RED);
                binfo.setText(time());
                bwho.setText(name);
                music.soundDoor();

            }
            if(b.getBackground().equals(Color.RED)){
                //DONOTHING
            }
        }
        //пересылаем полученные данные всем пользователям из списка лист

        for (int i = 0; i <listOfClients.size() ; i++) {
            try{
                Server.ConnectionPoint con = (Server.ConnectionPoint)listOfClients.get(i);
                con.dataout.writeUTF(b.getY() +"_"+color+"_"+name+"_"+when);
                con.dataout.flush();

            }catch(Exception e){
                e.printStackTrace();
                StackTraceElement [] stack = e.getStackTrace();
                //logger.log(Level.INFO,e.toString()+"\r\n"+stack[0]+"\r\n"+Thread.currentThread()+" disconnected \r\n");
                //JOptionPane.showMessageDialog(null,"Помилка в передачі даних до всіх клієнтів. Зверніться до Адміністратора.");
                //e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {

            datain = new DataInputStream(socket.getInputStream());
            dataout = new DataOutputStream(socket.getOutputStream());

            mainframe.countClients.setText("Кількість клієнтів - " +listOfClients.size());
            //отправка состояния кнопок клиентам
            Gson gson = new Gson();
            StatusButtons statusButtons = new StatusButtons(mainframe.mainButtons,mainframe.timeButtons,mainframe.placeButtons);
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



    private void writeStatusOFButtons() throws IOException {
        StatusButtons statusButtons = new StatusButtons(mainframe.mainButtons,mainframe.timeButtons,mainframe.placeButtons);
        FileOutputStream file = new FileOutputStream("status.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
        objectOutputStream.writeObject(statusButtons);
        objectOutputStream.flush();
        objectOutputStream.close();
        file.close();
    }
    private void readStatusOFButtons() throws IOException, ClassNotFoundException {
        try{

            FileInputStream file = new FileInputStream("status.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(file);

            StatusButtons statusButtons = (StatusButtons) objectInputStream.readObject();
            objectInputStream.close();
            file.close();
            for (int i = 0; i < mainframe.mainButtons.length; i++) {
                mainframe.mainButtons[i].setBackground(statusButtons.mainButtons[i].getBackground());
                mainframe.timeButtons[i].setText(statusButtons.mainButtons[i].getText());
                mainframe.placeButtons[i].setText(statusButtons.placeButtons[i].getText());
            }
        }catch (Exception e){
            e.printStackTrace();
           //StackTraceElement [] stack = e.getStackTrace();
            //logger.log(Level.INFO,e.toString()+"\r\n"+stack[0]+"\r\n"+Thread.currentThread()+" disconnected \r\n");
            //JOptionPane.showMessageDialog(null,"Помилка при зчитуванні попередніх змін.");

        }

    }

    private class StatusButtons implements Serializable {
        // private static final long serialVersionUID = 1L;
        //Класс фиксирующий состояние (цвет) кнопок. Отправляется в виде JSON на клиенты при подключении
        public JButton[] mainButtons,timeButtons,placeButtons;

        public StatusButtons(JButton [] mainButtons,JButton [] timeButtons,JButton [] placeButtons) {
            this.mainButtons = mainButtons;
            this.timeButtons = timeButtons;
            this.placeButtons = placeButtons;

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