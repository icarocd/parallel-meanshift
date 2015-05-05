

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MathUtils
{
	public static BigDecimal asBigDecimal(Object value) {
	    if (value instanceof BigDecimal) {
	        return (BigDecimal) value;
	    }
	    if (value instanceof Double) {
	        return new BigDecimal(Double.toString((Double) value));
	    }
	    if (value instanceof Number) {
	        return new BigDecimal(((Number) value).doubleValue());
	    }
		if (value != null) {
			String s = value.toString();
			if (!s.isEmpty()) {
				return new BigDecimal(s);
			}
		}
	    return null;
	}

	public static BigInteger asBigInteger(Object value) {
	    if (value instanceof BigInteger) {
	        return (BigInteger) value;
	    }
	    if (value instanceof BigDecimal) {
	        return ((BigDecimal) value).toBigInteger();
	    }
	    if (value instanceof Double) {
	        return new BigDecimal(Double.toString((Double) value)).toBigInteger();
	    }
	    if (value instanceof Number) {
	        return BigInteger.valueOf(((Number) value).longValue());
	    }
	    if (value != null) {
			String s = value.toString();
			if (!s.isEmpty()) {
				return new BigInteger(s);
			}
		}
	    return null;
	}

	public static boolean isZero(float value) {
		return value > -0.00001F && value < 0.00001F;
	}

	public static boolean isNotZero(float value) {
		return value <= -0.00001F || value >= 0.00001F;
	}

	public static Double round(Double v, int scale, RoundingMode roundingMode) {
        return v == null ? null : round(asBigDecimal(v), scale, roundingMode).doubleValue();
    }

    public static BigDecimal round(BigDecimal v, int scale, RoundingMode roundingMode) {
        return v == null ? null : v.setScale(scale, roundingMode);
    }

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

	public static double logANaBaseB(double a, double b)
	{
		if(b==Math.E) { //desnecessario fazer conversao de base neste caso
			return Math.log(a);
		}

		//log a na base b = log a na base W dividido por log b na base W (pra qualquer W)
		return Math.log(a) / Math.log(b);
	}

	/**
	 * Returns the minimum value in the array a[], +infinity if no such value.
	 */
	public static double min(double[] elements) {
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] < min) {
                min = elements[i];
            }
		}
		return min;
	}

	/**
	 * Returns the minimum value in the array a[], +infinity if no such value.
	 */
	public static double min(Collection<Double> elements) {
		double min = Double.POSITIVE_INFINITY;
		for (Double a : elements) {
			if (a < min) {
                min = a;
            }
		}
		return min;
	}

	/**
	 * Returns the maximum value in the array a[], -infinity if no such value.
	 */
	public static double max(double[] elements) {
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] > max) {
                max = elements[i];
            }
		}
		return max;
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

	/** Returns the maximum value in the collection, or null if no such value. */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T max(T[] elements) {
		Comparable max = null;
		for (T a : elements) {
			Comparable el = (Comparable)a;
			if (max==null || el.compareTo(max) > 0) {
                max = el;
            }
		}
		return (T) max;
	}

	/** Returns the maximum value in the collection, or null if no such value. */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T max(Collection<T> elements) {
		Comparable max = null;
		for (T a : elements) {
			Comparable el = (Comparable)a;
			if (max==null || el.compareTo(max) > 0) {
                max = el;
            }
		}
		return (T) max;
	}

	public static float mean(float[] values) {
		return sum(values) / values.length;
	}

	public static float mean(Float[] values) {
		return sum(values) / values.length;
	}

	public static float sum(float[] values) {
		float total = 0;
		for (float element : values) {
			total += element;
		}
		return total;
	}

	public static float sum(Float[] values) {
		float total = 0;
		for (float element : values) {
			total += element;
		}
		return total;
	}

	public static double mean(Collection<Double> values) {
		double total = 0;
		for (Double element : values) {
			total += element;
		}
		return total / values.size();
	}

	public static double variance(Collection<Double> values) {
		return variance(values, mean(values));
	}

	public static double variance(float[] values) {
	    return variance(values, mean(values));
	}

	public static double variance(Float[] values) {
        return variance(values, mean(values));
    }

	public static double variance(float[] values, float mean) {
	    float temp = 0;
        for (float a : values) {
            float diff = a - mean;
            temp += diff * diff;
        }
        return temp / values.length;
    }

	public static double variance(Float[] values, float mean) {
        float temp = 0;
        for (float a : values) {
            float diff = a - mean;
            temp += diff * diff;
        }
        return temp / values.length;
    }

	private static double variance(Collection<Double> values, double mean) {
		double temp = 0;
		for (Double a : values) {
			double diff = a - mean;
			temp += diff * diff;
		}
		return temp / values.size();
	}

	public static double standardDeviation(Collection<Double> values) {
		return standardDeviation(variance(values));
	}

	public static double standardDeviation(float[] values) {
	    return standardDeviation(variance(values));
	}

	public static double standardDeviation(Float[] values) {
        return standardDeviation(variance(values));
    }

	public static double standardDeviation(double variance) {
		return Math.sqrt(variance);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<Integer> indicesOfValuesLowerThan(T[] array, T value) {
		List<Integer> indices = new ArrayList<>();
		Comparable value_ = (Comparable) value;

		for (int i = 0; i < array.length; i++) {
			if(value_.compareTo(array[i]) >= 0){
				indices.add(i);
			}
		}
		return indices;
	}

	public static int argmin(float[] dist) {
		int argmin = 0;
		float min = Float.MAX_VALUE;
		for (int i = 0; i < dist.length; i++) {
			float v = dist[i];
			if(v < min){
				argmin = i;
				min = v;
			}
		}
		return argmin;
	}

	public static float[] getMaxValuesFromRows(float[][] matrix) {
		float[] maxValuesInRows = new float[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			maxValuesInRows[i] = max(matrix[i]);
		}
		return maxValuesInRows;
	}
}
