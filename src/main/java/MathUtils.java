

import java.util.ArrayList;
import java.util.List;

public class MathUtils
{
	/**
	 * Generates an array from 0 to length-1, inclusive. Exemple for length 4: [0 1 2 3].
	 */
	public static List<Integer> range(int length) {
		List<Integer> range = new ArrayList<>(length);
		for (int i = 0; i < length; i++) {
			range.add(i);
		}
		return range;
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
