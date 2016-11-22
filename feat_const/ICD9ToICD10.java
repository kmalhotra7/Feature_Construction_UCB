package DataStandardization;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

//creates csv ICD-10code,ICD-9 mapping. 
public class ICD9ToICD10 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// 2 ICD -9
		//4 ICD-10
		PrintWriter writer = null;
		writer = new PrintWriter("/nethome/kmalhotra7/data-preprocess/data/icd9_to_icd10_dict.csv");
		
		String fileMerged  = "/Users/kmalhotra7/Desktop/merged_test.csv"; //"/nethome/kmalhotra7/data-preprocess/data/MERGED.csv";
		 
		BufferedReader br = new BufferedReader(new FileReader(fileMerged));
		
		String strLine = "";
		int cntLine = 0;
		HashMap<String, Integer> mapCodesVisited = new HashMap<String, Integer>();
		
		writer.println("ICD9_Code,ICD10_Code");
		
		while((strLine = br.readLine()) != null){
			
			if(cntLine!=0){
				String[] arr = strLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				if(arr.length >= 5){ // to ignore lines where ICD9 code doesnt have a corresponding ICD-10
					String ICD9 = arr[2].replace(".", "");
					String ICD10 = arr[4].replace(".", "");
					if(!ICD10.isEmpty() && !ICD9.isEmpty()){
						if(!mapCodesVisited.containsKey(ICD10)){
							writer.println(ICD9+","+ICD10);
							
							mapCodesVisited.put(ICD10, 1);
						}
						
					}
					
				}
			}
		cntLine++;
		}
		
		writer.close();
	}
}
