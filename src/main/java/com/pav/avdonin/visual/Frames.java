package com.pav.avdonin.visual;

import com.pav.avdonin.util.Names;
import com.pav.avdonin.clients.Client;
import com.pav.avdonin.dataExchangeFunctions.ActionListeners;
import com.pav.avdonin.util.CommonFunctions;
import com.pav.avdonin.dataExchangeFunctions.statusOfButtons.StatusOfButtons;
import com.pav.avdonin.server.ConnectionPoint;
import com.pav.avdonin.server.Server;
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
import java.util.Properties;

public class Frames extends JFrame {

    public final String SERVER_NAME = Names.EspiaServer.toString();
    public final String KPP_NAME = Names.КПП1.toString();
    public final String KTP_NAME = Names.КТП.toString();
    public final String ESPIA_JL_NAME = Names.EspiaJL.toString();


    public final int STANDART_HEIGHT = 1080;
    public final int STANDART_WIDTH = 1920;
    public final int STANDART_HEIGHT_DISTANCE_BETWEEN_BUTTONS = 60;
    public final int STANDART_WIDTH_DISTANCE_BETWEEN_BUTTONS = 330;


    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Properties properties = new Properties();
    public JFrame frame;
    public static Point boundsPoint = new Point();
    public String name = "";
    public JButton[] mainButtons, timeButtons, placeButtons;
    public ArrayList<String> listOfPersons;
    int countOfButtons;
    static public JLabel jLabel;
    static public JLabel connectionStatus;
    double scope = 1.0;





    public Frames() {
        try {
            properties.load(getClass().getResourceAsStream("/settings.properties"));
            scope = Double.valueOf(properties.getProperty("scope"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Помилка під час завантаження налаштувань. Повідомте про цю помилку адміністратора. (0685399549)");
        }

        frame = new JFrame();
        listOfPersons = new ArrayList<>();

    }

    public Frames(ArrayList<String> listOfPersons) {
        frame = new JFrame();
        this.listOfPersons = listOfPersons;


    }


    public void readListofPersons() {
        try {

            InputStream in = getClass().getResourceAsStream("/list.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF8"));
            String s = "";
            while ((s = reader.readLine()) != null) {
                listOfPersons.add(s.replace("\uFEFF", ""));
            }
            in.close();
            countOfButtons = listOfPersons.size();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void createWindow(String title, boolean infoSide) {
        int[] sizeofWindow = calculateSizeOfWindow(infoSide);
        frame.setVisible(false);
        if (title.equals(SERVER_NAME)) {
            frame.addWindowListener(new WindowAdapter() {
                @Override

                public void windowClosing(WindowEvent e) {
                    try {
                        for (int i = 0; i < Server.listOfClients.size(); i++) {
                            ConnectionPoint cp = (ConnectionPoint) Server.listOfClients.get(i);
                            Server.clientsConnectionHistory.exitFromSession(cp.getName(), CommonFunctions.getCurrentTimeWithSeconds(), cp.ID, "server stopped");
                            FileUtils.writeStringToFile(Server.client, "", "UTF8");
                        }
                        frame.dispose();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        frame.dispose();
                    }
                }
            });
        } else {
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        //com.pav.avdonin.clientsConnectionHistory.ClientsConnectionHistory.goodExitInformer(hash);
                        Client.dataout.writeUTF("exiting");
                        Client.dataout.flush();
                        frame.dispose();

                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, "Помилка під час відправлення інформації про вихід до БД");
                        frame.dispose();
                    } catch (Exception e2) {
                        frame.dispose();
                    }
                }
            });
        }
        frame.setLocation(boundsPoint);
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(sizeofWindow[0], sizeofWindow[1]); //340 / 560 server
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setLayout(null);


    }

    private int[] calculateSizeOfWindow(boolean infosize) {
        int additional = (int) (Math.round((double) 80 / STANDART_HEIGHT * screenSize.height * scope));
        int buttonSize = (int) (Math.round((double) 60 / STANDART_HEIGHT * screenSize.height * scope));
        int height = additional + (listOfPersons.size() * buttonSize);
        int width;

        if (infosize) {
            width = (int) (Math.round((double) 350 / STANDART_WIDTH * screenSize.width * scope));
            System.out.println("Ширина окна " + width);
        } else {
            width = (int) (Math.round((double) 260 / STANDART_WIDTH * screenSize.width * scope));
        }

        int[] size = {width, height};
        if (listOfPersons.size() > 10) {
            size[0] = size[0] * 2;
            size[1] = additional + 10 * buttonSize;
        }


        return size;
    }

    private void compareAndAlign(JButton[] jButtonsArray, ArrayList<String> listOfPersons) {
        int different = jButtonsArray.length - listOfPersons.size();
        if (different != 0) {
            if (different > 0) {
                for (int i = 0; i < different; i++) {
                    listOfPersons.add("...");
                }
            }
            if (different < 0) {
                for (int i = different; i < 0; i++) {
                    listOfPersons.remove(listOfPersons.size() + i);
                }
            }
        }
    }

    public void createJButtonsArraysForServer(boolean infoSide, ArrayList<String> listOfPersons) {
        mainButtons = new JButton[listOfPersons.size()];
        timeButtons = new JButton[listOfPersons.size()];
        placeButtons = new JButton[listOfPersons.size()];

        compareAndAlign(mainButtons, listOfPersons);
        fillingButtonsProperties(infoSide, name);
        fillingJLabelCountClients(name);

        frame.repaint();
        frame.setVisible(true);


    }

    public void createJButtonsArraysForClients(boolean infoSide, ArrayList<String> listOfPersons, StatusOfButtons statusOfButtons) {
        mainButtons = statusOfButtons.mainButtons;
        timeButtons = statusOfButtons.timeButtons;
        placeButtons = statusOfButtons.placeButtons;

        compareAndAlign(mainButtons, listOfPersons);

        if (frame.getTitle().equals(KTP_NAME)) {
            fillingButtonsProperties(infoSide, "Client");
            fillingJLabelCountClients(name);
        }
        if (frame.getTitle().equals(KPP_NAME)) {
            fillingButtonsProperties(infoSide, "Client");
            fillingJLabelCountClients(name);
        }
        if (frame.getTitle().equals(ESPIA_JL_NAME)) {
            fillingButtonsProperties(infoSide, "Client");
            fillingJLabelCountClients(name);
        }

        frame.repaint();


    }


    public int calculateHeightofButtons(int standartsize) {
        double pxl = (double) standartsize / STANDART_HEIGHT;
        return (int) Math.round(pxl * screenSize.height * scope);

    }

    public int calculateWidthofButtons(int standartsize, boolean infoSide) {
        if (infoSide == true) {
            double pxl = (double) standartsize / STANDART_WIDTH;
            return (int) Math.round(pxl * screenSize.width * scope);
        } else {
            double pxl = (double) standartsize * 1.1 / STANDART_WIDTH;
            return (int) Math.round(pxl * screenSize.width * scope);
        }


    }

    public int[] calculatePosition(int[] standartposition) {

        int x = (int) (Math.round((double) standartposition[0] / STANDART_WIDTH * screenSize.width * scope));


        int y = (int) (Math.round((double) standartposition[1] / STANDART_HEIGHT * screenSize.height * scope));


        return new int[]{x, y};
    }

    public int calculateFontforMainButtons(int standartsize) {
        return (int) (Math.round((double) standartsize / STANDART_WIDTH * screenSize.width * scope));


    }

    private void fillingButtonsProperties(boolean infoSide, String name) {
        int mainButtonHeight = calculateHeightofButtons(50);
        int mainButtonWidth = calculateWidthofButtons(200, infoSide);


        int timeplaceButtonHeight = calculateHeightofButtons(25);
        int timeplaceButtonWidth = calculateWidthofButtons(120, infoSide);

        int[] mainButtonPosition = calculatePosition(new int[]{10, 10});
        int[] timeButtonPosition = calculatePosition(new int[]{212, 10});
        int[] placeButtonPosition = calculatePosition(new int[]{212, 35});


        Rectangle mainButtonBounds = new Rectangle(mainButtonPosition[0], mainButtonPosition[1], mainButtonWidth, mainButtonHeight);
        Rectangle timeButtonBounds = new Rectangle(timeButtonPosition[0], timeButtonPosition[1], timeplaceButtonWidth, timeplaceButtonHeight);
        Rectangle placeButtonBounds = new Rectangle(placeButtonPosition[0], placeButtonPosition[1], timeplaceButtonWidth, timeplaceButtonHeight);


        Font fontMain = new Font("Times new Roman", Font.BOLD, calculateFontforMainButtons(17));
        Font fontTimePlace = new Font("Times new Roman", Font.BOLD, calculateFontforMainButtons(12));


        for (int i = 0; i < mainButtons.length; i++) {
            if (name.equals(SERVER_NAME)) {
                mainButtons[i] = new JButton(listOfPersons.get(i));
                mainButtons[i].setBackground(Color.RED);
            }
            mainButtons[i].setFont(fontMain);
            mainButtons[i].setBounds(mainButtonBounds);
            if (i == 0) System.out.println("Основная кнопка " + mainButtonBounds.x);
            if (i == 0) System.out.println("Основная кнопка длина " + mainButtonBounds.width);
            mainButtonBounds.y = mainButtonBounds.y + stepForHeight(60);


            if (i < 20) frame.add(mainButtons[i]);
            if (i == 9 && infoSide) {
                mainButtonBounds.setBounds(mainButtonPosition[0] + stepForWidth(STANDART_WIDTH_DISTANCE_BETWEEN_BUTTONS), mainButtonPosition[1], mainButtonWidth, mainButtonHeight);
            }
            if (i == 9 && !infoSide) {
                mainButtonBounds.setBounds(mainButtonPosition[0] + stepForWidth(60), mainButtonPosition[1], mainButtonWidth, mainButtonHeight);
            }
        }

        if (infoSide) {
            for (int i = 0; i < timeButtons.length; i++) {
                if (name.equals(SERVER_NAME)) {
                    timeButtons[i] = new JButton("....");
                }

                timeButtons[i].setFont(fontTimePlace);
                timeButtons[i].setBounds(timeButtonBounds);
                if (i == 0) System.out.println("Часовая кнопка положение " + timeButtonBounds.x);
                if (i == 0) System.out.println("Часовая кнопка  длина " + timeButtonBounds.getBounds().getWidth());

                timeButtonBounds.y = timeButtonBounds.y + stepForHeight(STANDART_HEIGHT_DISTANCE_BETWEEN_BUTTONS);

                timeButtons[i].setBackground(Color.YELLOW);
                if (i < 20) frame.add(timeButtons[i]);
                if (i == 9 && infoSide == true) {
                    timeButtonBounds.setBounds(timeButtonPosition[0] + stepForWidth(STANDART_WIDTH_DISTANCE_BETWEEN_BUTTONS), timeButtonPosition[1], timeplaceButtonWidth, timeplaceButtonHeight);
                }

            }
            for (int i = 0; i < placeButtons.length; i++) {
                if (name.equals(SERVER_NAME)) {
                    placeButtons[i] = new JButton("....");
                }
                placeButtons[i].setFont(fontTimePlace);
                placeButtons[i].setBounds(placeButtonBounds);
                //int tempX = (int) Math.round(0.03125*screenSize.width);
                placeButtonBounds.y = placeButtonBounds.y + stepForHeight(STANDART_HEIGHT_DISTANCE_BETWEEN_BUTTONS);
                placeButtons[i].setBackground(Color.YELLOW);
                if (i < 20) frame.add(placeButtons[i]);
                if (i == 9 && infoSide == true) {
                    //tempX = (int) Math.round(0.3055*screenSize.height);
                    int tempY = (int) Math.round(0.0231 * screenSize.height * scope);
                    placeButtonBounds.setBounds(timeButtonPosition[0] + stepForWidth(STANDART_WIDTH_DISTANCE_BETWEEN_BUTTONS), placeButtonPosition[1], timeplaceButtonWidth, timeplaceButtonHeight);
                }
            }
            if (name.equals(SERVER_NAME)) {
                addOfflineActionListeners();
            }

        }
    }

    private int stepForHeight(int standartSize) {
        return (int) Math.round((double) standartSize / STANDART_HEIGHT * screenSize.height * scope);
    }

    private int stepForWidth(int standartSize) {
        return (int) Math.round((double) standartSize / STANDART_WIDTH * screenSize.width * scope);
    }


    private void addOfflineActionListeners() {
        for (int i = 0; i < mainButtons.length; i++) {
            mainButtons[i].addActionListener(new ActionListeners().OfflineListener(mainButtons[i], timeButtons[i], placeButtons[i], SERVER_NAME));
        }
    }

    private void fillingJLabelCountClients(String name) {
        Rectangle countClientsLabel;
        Font fontJLabel = new Font("Times new Roman", Font.BOLD, calculateFontforMainButtons(15));
        int jLabelHeight = calculateHeightofButtons(6) + stepForHeight(STANDART_HEIGHT_DISTANCE_BETWEEN_BUTTONS) * listOfPersons.size();
        int jLabelWidht = calculateWidthofButtons(20, false);
        int[] jLabelPossition = {jLabelWidht, jLabelHeight};
        if (listOfPersons.size() < 10) {
            countClientsLabel = new Rectangle(jLabelPossition[0], jLabelPossition[1],
                    calculateWidthofButtons(200, false), calculateHeightofButtons(30));
        } else {
            countClientsLabel = new Rectangle(jLabelPossition[0], calculateHeightofButtons(6) + stepForHeight(600),
                    calculateWidthofButtons(200, false), calculateHeightofButtons(30));
        }
        if (name.equals(SERVER_NAME)) {
            jLabel = new JLabel("Кількість клієнтів - 0");
            jLabel.setFont(fontJLabel);
            jLabel.setBounds(countClientsLabel);
            frame.add(jLabel);
        } else {
            connectionStatus = new JLabel("З'єднання ...");
            connectionStatus.setFont(fontJLabel);
            connectionStatus.setBounds(countClientsLabel);
            frame.add(connectionStatus);
        }


    }

    private void fillingJLabelConnectionStatus() {
        Rectangle countClientsLabel;
        Font fontJLabel = new Font("Times new Roman", Font.BOLD, 15);
        if (listOfPersons.size() < 10) {
            countClientsLabel = new Rectangle(20, 6 + (60 * listOfPersons.size()), 200, 30);
        } else {
            countClientsLabel = new Rectangle(20, 6 + (600), 200, 30);
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
        label.setFont(new Font("Times new Roman", Font.BOLD, 25));
        label.setForeground(Color.RED);
        label.setBounds(80, 20, 200, 20);
        tempFrame.setSize(350, 100);
        tempFrame.setVisible(true);
        tempFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        tempFrame.add(label);
        for (int i = 30; i >= 0; i--) {
            label.setText("З'єднання " + i);
            label.setForeground(Color.RED);
            Thread.currentThread().sleep(1000);
        }
        tempFrame.dispose();
    }

}
