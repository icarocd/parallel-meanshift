import java.io.File;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Matrix {

	public abstract int getLineNumber();

	public abstract int getColumnNumber();

	public abstract float getValue(int i, int j);

	/**
	 * The values from line i, starting from 0.
	 * The array SHOULD NOT BE CHANGED, otherwise it may affect the matrix.
	 * Default implementation returns a new array containing copies, although subclasses
	 * may freely override it to optime the function.
	 */
	protected abstract float[] getValues(int i);

	protected List<Float> copyLine(int i){
		int columns = getColumnNumber();
		List<Float> line = new ArrayList<>(columns);
		for (int j = 0; j < columns; j++) {
			line.add(getValue(i, j));
		}
		return line;
	}

	public abstract void setValue(int i, int j, float value);

	/**
	 * Get a submatrix.
	 * @param i0 Initial row index
	 * @param i1 Final row index
	 * @param j0 Initial column index
	 * @param j1 Final column index
	 */
	public Matrix submatrix(int i0, int i1, int j0, int j1) {
		Matrix X = new FixedMatrix(i1 - i0 + 1, j1 - j0 + 1);

		for (int i = i0; i <= i1; i++) {
			for (int j = j0; j <= j1; j++) {
				X.setValue(i - i0, j - j0, getValue(i, j));
			}
		}
		return X;
	}

	/** For line i, returns the sum of its values */
    public float getLineSum(int i) {
        float sum = 0;
        for (int j = 0; j < getColumnNumber(); j++) {
            sum += getValue(i, j);
        }
        return sum;
    }

	/** For line i, and for each j indicated, returns the sum of all these cell values */
	public float getLineSum(int i, Collection<Integer> js) {
		float sum = 0;
		for (int j : js) {
			sum += getValue(i, j);
		}
		return sum;
	}

	/**
	 * @return the sum for each line.
	 */
    public float[] getLineSums() {
        float[] sumC = new float[getLineNumber()];
        for (int i = 0; i < getLineNumber(); i++) {
            for (int j = 0; j < getColumnNumber(); j++) {
                sumC[i] += getValue(i, j);
            }
        }
        return sumC;
    }

	/**
	 * Retorna o argmin por row: cada celula do array retornado corresponde a uma linha,
	 * e a celula da linha contem o indice da coluna da linha com o menor valor na linha.
	 */
	public int[] getArgminByLine(){
	    int lines = getLineNumber();

	    int[] result = new int[lines]; //cada indice contem, por linha, o indice da coluna de menor valor

	    // search for minimun
	    for(int i = 0; i < lines; i++){
	        result[i] = getArgminInLine(i);
	    }

	    return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getArgminInLine(final int lineIndex) {
		int argmin = -1;
		Comparable min = null;
		for (int j = 0; j < getColumnNumber(); j++) {
			Comparable value = getValue(lineIndex, j);
			if(min == null || value.compareTo(min) < 0){
				min = value;
				argmin = j;
			}
		}
		return argmin;
	}

	public void divideLineValues(int i, float divider) {
	    for (int j = 0; j < getColumnNumber(); j++) {
            setValue(i, j, getValue(i, j) / divider);
        }
	}

	/** Retrieves the Kth lowest value from line i, where both i and k starts in 1. */
	public float getKthLowestValueInLine(int i, int k) {
		List<Float> line = copyLine(i);
		return DataStructureUtils.kthLowest(line, k);
	}

	/** Releases any kind of open resource, such as files etc */
	public void destroyResources(){}

	public void save(File file) {
		save(file, false, getLineNumber());
	}

	protected void save(File file, boolean startColumnIdxAfterLineIdx, int lines) {
		try (PrintStream stream = FileUtils.createPrintStreamToFile(file)) {
	    	DecimalFormat format = FileUtils.getDecimalFormatter();

	        for (int i = 0; i < lines; i++) {
	        	if (i > 0) {
	        		stream.println();
	        	}
	            final int startJ = startColumnIdxAfterLineIdx ? i + 1 : 0;
	            for (int j = startJ; j < getColumnNumber(); j++) {
	                if (j > startJ) {
	                    stream.print(' ');
	                }
	                float value = getValue(i, j);
					stream.print(format.format(value));
	            }
	        }
		}
	}
}
