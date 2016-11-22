//package FeatureConstruction;

public class Feature {
	
	String value;
	int offset;
	boolean b;
	
	public Feature(String value, int offset){
		this.value = value;
		this.offset = offset;
		b = false;
	}
	
	public Feature(int offset){
		this.offset = offset;
		b = false;
	}
	

	
	public boolean isPresentInOneYear(){
		if(Math.abs(offset) <= 365){
			b = true;
		}else{
			b = false;
		}
		return b;
	}
	
	public boolean isPresentIn30Days(){
		if(Math.abs(offset) <= 30){
			b = true;
		}else{
			b = false;
		}
		return b;
	}
	
	
	
}
