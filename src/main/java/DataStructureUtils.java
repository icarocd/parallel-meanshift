

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import com.google.common.collect.Ordering;

public class DataStructureUtils {

    public static boolean isEmpty(Map<?,?> c) {
        return c == null || c.isEmpty();
    }

	public static boolean isEmpty(Collection<?> c) {
		return c == null || c.isEmpty();
	}

	@SafeVarargs
	public static <T> List<T> asList(T... elements) {
		List<T> list = new ArrayList<T>(elements.length);
		for (int i = 0; i < elements.length; i++) {
			list.add(elements[i]);
		}
		return list;
	}

    public static <X, Y extends Comparable<Y>> void addIfMaxKeyValue(Map<X, Y> maxWeightByTerm, X key, Y value) {
		Y currentMax = maxWeightByTerm.get(key);
		if(currentMax == null || currentMax.compareTo(value) < 0){
			maxWeightByTerm.put(key, value);
		}
	}

	public static <K, V extends Comparable<? super V>> Set<K> getMapKeysSortedByValue(Map<K, V> map, final boolean asc) {
	    return getMapKeysSortedByValue(map, asc, -1);
	}

	public static <K, V extends Comparable<? super V>> Set<K> getMapKeysSortedByValue(Map<K, V> map, final boolean asc, int limit) {
	    List<Entry<K, V>> entries = getMapEntriesSortedByValue(map, asc, limit);
        Set<K> keys = new LinkedHashSet<>(entries.size());
        for (Entry<K, V> entry : entries) {
            keys.add(entry.getKey());
        }
        return keys;
    }

	public static <K, V extends Comparable<? super V>> List<K> getMapKeysSortedByValueAsList(Map<K, V> map, final boolean asc, int limit) {
        List<Entry<K, V>> entries = getMapEntriesSortedByValue(map, asc, limit);
        List<K> keys = new ArrayList<>(entries.size());
        for (Entry<K, V> entry : entries) {
            keys.add(entry.getKey());
        }
        return keys;
    }

	public static <K, V extends Comparable<? super V>> Map<K, V> getMapSortedByValue(Map<K, V> map, final boolean asc) {
		List<Entry<K, V>> mapEntries = getMapEntriesSortedByValue(map, asc);

		Map<K, V> mapSortedByValue = new LinkedHashMap<>();
		for (Entry<K, V> entry : mapEntries) {
			mapSortedByValue.put(entry.getKey(), entry.getValue());
		}
		return mapSortedByValue;
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

	public static <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
		Set<T> cloneSet = new LinkedHashSet<T>(set1);
		cloneSet.retainAll(set2);
		return cloneSet;
	}

	/**
	 * Remove todas as entradas do mapa cujo value seja diferente do parametro especificado.
	 */
	public static <X, Y> void retainEntryMapsByValue(Map<X, Y> map, Y valueToRetain) {
		Iterator<Entry<X, Y>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<X, Y> entry = it.next();
			if (!valueToRetain.equals(entry.getValue())) {
				it.remove();
			}
		}
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
	public static <T extends Comparable<? super T>> T kthLowest(List<T> collection, int k){
		if(k >= collection.size()){
			throw new IllegalArgumentException("please to a regular sort and then get your desired kth element");
		}
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

	/**
	 * Transfers the elment from 'list' that exceeds 'limit' size, to a container.
	 * The order from exceeding elements will be maintained in container.
	 */
    public static <T> void transferExceedingElements(List<T> list, List<T> container, int limit) {
        while (list.size() > limit) {
            T element = list.remove(limit);
            container.add(element);
        }
    }

    public static void main(String[] args) {
        List<String> a = DataStructureUtils.asList("a", "b", "c");
        List<String> b = new ArrayList<String>();
        transferExceedingElements(a, b, 0);
        System.out.println(a);
        System.out.println(b);
    }
}
