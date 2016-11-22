
//package FeatureConstruction;

public class PhysicianSpecialty {
	String specialty;
	boolean b;
	
	public PhysicianSpecialty(String specialty){
		this.specialty = specialty;
		b = false;
	}

	public boolean isSpecialtyGeneral(){
		b = false;
		if(specialty.contains("INTERNAL MEDICINE") || 
				specialty.contains("FAMILY PRACTICE") || 
				specialty.contains("PSYCHIATRY") || 
				specialty.contains("EMERGENCY MEDICINE") ||
				specialty.contains("GENERAL PRACTICE")){
			b = true;
		}
		
		return b;
	}
	
	public boolean isSpecialtyPain(){
		b = false;
		if(specialty.contains("ANESTHESIOLOGY") || 
				specialty.contains("RHEUMATOLOGY") || 
				specialty.contains("ORTHOPEDIC SURGERY") || 
				specialty.contains("PAIN MEDICINE") ||
				specialty.contains("PODIATRY")){
			b = true;
		}
		
		return b;
	}
	
	public boolean isSpecialtyEmergency(){
		b = false;
		if(specialty.contains("EMERGENCY")){
			b = true;
		}
		return b;
	}
	
	public boolean isSpecialtyNeuro(){
		b = false;
		if(specialty.contains("NEURO")){
			b = true;
		}
		return b;
	}
	
	
}
