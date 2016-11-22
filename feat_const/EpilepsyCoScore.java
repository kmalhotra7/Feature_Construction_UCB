//package FeatureConstruction;

import java.util.ArrayList;

public class EpilepsyCoScore {
	
	ArrayList<String> diagCodes = new ArrayList<String>();
	ArrayList<String> features = new ArrayList<String>();
	String featPrefix = "EPCOMOR_";
	
	
	public EpilepsyCoScore(ArrayList<String> diagCodes) {
		this.diagCodes = diagCodes;
		features = new ArrayList<String>();
	}
	
	
	public ArrayList<String> getEpComorbFeatures(){
		for(String diagCode : diagCodes){
			Comorbidity cmd = new Comorbidity(diagCode);
			// implement the rest of the methods . 
			boolean bPulCircDis = cmd.hasPulCircDisorder();
			boolean bHyper = cmd.hasHyperTension();
			boolean bCardArryh = cmd.hasCardiacArrhythmias();
			
			boolean bSolidTum = cmd.hasSolidTumWoMeta();
			boolean bBrainTum = cmd.hasBrainTumor();
			boolean bAnxBrnInj = cmd.hasAnoxicBrainInjury();
			
			if(bPulCircDis == true && !features.contains(featPrefix+"PULCIRDDISEASE")){
				features.add(featPrefix+"MYOCARDINFARCT");
			}
			if(bHyper == true && !features.contains(featPrefix+"HYPERTENSION")){
				features.add(featPrefix+"HYPERTENSION");
			}
			if(bCardArryh == true && !features.contains(featPrefix+"CARDIACARRHYTHMIAS")){
				features.add(featPrefix+"CARDIACARRHYTHMIAS");
			}
			if(bSolidTum == true && !features.contains(featPrefix+"SOLIDTUMOR")){
				features.add(featPrefix+"SOLIDTUMOR");
			}
			if(bBrainTum == true && !features.contains(featPrefix+"BRAINTUMOR")){
				features.add(featPrefix+"BRAINTUMOR");
			}
			if(bAnxBrnInj == true && !features.contains(featPrefix+"ANOXICBRAININJURY")){
				features.add(featPrefix+"ANOXICBRAININJURY");
			}
			
		}
		return features;
	}
	
	public int getScore(){
		int score = 0;
		ArrayList<String> listComorbidities = new ArrayList<String>();
		
		for(String diagCode: diagCodes){
			Comorbidity cmd = new Comorbidity(diagCode);
			// implement the rest of the methods . 
			boolean bPulCircDis = cmd.hasPulCircDisorder();
			boolean bHyper = cmd.hasHyperTension();
			boolean bCardArryh = cmd.hasCardiacArrhythmias();
			boolean bCongesHeart = cmd.hasCongestHrtFail(); // CCI
			boolean bPerVasDis = cmd.hasPeriVasDis(); //CCI
			boolean bRenDis = cmd.hasRenalDisease(); //CCI
			boolean bSolidTum = cmd.hasSolidTumWoMeta();
			boolean bHemiPlegia = cmd.hasHemiplegia(); //CCI
			boolean bAspPneu = cmd.hasAspPneumonia(); //CCI
			boolean bDementia = cmd.hasDementia(); //CCI
			boolean bBrainTum = cmd.hasBrainTumor();
			boolean bAnxBrnInj = cmd.hasAnoxicBrainInjury();
			boolean bModSevLiver = cmd.hasModerateLiverDisease();//CCI
			boolean bMetCancer = cmd.hasMetSolidTumor();//CCI
			
			if(bPulCircDis == true && !listComorbidities.contains("PULCIRDDISEASE")){
				score += 1;
				listComorbidities.add("PULCIRDDISEASE");
			}
			
			if(bHyper == true && !listComorbidities.contains("HYPER")){
				score+=1;
				listComorbidities.add("HYPER");
			}
			
			if(bCardArryh == true && !listComorbidities.contains("CARDARRYH")){
				score += 1;
				listComorbidities.add("CARDARRYH");
			}
			
			if(bCongesHeart == true && !listComorbidities.contains("CONGESHEART")){
				score += 2;
				listComorbidities.add("CONGESHEART");
				
			}
			
			if(bPerVasDis == true && !listComorbidities.contains("PERVASDIS")){
				score += 2;
				listComorbidities.add("PERVASDIS");
			}
			
			if(bRenDis == true && !listComorbidities.contains("RENDIS")){
				score += 2;
				listComorbidities.add("RENDIS");
			}
			if(bSolidTum == true && !listComorbidities.contains("SOLIDTUM")){
				score += 2;
				listComorbidities.add("SOLIDTUM");
			}
			if(bHemiPlegia == true && !listComorbidities.add("HEMIPLEGIA")){
				score +=2;
				listComorbidities.add("HEMIPLEGIA");
			}
			if(bAspPneu == true && !listComorbidities.contains("ASPPNEU")){
				score += 2;
				listComorbidities.add("ASPPNEU");
			}
			if(bDementia == true && !listComorbidities.contains("DEMENTIA")){
				score += 2;
				listComorbidities.add("DEMENTIA");
			}
			
			if(bBrainTum == true && !listComorbidities.contains("BRAINTUM")){
				score += 3;
				listComorbidities.add("BRAINTUM");
			}
			if(bAnxBrnInj == true && !listComorbidities.contains("ANXBRNINJ")){
				score += 3;
				listComorbidities.add("ANXBRNINJ");
			}
			if(bModSevLiver == true && !listComorbidities.contains("MODSEVLIVER")){
				score += 3;
				listComorbidities.add("MODSEVLIVER");
			}
			if(bMetCancer == true && !listComorbidities.contains("METCANCER")){
				score += 6;
				listComorbidities.add("METCANCER");
			}
			
			
		}
		
		
		return score;
	}
	
	

}
