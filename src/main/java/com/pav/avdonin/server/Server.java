package com.pav.avdonin.server; /**
 * Created by CleBo on 07.12.2017.
 */
/*Смысл программы - фиксация местонахождения руководства.
Кнопками обозначены сокращения должностей руководства( Пример НРУ - НАЧАЛЬНИК Регионального управления).
Кнопки имеют два состояния - красные (должностное лицо отсутствует) и зеленые (должностное лицо на работе).
Сервер стоит у меня в кабинете, клиенты находяться на контрольно - пропускных пунктах. Если наряд на кпп нажимает
на кнопку, то соответствуюшие изменения отобразяться у меня на сервере.
* */



import com.pav.avdonin.functions.ActListeners;
import com.pav.avdonin.functions.StatusButtons;
import com.pav.avdonin.visual.Frames;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.List;


public class Server extends JFrame {
    transient private Socket socket;
    transient private ServerSocket server;
    Properties properties = new Properties();
    public static File client = new File("clients.txt");

    transient public static List  listOfClients = Collections.synchronizedList(new ArrayList<ConnectionPoint>());
    public static HashMap<String,String > mapallowedClients;
    static public StatusButtons statusButtons;
    public static Frames mainframes;


    public Server(String name)  {
         mainframes= new Frames();
         mainframes.name = name;
         mainframes.readListofPersons();
         mainframes.createWindow(name, true);
         mainframes.createJButtonsArraysForServer(true,mainframes.listOfPersons);

        statusButtons = new StatusButtons(mainframes.mainButtons, mainframes.timeButtons, mainframes.placeButtons,mainframes.listOfPersons);

        try {
            properties.load(getClass().getResourceAsStream("/settings.properties"));
           // createLogger();
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

            int temp = 0;//временная переменная для if else что ниже
            while(true){
                //Удаление слушателя OnlyServer если кто-то подключился. Так как нам уже не нужен этот слушатель
                //Добавляем слушателя OnlineListener
                //!!!!НАДО добавить востановление этого слушателя в случае если список соединений будет опять равняться нулю
                if(listOfClients.size()>0 && temp==0) {
                    for (JButton b:
                            mainframes.mainButtons) {
                        b.removeActionListener((b.getActionListeners())[0]);
                    }
                    for (int i = 0; i <mainframes.mainButtons.length ; i++) {
                        mainframes.mainButtons[i].addActionListener(new ActListeners().OnlineListenerForServer(mainframes.name,mainframes.mainButtons[i],
                                mainframes.timeButtons[i],mainframes.placeButtons[i]));
                    }
                    temp=1;
                }

                socket=server.accept();

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
        Server s = new Server("EspiaServer");


    }
}