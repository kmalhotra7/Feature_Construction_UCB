
public class Age {
	Double age;
	String bin;
	
	
	public Age(Double age){
		this.age = age;
	}
	
	public String getBin(){
		if(age < 45){
			bin = "16-45";
		}else if(age  > 45 && age < 65) {
			bin = "45-65";
		}else{
			bin = "65-above";
		}
		
		return bin;
	}
	
	
}
