/**
 * Created by CleBo on 07.12.2017.
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by CleBo on 23.11.2017.
 */
public class JustLook extends JFrame{
    JFrame frame;
    InetAddress ipAddress;
    Socket socket;
    DataInputStream datain;
    DataOutputStream dataout;
    JButton b1,b2,b3,b4,b5,b6,b7;
    JLabel connectionStatus;

    public JustLook() throws IOException {
        window();
        buttons();
        createClient();
        readData();
        close();
    }

    private class StatusButtons {
        Color b1,b2,b3,b4,b5,b6,b7;

        public StatusButtons(JButton b1, JButton b2, JButton b3, JButton b4, JButton b5, JButton b6, JButton b7) {
            this.b1 = b1.getBackground();
            this.b2 = b2.getBackground();
            this.b3 = b3.getBackground();
            this.b4 = b4.getBackground();
            this.b5 = b5.getBackground();
            this.b6 = b6.getBackground();
            this.b7 = b7.getBackground();
        }
    }

    public void window() {
        //Создаю основное окно
        frame = new JFrame();
        frame.setTitle("WiWClient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(227,500);
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
        connectionStatus = new JLabel("Отключено");


        Font font = new Font("Times new Roman",Font.BOLD,20);
        frame.setLayout(null);

        b1.setFont(font);
        b2.setFont(font);
        b3.setFont(font);
        b4.setFont(font);
        b5.setFont(font);
        b6.setFont(font);
        b7.setFont(font);
        connectionStatus.setFont(font);


        b1.setBounds(10, 10, 200, 50);
        b2.setBounds(10, 70, 200, 50);
        b3.setBounds(10, 130, 200, 50);
        b4.setBounds(10, 190, 200, 50);
        b5.setBounds(10, 250, 200, 50);
        b6.setBounds(10, 310, 200, 50);
        b7.setBounds(10, 370, 200, 50);
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


       /* b1.addActionListener(OnlineListener(b1));
        b2.addActionListener(OnlineListener(b2));
        b3.addActionListener(OnlineListener(b3));
        b4.addActionListener(OnlineListener(b4));
        b5.addActionListener(OnlineListener(b5));
        b6.addActionListener(OnlineListener(b6));
        b7.addActionListener(OnlineListener(b7));*/

        frame.add(b1);
        frame.add(b2);
        frame.add(b3);
        frame.add(b4);
        frame.add(b5);
        frame.add(b6);
        frame.add(b7);
        frame.add(connectionStatus);
    }
    private void createClient() {
        //Создаю клиент
        try {
            int serverPort = 6666;
            String address = "localhost";//10.244.1.121    localhost
            connectionStatus.setText("Соединение......");
            ipAddress = InetAddress.getByName(address);

            socket = new Socket(ipAddress, serverPort);
            datain = new DataInputStream(socket.getInputStream());
            dataout = new DataOutputStream(socket.getOutputStream());




            Thread.currentThread().sleep(2000);
            connectionStatus.setForeground(Color.GREEN);
            connectionStatus.setBounds(50,430,200,30);
            connectionStatus.setText("Подключено");
            //Прием состояния кнопок (цвет кнопок) сервера на момент подключения


            /*Gson gson = new Gson();
            String msg = datain.readUTF();
            StatusButtons statusButtons = gson.fromJson(msg,StatusButtons.class);*/

            String msg = datain.readUTF();
            String[] mas = msg.split(" ");
            Color[]colormas= new Color[7];
            for (int i = 0; i <colormas.length; i++) {
                if(mas[i].equals("0")){
                    colormas[i]=Color.GREEN;
                }else {
                    colormas[i]=Color.RED;
                }


            }

            b1.setBackground(colormas[0]);
            b2.setBackground(colormas[1]);
            b3.setBackground(colormas[2]);
            b4.setBackground(colormas[3]);
            b5.setBackground(colormas[4]);
            b6.setBackground(colormas[5]);
            b7.setBackground(colormas[6]);

        } catch (Exception e) {
            connectionStatus.setForeground(Color.RED);
            connectionStatus.setBounds(65,430,200,30);
            connectionStatus.setText("Ошибка1");
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
                    switchchoice(temp[1],b1);
                    break;
                case "70":
                    switchchoice(temp[1],b2);
                    break;
                case "130":
                    switchchoice(temp[1],b3);
                    break;
                case "190":
                    switchchoice(temp[1],b4);
                    break;
                case "250":
                    switchchoice(temp[1],b5);
                    break;
                case "310":
                    switchchoice(temp[1],b6);
                    break;
                case "370":
                    switchchoice(temp[1],b7);
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
    public void switchchoice (String color,JButton b){
        if (color.equals("green")) {
            if (b.getBackground().equals(Color.GREEN)) {
            }
            if (b.getBackground().equals(Color.RED)) {
                //DONOTHING
                b.setBackground(Color.GREEN);
            }
        }
        if (color.equals("red")) {
            if(b.getBackground().equals(Color.GREEN)){
                b.setBackground(Color.RED);
            }
            if(b.getBackground().equals(Color.RED)){
                //DONOTHING
            }
        }
    }

    public ActionListener OnlineListener (JButton b) {
        //Создаю слушатель, такой же как на сервере.
        //Изменяет состояние кнопок и передает инфо на сервер
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (b.getBackground().equals(Color.RED)) {
                        b.setBackground(Color.GREEN);
                        String g = new String("green");
                        dataout.writeUTF(b.getY()+" "+g);
                        dataout.flush();
                    }
                    else {
                        b.setBackground(Color.RED);
                        dataout.writeUTF(b.getY()+" red");
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
        JustLook s = new JustLook();
    }
}
