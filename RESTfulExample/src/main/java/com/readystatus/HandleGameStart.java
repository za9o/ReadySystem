/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.readystatus;

import com.readystatus.rest.JSONServicePostMethods;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 *
 * @author Torjus
 */
public class HandleGameStart {

    Clip clip;
    Timer timer;
    private final URL SOUND_FILE_PATH = this.getClass().getClassLoader().getResource("Sound/Air-Horn.wav");
    private int isStarted = -1;
    SQLConnectionHandler sqlConnect = new SQLConnectionHandler();

    public HandleGameStart(int seconds) {
        timer = new Timer();
        sqlConnect.updateGameStatusDB("Counting");
        timer.schedule(new countDownToGameStart(), seconds * 1000);
    }

//    @Timeout
//    public void callService() {
//        
//    }
    public void playStartSound(URI filename) throws Exception {
        final CountDownLatch playingFinished = new CountDownLatch(1);
        final Clip clip = (Clip) AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(new File(filename)));

        clip.addLineListener(new LineListener() {
            public void update(LineEvent event) {
                if (event.getType() == LineEvent.Type.STOP) {
                    event.getLine().close();
                    playingFinished.countDown();
                }
            }
        });
        clip.start();
        try {
            playingFinished.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    class countDownToGameStart extends TimerTask {

        @Override
        public void run() {
            try {
                sqlConnect.updateGameStatusDB("Started");
                playStartSound(SOUND_FILE_PATH.toURI());
                timer.cancel();
                timer.purge();
            } catch (URISyntaxException ex) {
                Logger.getLogger(JSONServicePostMethods.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(JSONServicePostMethods.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//    public void startGame() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    playStartSound(SOUND_FILE_PATH.toURI());
//                    isStarted = 1;
//                } catch (URISyntaxException ex) {
//                    Logger.getLogger(JSONServicePostMethods.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (Exception ex) {
//                    Logger.getLogger(JSONServicePostMethods.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                timer.cancel();
//                timer.purge();
//            }
//        }, 2000);
//    }
    public int getStartValue() {
        return isStarted;
    }

    public void setStartValue(int isStarted) {
        this.isStarted = isStarted;
    }
}
