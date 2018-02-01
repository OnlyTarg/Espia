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

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class WIWServer extends JFrame{
    JFrame frame;
    JButton b1,b2,b3,b4,b5,b6,b7;
    JLabel countClients;
    Clip clipClick,clipZvonok,clipDoor;
    File wavClick,wavZvonok,wavDoor;
    JButton b1info,b2info,b3info,b4info,b5info,b6info,b7info;



    public List  listOfClients = Collections.synchronizedList(new ArrayList<NewConnection>());;

    public void soundDoor(){
        try {
            wavDoor = new File("C:/Users/CleBo/IdeaProjects/PControler/src/main/resources/door.wav");
            clipDoor = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(wavDoor);
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
            wavClick = new File("C:/Users/CleBo/IdeaProjects/PControler/src/main/resources/click.wav");
            clipClick = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(wavClick);
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
            wavZvonok = new File("C:/Users/CleBo/IdeaProjects/PControler/src/main/resources/zv1.wav");
            clipZvonok = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(wavZvonok);
            clipZvonok.open(ais);
            ais.close();
            clipZvonok.start();
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


    public WIWServer() throws IOException {

        createWindow();
        createButtons();
        CreateServer ser = new CreateServer();


    }

    private void createWindow() {
        //Создаю основно окно
        //UIManager.setLookAndFeel(MotifButtonListener);
        frame = new JFrame();

        frame.setTitle("Espia");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(340,500);
        frame.setVisible(true);
        frame.setResizable(false);
    }
    private void createButtons() {
        //Создаю основные кнопки и одну надпись

        b1 = new JButton("Косік С.М.");
        b2 = new JButton("Бабюк В.Б.");
        b3 = new JButton("Зюзько Ю.В.");
        b4 = new JButton("Катюха І.С");
        b5 = new JButton("Ліщинський Д.В");
        b6 = new JButton("Ніколаєнко О.В.");
        b7 = new JButton("Гідзула В.О.");
        b1info = new JButton("....");
        b2info = new JButton("....");
        b3info = new JButton("....");
        b4info = new JButton("....");
        b5info = new JButton("....");
        b6info = new JButton("....");
        b7info = new JButton("....");



        Font fontGlobal = new Font("Times new Roman",Font.BOLD,20);
        Font fontStatus = new Font("Times new Roman",Font.BOLD,15);
        Font fontinfo = new Font("Times new Roman",Font.BOLD,15);
        frame.setLayout(null);

        countClients = new JLabel("Количество клиентов - " +listOfClients.size());
        countClients.setFont(fontStatus);
        countClients.setBounds(20,430,200,30);


        b1.setFont(fontGlobal);
        b2.setFont(fontGlobal);
        b3.setFont(fontGlobal);
        b4.setFont(fontGlobal);
        b5.setFont(fontGlobal);
        b6.setFont(fontGlobal);
        b7.setFont(fontGlobal);
        b1info.setFont(fontinfo);
        b2info.setFont(fontinfo);
        b3info.setFont(fontinfo);
        b4info.setFont(fontinfo);
        b5info.setFont(fontinfo);
        b6info.setFont(fontinfo);
        b7info.setFont(fontinfo);

        b1.setBounds(10, 10, 200, 50);
        b2.setBounds(10, 70, 200, 50);
        b3.setBounds(10, 130, 200, 50);
        b4.setBounds(10, 190, 200, 50);
        b5.setBounds(10, 250, 200, 50);
        b6.setBounds(10, 310, 200, 50);
        b7.setBounds(10, 370, 200, 50);
        b1info.setBounds(212, 10, 120, 50);
        b2info.setBounds(212, 70, 120, 50);
        b3info.setBounds(212, 130, 120, 50);
        b4info.setBounds(212, 190, 120, 50);
        b5info.setBounds(212, 250, 120, 50);
        b6info.setBounds(212, 310, 120, 50);
        b7info.setBounds(212, 370, 120, 50);

        b1.setBackground(Color.RED);
        b2.setBackground(Color.RED);
        b3.setBackground(Color.RED);
        b4.setBackground(Color.RED);
        b5.setBackground(Color.RED);
        b6.setBackground(Color.RED);
        b7.setBackground(Color.RED);
        b1info.setBackground(Color.YELLOW);
        b2info.setBackground(Color.YELLOW);
        b3info.setBackground(Color.YELLOW);
        b4info.setBackground(Color.YELLOW);
        b5info.setBackground(Color.YELLOW);
        b6info.setBackground(Color.YELLOW);
        b7info.setBackground(Color.YELLOW);

        b1.addActionListener(OfflineListener(b1,b1info));
        b2.addActionListener(OfflineListener(b2,b2info));
        b3.addActionListener(OfflineListener(b3,b3info));
        b4.addActionListener(OfflineListener(b4,b4info));
        b5.addActionListener(OfflineListener(b5,b5info));
        b6.addActionListener(OfflineListener(b6,b6info));
        b7.addActionListener(OfflineListener(b7,b7info));


        frame.add(b1);
        frame.add(b2);
        frame.add(b3);
        frame.add(b4);
        frame.add(b5);
        frame.add(b6);
        frame.add(b7);
        frame.add(countClients);
        frame.add(b1info);
        frame.add(b2info);
        frame.add(b3info);
        frame.add(b4info);
        frame.add(b5info);
        frame.add(b6info);
        frame.add(b7info);

    }
    private ActionListener OnlineListener (JButton b,JButton binfo) {

        /*Создаю слушатель для кнопок
        При нажатии на кнопку, она изменяет цвет на противоположный,
        в зависимости от того какой цвет был установлен до нажатия.
        После этого в OutputStream передается строка в виде "120 green"
        где первое значение - "положение кнопки по вертикали", а втрое - "цвет"*/

        ActionListener actionListener = new ActionListener() {

            @Override
            public  void actionPerformed(ActionEvent e) {
                soundClick();
                System.out.println(b1.getActionListeners().length);
                binfo.setText(time());
                Color buttoncolor = b.getBackground();
                try {
                    if (buttoncolor.equals(Color.RED)) {
                        b.setBackground(Color.GREEN);

                        for (int i = 0; i <listOfClients.size() ; i++) {
                            NewConnection con = (NewConnection)listOfClients.get(i);
                            con.dataout.writeUTF(b.getY()+" green");
                            con.dataout.flush();
                            //JOptionPane.showMessageDialog(null,"Данные отправлены клиенту");
                        }


                    } else {
                        b.setBackground(Color.RED);
                        binfo.setText(time());
                        for (int i = 0; i <listOfClients.size() ; i++) {
                            NewConnection con = (NewConnection)listOfClients.get(i);
                            con.dataout.writeUTF(b.getY()+" red");
                            con.dataout.flush();
                            //JOptionPane.showMessageDialog(null,"Данные отправлены клиенту");
                        }


                    }
                }catch (Exception ex){
                    ex.printStackTrace();

                }

            }
        };
        return actionListener;
    }
    private ActionListener OfflineListener (JButton b,JButton binfo) {
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
                    binfo.setText(time());
                    try {
                        //clipClick.start();
                        if (buttoncolor.equals(Color.RED)) {
                            b.setBackground(Color.GREEN);



                        } else {
                            b.setBackground(Color.RED);


                        }


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
                        b1.removeActionListener(mas1[mas1.length-1]);
                        b2.removeActionListener(mas2[mas2.length-1]);
                        b3.removeActionListener(mas3[mas3.length-1]);
                        b4.removeActionListener(mas4[mas4.length-1]);
                        b5.removeActionListener(mas5[mas5.length-1]);
                        b6.removeActionListener(mas6[mas6.length-1]);
                        b7.removeActionListener(mas7[mas7.length-1]);
                        b1.addActionListener(OnlineListener(b1,b1info));
                        b2.addActionListener(OnlineListener(b2,b2info));
                        b3.addActionListener(OnlineListener(b3,b3info));
                        b4.addActionListener(OnlineListener(b4,b4info));
                        b5.addActionListener(OnlineListener(b5,b5info));
                        b6.addActionListener(OnlineListener(b6,b6info));
                        b7.addActionListener(OnlineListener(b7,b7info));

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
                    String[] temp = value.split(" ");

                    switch (temp[0]) {
                        //case "value" - положение кнопок
                        case "10":
                            switchchoice(temp[1],temp[2],b1,b1info);
                            break;
                        case "70":
                            switchchoice(temp[1],temp[2],b2,b2info);
                            break;
                        case "130":
                            switchchoice(temp[1],temp[2],b3,b3info);
                            break;
                        case "190":
                            switchchoice(temp[1],temp[2],b4,b4info);
                            break;
                        case "250":
                            switchchoice(temp[1],temp[2],b5,b5info);
                            break;
                        case "310":
                            switchchoice(temp[1],temp[2],b6,b6info);
                            break;
                        case "370":
                            switchchoice(temp[1],temp[2],b7,b7info);
                            break;
                    }
                }catch(Exception e){
                    System.out.println(Thread.currentThread()+" disconnected");
                    listOfClients.remove(currentThread());
                    countClients.setText("Количество клиентов - " +listOfClients.size());
                    close();
                    break;
                }
            }
        }

        public void close() {
            try {
                dataout.close();
                datain.close();
                socket.close();

            } catch (IOException e) {
                System.out.println("Stream don't close");
            }


        }


        private void switchchoice (String color,String name,JButton b,JButton binfo){
            //метод решающий какую строку отправлять
            if (color.equals("green")) {
                if (b.getBackground().equals(Color.GREEN)) {
                    //DONOTHING
                }
                if (b.getBackground().equals(Color.RED)) {
                    b.setBackground(Color.GREEN);
                    binfo.setText(time());
                    soundZvonok();
                }

            }
            if (color.equals("red")) {
                if(b.getBackground().equals(Color.GREEN)){
                    b.setBackground(Color.RED);
                    binfo.setText(time());
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
                    con.dataout.writeUTF(b.getY() +" "+color);
                    con.dataout.flush();
                }catch(Exception e){
                    System.out.println("Misstake in concorrensy");
                    e.printStackTrace();
                }
            }
        }

       /* public String Velik(JButton b1,JButton b2,JButton b3,JButton b4,JButton b5,JButton b6,JButton b7){
            String temp = b1.getBackground().getRed() + " " + b2.getBackground().getRed() + " "+b3.getBackground().getRed() + " "+
                    b4.getBackground().getRed() + " "+b5.getBackground().getRed() + " "+b6.getBackground().getRed() + " "+b7.getBackground().getRed() + " ";
            return temp;
        }*/

        @Override
        public void run() {
            try {
                datain = new DataInputStream(socket.getInputStream());
                dataout = new DataOutputStream(socket.getOutputStream());
                countClients.setText("Количество клиентов - " +listOfClients.size());
                //отправка состояния кнопок клиентам
                Gson gson = new Gson();
                StatusButtons statusButtons = new StatusButtons(b1.getBackground(),b2.getBackground(),b3.getBackground(),
                        b4.getBackground(),b5.getBackground(),b6.getBackground(),b7.getBackground(),
                        b1info.getText(),b2info.getText(),b3info.getText(),b4info.getText(),
                        b5info.getText(),b6info.getText(),b7info.getText());
                dataout.writeUTF(gson.toJson(statusButtons));
                dataout.flush();
                readData();

            }catch (IOException e){
                e.printStackTrace();
                System.out.println("Mistake in method run()");
            }

        }


    }
    private class StatusButtons {
        //Класс фиксирующий состояние (цвет) кнопок. Отправляется в виде JSON на клиенты при подключении
        Color b1,b2,b3,b4,b5,b6,b7;
        String b1info,b2info,b3info,b4info,b5info,b6info,b7info;

        public StatusButtons(Color b1, Color b2, Color b3, Color b4, Color b5, Color b6, Color b7,
                             String b1info, String b2info, String b3info, String b4info, String b5info,
                             String b6info, String b7info) {
            this.b1 = b1;
            this.b2 = b2;
            this.b3 = b3;
            this.b4 = b4;
            this.b5 = b5;
            this.b6 = b6;
            this.b7 = b7;
            this.b1info = b1info;
            this.b2info = b2info;
            this.b3info = b3info;
            this.b4info = b4info;
            this.b5info = b5info;
            this.b6info = b6info;
            this.b7info = b7info;
        }
    }



    public static void main(String[] args) throws IOException {
        WIWServer w = new WIWServer();


    }
}
