package edu.sjsu.cmpe295.influencers.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import javax.annotation.Resource;

import java.util.Map.Entry;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import edu.sjsu.cmpe295.influencers.service.NodeService;


public class GraphDS {


	int level;
	String decayFactor;
	static Double threshold = 0.0;
	public Map<String,Double> nodeInfluences=new HashMap<String,Double>();
	public Map<String,Integer> numNodes = new HashMap<String, Integer>();
	public HashMap<String, List<DestValueDS>> given_adjList = new HashMap<String, List<DestValueDS>>();
	public Map<Integer,List<String>> numMap= new TreeMap<Integer,List<String>>();
	public HashMap<String,Double> newnodeInfluences = new HashMap<String,Double>();
	
	

	public GraphDS() {
		// TODO Auto-generated constructor stub
	}

	public GraphDS(int Level,String decayFactor) throws IOException{


		
		HashMap<String, List<DestValueDS>> adjList = new HashMap<String, List<DestValueDS>>();

		//node influence
		HashMap<String, Double> outgoingSum = new HashMap<String,Double>();

		//change path of the file
		String filePath = "src/main/resources/astro-ph.gml";
		//String filePath = "/Users/dhanyaramesh/Desktop/karate.gml";
		Graph g = new DefaultGraph("g");
		FileSource fs = FileSourceFactory.sourceFor(filePath);

		fs.addSink(g);
		fs.readAll(filePath);

		//initializing the adjacency list with empty lists
		//initializing the outgoing sum to 0 for each node 
		for(Node n : g) {			
			adjList.put(n.getId(), new ArrayList<DestValueDS>());
			outgoingSum.put(n.getId(), 0.0);
		}

		System.out.println("number of nodes :"+adjList.size());

		for(Edge e : g.getEdgeSet()){
			//add here for both directions

			DestValueDS destVal = new DestValueDS(e.getTargetNode().getId(),e.getAttribute("value").toString());
			//DestValueDS destValReverse = new DestValueDS(e.getSourceNode().getId(),e.getAttribute("value").toString());
			//DestValueDS destValReverse = new DestValueDS(e.getSourceNode().getId(),"1");
			List<DestValueDS> destValList = adjList.get(e.getSourceNode().getId());
			destValList.add(destVal);
			adjList.put(e.getSourceNode().getId(), destValList);

			//List<DestValueDS> destValList1 = adjList.get(e.getTargetNode().getId());
			//destValList1.add(destValReverse);
			//adjList.put(e.getTargetNode().getId(), destValList1);


			outgoingSum.put(e.getSourceNode().getId(), outgoingSum.get(e.getSourceNode().getId())+destVal.getValue());

			//outgoingSum.put(e.getTargetNode().getId(), outgoingSum.get(e.getTargetNode().getId())+destValReverse.getValue());		


		}
		//System.out.println("outgoing "+outgoingSum);

		//calculation influence values

		for(Entry<String, List<DestValueDS>> thisEntry : adjList.entrySet()){

			Double thisOutgoingSum = outgoingSum.get(thisEntry.getKey());
			for(DestValueDS thisListEntry: thisEntry.getValue()){
				if(thisOutgoingSum !=null)
					thisListEntry.value = thisListEntry.getValue()/thisOutgoingSum;
			}
		}

		//Map<Integer,List<String>> numMap= new TreeMap<Integer,List<String>>();
		for(Entry<String, List<DestValueDS>> thisEntry : adjList.entrySet()){
			if(numMap.containsKey(thisEntry.getValue().size())){
				List<String> sL = numMap.get(thisEntry.getValue().size());
				sL.add(thisEntry.getKey());
				numMap.put(thisEntry.getValue().size(), sL);
			}
			else{
				List<String> sL = new ArrayList<String>();
				sL.add(thisEntry.getKey());
				numMap.put(thisEntry.getValue().size(), sL);
			}
		}
		//n.putAll(adjList);
		Map<Double,String> treeMap = new TreeMap<Double,String>();
		for(Map.Entry<String, Double> eachNode : outgoingSum.entrySet()){
			double influence=0.0;
			int number;
			LevelBasedDFS lb_dfs = new LevelBasedDFS(adjList,eachNode.getKey(),Level,decayFactor);
			influence = lb_dfs.Influence;
			number = lb_dfs.NumberOfNodes;
			newnodeInfluences.put(eachNode.getKey(), influence);
			treeMap.put(influence, eachNode.getKey());
			numNodes.put(eachNode.getKey(), number);
		}
		//System.out.println("new influences"+newnodeInfluences);
		//Descending order
		Map<Double,String>newMap = new TreeMap(Collections.reverseOrder());
		newMap.putAll(treeMap);

		//Printing the top ten nodes 
		int topn = 10;
		int i=0;
		for(Map.Entry<Double, String> thisNode : newMap.entrySet()){
			if(!thisNode.getKey().isNaN()){
				if(i++ > topn) break;
				nodeInfluences.put(thisNode.getValue(),thisNode.getKey());
				System.out.println(thisNode.getValue() + ":  " + thisNode.getKey()+ ":  "+ numNodes.get(thisNode.getValue()));	

			}


		}


	}


}
