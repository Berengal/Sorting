package no.karevoll.sorting;

public class Settings {
    public final int DELAY_MULTIPLIER = 10;

    public int READ_TIME = 1 * DELAY_MULTIPLIER;
    public int WRITE_TIME = 2 * DELAY_MULTIPLIER;
    public int COMPARE_TIME = 2 * DELAY_MULTIPLIER;
    public int REMOVE_TIME = 0 * DELAY_MULTIPLIER;

    public int LIST_SIZE = 10000;
    public int MAX_VALUE = 10000;

    public int THREAD_POOL_SIZE = 768;
    public int PARALLELL_CUTOFF = 2;

    public int FRAME_WIDTH = 1024;
    public int FRAME_HEIGHT = 768;
    public boolean FULLSCREEN = true;
    public boolean DISPLAY = true;
}
