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

    /**
     * Returns a formatted time representation easier to understand.
     */
	public String getTime() {
		float value = getTimeInNanoSecs();
    	if(value < 1000){
    		return value+ " nanosecs";
    	}

    	float newValue = value / 1000F;
		if(newValue < 1000){
			return newValue+ " microsecs";
		}

		value = newValue;
		newValue = value / 1000F;
		if(newValue < 1000){
			return newValue+ " milisecs";
		}

		value = newValue;
		newValue = value / 1000F;
		if(newValue < 1000){
			return newValue+ " secs";
		}

		value = newValue;
		newValue = value / 60F;
		if(newValue < 60){
			return newValue+ " mins";
		}

		value = newValue;
		newValue = value / 60F;
		return newValue+ " hours";
	}
}
