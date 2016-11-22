
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class ExtractIndexDate {
	static PrintWriter writer = null;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		writer = new PrintWriter("/nethome/kmalhotra7/phaseII/data/v10_pred_modeling_fail_1_hold_out/test/listIndexDate_Test.csv");
		
		String fileIndex = "/nethome/kmalhotra7/phaseII/data/listIndexDate.csv";
		BufferedReader brIndex = new BufferedReader(new FileReader(fileIndex));
		
		String fileLabel = "/nethome/kmalhotra7/phaseII/data/v10_pred_modeling_fail_1_hold_out/test/labelTestPatientGeneralPopulation.csv";
		BufferedReader brLabels = new BufferedReader(new FileReader(fileLabel));
		
		String strLine = "";
		HashMap<String,String> mapIndexDates = new HashMap<String, String>();
		
		while((strLine = brIndex.readLine()) != null){
			String [] arr = strLine.split(",");
			mapIndexDates.put(arr[0], arr[1]);
		}
		
		strLine = "";
		while((strLine = brLabels.readLine()) != null){
			String [] arr = strLine.split(",");
			if(mapIndexDates.containsKey(arr[0])){
				writer.println(arr[0]+","+mapIndexDates.get(arr[0]));
			}
		}
		
		
		writer.close();

	}

}
