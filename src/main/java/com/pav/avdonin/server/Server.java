package com.pav.avdonin.server; /**
 * Created by CleBo on 07.12.2017.
 */

import com.pav.avdonin.functions.ActListeners;
import com.pav.avdonin.functions.AnotherFunctions;
import com.pav.avdonin.functions.StatusButtons;
import com.pav.avdonin.logger.Logging;
import com.pav.avdonin.sql.SQL;
import com.pav.avdonin.visual.Frames;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.List;



public class Server extends JFrame {
    transient public static SQL sql = new SQL();
    transient private Socket socket;
    transient private ServerSocket server;
    transient public static Frames mainframes;
    transient static boolean statusOfLogger = false;
    transient public static  Logging logging;
    transient public static HashMap<String,String > mapallowedClients;

    transient Properties properties = new Properties();
    transient public static File client = new File("clients.txt");
    transient public static List  listOfClients = Collections.synchronizedList(new ArrayList<ConnectionPoint>());

    static public StatusButtons statusButtons;

    public Server(String name)  {
        createServerLogger();
        sql.createSQL();
        createGUI(name);

        loadPropertiesOptions();
        readAllowedClients();
        setSavedStatusOfButtons();
        startServer();
    }

    private void createServerLogger() {
        if(logging==null) {
            logging = new Logging();
            logging.createLogger();
        }
    }

    private void startServer() {
        try{
            while(true){
                /*Удаление офлайнслушателя  если кто-то подключился. Так как нам уже не нужен этот слушатель
                Добавляем слушателя OnlineListener*/
                if(listOfClients.size()>0) addOnlineListeners();
                waitingForNewClient();
            }
        }catch (Exception e){
            e.printStackTrace();
            logging.writeExeptionToLogger(e,statusOfLogger,Thread.currentThread());
            JOptionPane.showMessageDialog(null,"Помилка при створенні серверу");
        }
    }

    private void waitingForNewClient()  {
        try {
            socket=server.accept();

        ConnectionPoint connection = new ConnectionPoint(socket,mainframes);
        String ip = socket.getRemoteSocketAddress().toString().substring(1,socket.getRemoteSocketAddress().toString().indexOf(":"));
        connection.setName(ip);
        connection.start();
        listOfClients.add(connection);
        Thread.currentThread().sleep(500);
        FileUtils.writeStringToFile(client,"","UTF8");



        for (int i = 0; i <listOfClients.size() ; i++) {
            FileUtils.writeStringToFile(client,listOfClients.get(i).toString()+" "+mapallowedClients.get(listOfClients.get(i).toString())+"\r\n","UTF8",true);
        }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addOnlineListeners() {
        for (JButton b:
                mainframes.mainButtons) {
            b.removeActionListener((b.getActionListeners())[0]);
        }
        for (int i = 0; i <mainframes.mainButtons.length ; i++) {
            mainframes.mainButtons[i].addActionListener(new ActListeners().OnlineListenerForServer(mainframes, mainframes.name,mainframes.mainButtons[i],
                    mainframes.timeButtons[i],mainframes.placeButtons[i]));
        }
    }

    private void loadPropertiesOptions() {
        try {
            properties.load(getClass().getResourceAsStream("/settings.properties"));
            server = new ServerSocket((Integer.valueOf(properties.getProperty("port"))));
            if(properties.getProperty("logger").equals("true"))statusOfLogger=true;
        } catch (IOException e) {
            e.printStackTrace();
            logging.writeExeptionToLogger(e,statusOfLogger,Thread.currentThread());
        }
    }

    private void setSavedStatusOfButtons()  {
        statusButtons = new StatusButtons(mainframes.mainButtons, mainframes.timeButtons,
                mainframes.placeButtons,mainframes.listOfPersons);
        File file = new File("status.txt");
        if(file.length()>0){
            try {
                statusButtons.readStatusOFButtons();
            } catch (IOException e) {
                logging.writeExeptionToLogger(e,statusOfLogger,Thread.currentThread());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                logging.writeExeptionToLogger(e,statusOfLogger,Thread.currentThread());
                e.printStackTrace();
            }
        }
    }

    private void createGUI(String name) {
        mainframes= new Frames();
        mainframes.name = name;
        mainframes.readListofPersons();
        mainframes.createWindow(name, true);
        mainframes.createJButtonsArraysForServer(true,mainframes.listOfPersons);




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
            logging.writeExeptionToLogger(e,statusOfLogger,Thread.currentThread());
            //StackTraceElement [] stack = e.getStackTrace();

        }

    }

    public static void main(String[] args)  {
        Server s = new Server("EspiaServer");


    }
}