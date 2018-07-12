package com.pav.avdonin.functions;

import javax.swing.*;
import java.io.*;

public class StatusButtons implements Serializable {
    // private static final long serialVersionUID = 1L;
    //Класс фиксирующий состояние (цвет) кнопок. Отправляется в виде JSON на клиенты при подключении

    public JButton[] mainButtons,timeButtons,placeButtons;

    public StatusButtons(JButton [] mainButtons,JButton [] timeButtons,JButton [] placeButtons) {
        this.mainButtons = mainButtons;
        this.timeButtons = timeButtons;
        this.placeButtons = placeButtons;

    }


    public void writeStatusOFButtons() throws IOException {
        StatusButtons statusButtons = new StatusButtons(mainButtons,timeButtons,placeButtons);
        FileOutputStream file = new FileOutputStream("status.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
        objectOutputStream.writeObject(statusButtons);
        objectOutputStream.flush();
        objectOutputStream.close();
        file.close();
    }
    public void readStatusOFButtons() throws IOException, ClassNotFoundException {
        try{

            FileInputStream file = new FileInputStream("status.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(file);

            StatusButtons statusButtons = (StatusButtons) objectInputStream.readObject();
            objectInputStream.close();
            file.close();
            for (int i = 0; i < mainButtons.length; i++) {
                mainButtons[i].setBackground(statusButtons.mainButtons[i].getBackground());
                timeButtons[i].setText(statusButtons.timeButtons[i].getText());
                placeButtons[i].setText(statusButtons.placeButtons[i].getText());
            }
        }catch (Exception e){
            e.printStackTrace();
            //StackTraceElement [] stack = e.getStackTrace();
            //logger.log(Level.INFO,e.toString()+"\r\n"+stack[0]+"\r\n"+Thread.currentThread()+" disconnected \r\n");
            //JOptionPane.showMessageDialog(null,"Помилка при зчитуванні попередніх змін.");

        }

    }

}

