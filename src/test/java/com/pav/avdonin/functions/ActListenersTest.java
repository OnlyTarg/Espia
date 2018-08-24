/*
package com.pav.avdonin.functions;

import com.pav.avdonin.server.Server;
import com.pav.avdonin.visual.Frames;
import javafx.beans.binding.When;
import org.junit.Test;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ActListenersTest {

    @Test
    public void onlineListenerForServer() {
        Frames frames = mock(Frames.class);
        JButton b = mock(JButton.class);
        String name = "EspiaServer";
        when(b.getText()).thenReturn("Kosik");
        when(b.getBackground()).thenReturn(Color.RED);

        JButton text = mock(JButton.class);
        JButton place = mock(JButton.class);
        ActionEvent actionEvent = mock(ActionEvent.class);



        ActionListener actionListener = new ActListeners().onlineListenerForServer(frames,name,b,text,place);
        try {
            actionListener.actionPerformed(actionEvent);
        }catch (NullPointerException e){
            e.printStackTrace();
        }




    }

    @Test
    public void offlineListener() {
    }

    @Test
    public void choiceWhoAndWhen() {
    }
}*/
