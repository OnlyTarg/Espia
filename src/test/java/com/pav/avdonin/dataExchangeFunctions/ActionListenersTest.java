package com.pav.avdonin.dataExchangeFunctions;

import com.pav.avdonin.util.Names;
import com.pav.avdonin.dataExchangeFunctions.statusOfButtons.StatusOfButtons;
import com.pav.avdonin.server.Server;
import com.pav.avdonin.visual.Frames;
import org.junit.Test;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class ActionListenersTest {


    @Test
    public void offlineListener() throws IOException {

        JButton someButton = mock(JButton.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        Frames frames = Server.mainframes = mock(Frames.class);
        StatusOfButtons statusOfButtons = Server.statusOfButtons = mock(StatusOfButtons.class);

        when(someButton.getBackground()).thenReturn(Color.RED);
        doNothing().when(statusOfButtons).writeStatusOFButtons();
        doNothing().when(frames).setAlwaysOnTop(false);
        new ActionListeners().OfflineListener(someButton, someButton, someButton, Names.КПП1.toString()).actionPerformed(actionEvent);
    }
}