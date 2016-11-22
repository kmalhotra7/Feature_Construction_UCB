//package FeatureConstruction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;




/*
 *Needs the events file which has the MOLECULE events which is feat_set 1

 * */




//import phaseII.ConnectToDatabase;

public class Main_FeatSet_3 {
	static PrintWriter writer = null;
	
	public static void main(String[] args) throws IOException, SQLException {
		// TODO Auto-generated method stub
		
		//output file 
		writer = new PrintWriter("/nethome/kmalhotra7/epilepsy_2006_2015/combined_data_v1/v16_pred_modeling_fail_1_hold_out/eventsTest_FeatSet3.csv");
		
		// Connect to the UCB database 
		ConnectToDatabase ctd = new ConnectToDatabase();
		Connection conn = ctd.connect();
		
		// reading the USP medication class dictionary 
		String fileUspClass = "/nethome/kmalhotra7/data-preprocess/data/usp_v6_newLine.csv";
		BufferedReader brUSP = new BufferedReader(new FileReader(fileUspClass));
		
		// reading the index dates of patients given by sungtae
		String fileIndexDates = "/nethome/kmalhotra7/epilepsy_2006_2015/combined_data_v1/v16_pred_modeling_fail_1_hold_out/listIndexDates_Test.csv";
		BufferedReader brIndex = new BufferedReader(new FileReader(fileIndexDates));
		
		// reading the events file 
		String eventsRawFile = "/nethome/kmalhotra7/epilepsy_2006_2015/combined_data_v1/v16_pred_modeling_fail_1_hold_out/eventsTest_FeatSet1.csv";
		BufferedReader brRawEvents = new BufferedReader(new FileReader(eventsRawFile));
		
		
		HashMap<String, ArrayList<String>> mapDrugToUSP = constructUSPMap(brUSP);
		
		
		String strLine = "";
		
		Statement st = conn.createStatement();
		
		
		// getting the USP class of AED which failed 
		while((strLine = brIndex.readLine()) != null){
			String [] arr = strLine.split(",");
			String patID = arr[0];
			String indexDate = arr[1];
			String query = "Select generic_name from new_epilepsy_rx_claims_2006_2015_epi_oby_pt_sdt "
					+ "where patient_id = '"+patID+"' and service_date < '"+indexDate+"'"
					+ " and generic_name IN (Select generic_name from epilipsy_aed_reduced where included = 1) order by service_date DESC limit 1"; // there could be more than one AEDs in the result since multiple AEDs may have been prescribed on the same date
			System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			
			while(rs.next()){
				String genericName = rs.getString("generic_name").toLowerCase();
				String [] arrGenericName = genericName.split(" ");
				
				  
				ArrayList<String> list = mapDrugToUSP.get(arrGenericName[0]);
				
				for(String uspClass : list){
					writer.println(patID+",USP_"+uspClass+",0,1");
					
				}
				
			}
			
		}
	

		// getting USP of non AED medications since the events file doesnt consist of previous AED names as MOLECULE_AED. This is a separate method so that if there is a requirement in the future about excluding the information 
		// of the AED that failed then based on the events file there is no way to distinguish between an AED and non AED based on prefix. In such a case, you can comment the code block above this. 
		strLine = "";
		while((strLine = brRawEvents.readLine()) != null){
			//System.out.println(strLine);
			String [] arr = strLine.split(",");
			ArrayList<String> uspClassList = new ArrayList<String>();
			
			if(arr[1].contains("MOLECULE_")){
				String [] medArr = arr[1].split("_");
				String medNamePrefix = medArr[1].toLowerCase();
				medNamePrefix = medNamePrefix.replaceAll("[-+.^:,]"," "); // removing special characters in the prefix. 
				String [] medNamePrefixArr = medNamePrefix.split(" "); // just in case the prefix has multiple keywords 
				
				try{
					uspClassList = mapDrugToUSP.get(medNamePrefixArr[0]);
					createModifiedEvent(arr, uspClassList);
				}catch (NullPointerException e){
					continue;
				}
				
				//arr[1] = "USP_"+uspClass;
			}else{
				writer.println(strLine);
			}
			
			
			
		}
		
		writer.close();
		
	}
	
	private static void createModifiedEvent(String[] arr, ArrayList<String> uspClassList) {
		// TODO Auto-generated method stub
		
		
		for(String uspClass : uspClassList){
			arr[1] = "USP_"+uspClass;
			String event ="";
			for(int i = 0; i < arr.length; i++){
				if(i == arr.length-1){
					event+= arr[i];
				}else{
					event+= arr[i]+",";
				}
			}
			
			writer.println(event);
		
		}
	
	}
	
	// creating the USP hashmap 
	private static HashMap<String, ArrayList<String>> constructUSPMap(BufferedReader brUSP) throws IOException {
		// TODO Auto-generated method stub
		String strLine = "";
		
		int countLine = 0;
		HashMap<String, ArrayList<String>> mapDrugToUSP = new HashMap<String, ArrayList<String>>();
		//String uspClass = "/Users/kmalhotra7/Documents/ComputerScience/UCB/data-preprocess/usp_v6.csv";
		String uspClass = "";
		
		while((strLine = brUSP.readLine()) != null){
			
			
			if(countLine != 0){
				String[] arr = strLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				
				if(!arr[1].equals("")){
					arr[1] = arr[1].replace("\"", "").replace(",", "");
					uspClass = arr[1].trim();
				}else{
					if(arr[2].contains("/")){
						String [] arrMed = arr[2].split("/");
						for(String med: arrMed){
							
							String formattedMed = med.trim().toLowerCase();
							
							mapDrugToUSP = addToMap(formattedMed, mapDrugToUSP, uspClass);
						}
					}else{
						String med = arr[2].trim().toLowerCase();
						
						mapDrugToUSP = addToMap(med, mapDrugToUSP, uspClass);
						//mapDrugToUSP.put(med, uspClass);
					}
				
					
				}
			}
			countLine++;
		}
		
		
		
			
		
		
		
		return mapDrugToUSP;
	}

	private static HashMap<String, ArrayList<String>> addToMap(String med,
			HashMap<String, ArrayList<String>> mapDrugToUSP, String uspClass) {
		// TODO Auto-generated method stub
		
		if(mapDrugToUSP.containsKey(med)){
			ArrayList<String> list = mapDrugToUSP.get(med);
			if(!list.contains(uspClass)){
				list.add(uspClass);
			}
			mapDrugToUSP.put(med, list);
		}else{
			ArrayList<String> list = new ArrayList<String>();
			list.add(uspClass);
			mapDrugToUSP.put(med, list);
		}
		
		return mapDrugToUSP;
	}

}
