package com.pav.avdonin.visual;

import javax.swing.*;
import java.awt.*;

public class FlashingLight extends Thread{
        JButton b;
        Color yellowColor = Color.YELLOW;
        public FlashingLight(JButton b){
            this.b = b;

        }

        @Override
        public void run() {
            try {
               lighting();
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,"Проінформуйте про дану помилку адміністратора");
                e.printStackTrace();
            }
        }
        public void lighting(){
        Thread.currentThread().setName("FlashingLight" + b.getText());
        for (int i = 0; i < 15; i++) {
            try {

                b.setForeground(yellowColor);
                Thread.currentThread().sleep(200);
                b.setForeground(Color.BLACK);
                Thread.currentThread().sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    }