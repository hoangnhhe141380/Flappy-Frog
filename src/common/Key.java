/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author DELL
 */
public class Key implements KeyListener {

    boolean press = false;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public boolean isPress() {
        return press;
    }

    public void setPress(boolean press) {
        this.press = press;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            press = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        press = false;
    }

}
