package com.pav.avdonin.functions;

import com.pav.avdonin.server.Server;
import com.pav.avdonin.visual.Frames;
import org.junit.Test;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class ActListenersTest {


    @Test
    public void offlineListener() throws IOException {

        JButton someButton = mock(JButton.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        Frames frames = Server.mainframes = mock(Frames.class);
        StatusButtons statusButtons = Server.statusButtons = mock(StatusButtons.class);

        when(someButton.getBackground()).thenReturn(Color.RED);
        doNothing().when(statusButtons).writeStatusOFButtons();
        doNothing().when(frames).setAlwaysOnTop(false);
        new ActListeners().OfflineListener(someButton, someButton, someButton, "КПП1").actionPerformed(actionEvent);
    }
}