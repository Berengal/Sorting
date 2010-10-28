package no.karevoll.sorting;

public class Counter {
    private long reads = 0;
    private long removes = 0;
    private long writes = 0;
    private long compares = 0;
    private final Settings settings;

    public Counter(final Settings settings) {
	this.settings = settings;
    }

    public void read() {
	reads++;
	sleep(settings.READ_TIME);
    }

    public void write() {
	writes++;
	sleep(settings.WRITE_TIME);
    }

    public void compare() {
	compares++;
	sleep(settings.COMPARE_TIME);
    }

    public void remove() {
	removes++;
	sleep(settings.REMOVE_TIME);
    }

    private void sleep(int time) {
	if (time != 0 && settings.DISPLAY) {
	    try {
		Thread.sleep(time);
	    } catch (InterruptedException ex) {
	    }
	}
    }

    public double getTime() {
	long r = 0;

	r += reads * settings.READ_TIME;
	r += writes * settings.WRITE_TIME;
	r += removes * settings.REMOVE_TIME;
	r += compares * settings.COMPARE_TIME;

	return r / 1000.0;
    }
}
