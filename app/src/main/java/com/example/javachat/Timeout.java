package com.example.javachat;

public class Timeout extends Thread {

    private int millis = 7000;
    private boolean timeout = false;
    private boolean timeout2 = false;

    public Timeout(int millis) {
        this.millis = millis;
    }

    @Override
    public void run() {
        while (!(timeout && timeout2)) {
            if (timeout)
                timeout2 = true;
            timeout = true;
            try {
                sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        boolean stopped = true;
    }

    public void resetTimeout() {
        timeout = false;
        timeout2 = false;
    }

}
