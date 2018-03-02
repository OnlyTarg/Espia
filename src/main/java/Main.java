import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import static javax.swing.JOptionPane.showInputDialog;

/**
 * Created by CleBo on 19.01.2018.
 */
public class Main extends JFrame {


    String ipKPP="", ipKTP="", ipServer="",ip120="";
    String currentIP;
    ArrayList<String> listIP;

    public Main() {

        try {
            currentIP = Inet4Address.getLocalHost().getHostAddress();
            InputStream in = getClass().getResourceAsStream("/listIP.txt");
            readListIP(in);
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка при считывании файла");
        }

        try {
            if(listIP.contains(currentIP)){
                if (currentIP.equals(ipKPP)){
                    new Client("КПП-1");
                }
                if (currentIP.equals(ipKTP)){
                    new Client("КПП-2(КТП)");
                }
                if (currentIP.equals(ipServer)){
                    new Server("ЦУС(121)");
                }
                if (currentIP.equals(ip120)){
                    new Client120("ЦУС(120)");
                }
            }
            else {
                new EspiaJL();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }





    }
    public void enterName() {

    }
    private void readListIP(InputStream input){
        Scanner scanner = new Scanner(input,"Cp1251");
        listIP = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String [] values = line.split("=");
            listIP.add(values[1]);
        }
        ipKPP = listIP.get(0).trim();
        ipKTP = listIP.get(1).trim();
        ipServer = listIP.get(2).trim();
        ip120 = listIP.get(3).trim();
        scanner.close();
    }




    public static void main(String[] args) throws IOException, InterruptedException {
        Main main = new Main();
        }

}