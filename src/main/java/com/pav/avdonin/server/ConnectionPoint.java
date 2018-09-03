package com.pav.avdonin.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pav.avdonin.util.CommonFunctions;
import com.pav.avdonin.dataexchange.statusofbuttons.StatusOfButtons;
import com.pav.avdonin.dataexchange.statusofbuttons.StatusButtonsSerializer;
import com.pav.avdonin.dataexchange.IdentifierIncomingChangesFromClients;
import com.pav.avdonin.visual.Frames;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ConnectionPoint extends Thread {

    public long ID = 0;
    public String reason = "";
    Frames mainframe;

    private Socket socket;
    public DataOutputStream dataout;
    public DataInputStream datain;
    public final String ENCODING = "UTF8";

    public ConnectionPoint(Socket socket, Frames mainframe) {
        this.mainframe = mainframe;
        this.socket = socket;
        try {
            datain = new DataInputStream(socket.getInputStream());
            dataout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            mainframe.jLabel.setText("Кількість клієнтів - " + Server.listOfClients.size());
            sentStatusOfButtonsToNewClient();
            CheckingSignal checkingSignal = startCheckingSignal();
            startDataExchange();
            checkingSignal.interrupt();
            new CommonFunctions().close(dataout, datain, socket);
        } catch (SocketException e) {
            Server.logging.writeExeptionToLogger(e, Server.statusOfLogger, Thread.currentThread());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Server.logging.writeExeptionToLogger(e, Server.statusOfLogger, Thread.currentThread());
        }

    }

    private CheckingSignal startCheckingSignal() {
        CheckingSignal checkingSignal = new CheckingSignal(this);
        checkingSignal.setDaemon(true);
        checkingSignal.start();
        return checkingSignal;
    }

    private void sentStatusOfButtonsToNewClient() throws IOException {
        StatusOfButtons statusOfButtons = new StatusOfButtons(mainframe.mainButtons, mainframe.timeButtons,
                mainframe.placeButtons, mainframe.listOfPersons);
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(StatusOfButtons.class, new StatusButtonsSerializer());
        Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
        dataout.writeUTF(gson.toJson(statusOfButtons));
        dataout.flush();
    }

    private void startDataExchange() throws IOException {
        //String value = "";
        IdentifierIncomingChangesFromClients identifierIncomingChangesFromClients = new IdentifierIncomingChangesFromClients();

        while (true) {
            try {
                String ip = Server.listOfClients.get(Server.listOfClients.size() - 1).toString();
                identifierIncomingChangesFromClients.determineButton(this, ip, Server.mapallowedClients.get(ip), socket, datain, dataout, mainframe, ID);
                Server.statusOfButtons.writeStatusOFButtons();

            } catch (SocketException e) {
                e.printStackTrace();
                Server.listOfClients.remove(currentThread());
                System.out.println("ID after exiting = " + ID);
                if (reason.equals("press exit")) {
                    Server.clientsConnectionHistory.exitFromSession(Thread.currentThread().getName(), CommonFunctions.getCurrentTimeWithSeconds(), ID, reason);
                } else {
                    Server.clientsConnectionHistory.exitFromSession(Thread.currentThread().getName(), CommonFunctions.getCurrentTimeWithSeconds(), ID, e.getMessage());
                    Server.logging.writeExeptionToLogger(e, Server.statusOfLogger, Thread.currentThread());
                }
                FileUtils.writeStringToFile(Server.client, "", ENCODING);
                for (int i = 0; i < Server.listOfClients.size(); i++) {
                    FileUtils.writeStringToFile(Server.client, Server.listOfClients.get(i).toString() + " " + Server.mapallowedClients.get(Server.listOfClients.get(i).toString()) + "\r\n", "UTF8", true);
                }
                mainframe.jLabel.setText("Кількість клієнтів - " + Server.listOfClients.size());
                new CommonFunctions().close(dataout, datain, socket);
                break;
            } catch (Exception e) {
                Server.logging.writeExeptionToLogger(e, Server.statusOfLogger, Thread.currentThread());
                e.printStackTrace();
                Server.listOfClients.remove(currentThread());
                FileUtils.writeStringToFile(Server.client, "", ENCODING);
                for (int i = 0; i < Server.listOfClients.size(); i++) {
                    FileUtils.writeStringToFile(Server.client, Server.listOfClients.get(i).toString() + " " + Server.mapallowedClients.get(Server.listOfClients.get(i).toString()) + "\r\n", ENCODING, true);
                }
                mainframe.jLabel.setText("Кількість клієнтів - " + Server.listOfClients.size());
                new CommonFunctions().close(dataout, datain, socket);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return socket.getInetAddress().toString().substring(1);
    }

    private class CheckingSignal extends Thread {
        ConnectionPoint connectionPoint;

        public CheckingSignal(ConnectionPoint connection) {
            this.connectionPoint = connection;
        }

        @Override
        public void run() {
            this.setName(connectionPoint.getName() + "(signal)");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    connectionPoint.dataout.writeUTF("Connection test");
                    connectionPoint.dataout.flush();
                    Thread.currentThread().sleep(10000);

                } catch (IOException e) {
                    Server.logging.writeExeptionToLogger(e, Server.statusOfLogger, Thread.currentThread());
                    e.printStackTrace();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }

            }

        }
    }


}