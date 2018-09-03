package com.pav.avdonin.dataexchange.statusofbuttons;

import com.google.gson.annotations.Expose;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class StatusOfButtons implements Serializable {
    @Expose
    public JButton[] mainButtons, timeButtons, placeButtons;
    @Expose
    public ArrayList<String> listOfPersons;

    public StatusOfButtons(JButton[] mainButtons, JButton[] timeButtons, JButton[] placeButtons, ArrayList<String> listOfPersons) {
        this.mainButtons = mainButtons;
        this.timeButtons = timeButtons;
        this.placeButtons = placeButtons;
        this.listOfPersons = listOfPersons;

    }

    public StatusOfButtons(int sizeOfButtonArrays) {
        mainButtons = new JButton[sizeOfButtonArrays];
        timeButtons = new JButton[sizeOfButtonArrays];
        placeButtons = new JButton[sizeOfButtonArrays];
        listOfPersons = new ArrayList<>();
    }


    public void writeStatusOFButtons() throws IOException {
        StatusOfButtons statusOfButtons = new StatusOfButtons(mainButtons, timeButtons, placeButtons, listOfPersons);
        FileOutputStream file = new FileOutputStream("status.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
        objectOutputStream.writeObject(statusOfButtons);
        objectOutputStream.flush();
        objectOutputStream.close();
        file.close();
    }


    public void readStatusOFButtons() throws IOException, ClassNotFoundException {
        try {

            FileInputStream file = new FileInputStream("status.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(file);

            StatusOfButtons statusOfButtons = (StatusOfButtons) objectInputStream.readObject();
            objectInputStream.close();
            file.close();
            for (int i = 0; i < mainButtons.length; i++) {
                mainButtons[i].setBackground(statusOfButtons.mainButtons[i].getBackground());
                timeButtons[i].setText(statusOfButtons.timeButtons[i].getText());
                placeButtons[i].setText(statusOfButtons.placeButtons[i].getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

