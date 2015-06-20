package meanshift;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import util.MathUtils;
import util.parallel.Action;
import util.parallel.Function;
import util.parallel.Parallel;


public class ParallelTest {
    @Test
    public void testForEach() throws Exception {
        Set<String> text = new HashSet<String>(Arrays.asList("0", "00", "000", "0000"));
        Collection<Integer> result = Parallel.ForEach(text, new Function<String, Integer>() {
            public Integer apply(String s) {
                return s.length();
            }
        });
        int total = 0;
        for(Integer i : result) {total += i;}
        assertEquals(10, total);
    }

    @Test
    public void testFor() throws Exception {
        final AtomicInteger counter = new AtomicInteger(0);
        Parallel.For(0, 1000, new Action<Long>() {
            public void doAction(Long element) {
                counter.incrementAndGet();
            }
        });
        assertEquals(1000, counter.intValue());
    }

    @Test
    public void testForInt() throws Exception {
        final AtomicInteger counter = new AtomicInteger(0);
        Parallel.For(0, 1000, new Action<Integer>() {
            public void doAction(Integer element) {
                counter.incrementAndGet();
            }
        });
        assertEquals(1000, counter.intValue());
    }

    @Test
    public void testOneThread() throws ExecutionException, InterruptedException {
        final ConcurrentHashMap<Long, String> map = new ConcurrentHashMap<Long, String>();
        Parallel.ForEach(MathUtils.rangeIterable(10000), 1, new Action<Integer>() {
            public void doAction(Integer element) {
                map.put(Thread.currentThread().getId(), "");
            }
        });
        assertEquals(1, map.keySet().size());
    }

    @Test
    public void testFourThread() throws ExecutionException, InterruptedException {
        final ConcurrentHashMap<Long, String> map = new ConcurrentHashMap<Long, String>();
        Parallel.ForEach(MathUtils.rangeIterable(10000), 4, new Action<Integer>() {
            public void doAction(Integer element) {
                map.put(Thread.currentThread().getId(), "");
            }
        });

        assertEquals(4, map.keySet().size());
    }
}
