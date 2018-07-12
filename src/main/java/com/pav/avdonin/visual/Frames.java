package com.pav.avdonin.visual;
import com.pav.avdonin.functions.ActListeners;
import org.apache.commons.io.FileUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;

public  class  Frames extends JFrame {

    public static void main(String[] args) {
        new Frames("EspiaServer",false);
    }

    JFrame frame = new JFrame();
    public JButton[] mainButtons,timeButtons,placeButtons;
    ArrayList<String> listOfPersons;
    int countOfButtons;
    static public JLabel countClients;


    public Frames(String frameTitle, boolean infoSide) {
        listOfPersons = new ArrayList<>();
        readListofPersons();
        createWindow(frameTitle,infoSide);
        createJButtonsArrays(infoSide);

    }
    public Frames(String frameTitle, boolean infoSide, ArrayList<String> listOfPersons) {
        this.listOfPersons = listOfPersons;
        createWindow(frameTitle,infoSide);
        createJButtonsArrays(infoSide);

    }
    

    private void readListofPersons()  {
        try{

            InputStream in = getClass().getResourceAsStream("/list.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF8"));
            String s = "";
            while ((s = reader.readLine()) != null) {
                listOfPersons.add(s.replace("\uFEFF", ""));
            }
            in.close();
            countOfButtons=listOfPersons.size();

        }catch (Exception e){
            e.printStackTrace();
            /*StackTraceElement [] stack = e.getStackTrace();
            logger.log(Level.INFO,e.toString()+"\r\n"+stack[0]+"\r\n"+Thread.currentThread()+" disconnected \r\n");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Помилка під зчитування файлу з списком керівників");*/
        }

    }

    private void createWindow(String title,boolean infoSide) {
        int [] sizeofWindow = calculateSizeOfWindow(infoSide);
        frame = new JFrame();
        frame.setVisible(false);
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(sizeofWindow[0],sizeofWindow[1]); //340 / 560 server
        frame.setResizable(true);
        frame.setAlwaysOnTop(true);
        frame.setLayout(null);
    }
    private int[] calculateSizeOfWindow(boolean infosize) {

        int height = 80 + (listOfPersons.size() *60);
        int width;

        if (infosize) {
            width= 340;
        } else {
            width = 234;
        }

        int [] size = {width,height};
        if(listOfPersons.size()>10){
            size[0] = size[0]*2;
            size[1] = 680;
        }


        return  size;
    }
    private void compareAndAlign(JButton[] jButtonsArray, ArrayList<String> listOfPersons) {
        int different = jButtonsArray.length - listOfPersons.size();
        if(different!=0){
            if (different > 0) {
                for (int i = 0; i <different ; i++) {
                    listOfPersons.add("...");
                }
            }
            if (different <0){
                for (int i = different; i <0 ; i++) {
                    listOfPersons.remove(listOfPersons.size() + i);
                }
            }
        }
    }

    private void createJButtonsArrays(boolean infoSide) {
        mainButtons = new JButton[listOfPersons.size()];
        timeButtons = new JButton[listOfPersons.size()];
        placeButtons = new JButton[listOfPersons.size()];

        compareAndAlign(mainButtons,listOfPersons);
        fillingButtonsProperties(infoSide);
        fillingJLabelProperties();

        frame.repaint();
        frame.setVisible(true);



    }
    private void fillingButtonsProperties(boolean infoSide) {
        Rectangle mainButtonBounds = new Rectangle(10, 10, 200, 50);
        Rectangle timeButtonBounds = new Rectangle(212, 10, 120, 25);
        Rectangle placeButtonBounds = new Rectangle(212, 35, 120, 25);

        Font fontMain = new Font("Times new Roman",Font.BOLD,20);
        Font fontTimePlace = new Font("Times new Roman",Font.BOLD,14);


        for (int i = 0; i <mainButtons.length ; i++) {
            mainButtons[i] = new JButton(listOfPersons.get(i));
            mainButtons[i].setFont(fontMain);
            mainButtons[i].setBounds(mainButtonBounds);
            mainButtonBounds.y = mainButtonBounds.y+60;
            mainButtons[i].setBackground(Color.RED);
            frame.add(mainButtons[i]);

            System.out.println(mainButtons[i].getX()+" "+mainButtons[i].getY());
            if(i==9&&infoSide){
                mainButtonBounds.setBounds(335, 10, 200, 50);
            }
            if(i==9&&!infoSide){
                mainButtonBounds.setBounds(230, 10, 200, 50);
            }
        }

        if(infoSide) {
            for (int i = 0; i < timeButtons.length; i++) {
                timeButtons[i] = new JButton("....");
                timeButtons[i].setFont(fontTimePlace);
                timeButtons[i].setBounds(timeButtonBounds);
                timeButtonBounds.y = timeButtonBounds.y + 60;
                timeButtons[i].setBackground(Color.YELLOW);
                frame.add(timeButtons[i]);
                if(i==9&&infoSide==true){
                    timeButtonBounds.setBounds(538, 10, 120, 25);
                }

            }
            for (int i = 0; i < placeButtons.length; i++) {
                placeButtons[i] = new JButton("....");
                placeButtons[i].setFont(fontTimePlace);
                placeButtons[i].setBounds(placeButtonBounds);
                placeButtonBounds.y = placeButtonBounds.y + 60;
                placeButtons[i].setBackground(Color.YELLOW);
                frame.add(placeButtons[i]);
                if(i==9&&infoSide==true){
                    placeButtonBounds.setBounds(538, 35, 120, 25);
                }
            }
            addOfflineActionListeners();
        }
    }

    private void addOfflineActionListeners() {
        for (int i = 0; i <mainButtons.length ; i++) {
            mainButtons[i].addActionListener(new ActListeners().OfflineListener(mainButtons[i],timeButtons[i],placeButtons[i]));
        }
    }
    private void fillingJLabelProperties() {
        Rectangle countClientsLabel;
        Font fontJLabel = new Font("Times new Roman",Font.BOLD,15);
        if(listOfPersons.size()<10){
            countClientsLabel = new Rectangle(20,6+(60*listOfPersons.size()),200,30);
        }else {
            countClientsLabel = new Rectangle(20,6+(600),200,30);
        }
        countClients = new JLabel("Кількість клієнтів - 0");
        countClients.setFont(fontJLabel);
        countClients.setBounds(countClientsLabel);
        frame.add(countClients);
    }












          /*  frame.addWindowListener(new WindowAdapter() {
            @Override

            public void windowClosing(WindowEvent e) {
                try {
                    //SQL.goodExitInformer(hash);
                    for (int i = 0; i <listOfClients.size() ; i++) {
                        FileUtils.writeStringToFile(client,"","UTF8");
                        System.out.println(listOfClients.get(i).hashCode());
                        ConnectionPoint con = (ConnectionPoint)listOfClients.get(i);
                        sql.exitFromSession(con.getName(), timeWithSeconds(),con.hash,"server stopted");


                    }
                    frame.dispose();
                }catch(Exception e2){
                    e2.printStackTrace();
                    frame.dispose();
                }
            }
        });*/

}
