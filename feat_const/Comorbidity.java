
public class Comorbidity {
	
	String comorbidityName;
	String code;
	
	boolean b;
	
	public Comorbidity(String code){
		this.code = code;
		this.b = false;
	}
	
	private int getCode(String cd){
		int codeInt = 0;
		
		try {
			codeInt = Integer.parseInt(code);
		    }
		    catch (NumberFormatException e) {
		        codeInt  = -1;
		    }
		 
		return codeInt;
	}
	
	public boolean hasSerMentalIllnes(){
		b = false;
		int codeInt = getCode(code);
		
		
		if(code.startsWith("295") && code.length() == 5){ // schizophrenia 
			
 			if(code.substring(3,5).equals("50")){
 				
			}else{
				b = true;
			}
		}else if(codeInt <= 29899 && codeInt >= 29700){ // other psychosis
			b = true;
		} else if((codeInt >= 29600 && codeInt < 29620) || (codeInt >= 29640 && codeInt <= 29680)){
			b = true;
		}else{
			
		}
		
		return b;
	}
	
	
	public boolean hasSubstanceAbuse(){
		b = false;
		int codeInt = getCode(code);
		
		if(code.startsWith("291") || code.startsWith("292") && code.length() == 5){
			b = true;
		}else if((codeInt >= 30300 && codeInt <= 30509) || (codeInt >= 30520 && codeInt <= 30599)){
			b = true;
		}
		
		return b;
	}
	
	
	public boolean hasAffectiveDisorders(){
		b = false;
		int codeInt = getCode(code);
		
		if(codeInt >= 29620 && codeInt <= 29640){ // depression
			b = true;
		
		}else if(code.startsWith("311") || code.equals("30000") || code.equals("30002")|| code.equals("30009") || code.equals("30981")){
			b = true;
		}
		
		
		return b;
	}
	
	public boolean hasOtherMentalDisorder(){
		b = false;
		int codeInt = getCode(code);
		
		if(codeInt >= 29000 && codeInt <= 31299){
			b = true;
		}
		return b;
	}
	
	public boolean hasAutoImmuneDisorders(){
		b = false;
		
		if((code.startsWith("696") || code.startsWith("555") || code.startsWith("714") || code.startsWith("2794") || code.equals("2830") || code.equals("57142")) && code.length() == 5){
			b = true;
		}
		return b;
		
	}
	
	
	public boolean hasCardioVas(){
		b = false;
		
		int lowerLimit = 390;
		int upperLimit = 459;
		

		
		for(int i = lowerLimit; i <= upperLimit; i++){
			if(code.startsWith(String.valueOf(i))){
				b = true;
				break;
			}
		}
		return b;
	}
	
	public boolean hasLiverDisease(){
		b = false;
		
		if(code.startsWith("571")){
			b = true;
		}
		return b;
	}
	
	public boolean hasDiabetes(){
		b = false;
		
		if(code.startsWith("250")){
			b = true;
		}
		return b;
	}
	
	public boolean hasOsteoporosis(){
		b = false;
		
		if(code.startsWith("7330")){
			b = true;
		}
		return b;
	}

	public boolean hasObesity() {
		b = false;
		
		
		if(code.startsWith("2780")){
			b = true;
		}
		return b;
	}
	
	public boolean hasSleepDisturbance(){
		b = false;
		
		if(code.startsWith("7805")){
			b = true;
		}
		return b;
	}

	public boolean hasMentalRetardation() {
		b = false;
		// TODO Auto-generated method stub
		int lowerLimit = 317;
		int upperLimit = 319;
		
		
		
		for(int i = lowerLimit; i <= upperLimit; i++){
			if(code.startsWith(String.valueOf(i))){
				b = true;
				break;
			}
		}
		return b;
	}
	
	public boolean hasMyoCardInfarc(){
		b = false;
		if(code.startsWith("410") || code.startsWith("411")){
			b = true;
		}
		
		return b;
	}
	
	public boolean hasCongestHrtFail(){
		b = false;
		if(code.startsWith("398") || code.startsWith("402") || code.startsWith("428")){
			b = true;
		}
		return b;
	}
	
	public boolean hasPeriVasDis(){
		b = false;
		
		int lowerLimit = 440;
		int upperLimit = 447;
		
		
		for(int i = lowerLimit; i <= upperLimit; i++){
			if(code.startsWith(String.valueOf(i))){
				b = true;
				break;
			}
		}
		return b;
		
	}
	
	// create the rest of the methods for CCI 
	
	public boolean hasCerebrovasDisease(){
		b = false;
		
		if(code.startsWith("430") || code.startsWith("431") || code.startsWith("432") || code.startsWith("433") || code.startsWith("435")){
			b = true;
		}
		return b;
	}
	
	public boolean hasDementia(){
		b = false;
		if(code.startsWith("290") || code.startsWith("291") || code.startsWith("294")){
			b = true;
		}
		return b;
	}
	
	public boolean hasChronicPulDisease(){
		b = false;
		int lowerLimit = 491;
		int upperLimit = 493;
		
		
		for(int i = lowerLimit; i <= upperLimit; i++){
			if(code.startsWith(String.valueOf(i))){
				b = true;
				break;
			}
		}
		return b;
		
	}
	
	public boolean hasRheumaDisease(){
		b  = false;
		
		if(code.startsWith("710") || code.startsWith("714") || code.startsWith("725")){
			b = true;
			
		}
		return b;
	}
	
	public boolean hasPepticUlcerDis(){
		b = false;
		int lowerLimit = 531;
		int upperLimit = 534;
		
		
		for(int i = lowerLimit; i <= upperLimit; i++){
			if(code.startsWith(String.valueOf(i))){
				b = true;
				break;
			}
		}
		return b;
	}
	
	public boolean hasMildLiverDisease(){
		b = false;
		if(code.startsWith("571") || code.startsWith("573")){
			b = true;
		}
		return b;
	}
	
	public boolean hasHemiplegia() {
		b = false;
		if(code.startsWith("342") || code.startsWith("434") || code.startsWith("436") || code.startsWith("437")){
			b = true;
		}
		return b;
	}
	
	public boolean hasRenalDisease(){
		b = false;
		if(code.startsWith("403") || code.startsWith("404")){
			b= true;
		}
		
		
		
		if(b == false){
			int lowerLimit = 580;
			int upperLimit = 586;
			
			for(int i = lowerLimit; i <= upperLimit; i++){
				if(code.startsWith(String.valueOf(i))){
					b = true;
					break;
				}
			}
			
		}
		
		
		return b;
	}
	
	public boolean hasLeukamia(){
		b = false;
		int lowerLimit = 204;
		int upperLimit = 207;
		
		
		for(int i = lowerLimit; i <= upperLimit; i++){
			if(code.startsWith(String.valueOf(i))){
				b = true;
				break;
			}
		}
		return b;
	}
	
	public boolean hasLymphoma(){
		b = false;
		int lowerLimit = 200;
		int upperLimit = 203;
		
		
		for(int i = lowerLimit; i <= upperLimit; i++){
			if(code.startsWith(String.valueOf(i))){
				b = true;
				break;
			}
		}
		return b;
	}
	
	public boolean hasAnyTumor(){
		b = false;
		int lowerLimit = 140;
		int upperLimit = 195;
		
		
		for(int i = lowerLimit; i <= upperLimit; i++){
			if(code.startsWith(String.valueOf(i))){
				b = true;
				break;
			}
		}
		return b;
	}
	
	public boolean hasModerateLiverDisease(){
		b = false;
		if(code.startsWith("070") || code.startsWith("570") || code.startsWith("572")){
			b = true;
		}
		return b;
	}
	
	public boolean hasMetSolidTumor(){
		b = false;
		int lowerLimit = 196;
		int upperLimit = 199;
		
		
		for(int i = lowerLimit; i <= upperLimit; i++){
			if(code.startsWith(String.valueOf(i))){
				b = true;
				break;
			}
		}
		return b;
	}
	
	public boolean hasRenalInsufficiency(){
		b = false;
		if(code.startsWith("5939")){
			b = true;
		}
		return b;
	}
	
	public boolean hasPorphyrinMetabolism(){
		b = false;
		if(code.startsWith("2771")){
			b = true;
		}
		return b;
	}
	
	public boolean hasMitochondrialMetabolism(){
		b = false;
		if(code.startsWith("27787")){
			b = true;
		}
		return b;
	}
	
	
	public boolean hasHIV(){
		b = false;
		if(code.startsWith("042")){
			b = true;
		}
		return b;
	}
	
	public boolean hasMigraine(){
		b = false;
		if(code.startsWith("346")){
			b = true;
		}
		return b;
	}
	
	public boolean hasCalcOfKidney(){
		b = false;
		if(code.startsWith("5920")){
			b = true;
		}
		return b;
	}

	
	public boolean hasDepressiveDisorders(){
		b = false;
		if(code.startsWith("311") || code.startsWith("3004") || code.startsWith("300")){
			b = true;
		}
		return b;
	}
	
	
	public boolean hasPsychosis(){
		b = false;
		int lowerLimit = 290;
		int upperLimit = 299;
		
		
		for(int i = lowerLimit; i <= upperLimit; i++){
			if(code.startsWith(String.valueOf(i))){
				b = true;
				break;
			}
		}
		return b;
	}
	
	public boolean hasPulCircDisorder(){
		b = false;
		if(code.startsWith("416") || code.startsWith("4179")){
			b = true;
		}
		return b;
		
	}
	
	public boolean hasHyperTension(){
		b = false;
		
		int lowerLimit = 401;
		int upperLimit = 405;
		
		for(int i = lowerLimit; i <= upperLimit; i++){
			if(code.startsWith(String.valueOf(i))){
				b = true;
				break;
			}
		}
		
		
		
		int codeInt = getCode(code);
		
		if(b == false){
			if((codeInt >= 64200 && codeInt <= 64204) || (codeInt >= 64210 && codeInt <= 64224) || (codeInt >= 64270 && codeInt <= 64294)){
				b = true;
			}
		}
		
		return b;
	}
	
	
	public boolean hasCardiacArrhythmias(){
		b = false;
		if(code.startsWith("42610") || code.startsWith("42611") || code.startsWith("42613") 
				|| code.startsWith("42731") || code.startsWith("42760") || code.startsWith("4279") || code.startsWith("7850") 
				|| code.startsWith("v45") || code.startsWith("V533")){
			b = true;
		}else if(b == false){
			b = checkInRange(4262, 4264);		
		}else if(b == false){
			b = checkInRange(42650, 42653);
		}else if(b == false){
			b = checkInRange(4266, 4267);
		}else if(b == false){
			b = checkInRange(42680, 42689);
		}else if(b == false){
			b = checkInRange(4270, 4272);
		}
		
		return b;		
	}
	
	public boolean hasSolidTumWoMeta(){
		b = false;
		b = checkInRange(1400, 1729);
		if(b == false){
			b = checkInRange(1740,  1759);
		}else if(b == false){
			b = checkInRange(179, 195);
		}
		
		return b;
	}
	
	public boolean hasAspPneumonia(){
		b = false;
		if(code.startsWith("99732")){
			b = true;
		}
		return b;
	}
	
	public boolean hasBrainTumor(){
		b = false;
		if(code.startsWith("191") || code.startsWith("225")){
			b = true;
		}
		return b;
	}
	
	public boolean hasAnoxicBrainInjury(){
		b = false;
		if(code.startsWith("4379")){
			b = true;
		}
		return b;
	}
	
	
	public boolean checkInRange(int lowerLimit, int upperLimit){
		b = false;
		
		for(int i = lowerLimit; i <= upperLimit; i++){
			if(code.startsWith(String.valueOf(i))){
				b = true;
				break;
			}
		}
		return b;
	}

}
