/**
 * Created by CleBo on 07.12.2017.
 */
/*Смысл программы - фиксация местонахождения руководства.
Кнопками обозначены сокращения должностей руководства( Пример НРУ - НАЧАЛЬНИК Регионального управления).
Кнопки имеют два состояния - красные (должностное лицо отсутствует) и зеленые (должностное лицо на работе).
Сервер стоит у меня в кабинете, клиенты находяться на контрольно - пропускных пунктах. Если наряд на кпп нажимает
на кнопку, то соответствуюшие изменения отобразяться у меня на сервере.
* */



import com.google.gson.Gson;
import jdk.nashorn.internal.scripts.JO;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class Server extends JFrame {
    String name;
    JFrame frame;
    JButton b1,b2,b3,b4,b5,b6,b7,b8;
    JButton b1info,b2info,b3info,b4info,b5info,b6info,b7info,b8info;
    JButton b1who,b2who,b3who,b4who,b5who,b6who,b7who,b8who;
    JLabel countClients;
    ArrayList<String> listOfPersons = new ArrayList<>();
    int [] countMigalka = {0,0,0,0,0,0,0,0,0}; //Счетчик для выхода из потока в случае, если была
    // повторно нажата кнопка до переставания мигания после первого нажаия
    // порядковый номер элемента массива соответсвует номеру кнопки (начинае с первого елемента). Тоесть countMigalka[1] Соответствует первой кнопке
    transient Clip clipClick,clipZvonok,clipDoor;
    transient public List  listOfClients = Collections.synchronizedList(new ArrayList<NewConnection>());;
    ArrayList<String> allowedClients;



    public Server(String name) throws IOException, ClassNotFoundException {
        this.name = name;
        readListofPersons();
        createWindow();
        createButtons();
        readAllowedClients();


        repaint();
        File file = new File("status.txt");
        if(file.length()>0){
        readStatusOFButtons();
        }

        CreateServer ser = new CreateServer();


    }
    private class CreateServer{
        private Socket socket;
        private ServerSocket server;

        public CreateServer (){
            try{
                server = new ServerSocket(6666);
                frame.repaint();
                int temp = 0;//временная переменная для if else что ниже

                while(true){
                    //Удаление слушателя OnlyServer если кто-то подключился. Так как нам уже не нужен этот слушатель
                    //Добавляем слушателя OnlineListener
                    //!!!!НАДО добавить востановление этого слушателя в случае если список соединений будет опять равняться нулю
                    if(listOfClients.size()>0 && temp==0) {
                        ActionListener [] mas1 = b1.getActionListeners();
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
                        b8.addActionListener(OnlineListener(b8,b8info,b8who));
                        temp=1;
                    }
                    System.out.println("Waiting for someone");
                    socket=server.accept();
                    System.out.println("Somebody is connected");
                    NewConnection connection = new NewConnection(socket);
                    connection.start();
                    listOfClients.add(connection);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public class NewConnection extends Thread{

        private Socket socket;
        DataOutputStream dataout;
        DataInputStream datain;


        public NewConnection(Socket socket){
            this.socket = socket;
        }

        private void readData() throws IOException {
            //Метод для принятия данных от клиента
            String value = "";

            while(true) {

                try {
                    value = datain.readUTF();
                    String[] values = value.split("_");
                    System.out.println(value);

                /*Массив с значениями с входящего потока:
                values[0] - положение кнопки по вертекали
                values[1] - цвет (green;red)
                values[2] - имя (КПП-1; КПП-2(КТП))
                values[3] - время
                */
                    switch (values[0]) {
                        //case "value" - положение кнопок
                        case "10":
                            switchchoice(values[1],values[2],values[3],b1,b1info,b1who);
                            break;
                        case "70":
                            switchchoice(values[1],values[2],values[3],b2,b2info,b2who);
                            break;
                        case "130":
                            switchchoice(values[1],values[2],values[3],b3,b3info,b3who);
                            break;
                        case "190":
                            switchchoice(values[1],values[2],values[3],b4,b4info,b4who);
                            break;
                        case "250":
                            switchchoice(values[1],values[2],values[3],b5,b5info,b5who);
                            break;
                        case "310":
                            switchchoice(values[1],values[2],values[3],b6,b6info,b6who);
                            break;
                        case "370":
                            switchchoice(values[1],values[2],values[3],b7,b7info,b7who);
                            break;
                        case "430":
                            switchchoice(values[1],values[2],values[3],b8,b8info,b8who);
                            break;
                        case "candidate":
                           // JOptionPane.showMessageDialog(null,"GOOD!");
                            System.out.println(allowedClients);
                            if(allowedClients.contains(values[1])){
                                dataout.writeUTF("isAllowed_"+"YES");
                                dataout.flush();
                            }
                            else {
                                dataout.writeUTF("isAllowed_"+"NO");
                                dataout.flush();
                            }
                            break;
                    }
                    writeStatusOFButtons();
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println(Thread.currentThread()+" disconnected");
                    listOfClients.remove(currentThread());
                    countClients.setText("Кількість клієнтів - " +listOfClients.size());
                    close();
                    break;
                }
            }
        }


        private void switchchoice (String color,String name,String when, JButton b,JButton binfo,JButton bwho) throws IOException {
            //метод решающий какую строку отправлять
            new Migalkaa(b,Color.YELLOW).start();
            if (color.equals("green")) {
                if (b.getBackground().equals(Color.GREEN)) {
                    //DONOTHING
                }
                if (b.getBackground().equals(Color.RED)) {

                    b.setBackground(Color.GREEN);
                    binfo.setText(time());
                    bwho.setText(name);
                    soundZvonok();
                }

            }
            if (color.equals("red")) {
                if(b.getBackground().equals(Color.GREEN)){
                    b.setBackground(Color.RED);
                    binfo.setText(time());
                    bwho.setText(name);
                    soundDoor();

                }
                if(b.getBackground().equals(Color.RED)){
                    //DONOTHING
                }
            }
            //пересылаем полученные данные всем пользователям из списка лист




            for (int i = 0; i <listOfClients.size() ; i++) {
                try{
                    NewConnection con = (NewConnection)listOfClients.get(i);
                    con.dataout.writeUTF(b.getY() +"_"+color+"_"+name+"_"+when);
                    con.dataout.flush();

                }catch(Exception e){
                    JOptionPane.showMessageDialog(null,"Misstake in concorrensy");
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            try {

                datain = new DataInputStream(socket.getInputStream());
                dataout = new DataOutputStream(socket.getOutputStream());
                countClients.setText("Кількість клієнтів - " +listOfClients.size());
                //отправка состояния кнопок клиентам
                Gson gson = new Gson();
                StatusButtons statusButtons = new StatusButtons(b1.getBackground(),b2.getBackground(),b3.getBackground(),
                        b4.getBackground(),b5.getBackground(),b6.getBackground(),b7.getBackground(),b8.getBackground(),
                        b1info.getText(),b2info.getText(),b3info.getText(),b4info.getText(),
                        b5info.getText(),b6info.getText(),b7info.getText(),b8info.getText(),b1who.getText(),b2who.getText(),b3who.getText(),
                        b4who.getText(),b5who.getText(),b6who.getText(),b7who.getText(),b8who.getText(),b1.getText(),b2.getText(),b3.getText(),
                        b4.getText(),b5.getText(),b6.getText(),b7.getText(),b8.getText());
                dataout.writeUTF(gson.toJson(statusButtons));
                dataout.flush();
                readData();

            }catch (IOException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Mistake in method run()");
            }

        }

        public void close() {
            try {
                dataout.close();
                datain.close();
                socket.close();

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,"Stream don't close");
            }


        }


    }
    private class StatusButtons implements Serializable {
        // private static final long serialVersionUID = 1L;
        //Класс фиксирующий состояние (цвет) кнопок. Отправляется в виде JSON на клиенты при подключении
        Color b1,b2,b3,b4,b5,b6,b7,b8;
        String b1info,b2info,b3info,b4info,b5info,b6info,b7info,b8info;
        String b1who,b2who,b3who,b4who,b5who,b6who,b7who,b8who;
        String b1name,b2name,b3name,b4name,b5name,b6name,b7name,b8name;


        public StatusButtons(Color b1, Color b2, Color b3, Color b4, Color b5, Color b6, Color b7,Color b8,
                             String b1info, String b2info, String b3info, String b4info, String b5info,
                             String b6info, String b7info,String b8info, String b1who, String b2who, String b3who, String b4who,
                             String b5who, String b6who, String b7who,String b8who, String b1name,String b2name,String b3name,String b4name,
                             String b5name,String b6name,String b7name,String b8name) {
            this.b1 = b1;
            this.b2 = b2;
            this.b3 = b3;
            this.b4 = b4;
            this.b5 = b5;
            this.b6 = b6;
            this.b7 = b7;
            this.b8 = b8;
            this.b1info = b1info;
            this.b2info = b2info;
            this.b3info = b3info;
            this.b4info = b4info;
            this.b5info = b5info;
            this.b6info = b6info;
            this.b7info = b7info;
            this.b8info = b8info;
            this.b1who = b1who;
            this.b2who = b2who;
            this.b3who = b3who;
            this.b4who = b4who;
            this.b5who = b5who;
            this.b6who = b6who;
            this.b7who = b7who;
            this.b8who = b8who;
            this.b1name = b1name;
            this.b2name = b2name;
            this.b3name = b3name;
            this.b4name = b4name;
            this.b5name = b5name;
            this.b6name = b6name;
            this.b7name = b7name;
            this.b8name = b8name;
        }
    }
    public class Migalkaa extends Thread{
        JButton b;
        Color color;

        public Migalkaa(JButton b,Color color){
            this.b = b;
            this.color = color;
        }
        public void action(int migalka){
            migalka++;
            Thread.currentThread().setName("Migalka" + b.getText());


            for (int i = 0; i < 15; i++) {

                try {
                    b.setForeground(color);
                    Thread.currentThread().sleep(200);
                    //repaint();
                    b.setForeground(Color.BLACK);
                    Thread.currentThread().sleep(200);

                    if(migalka >1){
                        migalka--;
                        return;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            migalka--;
        }
        @Override
        public void run() {
            try {
                if (b.equals(b1)) action(countMigalka[1]);
                if (b.equals(b2)) action(countMigalka[2]);
                if (b.equals(b3)) action(countMigalka[3]);
                if (b.equals(b4)) action(countMigalka[3]);
                if (b.equals(b5)) action(countMigalka[3]);
                if (b.equals(b6)) action(countMigalka[3]);
                if (b.equals(b7)) action(countMigalka[3]);
                if (b.equals(b8)) action(countMigalka[3]);
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,"Одновременное нажатие");
                e.printStackTrace();
            }


        }

    }

    private void createWindow() {
        //Создаю основно окно
        //UIManager.setLookAndFeel(MotifButtonListener);
        frame = new JFrame();

        frame.setTitle("EspiaServer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(340,560);
        frame.setVisible(true);
        frame.setResizable(false);
    }
    private void createButtons() {
        //Создаю основные кнопки и одну надпись

        b1 = new JButton("");
        b2 = new JButton("");
        b3 = new JButton("");
        b4 = new JButton("");
        b5 = new JButton("");
        b6 = new JButton("");
        b7 = new JButton("");
        b8 = new JButton("");
try{
        b1.setText(listOfPersons.get(0));
        b2.setText(listOfPersons.get(1));
        b3.setText(listOfPersons.get(2));
        b4.setText(listOfPersons.get(3));
        b5.setText(listOfPersons.get(4));
        b6.setText(listOfPersons.get(5));
        b7.setText(listOfPersons.get(6));
        b8.setText(listOfPersons.get(7));}

catch(Exception e){
    e.printStackTrace();
    JOptionPane.showMessageDialog(null,e.getMessage());
}
listOfPersons.clear();

        b1info = new JButton("....");
        b2info = new JButton("....");
        b3info = new JButton("....");
        b4info = new JButton("....");
        b5info = new JButton("....");
        b6info = new JButton("....");
        b7info = new JButton("....");
        b8info = new JButton("....");


        b1who = new JButton("....");
        b2who = new JButton("....");
        b3who = new JButton("....");
        b4who = new JButton("....");
        b5who = new JButton("....");
        b6who = new JButton("....");
        b7who = new JButton("....");
        b8who = new JButton("....");



        Font fontGlobal = new Font("Times new Roman",Font.BOLD,20);
        Font fontStatus = new Font("Times new Roman",Font.BOLD,15);
        Font fontinfo = new Font("Times new Roman",Font.BOLD,14);
        frame.setLayout(null);

        countClients = new JLabel("Кількість клієнтів - " +listOfClients.size());
        countClients.setFont(fontStatus);
        countClients.setBounds(20,490,200,30);


        b1.setFont(fontGlobal);
        b2.setFont(fontGlobal);
        b3.setFont(fontGlobal);
        b4.setFont(fontGlobal);
        b5.setFont(fontGlobal);
        b6.setFont(fontGlobal);
        b7.setFont(fontGlobal);
        b8.setFont(fontGlobal);

        b1info.setFont(fontinfo);
        b2info.setFont(fontinfo);
        b3info.setFont(fontinfo);
        b4info.setFont(fontinfo);
        b5info.setFont(fontinfo);
        b6info.setFont(fontinfo);
        b7info.setFont(fontinfo);
        b8info.setFont(fontinfo);

        b1who.setFont(fontinfo);
        b2who.setFont(fontinfo);
        b3who.setFont(fontinfo);
        b4who.setFont(fontinfo);
        b5who.setFont(fontinfo);
        b6who.setFont(fontinfo);
        b7who.setFont(fontinfo);
        b8who.setFont(fontinfo);

        b1.setBounds(10, 10, 200, 50);
        b2.setBounds(10, 70, 200, 50);
        b3.setBounds(10, 130, 200, 50);
        b4.setBounds(10, 190, 200, 50);
        b5.setBounds(10, 250, 200, 50);
        b6.setBounds(10, 310, 200, 50);
        b7.setBounds(10, 370, 200, 50);
        b8.setBounds(10, 430, 200, 50);

        b1info.setBounds(212, 10, 120, 25);
        b2info.setBounds(212, 70, 120, 25);
        b3info.setBounds(212, 130, 120, 25);
        b4info.setBounds(212, 190, 120, 25);
        b5info.setBounds(212, 250, 120, 25);
        b6info.setBounds(212, 310, 120, 25);
        b7info.setBounds(212, 370, 120, 25);
        b8info.setBounds(212, 430, 120, 25);

        b1who.setBounds(212, 35, 120, 25);
        b2who.setBounds(212, 95, 120, 25);
        b3who.setBounds(212, 155, 120, 25);
        b4who.setBounds(212, 215, 120, 25);
        b5who.setBounds(212, 275, 120, 25);
        b6who.setBounds(212, 335, 120, 25);
        b7who.setBounds(212, 395, 120, 25);
        b8who.setBounds(212, 455, 120, 25);

        b1.setBackground(Color.RED);
        b2.setBackground(Color.RED);
        b3.setBackground(Color.RED);
        b4.setBackground(Color.RED);
        b5.setBackground(Color.RED);
        b6.setBackground(Color.RED);
        b7.setBackground(Color.RED);
        b8.setBackground(Color.RED);

        b1info.setBackground(Color.YELLOW);
        b2info.setBackground(Color.YELLOW);
        b3info.setBackground(Color.YELLOW);
        b4info.setBackground(Color.YELLOW);
        b5info.setBackground(Color.YELLOW);
        b6info.setBackground(Color.YELLOW);
        b7info.setBackground(Color.YELLOW);
        b8info.setBackground(Color.YELLOW);
        b1who.setBackground(Color.YELLOW);
        b2who.setBackground(Color.YELLOW);
        b3who.setBackground(Color.YELLOW);
        b4who.setBackground(Color.YELLOW);
        b5who.setBackground(Color.YELLOW);
        b6who.setBackground(Color.YELLOW);
        b7who.setBackground(Color.YELLOW);
        b8who.setBackground(Color.YELLOW);

        b1.addActionListener(OfflineListener(this,b1,b1info,b1who));
        b2.addActionListener(OfflineListener(this,b2,b2info,b2who));
        b3.addActionListener(OfflineListener(this,b3,b3info,b3who));
        b4.addActionListener(OfflineListener(this,b4,b4info,b4who));
        b5.addActionListener(OfflineListener(this,b5,b5info,b5who));
        b6.addActionListener(OfflineListener(this,b6,b6info,b6who));
        b7.addActionListener(OfflineListener(this,b7,b7info,b7who));
        b8.addActionListener(OfflineListener(this,b8,b8info,b8who));


        frame.add(b1);
        frame.add(b2);
        frame.add(b3);
        frame.add(b4);
        frame.add(b5);
        frame.add(b6);
        frame.add(b7);
        frame.add(b8);


        frame.add(countClients);
        frame.add(b1info);
        frame.add(b2info);
        frame.add(b3info);
        frame.add(b4info);
        frame.add(b5info);
        frame.add(b6info);
        frame.add(b7info);
        frame.add(b8info);
        frame.add(b1who);
        frame.add(b2who);
        frame.add(b3who);
        frame.add(b4who);
        frame.add(b5who);
        frame.add(b6who);
        frame.add(b7who);
        frame.add(b8who);

    }
    public void soundDoor(){
        try {
            clipDoor = AudioSystem.getClip();
            InputStream input = new BufferedInputStream(getClass().getResourceAsStream("door.wav"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(input);
            clipDoor.open(ais);
            clipDoor.start();
            ais.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void soundClick(){
        try {
            clipClick = AudioSystem.getClip();
            InputStream input = new BufferedInputStream(getClass().getResourceAsStream("click1.wav"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(input);
            clipClick.open(ais);
            clipClick.start();
            ais.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void soundZvonok(){
        try {
            clipZvonok = AudioSystem.getClip();
            InputStream input = new BufferedInputStream(getClass().getResourceAsStream("zv1.wav"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(input);
            clipZvonok.open(ais);
            clipZvonok.start();
            ais.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public String time (){
        DateFormat df = new SimpleDateFormat("dd.MM HH:mm");
        Date currenttime = Calendar.getInstance().getTime();
        String time = df.format(currenttime);
        return time;

    }
    private void readAllowedClients(){
        try {
            InputStream in = getClass().getResourceAsStream("/allowedClients.txt");
            Scanner scanner = new Scanner(in, "Cp1251");
            allowedClients = new ArrayList<>();
            while (scanner.hasNext()) {
                String client = scanner.nextLine();
                allowedClients.add(client.trim());
            }
            scanner.close();
        }
            catch(Exception e){
                e.printStackTrace();
                System.out.println("Ошибка при считывании файл разрешенных клиентов");
            }

    }
    private  void writeStatusOFButtons() throws IOException {
        StatusButtons statusButtons = new StatusButtons(b1.getBackground(),b2.getBackground(),b3.getBackground(),
                b4.getBackground(),b5.getBackground(),b6.getBackground(),b7.getBackground(),b8.getBackground(),
                b1info.getText(),b2info.getText(),b3info.getText(),b4info.getText(),
                b5info.getText(),b6info.getText(),b7info.getText(),b8info.getText(),b1who.getText(),b2who.getText(),b3who.getText(),
                b4who.getText(),b5who.getText(),b6who.getText(),b7who.getText(),b8who.getText(),b1.getText(),b2.getText(),b3.getText(),
                b4.getText(),b5.getText(),b6.getText(),b7.getText(),b8.getText());
        FileOutputStream file = new FileOutputStream("status.txt");
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(statusButtons);
        out.flush();
        out.close();
        file.close();
    }
    private  void readStatusOFButtons() throws IOException, ClassNotFoundException {
        try{

            FileInputStream input = new FileInputStream("status.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(input);

            StatusButtons statusButtons = (StatusButtons) objectInputStream.readObject();
            objectInputStream.close();
            input.close();
            b1.setBackground(statusButtons.b1);
            b2.setBackground(statusButtons.b2);
            b3.setBackground(statusButtons.b3);
            b4.setBackground(statusButtons.b4);
            b5.setBackground(statusButtons.b5);
            b6.setBackground(statusButtons.b6);
            b7.setBackground(statusButtons.b7);
            b8.setBackground(statusButtons.b8);

            b1info.setText(statusButtons.b1info);
            b2info.setText(statusButtons.b2info);
            b3info.setText(statusButtons.b3info);
            b4info.setText(statusButtons.b4info);
            b5info.setText(statusButtons.b5info);
            b6info.setText(statusButtons.b6info);
            b7info.setText(statusButtons.b7info);
            b8info.setText(statusButtons.b8info);

            b1who.setText(statusButtons.b1who);
            b2who.setText(statusButtons.b2who);
            b3who.setText(statusButtons.b3who);
            b4who.setText(statusButtons.b4who);
            b5who.setText(statusButtons.b5who);
            b6who.setText(statusButtons.b6who);
            b7who.setText(statusButtons.b7who);
            b8who.setText(statusButtons.b8who);

        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Misstake from read class");
            System.out.println("Misstake from read class");
        }

    }
    private  void readListofPersons() throws IOException, ClassNotFoundException {
        try{

            InputStream in = getClass().getResourceAsStream("/list.txt");
           // FileInputStream in = new FileInputStream("list.txt");

            Scanner scanner = new Scanner(in,"Cp1251");
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                listOfPersons.add(line.trim());

            }
            scanner.close();
            in.close();









        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Misstake from read list");
        }

    }
    public  void choiceWhoAndWhen(JButton binfo,JButton bwho){
        Object when = JOptionPane.showInputDialog(null,
                "", "Введіть дату",
                JOptionPane.INFORMATION_MESSAGE, null,null,time());
        binfo.setText(when.toString());
        Object[] possibleValues = { "КПП-1", "КПП-2(КТП)" };
        Object who = JOptionPane.showInputDialog(null,
                "", "Виберіть правильне",
                JOptionPane.INFORMATION_MESSAGE, null,
                possibleValues, possibleValues[0]);

        bwho.setText(who.toString());
    }


    private ActionListener OnlineListener (JButton b,JButton binfo, JButton bwho) {

        /*Создаю слушатель для кнопок
        При нажатии на кнопку, она изменяет цвет на противоположный,
        в зависимости от того какой цвет был установлен до нажатия.
        После этого в OutputStream передается строка в виде "120 green"
        где первое значение - "положение кнопки по вертикали", а втрое - "цвет"*/

        ActionListener actionListener = new ActionListener() {

            @Override
            public  void actionPerformed(ActionEvent e) {
                soundClick();
                choiceWhoAndWhen(binfo,bwho);

                Color buttoncolor = b.getBackground();
                try {
                    if (buttoncolor.equals(Color.RED)) {
                        b.setBackground(Color.GREEN);

                        for (int i = 0; i <listOfClients.size() ; i++) {
                            NewConnection con = (NewConnection)listOfClients.get(i);
                            con.dataout.writeUTF(b.getY() + "_green" + "_" + bwho.getText() + "_" + binfo.getText());
                            con.dataout.flush();
                            //JOptionPane.showMessageDialog(null,"Данные отправлены клиенту");
                        }


                    } else {
                        b.setBackground(Color.RED);
                        //binfo.setText(time());
                        for (int i = 0; i <listOfClients.size() ; i++) {
                            NewConnection con = (NewConnection)listOfClients.get(i);
                            con.dataout.writeUTF(b.getY() + "_red" + "_" + bwho.getText() + "_" + binfo.getText());
                            con.dataout.flush();
                            //JOptionPane.showMessageDialog(null,"Данные отправлены клиенту");
                        }
                        writeStatusOFButtons();


                    }

                }catch (Exception ex){
                    ex.printStackTrace();

                }


            }
        };
        return actionListener;
    }
    private ActionListener OfflineListener (Server s,JButton b,JButton binfo,JButton bwho) {
        /*Создаю слушатель для кнопок
        При нажатии на кнопку, она изменяет цвет на противоположный,
        в зависимости от того какой цвет был установлен до нажатия.
        Работает только в офлайне
       */

        ActionListener actionListener = null;
        try {
            actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    soundClick();
                    Color buttoncolor = b.getBackground();

                    choiceWhoAndWhen(binfo,bwho);

                    try {
                        //clipClick.start();
                        if (buttoncolor.equals(Color.RED)) {
                            b.setBackground(Color.GREEN);




                        } else {
                            b.setBackground(Color.RED);



                        }
                        writeStatusOFButtons();

                       // writeStatusOFButtons(s,"status.txt");

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }


                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actionListener;
    }







    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server s = new Server("121(ЦУС)");


    }
}
