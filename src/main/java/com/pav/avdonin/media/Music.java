package com.pav.avdonin.media;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Music {



    Clip clipClick,clipZvonok,clipDoor;
    public void soundDoor(){

        try {
            clipDoor = AudioSystem.getClip();
            InputStream input = new BufferedInputStream(getClass().getResourceAsStream("/door.wav"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(input);
            clipDoor.open(ais);
            clipDoor.start();
            ais.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
           // JOptionPane.showMessageDialog(null,"Помилка під час програвання аудіо (1)");
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
           // JOptionPane.showMessageDialog(null,"Помилка під час програвання аудіо (2)");
        } catch (IOException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null,"Помилка під час програвання аудіо (3)");
        }

    }
    public void soundClick(){
        try {
            clipClick = AudioSystem.getClip();
            InputStream input = new BufferedInputStream(getClass().getResourceAsStream("/click1.wav"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(input);
            clipClick.open(ais);
            clipClick.start();
            ais.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null,"Помилка під час програвання аудіо (1)");
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null,"Помилка під час програвання аудіо (2)");
        } catch (IOException e) {
            e.printStackTrace();
           // JOptionPane.showMessageDialog(null,"Помилка під час програвання аудіо (3)");
        }

    }
    public void soundZvonok(){
        try {
            clipZvonok = AudioSystem.getClip();
            InputStream input = new BufferedInputStream(getClass().getResourceAsStream("/zv1.wav"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(input);
            clipZvonok.open(ais);
            clipZvonok.start();
            ais.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
           // JOptionPane.showMessageDialog(null,"Помилка під час програвання аудіо (1)");
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
           // JOptionPane.showMessageDialog(null,"Помилка під час програвання аудіо (2)");
        } catch (IOException e) {
            e.printStackTrace();
           // JOptionPane.showMessageDialog(null,"Помилка під час програвання аудіо (3)");
        }

    }
}