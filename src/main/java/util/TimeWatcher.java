package util;


public class TimeWatcher {

    private static final long NANO_2_MILLIS = 1000000L;

    private Long startTime;

    public TimeWatcher start() {
        startTime = System.nanoTime();
        return this;
    }

    public long getTimeInNanoSecs() {
        return System.nanoTime() - startTime;
    }

    public long getTimeInMiliSecs() {
        long nanoTime = getTimeInNanoSecs();
        return nanoTime / NANO_2_MILLIS;
    }

    public long getTimeInSecs() {
        return getTimeInMiliSecs() / 1000;
    }

    public float getTimeInMinutes() {
        return getTimeInSecs() / 60F;
    }
}
