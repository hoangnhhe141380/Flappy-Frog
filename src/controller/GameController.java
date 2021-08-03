/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import common.Key;
import gui.GameFrm;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class GameController {

    GameFrm gf = new GameFrm();
    JButton frog = new JButton(new ImageIcon(getClass().getResource("/resources/frog.PNG")));
    Key key = new Key();
    Timer runGame;
    ArrayList<JButton> list = new ArrayList<>();
    ArrayList<JButton> listSave = new ArrayList<>();
    int wPanel = gf.getPnGame().getWidth();//411
    int hPanel = gf.getPnGame().getHeight();//305

    int xfrog = 150, hfrog = 40, wfrog = 40;
    double yfrog = 100, yfrogSave = 0;

    int wPipe = 40, yPipe = 0;
    int gapPipes = 130; //Gap 2 pipes in same column
    int gapTwoPipes = 200; //Gap between 2 pipes top and bot 
    int gapTwoPipesSave = 0;

    double accelerationFalls = 0.001; //Gia toc roi
    double powerUp = 0.1; // Power press up

    String rank = "";
    String rankSave = "";
    int mark = 0, markSave = 0;
    int speed = 0, speedSave = 0;

    boolean checkPause = false, checkSave = false, isUp = false;

    public GameController() {

        gf.getPnGame().add(frog);
        frog.addKeyListener(key);
        btnPauseAction();
        btnSaveAction();
        gf.getBtnSave().addKeyListener(key);
        gf.getBtnPause().addKeyListener(key);
        run();
        gf.setVisible(true);
    }

    public void run() {
        runGame = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                speed++;
                if (key.isPress()) {
                    isUp = true;
                    speed = 1;
                    key.setPress(false);
                }
                if (!isUp) {
                    yfrog += getSpeed();
                } else {
                    if (powerUp <= getSpeed()) {
                        isUp = false;
                        speed = 0;
                        return;
                    }
                    yfrog = yfrog - (powerUp - getSpeed());
                }
                frog.setBounds(xfrog, (int) yfrog, wfrog, hfrog);

                if (gapTwoPipes == 200) {
                    addPipe();
                    gapTwoPipes = 0;
                }
                for (int i = 0; i < list.size(); i++) {
                    int x = list.get(i).getBounds().x - 1;
                    int y = list.get(i).getBounds().y;
                    list.get(i).setLocation(x, y);
                    if (x <= -40) {
                        list.remove(i);
                        i--;
                    }
                }
                getMark();
                gapTwoPipes++;
                if (checkImpact()) {
                    runGame.stop();
                    showMessage();
                }
            }
        });
        runGame.start();
    }

    public void addPipe() {
        Random r = new Random();
        JButton pipeUp = new JButton();
        int hPipeUp = r.nextInt(80) + 40;
        pipeUp.setBounds(wPanel, yPipe, wPipe, hPipeUp);
        JButton pipeDown = new JButton();
        pipeDown.setBounds(wPanel, hPipeUp + gapPipes, wPipe, wPanel - hPipeUp - gapPipes);
        list.add(pipeUp);
        list.add(pipeDown);
        gf.getPnGame().add(pipeUp);
        gf.getPnGame().add(pipeDown);
    }

    public boolean checkImpact() {
        //Touch top
        if (yfrog <= 0) {
            frog.setBounds(xfrog, 0, wfrog, hfrog);
            return true;
        }
        //Touch bottom
        if (yfrog >= hPanel - 40) {
            frog.setBounds(xfrog, hPanel - 40, wfrog, hfrog);
            return true;
        }
        Rectangle frog = new Rectangle(this.frog.getX(), this.frog.getY(), this.frog.getWidth(), this.frog.getHeight());
        for (JButton pipe : list) {
            Rectangle p = new Rectangle(pipe.getX(), pipe.getY(), pipe.getWidth(), pipe.getHeight());
            if (frog.intersects(p)) {
                if (frog.getX() > pipe.getX() - wfrog + 1 && frog.getX() < pipe.getX() + wPipe - 1 && yfrog >= pipe.getHeight()) {
                    yfrog = pipe.getHeight();
                    frog.setBounds(xfrog, (int) yfrog, wfrog, hfrog);
                }
                return true;
            }
        }
        return false;
    }

    public void btnPauseAction() {
        gf.getBtnPause().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (checkPause == false) {
                    runGame.stop();
                    gf.getBtnPause().setText("Resume");
                    checkPause = true;
                } else {
                    runGame.start();
                    gf.getBtnPause().setText("Pause");
                    checkPause = false;
                }
            }
        });
    }

    public void getMark() {
        for (JButton btn : list) {
            if (frog.getX() == btn.getX()) {
                mark++;
                gf.getLblPoint().setText("Point: " + mark / 2);
                if ((mark / 2) >= 40) {

                    rank = "";
                    rank = "Platinum medal";

                } else if ((mark / 2) >= 30) {

                    rank = "";
                    rank = "Gold medal";
                } else if ((mark / 2) >= 20) {

                    rank = "";
                    rank = "Siliver medal";
                } else if ((mark / 2) >= 10) {
                    rank = "";
                    rank = "Bronze medal";

                }
                gf.getLblRank().setText(rank);
            }
        }
    }

    public void btnSaveAction() {
        gf.getBtnSave().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkSave = true;
                listSave.clear();
                for (JButton pipe : list) {
                    JButton pipeSave = new JButton();
                    int x = pipe.getX();
                    int y = pipe.getY();
                    int width = pipe.getWidth();
                    int height = pipe.getHeight();
                    pipeSave.setBounds(x, y, width, height);
                    listSave.add(pipeSave);
                }
                gapTwoPipesSave = gapTwoPipes;
                markSave = mark;
                yfrogSave = yfrog;
                System.out.println(yfrogSave);
                rankSave = rank;
                speedSave = speed;
            }
        });
    }

    public void showMessage() {
        if (!checkSave) {
            Object mes[] = {"New Game", "Exit"};
            int option = JOptionPane.showOptionDialog(null, "Do you want to play new game?",
                    "Notice!",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, mes, mes[0]);
            switch (option) {
                case 0: {
                    newGame();
                    break;
                }
                case 1: {
                    System.exit(0);
                    break;
                }
                case -1: {
                    showMessage();
                    break;
                }
            }
        } else {
            Object mes[] = {"New Game", "Re-play", "Exit"};
            int option = JOptionPane.showOptionDialog(null, "Do you want to play new game?",
                    "Notice!",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, mes, mes[0]);
            switch (option) {
                case 0: {
                    newGame();
                    break;
                }
                case 1: {
                    playFromSavePoint();
                    break;
                }
                case 2: {
                    System.exit(0);
                    break;
                }
                case -1: {
                    showMessage();
                    break;
                }
            }
        }
    }

    public void newGame() {
        gf.getPnGame().removeAll();
        gf.getPnGame().repaint();
        list.clear();
        listSave.clear();
        mark = 0;
        markSave = 0;
        gapTwoPipes = 130;
        gapTwoPipesSave = 0;
        gf.getLblPoint().setText("Point: 0");
        yfrog = 40;
        gf.getPnGame().add(frog);
        speed = 0;
        isUp = false;
        rank = "";
        rankSave = "";
        gf.getLblRank().setText(rank);
        runGame.start();
    }

    public void playFromSavePoint() {
        gf.getPnGame().removeAll();
        gf.getPnGame().repaint();
        yfrog = yfrogSave;
        System.out.println(yfrog);
        gf.getPnGame().add(frog);
        list.clear();
        for (JButton pipeSave : listSave) {
            JButton pipe = new JButton();
            int x = pipeSave.getX();
            int y = pipeSave.getY();
            int width = pipeSave.getWidth();
            int height = pipeSave.getHeight();
            pipe.setBounds(x, y, width, height);
            list.add(pipe);
            gf.getPnGame().add(pipe);
        }
        gapTwoPipes = gapTwoPipesSave;
        mark = markSave;
        gf.getLblPoint().setText("Point: " + mark / 2);

        rank = "";
        rank = rankSave;
        speed = speedSave;
        gf.getLblRank().setText(rank);

        runGame.start();
    }

    public double getSpeed() {
        return (speed * accelerationFalls);
    }

}
