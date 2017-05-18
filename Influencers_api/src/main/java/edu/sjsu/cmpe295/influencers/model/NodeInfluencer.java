package edu.sjsu.cmpe295.influencers.model;

public class NodeInfluencer {
	
	String nodeId;
	String totalInfluence;
	double depth;
	
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getTotalInfluence() {
		return totalInfluence;
	}
	public void setTotalInfluence(String totalInfluence) {
		this.totalInfluence = totalInfluence;
	}
	public double getDepth() {
		return depth;
	}
	public void setDepth(double depth) {
		this.depth = depth;
	}
	
}
