package net.brian.mythicpet.util;

public class Pair<T,U> {
    private final T key;
    private final U value;

    public Pair(T key, U value) {
        this.key = key;
        this.value = value;
    }
    public static <T, U> Pair<T,U> of(T key, U value) {
        return new <T, U> Pair<T,U>(key, value);
    }

    public T fst(){
        return key;
    }
    public U snd(){
        return value;
    }

}
