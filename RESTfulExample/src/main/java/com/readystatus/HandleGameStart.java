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
    private int isStarted = -1;
    SQLConnectionHandler sqlConnect = new SQLConnectionHandler();
    int selectSound;
    private final URL SOUND_FILE_START_GAME_PATH = this.getClass().getClassLoader().getResource("Sound/Air-Horn.wav");
    private final URL SOUND_FILE_ONE_TEAM_READY_PATH = this.getClass().getClassLoader().getResource("Sound/Beep-on-team-ready.wav");

    public HandleGameStart(int seconds, int soundFileIndicator) {
        timer = new Timer();
        this.selectSound = soundFileIndicator;
        sqlConnect.updateGameStatusDB("Counting");
        timer.schedule(new countDownToGameStart(), seconds * 1000);
    }

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

    private URL SOUND_FILE(int i) {
        if (i == 1) {
            return SOUND_FILE_ONE_TEAM_READY_PATH;
        } else if (i == 2) {
            return SOUND_FILE_START_GAME_PATH;
        }
        return null;
    }

    class countDownToGameStart extends TimerTask {

        @Override
        public void run() {
            try {
                sqlConnect.updateGameStatusDB("Started");
                playStartSound(SOUND_FILE(selectSound).toURI());
                timer.cancel();
                timer.purge();
            } catch (URISyntaxException ex) {
                Logger.getLogger(JSONServicePostMethods.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(JSONServicePostMethods.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
