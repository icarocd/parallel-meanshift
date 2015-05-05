
public class FixedMatrix extends Matrix {

    private final float[][] values;

	public FixedMatrix(float[][] content) {
        values = content;
    }

	public FixedMatrix(int lines, int columns) {
		this(new float[lines][columns]);
    }

	@Override
	public int getLineNumber() {
		return values.length;
	}

	@Override
	public int getColumnNumber() {
		return values[0].length;
	}

	public float getValue(int i, int j) {
		return values[i][j];
	}

	@Override
	public void setValue(int i, int j, float value) {
		values[i][j] = value;
    }

	@Override
	protected float[] getValues(int i) {
		return values[i];
	}
}
