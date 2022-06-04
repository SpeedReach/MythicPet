package net.brian.mythicpet.utils.time;

public class TimeInfo {

    final long millSeconds;

    public TimeInfo(long millSeconds){
        this.millSeconds = millSeconds;
    }

    public Integer getTime(TimeUnit unit){
        return (int) (millSeconds/ unit.l);
    }



    public Long getMillSec(){
        return millSeconds;
    }
}
