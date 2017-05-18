package edu.sjsu.cmpe295.influencers.model;

public class DestValueDS {
	
	
	public String dest;
	public Double value;
	public Double influence;

	public DestValueDS(String dest, String val,Double influence){
	
		this.dest = dest;
		this.value = Double.parseDouble(val);
		this.influence = influence;
	}
	
	public DestValueDS(String dest, String val) {
		// TODO Auto-generated constructor stub
		this.dest = dest;
		this.value = Double.parseDouble(val);
	}
	
	public DestValueDS(String dest, double value) {
		// TODO Auto-generated constructor stub
		this.dest = dest;
		this.value = value;
	}

	public String getDest(){
		return this.dest;
	}
	
	public Double getValue(){
		return this.value;
	}
	
	
	public Double getInfluence()
	{
		return influence;
	}
	

}