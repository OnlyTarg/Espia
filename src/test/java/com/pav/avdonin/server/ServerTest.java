package com.pav.avdonin.server;

import com.pav.avdonin.util.CommonFunctions;
import org.junit.Before;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

import static org.junit.Assert.*;

public class ServerTest {
    Server server;
    String serverIP = "127.0.0.1";
    int serverPort ;
    private final String testIP = "192.168.1.101";

    @Before
    public void loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/settings.properties"));
        serverPort=Integer.valueOf(properties.getProperty("port"));
    }

    @Before
    public void startServer() {
        Thread serverStart = new Thread(new Runnable() {
            @Override
            public void run() {
                server = new Server("EspiaServer");
                server.startServer();
            }
        });
        serverStart.start();
    }


    @Test
    public void testConnection() throws IOException {
        Socket socket = new Socket(serverIP, serverPort);
        DataInputStream datain = new DataInputStream(socket.getInputStream());
        DataOutputStream dataout = new DataOutputStream(socket.getOutputStream());
        dataout.writeUTF("candidate_" + testIP + "_" + 1111);
        dataout.flush();
        datain.readUTF(); //recieve json (status of buttons from server)
        String isAllowed = datain.readUTF();
        CommonFunctions.close(dataout,datain,socket);
        assertEquals("isAllowed_YES",isAllowed);
    }

}