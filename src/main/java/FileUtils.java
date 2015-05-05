import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FileUtils {

	public static PrintStream createPrintStreamToFile(File file) {
        try {
            return new PrintStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

	public static DecimalFormat getDecimalFormatter() {
		return getDecimalFormatter(null);
	}

	public static DecimalFormat getDecimalFormatter(Integer maxDecimalDigits) {
		DecimalFormat format = new DecimalFormat();
        format.setGroupingUsed(false);
        format.setDecimalSeparatorAlwaysShown(false);
        if(maxDecimalDigits != null){
        	format.setMaximumFractionDigits(maxDecimalDigits);
        }else{
        	format.setMaximumFractionDigits(6);
        }
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setDecimalSeparator('.');
        format.setDecimalFormatSymbols(formatSymbols);
        return format;
	}
}
