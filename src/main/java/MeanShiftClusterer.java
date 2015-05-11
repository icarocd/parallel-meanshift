import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Perform MeanShift Clustering of data using a flat kernel.
 * Returns indices of samples from input matrix, where each index relates to a sample that was considered a cluster.
 */
public class MeanShiftClusterer {

	/**
	 * @param distanceMatrix
	 * @param maxSeeds        the maximum number of seeds. Use -1 to use all samples as seeds.
	 * @param quantile        should be between [0, 1]. 0.5 means that the median of all pairwise distances is used. In doubt, use around 0.3.
	 * @param maxIterations   the maximum number of iteration steps used by seed convergence. In doubt, use 300.
	 * @return
	 */
	public List<Integer> mean_shift(Matrix distanceMatrix, int maxSeeds, final float quantile, final int maxIterations) {
	    List<Integer> seeds = MathUtils.range(distanceMatrix.getLineNumber());
        if (maxSeeds > 0 && maxSeeds < seeds.size()) {
            DataStructureUtils.reduceRandomly(seeds, maxSeeds);
            System.out.println(seeds.size()+" seeds to be used, from " + distanceMatrix.getLineNumber());
        }

        return mean_shift(distanceMatrix, seeds, quantile, maxIterations);
	}

	/**
	 *
	 * @param distanceMatrix
	 * @param seeds
	 * @param quantile
	 * @param maxIterations
	 * @return
	 */
	public List<Integer> mean_shift(Matrix distanceMatrix, List<Integer> seeds, final float quantile, final int maxIterations) {
	    final float bandwidth = estimateBandwidth(distanceMatrix, quantile);

		Map<Integer, Integer> intensityByCenter = new HashMap<>();
		{
		    double stop_thresh = 1e-3 * bandwidth; // when mean has converged

		    // For each seed, climb gradient until convergence or max_iterations
		    for (int i = 0; i < seeds.size(); i++) {
		        int seed = seeds.get(i);
		        if(i % 20 == 0){
		        	System.out.println("[MeanShiftClusterer] Running... Now on seed_index " + i + "(up to " + seeds.size() + "-1). Current seed:" + seed);
		        }
		        converge(distanceMatrix, maxIterations, bandwidth, stop_thresh, intensityByCenter, seed);
		    }
		}

	    // POST PROCESSING: remove near duplicate points
		// If the distance between two kernels is less than the bandwidth,
		// then we have to remove one because it is a duplicate: remove the one with fewer points.
	    TimeWatcher timeWatcher = new TimeWatcher().start();
	    List<Integer> sortedCenters = DataStructureUtils.getMapKeysSortedByValueAsList(intensityByCenter, false, -1);
	    intensityByCenter = null;
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
		System.out.println("post processing finished after "+timeWatcher.getTimeInMiliSecs()+" milisecs");

        return centers;
	}

    private void converge(Matrix distanceMatrix, final int max_iterations, final float bandwidth, double stop_thresh,
        Map<Integer, Integer> intensityByCenter, int seed)
    {
        int completed_iterations = 0;
        while(true){
        	// Find mean of points within bandwidth
        	List<Integer> pointsWithinRadius = indicesOfNeighborsWithinRadius(seed, distanceMatrix, bandwidth, null);
        	if(pointsWithinRadius.isEmpty()){
        		break; // Depending on seeding strategy this condition may occur
        	}

        	/*if((float)pointsWithinRadius.size() / distanceMatrix.getLineNumber() >= 0.6){
        	    Logs.warn("For seed "+seed+" iteration "+completed_iterations+", points within seed radius returned many elements: "+pointsWithinRadius.size()+" of "+distanceMatrix.getLineNumber()+". Please check your bandwith, it might be too high");
        	}*/

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

		float sumDistanceKNearestNeighbor = 0;

		for (int i = 0; i < numLines; i++) {
			float kNearestNeighborDistance = distanceMatrix.getKthLowestValueInLine(i, knn);
			sumDistanceKNearestNeighbor += kNearestNeighborDistance;
		}

		System.out.println("estimateBandwidth finished after "+timeWatcher.getTimeInMiliSecs()+" milisecs");

		return sumDistanceKNearestNeighbor / numLines;
	}

	/**
	 * Finds the neighbors within a given radius of a point. Returns indices of the neighbors.
	 * The points compared to the point are either those from 'points' argument if informed, otherwise all possible points.
	 */
	private List<Integer> indicesOfNeighborsWithinRadius(int elementIdx, Matrix distanceMatrix, float radius, List<Integer> points){
		List<Integer> indicesOfValuesLowerThanRadius = new ArrayList<>();

		if(points == null){
			int pointsLength = distanceMatrix.getColumnNumber();
			for (int idx = 0; idx < pointsLength; idx++) {
				int point = idx;
				if(distanceMatrix.getValue(elementIdx, point) < radius){
					indicesOfValuesLowerThanRadius.add(idx);
				}
			}
		}else{
			for(int idx = 0; idx < points.size(); idx++) {
				int point = points.get(idx);
				if(distanceMatrix.getValue(elementIdx, point) < radius){
					indicesOfValuesLowerThanRadius.add(idx);
				}
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
//	    int nElements = 1500;
//        Matrix m = new Matrix(nElements, nElements);
//        for (int i = 0; i < nElements; i++) {
//            for (int j = i + 1; j < nElements; j++) {
//                float v = (float) Math.random();
//                m.setValue(i, j, v);
//                m.setValue(j, i, v);
//            }
//        }
//        m.save(new File("/home/icaro/arq5000.in"));
//        System.exit(0);

        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: arg0 must be the path to a distance matrix file.");
        }

	    Matrix m = Matrix.load(new File(args[0]));

	    TimeWatcher timeWatcher = new TimeWatcher().start();
	    List<Integer> clusters = new MeanShiftClusterer().mean_shift(m, m.getLineNumber(), 0.5F, 100);
	    System.out.println("Time to run meanshift, in milisecs: " + timeWatcher.getTimeInMiliSecs());

	    System.out.println("Cluster centers:" + clusters);
    }
}
