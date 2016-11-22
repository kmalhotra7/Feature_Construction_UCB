//package FeatureConstruction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
/*
 * Dependent Classes: Age, Diagnosis, Comorbidity, Procedures, Features. 
 * 
 * Assumptions: The events file needs to be ordered by patient_id. 
 * */
public class Main_FeatSet_1 {
	
	static PrintWriter writer = null;
	static HashMap<Integer, String> mapIDToComorbidity = new HashMap<Integer, String>();
	static ArrayList<String> diagCodes = new ArrayList<String>();
	static HashMap<String, String> mapDIAGCCS = new HashMap<String, String>();
	static ArrayList<String> diagCodes365 = new ArrayList<String>();
	
	
	public static void main(String[] args) throws IOException {
		
		// output file 
		writer = new PrintWriter("/nethome/kmalhotra7/epilepsy_2006_2015/combined_data_v1/v15_pred_modeling_fail_1_hold_out/eventsTest_FeatSet1.csv");
		// TODO Auto-generated method stub
		
		// input file
		String fileEvents = "/nethome/kmalhotra7/epilepsy_2006_2015/combined_data_v1/v15_pred_modeling_fail_1_hold_out/eventsTestConvertedToICD9.csv";
		BufferedReader brEvents = new BufferedReader(new FileReader(fileEvents));
		
		
		String strLine = "";
		String prevPatID = "";
		String currPatID = "";
		ArrayList<Integer> comorbidities = new ArrayList<Integer>();
		int countLine = 0;
		
		//hashmap of comorbidities
		mapIDToComorbidity.put(1, "SERMENTILL");
		mapIDToComorbidity.put(2, "SUBSTABUSE");
		mapIDToComorbidity.put(3, "AFFECDISOR");
		mapIDToComorbidity.put(4, "OTHRMENT");
		mapIDToComorbidity.put(5, "AUTOIMMUNE");
		mapIDToComorbidity.put(6, "CARDIO");
		mapIDToComorbidity.put(7, "LIVER");
		mapIDToComorbidity.put(8, "DIAB");
		mapIDToComorbidity.put(9, "OSTEO");
		mapIDToComorbidity.put(10, "OBESITY");
		mapIDToComorbidity.put(11, "SLEEP");
		mapIDToComorbidity.put(12, "MENTRETRD");
		mapIDToComorbidity.put(13, "RENALINSUFF");
		mapIDToComorbidity.put(14, "POLYPHYRINMETABOLISM");
		mapIDToComorbidity.put(15, "MITOCHONDRIALMETABOLISM");
		mapIDToComorbidity.put(16, "HIV");
		mapIDToComorbidity.put(17, "MIGRAINE");
		mapIDToComorbidity.put(18, "CALCOFKIDNEY");
		mapIDToComorbidity.put(19,  "DEPRESSIVEDISORDERS");
		mapIDToComorbidity.put(20, "PSYCHOSIS");
		
		//constructing diag to ccs mapping 
		constructCCSMap();
		
		
		while((strLine = brEvents.readLine()) != null){
			String [] arr = strLine.split(",");
			
			currPatID  = arr[0];
			
			//if same patient
			if(currPatID.equals(prevPatID)){
				
				comorbidities = constructFeat(strLine, arr, comorbidities, currPatID);
				
			} else{
				//PRINT COMORBIDITY FEATS
				if(countLine != 0){
					
					//charleson score 
					CharlesonScore cci = new CharlesonScore(diagCodes);
					cci.getCharlesonFeatures();
					
					ArrayList<String> charlsonFeatures = cci.features;
					int charlesonScore = cci.score;
					
					// writing all the charleson score associated comorbidities 
					for(String feat: charlsonFeatures){
						writer.println(prevPatID+","+feat+",0,1");
					}
					
					// writing the actual Charleson Score 
					writer.println(prevPatID+",CCISCORE,0,"+charlesonScore);
					
					// epilepsy_score
					EpilepsyCoScore epScore = new EpilepsyCoScore(diagCodes);
					int epilepsyScore = epScore.getScore();
					
					ArrayList<String> epilepsyFeatures = epScore.getEpComorbFeatures();
					
					// writing all the epilepsy comorbidity score related features 
					for(String feat: epilepsyFeatures){
						writer.println(prevPatID+","+feat+",0,1");
					}
					
					// writing the actual epilepsy comorbidity score 
					writer.println(prevPatID+",EPSCORE,0,"+epilepsyScore);
					
					//rest of the comorbidities
					
					printComorbidityEvents(comorbidities, prevPatID);
				
				}
				
				//reinitializing for the next patient 
				diagCodes365 = new ArrayList<String>();
				diagCodes = new ArrayList<String>();
				prevPatID = currPatID;
				comorbidities = new ArrayList<Integer>();
				
				//capturing the 1st row for the next patient.
				
				comorbidities = constructFeat(strLine, arr, comorbidities, currPatID);
				
			}
			
			countLine++;
		} // end of while 
		
		
		// for the last patient 
		CharlesonScore cci = new CharlesonScore(diagCodes);
		cci.getCharlesonFeatures();
		
		ArrayList<String> charlsonFeatures = cci.features;
		int charlesonScore = cci.score;
		
		
		for(String feat: charlsonFeatures){
			System.out.println(feat);
			writer.println(prevPatID+","+feat+",0,1");
		}
		
		writer.println(prevPatID+",CCISCORE,0,"+charlesonScore);

		
		EpilepsyCoScore epScore = new EpilepsyCoScore(diagCodes);
		int epilepsyScore = epScore.getScore();
		
		ArrayList<String> epilepsyFeatures = epScore.getEpComorbFeatures();
		
		for(String feat: epilepsyFeatures){
			System.out.println(feat);
			writer.println(prevPatID+","+feat+",0,1");
		}
		
		writer.println(prevPatID+",EPSCORE,0,"+epilepsyScore);
		
		
		printComorbidityEvents(comorbidities, prevPatID);
		
		
		
		writer.close();
	}
	
	

	

	/*
	 * Constructing the DIAG to CCS map
	 * */
	private static void constructCCSMap() throws IOException {
		// TODO Auto-generated method stub
		String fileCCSDX = "/nethome/kmalhotra7/data-preprocess/data/ccs_dx_2015.csv";
		BufferedReader brCCSDx = new BufferedReader(new FileReader(fileCCSDX));
		
		String strLine = "";
		int cntLine = 0;
		
		while((strLine = brCCSDx.readLine()) != null){
			if(cntLine != 0){
				strLine = strLine.replace("'", "").replaceAll("\\s+", "");
				String [] arr = strLine.split(",");
				mapDIAGCCS.put(arr[0], arr[1]);	
			}
		cntLine++;
		}
		
		
	}



	private static void printComorbidityEvents(
			ArrayList<Integer> comorbidities, String patID) {
		// TODO Auto-generated method stub
		
		//printing comorbidities. 
		for(int comorbidity : comorbidities){
			writer.println(patID+","+mapIDToComorbidity.get(comorbidity)+",0,1");
		}
		
		
		//checking for neurocomorbidities in CCS 
		boolean bNeuro = checkNeuroComorbidity();
		if(bNeuro == true){
			writer.println(patID+",NEUROCMD,0,1");
		}
		
	}
	
	// checking for neurological comorbidities. It looks for certain specific CCS codes 
	private static boolean checkNeuroComorbidity() {
		// TODO Auto-generated method stub
		boolean b = false;
		
		for(String code : diagCodes365){
			
			if(mapDIAGCCS.containsKey(code)){
				
				code = code;
				b = getStatusOfNeuroComorbidity(code);
				
			}else if(mapDIAGCCS.containsKey(code+"0")){
				code = code+"0";
				b = getStatusOfNeuroComorbidity(code);
				
			}else if(mapDIAGCCS.containsKey(code+"00")){
				code = code+"00";
				b = getStatusOfNeuroComorbidity(code);
				
			}else{
				// code not present
			}
						
			if(b == true){
				break;
			}
			
			
		}
	
		return b;
	}

	private static boolean getStatusOfNeuroComorbidity(String code){
		boolean b = false;
		if(mapDIAGCCS.get(code).equals("109") // checking for Acute Cerebrovascular
				|| mapDIAGCCS.get(code).equals("55") // electrolyte
				|| mapDIAGCCS.get(code).equals("245") // syncope
				|| mapDIAGCCS.get(code).equals("78") 
				|| mapDIAGCCS.get(code).equals("95") 
				|| mapDIAGCCS.get(code).equals("84") 
				|| mapDIAGCCS.get(code).equals("79") 
				|| mapDIAGCCS.get(code).equals("81")
				|| mapDIAGCCS.get(code).equals("82")
				|| mapDIAGCCS.get(code).equals("85")
				|| mapDIAGCCS.get(code).equals("111")
				|| mapDIAGCCS.get(code).equals("112")
				|| mapDIAGCCS.get(code).equals("113")) { 
			b = true;
		}
		return b;
	}




	// method to construct features 
	private static ArrayList<Integer> constructFeat(String strLine, String [] arr, ArrayList<Integer> comorbidities, String patID){

		if(arr[1].equals("AGE")){
			
			
			// binning the AGE feature
			String bin = constructAge(Double.parseDouble(arr[3]));
			//arr[3] = bin;
			arr[1] = arr[1]+"_"+bin;
			arr[3] = "1";
			displayData(arr);
			
			
		// for events consisting of diagnosis
		}else if(strLine.contains("DIAG_")){
		
		
		
			String [] codeStruct = arr[1].split("_");
			Diagnosis diag = new Diagnosis(codeStruct[1], Integer.parseInt(arr[2]));
			
			//storing all the diag codes in the list for score calculation 
			diagCodes.add(codeStruct[1]);
			
			// checking if the diagnosis event took place in the one year period before index date. 
			boolean bDiagInOneYear = diag.isPresentInOneYear();
			
			if(bDiagInOneYear == true){
				
				// replacing 'DIAG' prefix with 'DIAG365'
				String newDiagFeat = strLine.replace("DIAG_", "DIAG365_"); 
				writer.println(newDiagFeat);
			
				
				diagCodes365.add(codeStruct[1]);
				
				////// COMORBIDITIES ///////
				Comorbidity cmd = new Comorbidity(codeStruct[1]);
				ArrayList<Boolean> listOfBooleans = new ArrayList<Boolean>();
				
				// checking for MENTAL ILLNESS
				boolean bSerMentIll = cmd.hasSerMentalIllnes();
				boolean bSubsAbuse = cmd.hasSubstanceAbuse();
				boolean bAffectDisorder = cmd.hasAffectiveDisorders();
				boolean bOtherMental = false;
				
				listOfBooleans.add(bSerMentIll);
				listOfBooleans.add(bSubsAbuse);
				listOfBooleans.add(bAffectDisorder);
				
				if(!listOfBooleans.contains(true)){
					bOtherMental = cmd.hasOtherMentalDisorder();
				}
				
				if(bSerMentIll == true){ // 
					if(!comorbidities.contains(1)){
						comorbidities.add(1); // adding identifiers for each comorbidity. These numbers corresponding to the keys of mapIDToComorbidity hashMap
					}
					
				}
				if(bSubsAbuse == true){
					if(!comorbidities.contains(2)){
						comorbidities.add(2);
					}
				}
				if(bAffectDisorder == true){
					if(!comorbidities.contains(3)){
						comorbidities.add(3);
					}
				}
				if(bOtherMental == true){
					if(!comorbidities.contains(4)){
						comorbidities.add(4);
					}
				}
			
				
				//autoimmuneDisorders 
				boolean bAutoImmune = cmd.hasAutoImmuneDisorders();
				if(bAutoImmune == true){
					if(!comorbidities.contains(5)){
						comorbidities.add(5);
					}
				}
				
				//cardioVascular
				boolean bCardio = cmd.hasCardioVas();
				if(bCardio == true){
					if(!comorbidities.contains(6)){
						comorbidities.add(6);
					}
				}
				
				//LiverDisease
				boolean bLiver = cmd.hasLiverDisease();
				if (bLiver == true){
					if(!comorbidities.contains(7)){
						comorbidities.add(7);
					}
				}
				
				
				//diabetes
				boolean bDiabetes = cmd.hasDiabetes();
				if(bDiabetes == true){
					if(!comorbidities.contains(8)){
						comorbidities.add(8);
					}
				}
				
				//osteoporosis
				boolean bOsteo = cmd.hasOsteoporosis();
				if(bOsteo == true){
					if(!comorbidities.contains(9)){
						comorbidities.add(9);
					}
				}
				
				//obesity
				boolean bObesity = cmd.hasObesity();
				if(bObesity == true){
					if(!comorbidities.contains(10)){
						comorbidities.add(10);
					}
				}
				
				// sleep Disturbance
				boolean bSleep = cmd.hasSleepDisturbance();
				if(bSleep == true){
					if(!comorbidities.contains(11)){
						comorbidities.add(11);
					}
				}
				
				//mentalRetardation
				boolean bMenRet = cmd.hasMentalRetardation();
				if(bMenRet == true){
					if(!comorbidities.contains(12)){
						comorbidities.add(12);
					}
				}
				
				/// ADD THE REST OF THE FEATURES HERE
				
				//renal insufficiency
				boolean bRenalInsuff = cmd.hasRenalInsufficiency();
				if(bRenalInsuff == true){
					if(!comorbidities.contains(13)){
						comorbidities.add(13);
					}
				}
				
				//porphyrin metabolism
				boolean bPolyPh = cmd.hasPorphyrinMetabolism();
				if(bPolyPh == true){
					if(!comorbidities.contains(14)){
						comorbidities.add(14);
					}
				}
				
				
				// mitocho
				boolean bMitoch = cmd.hasMitochondrialMetabolism();
				if(bMitoch == true){
					if(!comorbidities.contains(15)){
						comorbidities.contains(15);
					}
				}
				
				// hiv
				boolean bHIV = cmd.hasHIV();
				if(bHIV == true){
					if(!comorbidities.contains(16)){
						comorbidities.contains(16);
					}
				}

				
				//migrain
				boolean bMigraine = cmd.hasMigraine();
				if(bMigraine == true){
					if(!comorbidities.contains(17)){
						comorbidities.contains(17);
					}
				}
				
				//calculus of kidney
				boolean bCalcOfKidney = cmd.hasCalcOfKidney();
				if(bCalcOfKidney == true){
					if(!comorbidities.contains(18)){
						comorbidities.contains(18);
					}
				}
				
				// depressive disorders
				boolean bDepDisorder = cmd.hasDepressiveDisorders();
				if(bDepDisorder == true){
					if(!comorbidities.contains(19)){
						comorbidities.contains(19);
					}
				}
				//psychosis
				boolean bPyschosis = cmd.hasPsychosis();
				if(bPyschosis == true){
					if(!comorbidities.contains(19)){
						comorbidities.contains(19);
					}
				}
				
				
			}else{ // END OF bDIAGONEYEAR == true 
				
				//write all the diagnosis events which dont occur within the 1 year period as it is 
				writer.println(strLine); 
			}
			
		// if the events are procedure events 
		}else if(strLine.contains("PROC_")){
			
			
			String [] codeStruct = arr[1].split("_");
			Procedure proc = new Procedure(codeStruct[1], Integer.parseInt(arr[2]));
			
			boolean bProcInOneYear = proc.isPresentInOneYear(); // checking for presence in the last one year before index date
			boolean bProcIn30Days = proc.isPresentIn30Days(); // checking for presence in the last 30 days 
			
			if(bProcInOneYear == true){ // needs to be used as LATEST in the config file
				// replacing the prefix of 'PROC' to 'PROC365' 
				String newProcFeat = strLine.replace("PROC_", "PROC365_"); 
				writer.println(newProcFeat);
				
				// writing the feature "patient has at least one medical procedure in the last 1 year"
				writer.println(patID+",HASPROC365,0,1");
				
				if(bProcIn30Days == true){
					
					// writing the feature "patient has at least one medical procedure in the last 30 days"
					writer.println(patID+",HASPROC30,0,1");
				}
				
				
			}else{
				//write the rest of the procedure events otherwise
				writer.println(strLine);
			}
			
			
		}else if(strLine.contains("INPATIENT") || strLine.contains("OUTPATIENT")){ // including only those inpatients and outpatients which occur within the year
			
			// similar to procedures 
			HospitalEncounter hospEnc = new HospitalEncounter(Integer.parseInt(arr[2]));
			boolean bHospInOneYear = hospEnc.isPresentInOneYear();
			boolean bHospIn30Days = hospEnc.isPresentIn30Days();
			
			if(bHospInOneYear == true){
				writer.println(patID+",HASHOSPENC365,0,1");
				if(bHospIn30Days == true){
					writer.println(patID+",HASHOSPENC30,0,1");
				}
			}
			
			// if events consists of drug class 
		} else if(strLine.contains("CLASS_")){
			
			String [] classArr = strLine.split("_");
			String molClass = classArr[1];
			
			MoleculeAHFS mClass = new MoleculeAHFS(Integer.parseInt(arr[2]));
			boolean bClassIn30Days = mClass.isPresentIn30Days(); // checking for presence in the last 30 days 
			
			if(bClassIn30Days == true){
				
				// checking for some relevant types of drugs 
				if(molClass.contains("ANTICOAGULANTS")){
					writer.println(patID+",COTREAT30_ANTICOAGULANTS,0,1");
				}
				
				if(molClass.contains("CORTICOSTEROIDS")){
					writer.println(patID+",COTREAT30_CORTICOSTEROIDS,0,1");
				}
				
				if(molClass.contains("DIURETICS")){
					writer.println(patID+",COTREAT30_DIURETICS,0,1");
				}
				
				if(molClass.contains("PROTON-PUMP INHIBITORS")){
					writer.println(patID+",COTREAT30_PPI,0,1");
				}
				
				// antibacterials for antibiotics 
				if(molClass.contains("ANTIBACTERIALS")){
					writer.println(patID+",COTREAT30_ANTIBIOTICS,0,1");
				}
				
				if(molClass.contains("BARBITURATES") || molClass.contains("BENZODIAZEPINES") 
						|| molClass.contains("ANTIPSYCHOTICS") || molClass.contains("ANTIDEPRESSANTS") || molClass.contains("TRICYCLICS")){
					writer.println(patID+",COTREAT30_PHARMACA,0,1");
				}
				if(molClass.contains("ANTILIPEMIC AGENTS") || molClass.contains("HMG-COA REDUCTASE INHIBITORS") || molClass.contains("FIBRIC ACID DERIVATIVES")){
					writer.println(patID+",COTREAT30_LIPIDLOWERING,0,1");
				}
				
			}else{
				writer.println(strLine);
			}
			

		}else{
			
			writer.println(strLine);
			
		}
		return comorbidities;
	}
	
	
	// this method is no longer used .. so you can take it out if you need to 
	private static HashMap<String, String> constructUSPMap(BufferedReader brUSP) throws IOException {
		// TODO Auto-generated method stub
		String strLine = "";
		
		int countLine = 0;
		HashMap<String, String> mapUSPToDrug = new HashMap<String, String>();
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
							String formattedMed = med.trim();
							mapUSPToDrug.put(formattedMed, uspClass);
						}
					}else{
						String med = arr[2].trim();
						mapUSPToDrug.put(med, uspClass);
					}
				
					
				}
			}
			countLine++;
		}
		
		return mapUSPToDrug;
	}
	
	private static String constructAge(Double age) {
		
		Age a = new Age(age);
		return a.getBin();
			
	}
	
	
	private static void displayData(String [] arr){
		for(int i = 0; i < arr.length; i++){
			if(i == arr.length-1){
				writer.println(arr[i]);
			}else{
				writer.print(arr[i]+",");
			}
			
		}
	}
	
	
	
}
