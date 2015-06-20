package util;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MathUtils
{
	/** Generates an array from 0 to length, exclusive. Exemple for length 4: [0 1 2 3]. */
	public static List<Integer> range(int length) {
		List<Integer> range = new ArrayList<>(length);
		for (int i = 0; i < length; i++) {
			range.add(i);
		}
		return range;
	}

	/** Generates an array from 0 to length, exclusive. Exemple for length 4: [0 1 2 3]. */
    public static int[] rangeArray(int length) {
        int[] range = new int[length];
        for (int i = 0; i < length; i++) {
            range[i] = i;
        }
        return range;
    }

	/** Generates an array from 0 to length, exclusive. Exemple for length 4: [0 1 2 3]. */
    public static Iterable<Integer> rangeIterable(int length) {
        return rangeIterable(0, length);
    }

	/** Generates an array from 'from', inclusive, until 'to', exclusive. */
	public static Iterable<Integer> rangeIterable(int from, int to) {
	    return new Iterable<Integer>() {
	    	public Iterator<Integer> iterator() {
	    		return new Iterator<Integer>() {
                    private int current = from;
                    public boolean hasNext() {
                        return current < to;
                    }
                    public Integer next() {
                        return current++;
                    }
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

	public static Iterable<Long> rangeIterable(long from, long to) {
		return new Iterable<Long>() {
			public Iterator<Long> iterator() {
				return new Iterator<Long>() {
					private long current = from;
					public boolean hasNext() {
						return current < to;
					}
					public Long next() {
						return current++;
					}
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	/**
	 * Returns the maximum value in the array a[], -infinity if no such value.
	 */
	public static float max(float[] elements) {
		float max = Float.NEGATIVE_INFINITY;
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] > max) {
                max = elements[i];
            }
		}
		return max;
	}

	public static float mean(float[] values) {
		return sum(values) / values.length;
	}

	public static float sum(float[] values) {
		float total = 0;
		for (float element : values) {
			total += element;
		}
		return total;
	}
}
