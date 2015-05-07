import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Matrix {

    private final float[][] values;

    public Matrix(int lines, int columns) {
        this(new float[lines][columns]);
    }

    public Matrix(float[][] content) {
        values = content;
    }

    public int getLineNumber() {
        return values.length;
    }

    public int getColumnNumber() {
        return values[0].length;
    }

    public float getValue(int i, int j) {
        return values[i][j];
    }

    public void setValue(int i, int j, float value) {
        values[i][j] = value;
    }

    public float[] getValues(int i) {
        return values[i];
    }

    protected List<Float> copyLine(int i){
        int columns = getColumnNumber();
        List<Float> line = new ArrayList<>(columns);
        for (int j = 0; j < columns; j++) {
            line.add(getValue(i, j));
        }
        return line;
    }

    /**
     * Get a submatrix.
     * @param i0 Initial row index
     * @param i1 Final row index
     * @param j0 Initial column index
     * @param j1 Final column index
     */
    public Matrix submatrix(int i0, int i1, int j0, int j1) {
        Matrix X = new Matrix(i1 - i0 + 1, j1 - j0 + 1);

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

    public int getArgminInLine(final int lineIndex) {
        int argmin = -1;
        float min = Float.MAX_VALUE;
        for (int j = 0; j < getColumnNumber(); j++) {
            float value = getValue(lineIndex, j);
            if(value < min){
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
                        stream.print(',');
                    }
                    float value = getValue(i, j);
                    stream.print(format.format(value));
                }
            }
        }
    }

    public static Matrix load(File file) throws IOException, ParseException {
        DecimalFormat format = FileUtils.getDecimalFormatter();

        List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        int nLines = lines.size();
        Matrix m = null;
        for (int i = 0; i < nLines; i++) {
            String[] linePieces = lines.get(i).split(",");
            if(i == 0){
                m = new Matrix(nLines, linePieces.length);
            }
            for (int j = 0; j < linePieces.length; j++) {
                float v = format.parse(linePieces[j]).floatValue();
                m.setValue(i, j, v);
            }
        }
        return m;
    }
}
