//package FeatureConstruction;

import java.util.ArrayList;

public class CharlesonScore {
	ArrayList<String> diagCodes = new ArrayList<String>();
	ArrayList<String> features = new ArrayList<String>();
	String featPrefix = "CHARLESON_";
	int score;
	
	public CharlesonScore(ArrayList<String> diagCodes) {
		this.diagCodes = diagCodes;
		features = new ArrayList<String>();
		score = 0;
	}
	
	
	
	
	public void getCharlesonFeatures(){
		
		for(String code : diagCodes){
			Comorbidity cmd = new Comorbidity(code);
			boolean bMyoInfarct = cmd.hasMyoCardInfarc();
			boolean bCongHeartFail = cmd.hasCongestHrtFail();
			boolean bPeriphVasDisease = cmd.hasPeriVasDis();
			boolean bCerebVasDisease = cmd.hasCerebrovasDisease();
			boolean bDementia = cmd.hasDementia();
			boolean bChronicPulmDisease = cmd.hasChronicPulDisease();
			boolean bRheumDisease = cmd.hasRheumaDisease();
			boolean bPepticUlcerDisease = cmd.hasPepticUlcerDis();
			boolean bMildLiverDisease = cmd.hasMildLiverDisease();
			boolean bDiabetes = cmd.hasDiabetes();
			boolean bHemiplegia = cmd.hasHemiplegia();
			boolean bRenalDisease = cmd.hasRenalDisease();
			boolean bLeukamia = cmd.hasLeukamia();
			boolean bLymphoma = cmd.hasLymphoma();
			boolean bAnyTumor = cmd.hasAnyTumor();
			boolean bModLivDisease = cmd.hasModerateLiverDisease();
			boolean bMetSolidTumor = cmd.hasMetSolidTumor();
			
			if(bMyoInfarct == true && !features.contains(featPrefix+"MYOCARDINFARCT")){
				score +=1;
				features.add(featPrefix+"MYOCARDINFARCT");
			}
			if(bCongHeartFail == true && !features.contains(featPrefix+"MYOCARDINFARCT")){
				score +=1;
				features.add(featPrefix+"MYOCARDINFARCT");
			}
			if(bPeriphVasDisease == true && !features.contains(featPrefix+"PERIPHVASDISEASE")){
				score +=1;
				features.add(featPrefix+"PERIPHVASDISEASE");
			}
			if(bCerebVasDisease == true && !features.contains(featPrefix+"CEREBVASDISEASE")){
				score +=1;
				features.add(featPrefix+"CEREBVASDISEASE");
			}
			if(bDementia == true && !features.contains(featPrefix+"DEMENTIA")){
				score +=1;
				features.add(featPrefix+"DEMENTIA");
			}
			if(bChronicPulmDisease == true && !features.contains(featPrefix+"CHRNPULMDISEASE")){
				score +=1;
				features.add(featPrefix+"CHRNPULMDISEASE");
			}
			if(bRheumDisease == true && !features.contains(featPrefix+"RHEUMDISEASE")){
				score +=1;
				features.add(featPrefix+"RHEUMDISEASE");
			}
			if(bPepticUlcerDisease == true && !features.contains(featPrefix+"PEPULCERDISEASE")){
				score +=1;
				features.add(featPrefix+"PEPULCERDISEASE");
			}
			if(bMildLiverDisease == true && !features.contains(featPrefix+"MILDLIVDISEASE")){
				score +=1;
				features.add(featPrefix+"MILDLIVDISEASE");
			}
			if(bDiabetes == true && !features.contains(featPrefix+"DIAB")){
				score +=2;
				features.add(featPrefix+"DIAB");
			}
			if(bHemiplegia == true && !features.contains(featPrefix+"HEMIPLEGIA")){
				score +=2;
				features.add(featPrefix+"HEMIPLEGIA");
			}
			if(bRenalDisease == true && !features.contains(featPrefix+"RENALDISEASE")){
				score +=2;
				features.add(featPrefix+"RENALDISEASE");
			}
			if(bLeukamia == true && !features.contains(featPrefix+"LEUKAMIA")){
				score +=2;
				features.add(featPrefix+"LEUKAMIA");
			}
			if(bLymphoma == true && !features.contains(featPrefix+"LYMPHOMA")){
				score +=2;
				features.add(featPrefix+"LYMPHOMA");
			}
			if(bAnyTumor == true && !features.contains(featPrefix+"ANYTUMOR")){
				score +=2;
				features.add(featPrefix+"ANYTUMOR");
			}
			if(bModLivDisease == true && !features.contains(featPrefix+"MODLIVDISEASE")){
				score +=6;
				features.add(featPrefix+"MODLIVDISEASE");
			}
			if(bMetSolidTumor == true && !features.contains(featPrefix+"METSOLIDTUMOR")){
				score +=6;
				features.add(featPrefix+"METSOLIDTUMOR");
			}
		}
		
	}
	
	public int getScore(){
		int score = 0;
		
		for(String code : diagCodes){
			Comorbidity cmd = new Comorbidity(code);
			boolean bMyoInfarct = cmd.hasMyoCardInfarc();
			boolean bCongHeartFail = cmd.hasCongestHrtFail();
			boolean bPeriphVasDisease = cmd.hasPeriVasDis();
			boolean bCerebVasDisease = cmd.hasCerebrovasDisease();
			boolean bDementia = cmd.hasDementia();
			boolean bChronicPulmDisease = cmd.hasChronicPulDisease();
			boolean bRheumDisease = cmd.hasRheumaDisease();
			boolean bPepticUlcerDisease = cmd.hasPepticUlcerDis();
			boolean bMildLiverDisease = cmd.hasMildLiverDisease();
			boolean bDiabetes = cmd.hasDiabetes();
			boolean bHemiplegia = cmd.hasHemiplegia();
			boolean bRenalDisease = cmd.hasRenalDisease();
			boolean bLeukamia = cmd.hasLeukamia();
			boolean bLymphoma = cmd.hasLymphoma();
			boolean bAnyTumor = cmd.hasAnyTumor();
			boolean bModLivDisease = cmd.hasModerateLiverDisease();
			boolean bMetSolidTumor = cmd.hasMetSolidTumor();
			
			if(bMyoInfarct == true || bCongHeartFail == true || bPeriphVasDisease == true 
					|| bCerebVasDisease == true || bDementia == true || bChronicPulmDisease == true 
					|| bRheumDisease == true 
					|| bPepticUlcerDisease == true || bMildLiverDisease == true){
					
					
					score+= 1;
					
			}
			
			if(bDiabetes == true || bHemiplegia == true || bRenalDisease == true 
					|| bLeukamia == true || bLymphoma == true || bAnyTumor == true){
				score += 2;
				
			}
			
			if(bModLivDisease == true || bMetSolidTumor == true){
				score += 6;
				
			}
			
			
		}
		return score;
		
	}
	
	
	
	
}
