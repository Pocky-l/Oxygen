package su.gamepoint.pocky.oxygen.utils;

public class TickLimiter {

    private int count = 0;

    private final int limit;

    public TickLimiter(int limit) {
        this.limit = limit;
    }

    public TickLimiter(int limit, boolean first) {
        if (first) {
            this.count = limit;
        }
        this.limit = limit;
    }


    public void inc() {
        count++;
    }

    public boolean check() {
        if (count > limit) {
            reset();
        }
        return count >= limit;
    }

    public void reset() {
        count = 0;
    }
}
