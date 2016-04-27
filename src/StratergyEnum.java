/**
 * Created by hannahzhang on 16/4/24.
 */
public enum StratergyEnum {
    VERY_FAST(1,100),
    FAST(3,500),
    NORMAL(10,3000);
    private int redisCount;
    private int sleepTime;

    private StratergyEnum(int redisCount, int sleepTime) {
        this.redisCount = redisCount;
        this.sleepTime = sleepTime;
    }

    public int getRedisCount() {
        return redisCount;
    }

    public void setRedisCount(int redisCount) {
        this.redisCount = redisCount;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }
}
