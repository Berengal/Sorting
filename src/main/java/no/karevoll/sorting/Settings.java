package no.karevoll.sorting;

public class Settings {
    public final int DELAY_MULTIPLIER = 10;

    public int       READ_TIME        = 1 * DELAY_MULTIPLIER;
    public int       WRITE_TIME       = 2 * DELAY_MULTIPLIER;
    public int       COMPARE_TIME     = 1 * DELAY_MULTIPLIER;
    public int       REMOVE_TIME      = 0 * DELAY_MULTIPLIER;

    public int       LIST_SIZE        = 10000;
    public int       MAX_VALUE        = 10000;

    public int       THREAD_POOL_SIZE = 512;
    public int       PARALLELL_CUTOFF = 8;

    public int       FRAME_WIDTH      = 1024 / 4;
    public int       FRAME_HEIGHT     = 768 / 4;
    public boolean   FULLSCREEN       = false;
    public boolean   DISPLAY          = true;
}
