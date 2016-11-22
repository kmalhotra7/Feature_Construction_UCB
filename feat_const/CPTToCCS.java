package DataStandardization;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class CPTToCCS {
	static PrintWriter writer  = null;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		writer = new PrintWriter("/Users/kmalhotra7/Documents/ComputerScience/UCB/testData/phaseII/eventsFile_proccss.csv");
		
		String fileCPT = "/Users/kmalhotra7/Documents/ComputerScience/UCB/data-preprocess/data/2016_ccs_services_procedures.csv";
		BufferedReader br = new BufferedReader(new FileReader(fileCPT));
		
		String fileICD9ProcCCS = "/Users/kmalhotra7/Documents/ComputerScience/UCB/data-preprocess/data/ccs_pr_2015.csv";
		BufferedReader brICD9ProcCCS = new BufferedReader(new FileReader(fileICD9ProcCCS));
		
		
		String eventsFile = "/Users/kmalhotra7/Documents/ComputerScience/UCB/testData/phaseII/eventsFile_diagccs.csv";
		BufferedReader brEvents = new BufferedReader(new FileReader(eventsFile));
		
		String strLine = "";
		int countLine = 0;
		
		HashMap<String,String> mapCPTCCS = new HashMap<String, String>();
		
		while((strLine = br.readLine()) != null){
			if(countLine == 0){
				// do nothing 
			}else{
				
				strLine = strLine.replace("'", "");
				String[] arr = strLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				String [] icd9CodeRange = arr[0].split("-");
				
				if(arr[0].matches("^[A-Z].*$")){ // if the code starts with letter 
				
					
					mapCPTCCS.put(icd9CodeRange[0], arr[1]);
					
				
				}else if(arr[0].matches("(.*)[A-Z]")){ // if the code ends with a letter 
					
					if(!icd9CodeRange[0].equals(icd9CodeRange[1])){
						openRange(icd9CodeRange, arr[1], mapCPTCCS, 1);
					}else{
						mapCPTCCS.put(icd9CodeRange[0], arr[1]);
					}
				}else{
					
					openRange(icd9CodeRange, arr[1], mapCPTCCS, 0);
					
				}
				
			} // else of countLine 
			
			countLine++;
		}
		
		
		//adding the icd9ProcCodeMapping 
		strLine = "";
		countLine = 0;
		while((strLine = brICD9ProcCCS.readLine()) != null){
			if(countLine == 0){
				
			}else
			{
				strLine = strLine.replace("'", "");
				
				String[] arr = strLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				String icd9ProcCode = arr[0].trim();
				String ccsCode = arr[1].trim();
				
				
				mapCPTCCS.put(icd9ProcCode, ccsCode);
				
			}
			countLine++;
		}
		
		strLine = "";
		String ccs = "";
		
		while((strLine = brEvents.readLine()) != null){
			if(strLine.contains("PROC")){
				System.out.println(strLine);
				String [] arr = strLine.split(",");
				String [] event = arr[1].split("_");
				if(event.length == 2){
					String cptCode = event[1];
					String prefix = event[0];
					
					if(mapCPTCCS.containsKey(cptCode)){
						ccs = mapCPTCCS.get(cptCode);
						arr[1] = prefix+"CCS_"+ccs;
					}else{
						
						arr[1] = prefix+"_"+cptCode;
					}
					
					for(int i = 0 ; i < arr.length; i++){
						if(i == arr.length - 1){
							writer.println(arr[i]);
						}else{
							writer.print(arr[i]+",");
						}
						
					}
				}else{
					writer.println(strLine); // writing boolean proc features e.g HASPROC365 
				}
				
				
			}else{
				writer.println(strLine); // writing other features
			}
			
			
		}
		
		writer.close();
		
	}

	private static void openRange(String[] icd9CodeRange, String ccs,
			HashMap<String, String> mapCPTCCS, int flag) {
		// TODO Auto-generated method stub
		
		if(flag == 1){
			
			String suffix =  icd9CodeRange[0].substring(4, 5);
			int beginCode = Integer.parseInt(icd9CodeRange[0].substring(0, 4));
			int endCode = Integer.parseInt(icd9CodeRange[1].substring(0, 4));
		
			for(int code = beginCode; code <= endCode; code++){
				String codeAsStr = String.format("%04d", code); // adding leading zeros before the code 
				mapCPTCCS.put(codeAsStr+suffix, ccs);
			}
		}else{
			
			int beginCode = Integer.parseInt(icd9CodeRange[0]);
			int endCode = Integer.parseInt(icd9CodeRange[1]);
			
			for(int code = beginCode; code <= endCode; code++){
				String codeAsStr = String.format("%05d", code); // adding leading zeros before the code. 
				mapCPTCCS.put(codeAsStr, ccs);
			}
			
			
		}
	
		//String numberAsStr = String.format("%04d", number);
		//System.out.println(numberAsStr);
		
		
		
		
	}

}
