package com.pav.avdonin.functions;

import com.google.gson.annotations.Expose;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class StatusButtons implements Serializable {
    // private static final long serialVersionUID = 1L;
    //Класс фиксирующий состояние (цвет) кнопок. Отправляется в виде JSON на клиенты при подключении
    @Expose
    public JButton[] mainButtons, timeButtons, placeButtons;
    @Expose
    public ArrayList<String> listOfPersons;

    public StatusButtons(JButton[] mainButtons, JButton[] timeButtons, JButton[] placeButtons, ArrayList<String> listOfPersons) {
        this.mainButtons = mainButtons;
        this.timeButtons = timeButtons;
        this.placeButtons = placeButtons;
        this.listOfPersons = listOfPersons;

    }

    public StatusButtons(int sizeOfButtonArrays) {
         mainButtons = new JButton[sizeOfButtonArrays];
         timeButtons = new JButton[sizeOfButtonArrays];
         placeButtons = new JButton[sizeOfButtonArrays];
        listOfPersons = new ArrayList<>();
    }


    public  void writeStatusOFButtons() throws IOException {
        StatusButtons statusButtons = new StatusButtons(mainButtons, timeButtons, placeButtons,listOfPersons);
        FileOutputStream file = new FileOutputStream("status.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
        objectOutputStream.writeObject(statusButtons);
        objectOutputStream.flush();
        objectOutputStream.close();
        file.close();
    }


    public void readStatusOFButtons() throws IOException, ClassNotFoundException {
        try {

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
        } catch (Exception e) {
            e.printStackTrace();
            }

    }

}

