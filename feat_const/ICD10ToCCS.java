package DataStandardization;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class ICD10ToCCS {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		PrintWriter writer = null;
		
		writer = new PrintWriter("/nethome/kmalhotra7/epilepsy_2006_2015/combined_data_v1/v14_pred_modeling_fail_1_hold_out/eventsCaseControl_FeatSet_1_2_3_diagICD10CCS.csv");
		String eventsFile = "/nethome/kmalhotra7/epilepsy_2006_2015/combined_data_v1/v14_pred_modeling_fail_1_hold_out/eventsCaseControl_FeatSet_1_2_3.csv";
		BufferedReader brEvents = new BufferedReader(new FileReader(eventsFile));
		
		String ccsDiagFile = "/nethome/kmalhotra7/data-preprocess/data/mapDiagCCS_icd10.csv";
		BufferedReader brCCSDiag = new BufferedReader(new FileReader(ccsDiagFile));
		
		String strLine = "";
		
		
		HashMap<String,String> mapICD10CCS = new HashMap<String, String>();
		
		while((strLine = brCCSDiag.readLine()) != null){
			
			String [] arr = strLine.split(",");
			
			mapICD10CCS.put(arr[0].trim(), arr[1].trim());
			
			
		}
		
		strLine = "";
		String ccs = "";
		String prefix = "";
		
		while((strLine = brEvents.readLine()) != null){
		
			String [] arr = strLine.split(",");
			if(arr[1].contains("DIAG_") || arr[1].contains("DIAG365_")){
				String [] event = arr[1].split("_");
		
				String code = event[1];
				
				if(mapICD10CCS.containsKey(code)){
					ccs = mapICD10CCS.get(code);
					prefix = event[0]+"CCS";
				}else{
					ccs = code;
					prefix = event[0];
				}
				
				System.out.println(arr[0]+","+prefix+"_"+ccs+","+arr[2]+","+arr[3]);
				writer.println(arr[0]+","+prefix+"_"+ccs+","+arr[2]+","+arr[3]);
			}else{
				System.out.println(strLine);
				writer.println(strLine);
			}
			
	
		}
		
		writer.close();
	}

}
