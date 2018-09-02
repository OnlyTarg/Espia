package com.pav.avdonin.media;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Music {


    Clip clipClick, clipZvonok, clipDoor;

    public void soundDoor() {

        try {
            clipDoor = AudioSystem.getClip();
            InputStream input = new BufferedInputStream(getClass().getResourceAsStream("/door.wav"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(input);
            clipDoor.open(ais);
            clipDoor.start();
            ais.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void soundClick() {
        try {
            clipClick = AudioSystem.getClip();
            InputStream input = new BufferedInputStream(getClass().getResourceAsStream("/click1.wav"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(input);
            clipClick.open(ais);
            clipClick.start();
            ais.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void soundRing() {
        try {
            clipZvonok = AudioSystem.getClip();
            InputStream input = new BufferedInputStream(getClass().getResourceAsStream("/zv1.wav"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(input);
            clipZvonok.open(ais);
            clipZvonok.start();
            ais.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}