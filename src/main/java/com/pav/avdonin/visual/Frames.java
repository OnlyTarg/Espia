package com.pav.avdonin.visual;
import com.pav.avdonin.functions.ActListeners;
import com.pav.avdonin.functions.StatusButtons;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public  class  Frames extends JFrame {



    public JFrame frame;
    public static Point boundsPoint = new Point();
    public String name = "";
    public JButton[] mainButtons,timeButtons,placeButtons;
    public ArrayList<String> listOfPersons;
    int countOfButtons;
    static public JLabel jLabel;


    public Frames() {
        frame = new JFrame();
        listOfPersons = new ArrayList<>();
        //readListofPersons();
        /*createWindow(frameTitle,infoSide);
        createJButtonsArrays(infoSide);*/

    }
    public Frames(ArrayList<String> listOfPersons) {
        frame = new JFrame();
        this.listOfPersons = listOfPersons;
        /*createWindow(frameTitle,infoSide);
        createJButtonsArrays(infoSide);*/

    }
    

    public void readListofPersons()  {
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


    public void createWindow(String title,boolean infoSide) {
        int [] sizeofWindow = calculateSizeOfWindow(infoSide);
        //frame = new JFrame();
        frame.setVisible(false);
        frame.setLocation(boundsPoint);
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(sizeofWindow[0],sizeofWindow[1]); //340 / 560 server
        frame.setResizable(false);
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

    public void createJButtonsArraysForServer(boolean infoSide, ArrayList<String> listOfPersons) {
        mainButtons = new JButton[listOfPersons.size()];
        timeButtons = new JButton[listOfPersons.size()];
        placeButtons = new JButton[listOfPersons.size()];

        compareAndAlign(mainButtons,listOfPersons);

       if (frame.getTitle().equals("КПП-2(КТП)")||
               frame.getTitle().equals("КПП1")||
               frame.getTitle().equals("EspiaJL")){
           fillingButtonsProperties(infoSide,name);
           System.out.println("CHECKING = "+ frame.getTitle()=="Client");
           fillingJLabelConnectionStatus();
       }
        else {
           fillingButtonsProperties(infoSide,name);
           fillingJLabelCountClients();
       }


        frame.repaint();
        frame.setVisible(true);



    }
    public void createJButtonsArraysForClients(boolean infoSide, ArrayList<String> listOfPersons, StatusButtons statusButtons) {
        mainButtons = statusButtons.mainButtons;
        timeButtons = statusButtons.timeButtons;
        placeButtons = statusButtons.placeButtons;
        System.out.println("Point2");

        compareAndAlign(mainButtons,listOfPersons);
        System.out.println(frame.getTitle());
        if (frame.getTitle().equals("КПП-2(КТП)")){
            fillingButtonsProperties(infoSide,"Client");
            System.out.println("CHECKING = "+ frame.getTitle().equals("Client"));

        }

        //fillingJLabelCountClients();

        frame.repaint();
        //frame.setVisible(true);



    }



    private void fillingButtonsProperties(boolean infoSide,String name) {
        System.out.println("Part 3 "+name);
        Rectangle mainButtonBounds = new Rectangle(10, 10, 200, 50);
        Rectangle timeButtonBounds = new Rectangle(212, 10, 120, 25);
        Rectangle placeButtonBounds = new Rectangle(212, 35, 120, 25);

        Font fontMain = new Font("Times new Roman",Font.BOLD,20);
        Font fontTimePlace = new Font("Times new Roman",Font.BOLD,14);


        for (int i = 0; i <mainButtons.length ; i++) {
            if(name.equals("EspiaServer")){
                mainButtons[i] = new JButton(listOfPersons.get(i));
                mainButtons[i].setBackground(Color.RED);
            }
            mainButtons[i].setFont(fontMain);
            mainButtons[i].setBounds(mainButtonBounds);
            mainButtonBounds.y = mainButtonBounds.y+60;

            frame.add(mainButtons[i]);
            if(i==9&&infoSide){
                mainButtonBounds.setBounds(335, 10, 200, 50);
            }
            if(i==9&&!infoSide){
                mainButtonBounds.setBounds(230, 10, 200, 50);
            }
        }

        if(infoSide) {
            for (int i = 0; i < timeButtons.length; i++) {
                if(name.equals("EspiaServer")){
                    timeButtons[i] = new JButton("....");
                }

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
                if(name.equals("EspiaServer")){
                    placeButtons[i] = new JButton("....");
                }
                placeButtons[i].setFont(fontTimePlace);
                placeButtons[i].setBounds(placeButtonBounds);
                placeButtonBounds.y = placeButtonBounds.y + 60;
                placeButtons[i].setBackground(Color.YELLOW);
                frame.add(placeButtons[i]);
                if(i==9&&infoSide==true){
                    placeButtonBounds.setBounds(538, 35, 120, 25);
                }
            }
            if (name.equals("EspiaServer")) {
                addOfflineActionListeners();
            }

        }
    }








    private void addOfflineActionListeners() {
        for (int i = 0; i <mainButtons.length ; i++) {
            mainButtons[i].addActionListener(new ActListeners().OfflineListener(mainButtons[i],timeButtons[i],placeButtons[i]));
        }
    }
    private void fillingJLabelCountClients() {
        Rectangle countClientsLabel;
        Font fontJLabel = new Font("Times new Roman",Font.BOLD,15);
        if(listOfPersons.size()<10){
            countClientsLabel = new Rectangle(20,6+(60*listOfPersons.size()),200,30);
        }else {
            countClientsLabel = new Rectangle(20,6+(600),200,30);
        }
        jLabel = new JLabel("Кількість клієнтів - 0");
        jLabel.setFont(fontJLabel);
        jLabel.setBounds(countClientsLabel);
        frame.add(jLabel);
    }

    private void fillingJLabelConnectionStatus() {
        Rectangle countClientsLabel;
        Font fontJLabel = new Font("Times new Roman",Font.BOLD,15);
        if(listOfPersons.size()<10){
            countClientsLabel = new Rectangle(20,6+(60*listOfPersons.size()),200,30);
        }else {
            countClientsLabel = new Rectangle(20,6+(600),200,30);
        }
        jLabel = new JLabel("З'єднання...");
        jLabel.setFont(fontJLabel);
        jLabel.setBounds(countClientsLabel);
        frame.add(jLabel);
    }

    public void tryToConnect() throws InterruptedException {
        JFrame tempFrame = new JFrame();
        tempFrame.setLocationRelativeTo(null);
        tempFrame.setResizable(false);
        tempFrame.setLayout(null);
        tempFrame.setTitle("ESPIA");
        JLabel label = new JLabel();
        label.setFont(new Font("Times new Roman",Font.BOLD,25));
        label.setForeground(Color.RED);
        label.setBounds(80,20,200,20);
        tempFrame.setSize(350,100);
        tempFrame.setVisible(true);
        tempFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        tempFrame.add(label);
        for (int i = 30; i >= 0; i--) {
            label.setText("З'єднання "+i);
            label.setForeground(Color.RED);
            Thread.currentThread().sleep(1000);
        }
        tempFrame.dispose();
    }

}
