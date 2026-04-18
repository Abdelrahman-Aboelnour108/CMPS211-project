package com.DOCS_ProMax.APT_3ala_barka_el_allah;

public class Clock {
    private long counter;
    Clock(){
        this.counter = 0;
    }

    public synchronized long tick(){
       return this.counter++;
    }
}
