package DataStandardization;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * input: 
 * 	1. events file in the pipeline format
 *  2. ccs_dx_2015.csv 
 *  
 * output: events file with  icd9_diagnosis codes replaced by ccs codes 
 * 
 * */


public class ICD9ToCCS {
	static PrintWriter writer = null;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		writer = new PrintWriter("/nethome/kmalhotra7/epilepsy_2006_2015/combined_data_v1/v14_pred_modeling_fail_1_hold_out/eventsTest_FeatSet3_diagCCS.csv");
		String eventsFile = "/nethome/kmalhotra7/epilepsy_2006_2015/combined_data_v1/v14_pred_modeling_fail_1_hold_out/eventsTest_FeatSet3.csv";
		BufferedReader brEvents = new BufferedReader(new FileReader(eventsFile));
		
		String ccsDiagFile = "/nethome/kmalhotra7/data-preprocess/data/ccs_dx_2015.csv";
		BufferedReader brCCSDiag = new BufferedReader(new FileReader(ccsDiagFile));
		
		String strLine = "";
		
		
		HashMap<String,String> mapICD9CCS = new HashMap<String, String>();
		
		while((strLine = brCCSDiag.readLine()) != null){
			strLine = strLine.replace("'", "").replaceAll("\\s+", "");
			String [] arr = strLine.split(",");
			
			mapICD9CCS.put(arr[0], arr[1]);
			
			
		}
		
		
		strLine = "";
		String ccs = "";
		
		while((strLine = brEvents.readLine()) != null){
			if(strLine.contains("DIAG")){
				String [] arr = strLine.split(",");
				String [] event = arr[1].split("_");
				String icd9Code = event[1];
				String prefix = event[0];

				if(mapICD9CCS.containsKey(icd9Code)){
					ccs = mapICD9CCS.get(icd9Code);
					arr[1] = prefix+"CCS_"+ccs; //TO ADDDD
				}else if(mapICD9CCS.containsKey(icd9Code+"0")){
					icd9Code = icd9Code+"0";
					ccs = mapICD9CCS.get(icd9Code);
					arr[1] = prefix+"CCS_"+ccs;
				}else if(mapICD9CCS.containsKey(icd9Code+"00")){
					icd9Code = icd9Code+"00";
					ccs = mapICD9CCS.get(icd9Code);
					arr[1] = prefix+"CCS_"+ccs;
				}
				
				else{
					
					ccs = icd9Code;
					arr[1] = prefix+"_"+ccs;
				}
				
				
				for(int i = 0 ; i < arr.length; i++){
					if(i == arr.length - 1){
						writer.println(arr[i]);
					}else{
						writer.print(arr[i]+",");
					}
					
				}
				
			}else{
				writer.println(strLine);
			}
			
		}
		
		
		writer.close();
		
		
	}

}
