package com.pav.avdonin.dataexchange;

import com.pav.avdonin.util.Names;
import com.pav.avdonin.clients.Client;
import com.pav.avdonin.media.Music;
import com.pav.avdonin.server.ConnectionPoint;
import com.pav.avdonin.server.Server;
import com.pav.avdonin.util.CommonFunctions;
import com.pav.avdonin.visual.FlashingLight;
import com.pav.avdonin.visual.Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListeners {
    String name;
    private final String ESPIA_SERVER_NAME = Names.ESPIA_SERVER.getValue();
    private final String KPP_NAME = Names.KPP.getValue();
    private final String KTP_NAME = Names.KTP.getValue();
    Music music = new Music();


    public ActionListener OnlineListenerForServer(Frames frames, String appName, JButton b, JButton time, JButton place) {
        this.name = appName;

        ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                music.soundClick();
                System.out.println(name);
                if (name.equals(ESPIA_SERVER_NAME)) {
                    choiceWhoAndWhen(time, place);
                }
                Color buttoncolor = b.getBackground();
                new FlashingLight(b).start();
                try {
                    if (buttoncolor.equals(Color.RED)) {
                        if (!name.equals(ESPIA_SERVER_NAME)) {
                            time.setText(CommonFunctions.getCurrentTime());
                            place.setText(name);
                        }
                        b.setBackground(Color.GREEN);
                        music.soundRing();

                        if (name.equals(ESPIA_SERVER_NAME)) {
                            for (int i = 0; i < Server.listOfClients.size(); i++) {
                                ConnectionPoint connectionPoint = (ConnectionPoint) Server.listOfClients.get(i);
                                connectionPoint.dataout.writeUTF(frames.listOfPersons.indexOf(b.getText()) + "_green" + "_" + place.getText() + "_" + time.getText());
                                connectionPoint.dataout.flush();
                            }
                        } else {
                            Client.dataout.writeUTF(frames.listOfPersons.indexOf(b.getText()) + "_green" + "_" + place.getText() + "_" + time.getText());
                            Client.dataout.flush();
                        }

                    } else {
                        b.setBackground(Color.RED);
                        if (!name.equals(ESPIA_SERVER_NAME)) {
                            time.setText(CommonFunctions.getCurrentTime());
                            place.setText(name);
                        }
                        music.soundDoor();
                        if (name.equals(ESPIA_SERVER_NAME)) {
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

                        if (name.equals(ESPIA_SERVER_NAME)) {
                            Server.statusOfButtons.writeStatusOFButtons();
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Помилка в передачі даних.(клас OnlineListener)");
                }
            }
        };
        return actionListener;
    }

    public ActionListener OfflineListener(JButton b, JButton time, JButton place, String name) {
        ActionListener actionListener = null;
        try {
            actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    music.soundClick();
                    Color buttoncolor = b.getBackground();
                    System.out.println(name);
                    if (name.equals(ESPIA_SERVER_NAME)) {
                        choiceWhoAndWhen(time, place);
                    }

                    try {
                        if (buttoncolor.equals(Color.RED)) {
                            b.setBackground(Color.GREEN);
                            music.soundRing();

                        } else {
                            b.setBackground(Color.RED);
                            music.soundDoor();
                        }
                        new FlashingLight(b).start();
                        Server.statusOfButtons.writeStatusOFButtons();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement[] stack = e.getStackTrace();
        }
        return actionListener;
    }

    public void choiceWhoAndWhen(JButton binfo, JButton bwho) {
        Server.mainframes.setAlwaysOnTop(false);
        Object when = JOptionPane.showInputDialog(null,
                "", "Введіть дату",
                JOptionPane.INFORMATION_MESSAGE, null, null, CommonFunctions.getCurrentTime());
        binfo.setText(when.toString());
        Object[] possibleValues = {KPP_NAME, KTP_NAME};
        Object who = JOptionPane.showInputDialog(null,
                "", "Виберіть правильне",
                JOptionPane.INFORMATION_MESSAGE, null,
                possibleValues, possibleValues[0]);
        bwho.setText(who.toString());
        Server.mainframes.setAlwaysOnTop(true);
    }
}
