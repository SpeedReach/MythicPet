package net.brian.mythicpet.utils.bar;

import org.bukkit.ChatColor;

public class BarBuilder {

    private final double current,max;
    private final ChatColor fullFileColor;
    private final int len;
    private final char c;


    public BarBuilder(double current,double max){
        this(current,max, ChatColor.RED,10,'|');
    }

    public BarBuilder(double current, double max,ChatColor fullFillColor ,int len ,char c){
        this.current = current;
        this.max = max;
        this.fullFileColor = fullFillColor;
        this.len = len;
        this.c = c;
    }

    public String build(){
        StringBuilder builder = new StringBuilder(fullFileColor.toString());
        double partition = max/len;

        for(double i=0;i<max;i+=partition){
            if(i<current){
                builder.append(fullFileColor).append(c);
            }
            else{
                builder.append("§7").append(c);
            }
        }

        return builder.toString();
    }


}
