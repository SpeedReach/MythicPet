package net.brian.mythicpet.utils.time;

public enum TimeUnit {

    SECOND(1000),MINUTE(1000*60),HOUR(1000*60*60),TICK(1000*20);

    public final long l;

    TimeUnit(long l){
        this.l = l;
    }


}
