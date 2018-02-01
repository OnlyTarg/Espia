/**
 * Created by CleBo on 07.12.2017.
 */

import com.google.gson.Gson;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by CleBo on 23.11.2017.
 */
public class WiWClient extends JFrame{
    String name;
    JFrame frame;
    InetAddress ipAddress;
    Socket socket;
    DataInputStream datain;
    DataOutputStream dataout;
    JButton b1,b2,b3,b4,b5,b6,b7;
    JButton b1info,b2info,b3info,b4info,b5info,b6info,b7info;
    JLabel connectionStatus= new JLabel("Статус соединения");
    Clip clipClick,clipZvonok,clipDoor;
    File wavClick,wavZvonok,wavDoor;


    public WiWClient(String s) throws IOException {
        this.name = s;
        window();
        buttons();
        createClient();
        readData();
        close();
    }

    public void window() {
        //Создаю основное окно
        frame = new JFrame();
        frame.setTitle("Espia");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(340,500);
        frame.setVisible(true);
        frame.setResizable(false);

    }
    public void buttons() {
        //Создаю все кнопки
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


        frame.setLayout(null);
        frame.repaint();


        Font font = new Font("Times new Roman",Font.BOLD,20);
        Font fontinfo = new Font("Times new Roman",Font.BOLD,15);



        b1.setFont(font);
        b2.setFont(font);
        b3.setFont(font);
        b4.setFont(font);
        b5.setFont(font);
        b6.setFont(font);
        b7.setFont(font);
        connectionStatus.setFont(font);
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


        connectionStatus.setBounds(40,430,200,30);
        connectionStatus.setHorizontalTextPosition(SwingConstants.CENTER);


        b1.setBackground(Color.RED);
        b2.setBackground(Color.RED);
        b3.setBackground(Color.RED);
        b4.setBackground(Color.RED);
        b5.setBackground(Color.RED);
        b6.setBackground(Color.RED);
        b7.setBackground(Color.RED);
        connectionStatus.setForeground(Color.BLACK);
        b1info.setBackground(Color.YELLOW);
        b2info.setBackground(Color.YELLOW);
        b3info.setBackground(Color.YELLOW);
        b4info.setBackground(Color.YELLOW);
        b5info.setBackground(Color.YELLOW);
        b6info.setBackground(Color.YELLOW);
        b7info.setBackground(Color.YELLOW);

        b1.addActionListener(OnlineListener(b1,b1info));
        b2.addActionListener(OnlineListener(b2,b2info));
        b3.addActionListener(OnlineListener(b3,b3info));
        b4.addActionListener(OnlineListener(b4,b4info));
        b5.addActionListener(OnlineListener(b5,b5info));
        b6.addActionListener(OnlineListener(b6,b6info));
        b7.addActionListener(OnlineListener(b7,b7info));

        frame.add(b1);
        frame.add(b2);
        frame.add(b3);
        frame.add(b4);
        frame.add(b5);
        frame.add(b6);
        frame.add(b7);
        frame.add(connectionStatus);
        frame.add(b1info);
        frame.add(b2info);
        frame.add(b3info);
        frame.add(b4info);
        frame.add(b5info);
        frame.add(b6info);
        frame.add(b7info);
        repaint();

    }

    //методы для воспросизведения звуков
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


    private void createClient() {
        //Создаю клиент
        try {
            int serverPort = 6666;
            String address = "localhost";//10.244.1.121    localhost
            connectionStatus.setText("Соединение......");
            ipAddress = InetAddress.getByName(address);

            socket = new Socket(ipAddress, serverPort);
            frame.repaint();
            datain = new DataInputStream(socket.getInputStream());
            dataout = new DataOutputStream(socket.getOutputStream());




            Thread.currentThread().sleep(2000);
            connectionStatus.setForeground(Color.GREEN);
            connectionStatus.setBounds(50,430,200,30);
            connectionStatus.setText("Подключено");
            //Прием состояния кнопок (цвет кнопок) сервера на момент подключения

            Gson gson = new Gson();
            String msg = datain.readUTF();
            StatusButtons statusButtons = gson.fromJson(msg,StatusButtons.class);
            b1.setBackground(statusButtons.b1);
            b2.setBackground(statusButtons.b2);
            b3.setBackground(statusButtons.b3);
            b4.setBackground(statusButtons.b4);
            b5.setBackground(statusButtons.b5);
            b6.setBackground(statusButtons.b6);
            b7.setBackground(statusButtons.b7);
            b1info.setText(statusButtons.b1info);
            b2info.setText(statusButtons.b2info);
            b3info.setText(statusButtons.b3info);
            b4info.setText(statusButtons.b4info);
            b5info.setText(statusButtons.b5info);
            b6info.setText(statusButtons.b6info);
            b7info.setText(statusButtons.b7info);

        } catch (Exception e) {
            connectionStatus.setForeground(Color.RED);
            connectionStatus.setBounds(30,430,200,30);
            connectionStatus.setText("Помилка (код 01)");
            //JOptionPane.showMessageDialog(null,"Ошибка при подключении к серверу");
            e.printStackTrace();

        }


    }
    public void readData()  {
        //Метод принимает данные от сервера
        String value = "";
        while(true){
            try{
                value = datain.readUTF();


            }catch (SocketException e){
                connectionStatus.setForeground(Color.RED);
                connectionStatus.setBounds(65,430,200,30);
                connectionStatus.setText("Ошибка2");
                break;

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }


            String [] temp = value.split(" ");
            switch(temp[0]){
                case "10":
                    switchchoice(temp[1],b1,b1info);
                    break;
                case "70":
                    switchchoice(temp[1],b2,b2info);
                    break;
                case "130":
                    switchchoice(temp[1],b3,b3info);
                    break;
                case "190":
                    switchchoice(temp[1],b4,b4info);
                    break;
                case "250":
                    switchchoice(temp[1],b5,b5info);
                    break;
                case "310":
                    switchchoice(temp[1],b6,b6info);
                    break;
                case "370":
                    switchchoice(temp[1],b7,b7info);
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
            System.out.println("Stream dont close");
        }


    }
    public String time (){
        DateFormat df = new SimpleDateFormat("dd.MM HH:mm");
        Date currenttime = Calendar.getInstance().getTime();
        String time = df.format(currenttime);
        return time;

    }
    public void switchchoice (String color,JButton b,JButton binfo){
        if (color.equals("green")) {
            if (b.getBackground().equals(Color.GREEN)) {

                //DONOTHING, ONLY SET TIME

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
                soundDoor();
            }
            if(b.getBackground().equals(Color.RED)){
                //DONOTHING, ONLY SET TIME

            }
        }
    }
    public ActionListener OnlineListener (JButton b,JButton binfo) {
        //Создаю слушатель, такой же как на сервере.
        //Изменяет состояние кнопок и передает инфо на сервер
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soundClick();
                try {
                    if (b.getBackground().equals(Color.RED)) {

                        binfo.setText(time());
                        b.setBackground(Color.GREEN);
                        dataout.writeUTF(b.getY()+" green" + " "+name);
                        dataout.flush();
                    }
                    else {
                        binfo.setText(time());
                        b.setBackground(Color.RED);
                        dataout.writeUTF(b.getY()+" red"+ " "+name);
                        dataout.flush();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };
        return actionListener;
    }

    public static void main(String[] args) throws IOException {
        WiWClient w = new WiWClient("КПП");


    }
}
