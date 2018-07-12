package com.pav.avdonin.functions;

import com.pav.avdonin.functions.StatusButtons;
import com.pav.avdonin.media.Music;
import com.pav.avdonin.server.ConnectionPoint;
import com.pav.avdonin.server.Server;
import com.pav.avdonin.visual.FlashingLight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

public class ActListeners {
    Music music = new Music();



/*    private ActionListener OnlineListener (JButton b, JButton binfo, JButton bwho) {

*//*Создаю слушатель для кнопок
        При нажатии на кнопку, она изменяет цвет на противоположный,
        в зависимости от того какой цвет был установлен до нажатия.
        После этого в OutputStream передается строка в виде "120 green"
        где первое значение - "положение кнопки по вертикали", а втрое - "цвет"*//*


        ActionListener actionListener = new ActionListener() {

            @Override
            public  void actionPerformed(ActionEvent e) {
                music.soundClick();
                choiceWhoAndWhen(binfo,bwho);
                Color buttoncolor = b.getBackground();
                new FlashingLight(b).start();
                try {
                    if (buttoncolor.equals(Color.RED)) {
                        b.setBackground(Color.GREEN);
                        music.soundZvonok();

                        for (int i = 0; i <listOfClients.size() ; i++) {
                            Server.ConnectionPoint con = (Server.ConnectionPoint)listOfClients.get(i);
                            con.dataout.writeUTF(b.getY() + "_green" + "_" + bwho.getText() + "_" + binfo.getText());
                            con.dataout.flush();
                            //JOptionPane.showMessageDialog(null,"Данные отправлены клиенту");
                        }


                    } else {
                        b.setBackground(Color.RED);
                        music.soundDoor();
                        //binfo.setText(time());
                        for (int i = 0; i <listOfClients.size() ; i++) {
                            ConnectionPoint con = (ConnectionPoint)listOfClients.get(i);
                            con.dataout.writeUTF(b.getY() + "_red" + "_" + bwho.getText() + "_" + binfo.getText());
                            con.dataout.flush();
                            //JOptionPane.showMessageDialog(null,"Данные отправлены клиенту");
                        }
                        writeStatusOFButtons();


                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    StackTraceElement [] stack = ex.getStackTrace();
                    logger.log(Level.INFO,e.toString()+"\r\n"+stack[0]+"\r\n"+Thread.currentThread()+" disconnected \r\n");
ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Помилка в передачі даних.(клас OnlineListener)");


                }


            }
        };
        return actionListener;
    }*/
    public ActionListener OfflineListener (JButton b, JButton binfo, JButton bwho) {
/*Создаю слушатель для кнопок
        При нажатии на кнопку, она изменяет цвет на противоположный,
        в зависимости от того какой цвет был установлен до нажатия.
        Работает только в офлайне*/



        ActionListener actionListener = null;
        try {
            actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    music.soundClick();
                    Color buttoncolor = b.getBackground();

                    choiceWhoAndWhen(binfo,bwho);


                    try {
                        //clipClick.start();
                        if (buttoncolor.equals(Color.RED)) {
                            b.setBackground(Color.GREEN);
                            music.soundZvonok();




                        } else {
                            b.setBackground(Color.RED);
                            music.soundDoor();



                        }
                        new FlashingLight(b).start();
                        Server.statusButtons.writeStatusOFButtons();

                    }catch (Exception ex){
                        ex.printStackTrace();

                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement [] stack = e.getStackTrace();

        }
        return actionListener;
    }
    public  void choiceWhoAndWhen(JButton binfo,JButton bwho){
        Server.mainframes.setAlwaysOnTop(false);
        Object when = JOptionPane.showInputDialog(null,
                "", "Введіть дату",
                JOptionPane.INFORMATION_MESSAGE, null,null,time());
        System.out.println(when);
        binfo.setText(when.toString());
        Object[] possibleValues = { "КПП-1", "КПП-2(КТП)" };
        Object who = JOptionPane.showInputDialog(null,
                "", "Виберіть правильне",
                JOptionPane.INFORMATION_MESSAGE, null,
                possibleValues, possibleValues[0]);

        bwho.setText(who.toString());
        Server.mainframes.setAlwaysOnTop(true);
    }
   /* private void writeStatusOFButtons() throws IOException {
        Server.StatusButtons statusButtons = new Server.StatusButtons(b1.getBackground(),b2.getBackground(),b3.getBackground(),
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
    }*/
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
}
