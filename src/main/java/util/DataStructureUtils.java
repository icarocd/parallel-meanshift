package util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import com.google.common.collect.Ordering;

public class DataStructureUtils {

	public static <K, V extends Comparable<? super V>> List<K> getMapKeysSortedByValueAsList(Map<K, V> map, final boolean asc, int limit) {
        List<Entry<K, V>> entries = getMapEntriesSortedByValue(map, asc, limit);
        List<K> keys = new ArrayList<>(entries.size());
        for (Entry<K, V> entry : entries) {
            keys.add(entry.getKey());
        }
        return keys;
    }

	public static <K, V extends Comparable<? super V>> List<Entry<K, V>> getMapEntriesSortedByValue(Map<K, V> map, final boolean asc) {
        return getMapEntriesSortedByValue(map, asc, -1);
    }

	public static <K, V extends Comparable<? super V>> List<Entry<K, V>> getMapEntriesSortedByValue(Map<K, V> map, final boolean asc, int limit) {
	    List<Entry<K, V>> mapEntries = new ArrayList<>(map.entrySet());

        Comparator<Entry<K, V>> valueMapComparator = getEntryMapComparatorByValue(asc);
        Collections.sort(mapEntries, valueMapComparator);

        if(limit > 0 && mapEntries.size() > limit){
            mapEntries = mapEntries.subList(0, limit);
        }

        return mapEntries;
    }

	public static <K, V extends Comparable<? super V>> Comparator<Entry<K, V>> getEntryMapComparatorByValue(boolean asc) {
		if (asc) {
			return new Comparator<Entry<K, V>>() {
				public int compare(Entry<K, V> o1, Entry<K, V> o2) {
					return o1.getValue().compareTo(o2.getValue());
				}
			};
		}
		return new Comparator<Entry<K, V>>() {
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		};
	}

	public static void setValue(boolean[] array, List<Integer> idxs, boolean value) {
		for (int idx : idxs) {
			array[idx] = value;
		}
	}

	public static List<Integer> collect(List<Integer> array, boolean[] filter) {
		List<Integer> filtered = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			if (filter[i]) {
				filtered.add(array.get(i));
			}
		}
		return filtered;
	}

	public static boolean[] newArrayTrue(int length) {
		boolean[] array = new boolean[length];
		Arrays.fill(array, true);
		return array;
	}

	/** Returns the kth lowest value from collection, where k starts in 1 */
	public static <T extends Comparable<? super T>> T kthLowest(Iterable<T> collection, int k){
		return Ordering.natural().leastOf(collection, k).get(k - 1);
	}

	/**
	 * Removes elements from collection until size <= limit.
	 * The elements to be removed are randomly chosen from collection.
	 */
	public static <T> void reduceRandomly(List<T> collection, int limit) {
		if (limit < 0) {
			throw new IllegalStateException();
		}
		Random r = new Random();
		while(collection.size() > limit){
			collection.remove( r.nextInt(collection.size()) );
		}
	}
}
