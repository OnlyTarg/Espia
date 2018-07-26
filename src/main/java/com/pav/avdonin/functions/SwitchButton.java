package com.pav.avdonin.functions;

import com.pav.avdonin.clients.Client;
import com.pav.avdonin.media.Music;
import com.pav.avdonin.server.ConnectionPoint;
import com.pav.avdonin.server.Server;
import com.pav.avdonin.sql.SQL;
import com.pav.avdonin.visual.FlashingLight;
import com.pav.avdonin.visual.Frames;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SwitchButton {
    AnotherFunctions functions = new AnotherFunctions();
    Music music = new Music();

    public void determineButton(String value, Socket socket, DataInputStream datain, DataOutputStream dataout, Frames mainframe, int ID) throws IOException {


        value = datain.readUTF();
        String[] values = value.split("_");


                /*Массив с значениями с входящего потока:
                values[0] - положение кнопки по вертекали
                values[1] - цвет (green;red)
                values[2] - имя (КПП-1; КПП-2(КТП))
                values[3] - время
                */
        switch (values[0]) {
            //case "value" - положение кнопок
            case "10*10":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[0], mainframe.timeButtons[0], mainframe.placeButtons[0]);
                break;
            case "10*70":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[1], mainframe.timeButtons[1], mainframe.placeButtons[1]);
                break;
            case "10*130":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[2], mainframe.timeButtons[2], mainframe.placeButtons[2]);
                break;
            case "10*190":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[3], mainframe.timeButtons[3], mainframe.placeButtons[3]);
                break;
            case "10*250":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[4], mainframe.timeButtons[4], mainframe.placeButtons[4]);
                break;
            case "10*310":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[5], mainframe.timeButtons[5], mainframe.placeButtons[5]);
                break;
            case "10*370":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[6], mainframe.timeButtons[6], mainframe.placeButtons[6]);
                break;
            case "10*430":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[7], mainframe.timeButtons[7], mainframe.placeButtons[7]);
                break;
            case "10*490":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[8], mainframe.timeButtons[8], mainframe.placeButtons[8]);
                break;
            case "10*550":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[9], mainframe.timeButtons[9], mainframe.placeButtons[9]);
                break;
            case "335*10":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[10], mainframe.timeButtons[10], mainframe.placeButtons[10]);
                break;
            case "335*70":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[11], mainframe.timeButtons[11], mainframe.placeButtons[11]);
                break;
            case "335*130":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[12], mainframe.timeButtons[12], mainframe.placeButtons[12]);
                break;
            case "335*190":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[13], mainframe.timeButtons[13], mainframe.placeButtons[13]);
                break;
            case "335*250":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[14], mainframe.timeButtons[14], mainframe.placeButtons[14]);
                break;
            case "335*310":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[15], mainframe.timeButtons[15], mainframe.placeButtons[15]);
                break;
            case "335*370":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[16], mainframe.timeButtons[16], mainframe.placeButtons[16]);
                break;
            case "335*430":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[17], mainframe.timeButtons[17], mainframe.placeButtons[17]);
                break;
            case "335*490":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[18], mainframe.timeButtons[18], mainframe.placeButtons[18]);
                break;
            case "335*550":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[19], mainframe.timeButtons[19], mainframe.placeButtons[19]);
                break;
            case "230*10":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[10], mainframe.timeButtons[10], mainframe.placeButtons[10]);
                break;
            case "230*70":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[11], mainframe.timeButtons[11], mainframe.placeButtons[11]);
                break;
            case "230*130":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[12], mainframe.timeButtons[12], mainframe.placeButtons[12]);
                break;
            case "230*190":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[13], mainframe.timeButtons[13], mainframe.placeButtons[13]);
                break;
            case "230*250":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[14], mainframe.timeButtons[14], mainframe.placeButtons[14]);
                break;
            case "230*310":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[15], mainframe.timeButtons[15], mainframe.placeButtons[15]);
                break;
            case "230*370":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[16], mainframe.timeButtons[16], mainframe.placeButtons[16]);
                break;
            case "230*430":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[17], mainframe.timeButtons[17], mainframe.placeButtons[17]);
                break;
            case "230*490":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[18], mainframe.timeButtons[18], mainframe.placeButtons[18]);
                break;
            case "230*550":
                switchButton(values[1], values[2], values[3],
                        mainframe.mainButtons[19], mainframe.timeButtons[19], mainframe.placeButtons[19]);
                break;


            case "candidate":
                try {
                    ID = Integer.parseInt(values[2]);
                    //String ip = socket.getRemoteSocketAddress().toString().substring(1,socket.getRemoteSocketAddress().toString().indexOf(":"));
                    //  sql.addEntering(dayOfWeek(),this.getName(),mapallowedClients.get(this.getName()), timeWithSeconds(),hash);
                } catch (Exception e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
                if (values.length == 3 && Server.mapallowedClients.containsKey(values[1])) {
                    dataout.writeUTF("isAllowed_" + "YES");
                    dataout.flush();
                } else {
                    dataout.writeUTF("isAllowed_" + "NO");
                    dataout.flush();
                }
                break;
            case "Connection test":
                //DO NOTHING;
                break;
            case "isAllowed":
                    if(values[1].equals("YES")){
                    Client.isAllowed=true;
                    mainframe.frame.setVisible(true);


                }else {
                    mainframe.frame.setVisible(false);
                    JOptionPane.showMessageDialog(null,"Вхід не дозволено. Зверніться до адміністратора.");
                    Client.isAllowed=false;
                    mainframe.dispose();
                    functions.close(dataout,datain,socket);
                }
                break;
            case "exiting":
                if (ID == 0) {
                    SQL.goodExitInformer(77777);
                } else {
                    SQL.goodExitInformer(ID);
                }
                break;
        }
    }
    private void switchButton(String color, String name, String when, JButton b, JButton binfo, JButton bwho) throws IOException {
        //метод решающий какую строку отправлять
        new FlashingLight(b).start();
        if (color.equals("green")) {
            if (b.getBackground().equals(Color.GREEN)) {
                //DONOTHING
            }
            if (b.getBackground().equals(Color.RED)) {
                b.setBackground(Color.GREEN);
                binfo.setText(functions.time());
                bwho.setText(name);
                music.soundZvonok();
            }

        }
        if (color.equals("red")) {
            if(b.getBackground().equals(Color.GREEN)){
                b.setBackground(Color.RED);
                binfo.setText(functions.time());
                bwho.setText(name);
                music.soundDoor();

            }
            if(b.getBackground().equals(Color.RED)){
                //DONOTHING
            }
        }
        //пересылаем полученные данные всем пользователям из списка лист

        for (int i = 0; i <Server.listOfClients.size() ; i++) {
            try{
                ConnectionPoint connectionPoint = (ConnectionPoint)Server.listOfClients.get(i);


                connectionPoint.dataout.writeUTF(b.getX() + "*" + b.getY() +"_"+color+"_"+name+"_"+when);
                connectionPoint.dataout.flush();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}