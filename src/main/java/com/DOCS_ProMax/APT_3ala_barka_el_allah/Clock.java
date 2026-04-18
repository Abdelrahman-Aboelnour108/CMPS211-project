package com.DOCS_ProMax.APT_3ala_barka_el_allah;

public class Clock {
    private long counter;

    Clock() {
        this.counter = 0;
    }

    public synchronized long tick() {
        return this.counter++;
    }


//      Advances the clock so its next tick() will return at least (value + 1).
//      Used when receiving a remote operation whose timestamp is ahead of our local clock,
//      ensuring we never generate an ID that collides with or precedes a known remote one.
//
    public synchronized void advanceTo(long value) {
        if (value >= this.counter) {
            this.counter = value + 1;
        }
    }
}