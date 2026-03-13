public class Clock {
    private long counter;
    Clock(){
        this.counter = 0;
    }

    public synchronized long tick(){
       return this.counter++;
    }
}
