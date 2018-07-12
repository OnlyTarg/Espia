package com.pav.avdonin.server; /**
 * Created by CleBo on 07.12.2017.
 */
/*Смысл программы - фиксация местонахождения руководства.
Кнопками обозначены сокращения должностей руководства( Пример НРУ - НАЧАЛЬНИК Регионального управления).
Кнопки имеют два состояния - красные (должностное лицо отсутствует) и зеленые (должностное лицо на работе).
Сервер стоит у меня в кабинете, клиенты находяться на контрольно - пропускных пунктах. Если наряд на кпп нажимает
на кнопку, то соответствуюшие изменения отобразяться у меня на сервере.
* */



import com.google.gson.Gson;
import com.pav.avdonin.functions.StatusButtons;
import com.pav.avdonin.visual.FlashingLight;
import com.pav.avdonin.sql.SQL;
import com.pav.avdonin.Main;
import com.pav.avdonin.media.Music;


import com.pav.avdonin.visual.Frames;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Server extends JFrame {
    transient private Socket socket;
    transient private ServerSocket server;
    Properties properties = new Properties();
    public static File client = new File("clients.txt");
    String name;
    transient public static List  listOfClients = Collections.synchronizedList(new ArrayList<ConnectionPoint>());
    static HashMap<String,String > mapallowedClients;
    static public StatusButtons statusButtons;
    public static Frames mainframes;


    public Server(String name)  {
         mainframes= new Frames("EspiaServer", true);
        statusButtons = new StatusButtons(mainframes.mainButtons, mainframes.timeButtons, mainframes.placeButtons);

        try {
            properties.load(getClass().getResourceAsStream("/settings.properties"));
           // createLogger();
            this.name = name;
            readAllowedClients();
            File file = new File("status.txt");
            if(file.length()>0){
                statusButtons.readStatusOFButtons();
            }
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try{
            server = new ServerSocket((Integer.valueOf(properties.getProperty("port"))));
            //System.out.println(server.getLocalPort());
            int temp = 0;//временная переменная для if else что ниже

            while(true){
                //Удаление слушателя OnlyServer если кто-то подключился. Так как нам уже не нужен этот слушатель
                //Добавляем слушателя OnlineListener
                //!!!!НАДО добавить востановление этого слушателя в случае если список соединений будет опять равняться нулю
                if(listOfClients.size()>0 && temp==0) {
                    /*ActionListener [] mas1 = b1.getActionListeners();
                    ActionListener [] mas2 = b2.getActionListeners();
                    ActionListener [] mas3= b3.getActionListeners();
                    ActionListener [] mas4 = b4.getActionListeners();
                    ActionListener [] mas5 = b5.getActionListeners();
                    ActionListener [] mas6 = b6.getActionListeners();
                    ActionListener [] mas7 = b7.getActionListeners();
                    ActionListener [] mas8 = b8.getActionListeners();
                    b1.removeActionListener(mas1[mas1.length-1]);
                    b2.removeActionListener(mas2[mas2.length-1]);
                    b3.removeActionListener(mas3[mas3.length-1]);
                    b4.removeActionListener(mas4[mas4.length-1]);
                    b5.removeActionListener(mas5[mas5.length-1]);
                    b6.removeActionListener(mas6[mas6.length-1]);
                    b7.removeActionListener(mas7[mas7.length-1]);
                    b8.removeActionListener(mas8[mas8.length-1]);
                    b1.addActionListener(OnlineListener(b1,b1info,b1who));
                    b2.addActionListener(OnlineListener(b2,b2info,b2who));
                    b3.addActionListener(OnlineListener(b3,b3info,b3who));
                    b4.addActionListener(OnlineListener(b4,b4info,b4who));
                    b5.addActionListener(OnlineListener(b5,b5info,b5who));
                    b6.addActionListener(OnlineListener(b6,b6info,b6who));
                    b7.addActionListener(OnlineListener(b7,b7info,b7who));
                    b8.addActionListener(OnlineListener(b8,b8info,b8who));*/
                    temp=1;
                }
                System.out.println("Waiting for someone");
                System.out.println(Inet4Address.getLocalHost().getHostAddress());
                socket=server.accept();

                System.out.println("Somebody is connected");
                ConnectionPoint connection = new ConnectionPoint(socket,mainframes);
                String ip = socket.getRemoteSocketAddress().toString().substring(1,socket.getRemoteSocketAddress().toString().indexOf(":"));
                connection.setName(ip);
                connection.start();
                listOfClients.add(connection);
                Thread.currentThread().sleep(500);

                FileUtils.writeStringToFile(client,"","UTF8");
                for (int i = 0; i <listOfClients.size() ; i++) {
                    FileUtils.writeStringToFile(client,listOfClients.get(i).toString()+" "+mapallowedClients.get(listOfClients.get(i).toString().substring(1))+"\r\n","UTF8",true);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Помилка при створенні серверу");
        }


    }





    private void readAllowedClients(){
        mapallowedClients = new HashMap<>();
        try {
            InputStream in = getClass().getResourceAsStream("/allowedClients.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF8"));
            String line="";
            while((line=reader.readLine())!=null){
                String[] client = line.replace("\uFEFF", "").split(":");
                if(client.length>1){
                    mapallowedClients.put(client[0],client[1]);
                }
                else if(client.length==0){
                    //DONOTHING
                }
                else{
                    mapallowedClients.put(client[0],"");

                }
            }
            //Scanner scanner = new Scanner(in, "UTF8");


            int temp=0;
            reader.close();
            in.close();

        }
        catch(Exception e){
            e.printStackTrace();
            StackTraceElement [] stack = e.getStackTrace();

        }

    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server s = new Server("121(ЦУС)");


    }
}