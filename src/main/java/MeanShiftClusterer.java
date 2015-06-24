import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import util.DataStructureUtils;
import util.MathUtils;
import util.Matrix;
import util.TimeWatcher;
import util.parallel.Action;
import util.parallel.AtomicFloat;
import util.parallel.Parallel;

/**
 * Perform MeanShift Clustering of data using a flat kernel.
 * Returns indices of samples from input matrix, where each index relates to a sample that was considered a cluster.
 */
public class MeanShiftClusterer {

    private static int OPTION = 1; //0 serial, 1 parallel API java 8, 2 parallel api customizada

	private Map<Integer, List<Integer>> neighborsByElement;

	private ExecutorService executorService;

    /**
     * @param distanceMatrix
     * @param maxSeeds        the maximum number of seeds. Use -1 to use all samples as seeds.
     * @param quantile        should be between [0, 1]. 0.5 means that the median of all pairwise distances is used. In doubt, use around 0.3.
     * @param maxIterations   the maximum number of iteration steps used by seed convergence. In doubt, use 300.
     * @return
     */
    public List<Integer> mean_shift(Matrix distanceMatrix, int maxSeeds, float quantile, int maxIterations) {
        List<Integer> seeds = MathUtils.range(distanceMatrix.getLineNumber());
        if (maxSeeds > 0 && maxSeeds < seeds.size()) {
            DataStructureUtils.reduceRandomly(seeds, maxSeeds);
            System.out.println(seeds.size()+" seeds to be used, from " + distanceMatrix.getLineNumber());
        }
        return mean_shift(distanceMatrix, seeds, quantile, maxIterations);
    }

    public List<Integer> mean_shift(Matrix distanceMatrix, List<Integer> seeds, final float quantile, final int maxIterations) {
    	executorService = OPTION==2 ? Executors.newFixedThreadPool(4) : null;

    	final float bandwidth = estimateBandwidth(distanceMatrix, quantile);

        final Map<Integer, Integer> intensityByCenter;
        {
            double stop_thresh = 1e-3 * bandwidth; // when mean has converged

        	computeIndicesOfAllNeighborsWithinRadius(seeds, distanceMatrix, bandwidth);

            // For each seed, climb gradient until convergence or max_iterations:

            intensityByCenter = OPTION==0 ? new HashMap<>() : new ConcurrentHashMap<>();
            {
            	TimeWatcher timeWatcher = new TimeWatcher().start();
            	if(OPTION==0){
            		for (Integer seed : seeds) {
            			converge(distanceMatrix, maxIterations, stop_thresh, intensityByCenter, seed);
            		}
            	}else{
            		if(OPTION==1){
            			seeds.parallelStream().forEach( seed -> {
            				converge(distanceMatrix, maxIterations, stop_thresh, intensityByCenter, seed);
            			});
            		}else{
						Parallel.ForEach(seeds, executorService, new Action<Integer>() {
							public void doAction(Integer seed) {
								converge(distanceMatrix, maxIterations, stop_thresh, intensityByCenter, seed);
							}
						});
            		}
            	}
            	System.out.println("convergence finished after "+timeWatcher.getTime());
            }
        }

        // POST PROCESSING: remove near duplicate points
        // If the distance between two kernels is less than the bandwidth,
        // then we have to remove one because it is a duplicate: remove the one with fewer points.
        TimeWatcher timeWatcher = new TimeWatcher().start();
        List<Integer> sortedCenters = DataStructureUtils.getMapKeysSortedByValueAsList(intensityByCenter, false, -1);

        boolean[] unique = DataStructureUtils.newArrayTrue(sortedCenters.size());
        for (int i = 0; i < sortedCenters.size(); i++) {
            int center = sortedCenters.get(i);
            if (unique[i]) {
                List<Integer> neighborCenters = indicesOfNeighborsWithinRadius(center, distanceMatrix, bandwidth, sortedCenters);
                DataStructureUtils.setValue(unique, neighborCenters, false);
                unique[i] = true; // leave the current point as unique
            }
        }
        List<Integer> centers = DataStructureUtils.collect(sortedCenters, unique);
        System.out.println("post processing finished after "+timeWatcher.getTime());

        if(executorService != null){
        	executorService.shutdown();
        }

        return centers;
    }

    private void converge(Matrix distanceMatrix, int max_iterations, double stop_thresh,
        Map<Integer, Integer> intensityByCenter, int seed)
    {
        int completed_iterations = 0;
        while(true){
            // Find mean of points within bandwidth
            List<Integer> pointsWithinRadius = neighborsByElement.get(seed);
            if(pointsWithinRadius.isEmpty()){
                break; // Depending on seeding strategy this condition may occur
            }

            int old_mean = seed; // save the old mean
            seed = getMeanPoint(pointsWithinRadius, distanceMatrix);

            // If converged or at max_iterations, add the cluster
            if(distanceMatrix.getValue(seed, old_mean) < stop_thresh || completed_iterations == max_iterations){
                intensityByCenter.put(seed, pointsWithinRadius.size());
                break;
            }
            completed_iterations++;
        }
    }

    private float estimateBandwidth(Matrix distanceMatrix, double quantile) {
        TimeWatcher timeWatcher = new TimeWatcher().start();

        final int numLines = distanceMatrix.getLineNumber();

        int knn = (int) (numLines * quantile);

        float bandwidth;
		if (OPTION==0) {
        	float sumDistanceKNearestNeighbor = 0;
        	for (int i = 0; i < numLines; i++) {
        		float kNearestNeighborDistance = distanceMatrix.getKthLowestValueInLine(i, knn);
        		sumDistanceKNearestNeighbor += kNearestNeighborDistance;
        	}
        	bandwidth = sumDistanceKNearestNeighbor / numLines;
		} else {
			AtomicFloat sumDistanceKNearestNeighbor = new AtomicFloat();
			if(OPTION==1){
				IntStream.range(0, numLines).parallel().forEach(i -> {
					float kNearestNeighborDistance = distanceMatrix.getKthLowestValueInLine(i, knn);
					sumDistanceKNearestNeighbor.addAndGet(kNearestNeighborDistance);
				});
			}else{
				Parallel.For(0, numLines, executorService, new Action<Integer>() {
					public void doAction(Integer i) {
						float kNearestNeighborDistance = distanceMatrix.getKthLowestValueInLine(i, knn);
						sumDistanceKNearestNeighbor.addAndGet(kNearestNeighborDistance);
					}
				});
			}
        	bandwidth = sumDistanceKNearestNeighbor.get() / numLines;
        }

        System.out.println("Bandwidth estimated: "+bandwidth+". Elapsed time: "+timeWatcher.getTime());

        return bandwidth;
    }

    /**
     * Finds the neighbors within a given radius of a point. Returns indices of the neighbors.
     * All points are compared to the point.
     */
    private void computeIndicesOfAllNeighborsWithinRadius(List<Integer> seeds, Matrix distanceMatrix, float bandwidth){
    	TimeWatcher timeWatcher = new TimeWatcher().start();
		if (OPTION==0) {
			neighborsByElement = new HashMap<>();
    		for (Integer seed : seeds) {
    			List<Integer> indicesOfValuesLowerThanRadius = new ArrayList<>();
    			neighborsByElement.put(seed, indicesOfValuesLowerThanRadius);
    			int pointsLength = distanceMatrix.getColumnNumber();
    			for (int idx = 0; idx < pointsLength; idx++) {
    				int point = idx;
    				if(distanceMatrix.getValue(seed, point) < bandwidth){
    					indicesOfValuesLowerThanRadius.add(idx);
    				}
    			}
    		}
    	} else {
    		neighborsByElement = new ConcurrentHashMap<>();
    		if(OPTION==1){
    			seeds.parallelStream().forEach( seed -> {
    				List<Integer> indicesOfValuesLowerThanRadius = new ArrayList<>();
    				neighborsByElement.put(seed, indicesOfValuesLowerThanRadius);
    				int pointsLength = distanceMatrix.getColumnNumber();
    				for (int idx = 0; idx < pointsLength; idx++) {
    					int point = idx;
    					if(distanceMatrix.getValue(seed, point) < bandwidth){
    						indicesOfValuesLowerThanRadius.add(idx);
    					}
    				}
    			});
    		}else{
    			Parallel.ForEach(seeds, executorService, new Action<Integer>() {
					public void doAction(Integer seed) {
						List<Integer> indicesOfValuesLowerThanRadius = new ArrayList<>();
	    				neighborsByElement.put(seed, indicesOfValuesLowerThanRadius);
	    				int pointsLength = distanceMatrix.getColumnNumber();
	    				for (int idx = 0; idx < pointsLength; idx++) {
	    					int point = idx;
	    					if(distanceMatrix.getValue(seed, point) < bandwidth){
	    						indicesOfValuesLowerThanRadius.add(idx);
	    					}
	    				}
					}
				});
    		}
    	}
		System.out.println("Time to compute neighbors: "+timeWatcher.getTime());
    }

    /**
     * Finds the neighbors within a given radius of a point. Returns indices of the neighbors.
     * The points compared to the point are those from 'points' argument.
     */
    private List<Integer> indicesOfNeighborsWithinRadius(int elementIdx, Matrix distanceMatrix, float radius, List<Integer> points){
        List<Integer> indicesOfValuesLowerThanRadius = new ArrayList<>();
        for(int idx = 0; idx < points.size(); idx++) {
            int point = points.get(idx);
            if(distanceMatrix.getValue(elementIdx, point) < radius){
                indicesOfValuesLowerThanRadius.add(idx);
            }
        }
        return indicesOfValuesLowerThanRadius;
    }

    /**
     * Returns, from points, that one which has the lowest sum of distances to the others.
     * I.e., the point that best centers all of them.
     */
    private int getMeanPoint(Collection<Integer> points, Matrix distanceMatrix) {
        Integer pointWithMinSumDistance = null;
        float minSumDistance = Float.MAX_VALUE;
        for (Integer point : points) {
            float sumDistance = distanceMatrix.getLineSum(point, points);
            if(sumDistance < minSumDistance){
                minSumDistance = sumDistance;
                pointWithMinSumDistance = point;
            }
        }
        return pointWithMinSumDistance;
    }

    public static void main(String[] args) throws Exception {
    	Matrix m;

    	/*
    	int nElements = 3500;
        m = new Matrix(nElements, nElements);
        for (int i = 0; i < nElements; i++) {
            for (int j = i + 1; j < nElements; j++) {
                float v = (float) Math.random();
                m.setValue(i, j, v);
                m.setValue(j, i, v);
            }
        }
        m.save(new File("/home/icaro/parallel-meanshift/arq"+nElements+".in"));
        System.exit(0);
        */

        if (args.length == 0) {
        	System.err.println("Usage:\narg0 is the path for the distance matrix file.\narg1 is optional: 0 for serial, 1 for parallel using java 8, 2 for parallel using custom parallel API");
        	System.exit(1);
        }

        File distanceMatrixFile = new File(args[0]);
        if(args.length > 1){
            OPTION = Integer.parseInt(args[1]);
        }else{
            OPTION = 1;
        }

        m = Matrix.load(distanceMatrixFile);

    	TimeWatcher timeWatcher = new TimeWatcher().start();
    	List<Integer> clusters = new MeanShiftClusterer().mean_shift(m, m.getLineNumber(), 0.5F, 100);
    	System.out.println("total time to run meanshift: " + timeWatcher.getTime());

    	Collections.sort(clusters);
    	System.out.println("Cluster centers:" + clusters);
    }
}
