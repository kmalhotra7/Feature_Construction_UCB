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

//import phaseII.ConnectToDatabase;

/*
 *INPUT: 
* 1. list of index dates for the patients in the cohort. 
* 2.  UCB_database
 * */
public class Main_FeatSet_2 {
	
	static PrintWriter writer = null;
	
	public static void main(String [] args) throws SQLException, IOException{
		
		//output file
		writer = new PrintWriter("/nethome/kmalhotra7/epilepsy_2006_2015/combined_data_v1/v16_pred_modeling_fail_1_hold_out/eventsTest_FeatSet2.csv");
		
		// Connecting to the ucb database 
		ConnectToDatabase ctd = new ConnectToDatabase();
		Connection conn = ctd.connect();
		
		Statement st = conn.createStatement();
		
		// input file 
		String fileIndexDates = "/nethome/kmalhotra7/epilepsy_2006_2015/combined_data_v1/v16_pred_modeling_fail_1_hold_out/listIndexDates_Test.csv"; // index dates of patients in the event set 
		BufferedReader brIndexDt = new BufferedReader(new FileReader(fileIndexDates));
		
		HashMap<String,String> mapPatIndexDates = new HashMap<String, String>();
		
		String strLine = "";
		
		while((strLine = brIndexDt.readLine()) != null){
			
			String [] arr = strLine.split(",");
			String patID = arr[0];
			String indexDate = arr[1];
			String indexDateStr = indexDate.substring(0,7).replace("-", "");

			System.out.println(patID);
			
			/*
			String query = "Select patient_id, rx_activity, dx_activity, hosp_activity, month_id from new_epilepsy_ptnt_eligibility "
					+ "where patient_id = '"+patID+"' and month_id_date < '"+indexDate+"' and month_id_date >= DATE_ADD('"+indexDate+"', INTERVAL -365 DAY) "
					+ "order by month_id";
			*/
			
			// extracting the rx_activity, dx_activity, hosp_activity from the ptnt_eligibility table  
			
			String query_mod = "Select patient_id, rx_activity, dx_activity, hosp_activity, month_id from new_epilepsy_ptnt_eligibility "
					+ "where patient_id = '"+patID+"' and '"+indexDateStr+"'-month_id <= 100 and '"+indexDateStr+"' - month_id >0 "
					+ "order by month_id";			
			
			
			ResultSet rs = st.executeQuery(query_mod);
			
			int countRx = 0;
			int countdx = 0;
			int countHosp = 0;
			
			while(rs.next()){
				if(rs.getString("rx_activity").equals("Y")){
					countRx++;
				}
				if(rs.getString("dx_activity").equals("Y")){
					countdx++;
				}
				if(rs.getString("hosp_activity").equals("Y")){
					countHosp++;
				}
				
				
			}
			
			writer.println(arr[0]+",RXACTIVITY365,0,"+countRx);
			writer.println(arr[0]+",DXACTIVITY365,0,"+countdx);
			writer.println(arr[0]+",HOSPACTIVITY365,0,"+countHosp);
			
			
			///To get payer of AED which failed
			//Select rx.payer_type, prov.pri_speciality_desc from epilipsy_rx_claims_oby_pt_gen_sdt_dos rx, epilipsy_ref_provider prov where rx.patient_id = 90230067 and rx.service_date < '2011-03-02' and rx.generic_name IN (Select generic_name from epilipsy_aed_reduced where included = 1) and rx.provider_id = prov.provider_id; 
			
			/// to get distinct payers in the last 365 days 
			//Select distinct payer_type from epilipsy_rx_claims_oby_pt_gen_sdt_dos where patient_id = 90230067 and service_date < '2011-03-02' and service_date >= DATE_ADD('2011-03-02', INTERVAL -365 DAY);
			
			
			// getting the payer information in the last 1 year 
			Statement stDataLast365 = conn.createStatement();
			ResultSet rsDataLast365 = stDataLast365.executeQuery("Select distinct payer_type "
					+ "from new_epilepsy_rx_claims_2006_2015_epi_oby_pt_sdt where patient_id = '"+patID+"' and service_date < '"+indexDate+"'"
							+ " and service_date >= DATE_ADD('"+indexDate+"', INTERVAL -365 DAY)");
			
			
			while(rsDataLast365.next()){
				String payerType = rsDataLast365.getString("payer_type");
				writer.println(patID+",PAYER365_"+payerType+",0,1");
				
			}
			
			
			
			// getting the payer information of the AED which failed.
			Statement stDataIndexDrug = conn.createStatement();
			ResultSet rsDataIndexDrug = stDataIndexDrug.executeQuery("Select rx.payer_type, prov.pri_speciality_desc "
					+ "from new_epilepsy_rx_claims_2006_2015_epi_oby_pt_sdt rx, new_epilepsy_ref_provider prov "
					+ "where rx.patient_id = '"+patID+"' and rx.service_date < '"+indexDate+"' "
							+ "and rx.generic_name IN (Select generic_name from epilipsy_aed_reduced where included = 1)"
							+ " and rx.provider_id = prov.provider_id order by rx.service_date desc limit 1");
			
			
			
			while(rsDataIndexDrug.next()){
				String payerType = rsDataIndexDrug.getString("rx.payer_type");
				String specialty = rsDataIndexDrug.getString("prov.pri_speciality_desc");
				
				writer.println(patID+",PAYERFIRSTAED_"+payerType+",0,1");
				
				
				PhysicianSpecialty spec = new PhysicianSpecialty(specialty);
				boolean bIsSpecGen = spec.isSpecialtyGeneral();
				boolean bIsSpecPain = spec.isSpecialtyPain();
				boolean bIsSpecEmer = spec.isSpecialtyEmergency();
				boolean bIsSpecNeuro = spec.isSpecialtyNeuro();
				
				
				if(bIsSpecGen == true){
					writer.println(patID+",PHYSPECGENERAL,0,1");
				}
				
				if(bIsSpecPain == true){
					writer.println(patID+",PHYSPECPAIN,0,1");
				}
				
				if(bIsSpecEmer == true){
					writer.println(patID+",PHYSPECEMER,0,1");
				}
				
				if(bIsSpecNeuro == true){
					writer.println(patID+",PHYSPECNEURO,0,1");
				}
				
				
			}
			
			
			
			//ZIPCODE
			Statement stDemo = conn.createStatement();
			ResultSet rsDemo = stDemo.executeQuery("Select zip3 from new_epilepsy_ref_patient where patient_id = '"+patID+"'");
			
			while(rsDemo.next()){
				String zipCode = rsDemo.getString("zip3");
				String [] digits = zipCode.split("");
				
				writer.println(patID+",zip1_"+digits[0]+",0,1");
				
			}
			
			
			
			// hasSeizureProxy345
			boolean bSeizProx345 = false;
			
			Statement stHospEnc = conn.createStatement();
			ResultSet rsHospEnc = stHospEnc.executeQuery("Select * from "
					+ "new_epilepsy_hosp_encounter "
					+ "where (admit_src = 'ER' or (admit_src = 'NON-ER' and ip_op_flag = 'I')) "
					+ "and (diag1 LIKE '345%' or diag2 LIKE '345%' or diag3 LIKE '345%' or diag4 LIKE '345%' "
					+ "or diag5 LIKE '345%' or diag6 LIKE '345%' or diag7 LIKE '345%' or diag8 LIKE '345%') "
					+ "and from_dt < '"+indexDate+"' and from_dt >= DATE_ADD('"+indexDate+"', INTERVAL -365 DAY) and patient_id = '"+patID+"'");
			
			if(!rsHospEnc.isBeforeFirst()){
				//nodata in hosp encounter 
				// checking in dxClaims
				
				Statement stDx = conn.createStatement();
				ResultSet rsDx = stDx.executeQuery("Select * from "
						+ "new_epilepsy_epi_dx_claims "
						+ "where (diag1 LIKE '345%' or diag2 LIKE '345%' or diag3 LIKE '345%' "
						+ "or diag4 LIKE '345%' or diag5 LIKE '345%' or diag6 LIKE '345%' or diag7 LIKE '345%' or diag8 LIKE '345%') "
						+ "and service_date < '"+indexDate+"' and service_date >= DATE_ADD('"+indexDate+"', INTERVAL -365 DAY) "
						+ "and patient_id = '"+patID+"'");
				
				if(!rsDx.isBeforeFirst()){
					
				}else{
					bSeizProx345 = true;
					writer.println(patID+",SEIZURE345,0,1");
				}
				
				
				
			}else{
				bSeizProx345 = true;
				writer.println(patID+",SEIZURE345,0,1");
			}
			
			
			//hadSeizureProxy345OR78039
			if(bSeizProx345 == false){
				Statement stHospEnc2 = conn.createStatement();
				ResultSet rsHospEnc2 = stHospEnc2.executeQuery("Select * from "
						+ "new_epilepsy_hosp_encounter "
						+ "where (admit_src = 'ER' or (admit_src = 'NON-ER' and ip_op_flag = 'I')) "
						+ "and (diag1 = '78039' "
						+ "or diag2 = '78039' "
						+ "or diag3 = '78039' "
						+ "or diag4 = '78039' "
						+ "or diag5 = '78039' "
						+ "or diag6 = '78039' "
						+ "or diag7 = '78039' "
						+ "or diag8 = '78039') "
						+ "and from_dt < '"+indexDate+"' and from_dt >= DATE_ADD('"+indexDate+"', INTERVAL -365 DAY) and patient_id = '"+patID+"'");
				
				if(!rsHospEnc.isBeforeFirst()){
					//nodata in hosp encounter 
					// checking in dxClaims
					
					Statement stDx = conn.createStatement();
					ResultSet rsDx = stDx.executeQuery("Select * from "
							+ "new_epilepsy_epi_dx_claims "
							+ "where (diag1 = '78039' or diag2 = '78039' or diag3 = '78039' "
							+ "or diag4 = '78039' or diag5 = '78039' or diag6 = '78039' or diag7 = '78039' or diag8 = '78039') "
							+ "and service_date < '"+indexDate+"' and service_date >= DATE_ADD('"+indexDate+"', INTERVAL -365 DAY) "
							+ "and patient_id = '"+patID+"'");
					
					if(!rsDx.isBeforeFirst()){
						
					}else{
						writer.println(patID+",SEIZURE345OR78039,0,1");
					}
					
					
					
				}else{
					
					writer.println(patID+",SEIZURE345OR7809,0,1");
				
				}
			
			} // end of bSeiz == false
			
			
		} // end of strLine of indexDate file 
		
		writer.close();
	} // end of main

	

} // end of class 
