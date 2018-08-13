package com.pav.avdonin.functions;

import com.pav.avdonin.clients.Client;
import com.pav.avdonin.functions.StatusButtons;
import com.pav.avdonin.media.Music;
import com.pav.avdonin.server.ConnectionPoint;
import com.pav.avdonin.server.Server;
import com.pav.avdonin.visual.FlashingLight;
import com.pav.avdonin.visual.Frames;

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
    String name;
    Music music = new Music();



    public ActionListener OnlineListenerForServer(Frames frames, String name,JButton b, JButton time, JButton place) {
        this.name = name;

        ActionListener actionListener = new ActionListener() {

            @Override
            public  void actionPerformed(ActionEvent e) {
                music.soundClick();
                if(name.equals("EspiaServer")){choiceWhoAndWhen(time,place);}
                Color buttoncolor = b.getBackground();
                new FlashingLight(b).start();
                try {
                    if (buttoncolor.equals(Color.RED)) {
                        if(!name.equals("EspiaServer")){
                            time.setText(time());
                            place.setText(name);
                        }
                        b.setBackground(Color.GREEN);
                        music.soundZvonok();

                        if(name.equals("EspiaServer")){
                            for (int i = 0; i <Server.listOfClients.size() ; i++) {
                                ConnectionPoint connectionPoint = (ConnectionPoint)Server.listOfClients.get(i);
                                connectionPoint.dataout.writeUTF( frames.listOfPersons.indexOf(b.getText())+ "_green" + "_" + place.getText() + "_" + time.getText());
                                System.out.println(frames.listOfPersons.indexOf(b.getText())+ "_green" + "_" + place.getText() + "_" + time.getText());
                                connectionPoint.dataout.flush();
                            }
                        }
                        else {
                            Client.dataout.writeUTF(frames.listOfPersons.indexOf(b.getText()) + "_green" + "_" + place.getText() + "_" + time.getText());
                            Client.dataout.flush();
                        }



                    } else {
                        b.setBackground(Color.RED);
                        if(!name.equals("EspiaServer")){
                            time.setText(time());
                            place.setText(name);
                        }
                        music.soundDoor();
                        if(name.equals("EspiaServer")) {
                            for (int i = 0; i < Server.listOfClients.size(); i++) {
                                ConnectionPoint connectionPoint = (ConnectionPoint) Server.listOfClients.get(i);
                                connectionPoint.dataout.writeUTF(frames.listOfPersons.indexOf(b.getText()) + "_red" + "_" + place.getText() + "_" + time.getText());
                                connectionPoint.dataout.flush();
                                //JOptionPane.showMessageDialog(null,"Данные отправлены клиенту");
                            }
                        } else {
                            Client.dataout.writeUTF(frames.listOfPersons.indexOf(b.getText()) + "_red" + "_" + place.getText() + "_" + time.getText());
                            Client.dataout.flush();
                        }

                       if(name.equals("EspiaServer")) {Server.statusButtons.writeStatusOFButtons();}


                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Помилка в передачі даних.(клас OnlineListener)");
                }
            }
        };
        return actionListener;
    }

    public ActionListener OfflineListener (JButton b, JButton time, JButton place) {
        ActionListener actionListener = null;
        try {
            actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    music.soundClick();
                    Color buttoncolor = b.getBackground();

                    choiceWhoAndWhen(time,place);


                    try {
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
        binfo.setText(when.toString());
        Object[] possibleValues = { "КПП-1", "КПП-2(КТП)" };
        Object who = JOptionPane.showInputDialog(null,
                "", "Виберіть правильне",
                JOptionPane.INFORMATION_MESSAGE, null,
                possibleValues, possibleValues[0]);

        bwho.setText(who.toString());
        Server.mainframes.setAlwaysOnTop(true);
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
}
