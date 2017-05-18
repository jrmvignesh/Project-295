package edu.sjsu.cmpe295.influencers.model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;  

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

public class EdgeGraphDS {

	static Double temp6=0.0,number=0.0,discount=0.9; 
		static Double threshold=0.0;
		//Stack data structure created
			 static Stack<String> stack_l=new Stack<String>();
			 static HashSet<String> visited = new HashSet<String>();
			 static HashMap<String, List<DestValueInf>> adjListInf = new HashMap<String, List<DestValueInf>>();
			 public EdgeGraphDS(Double thresh)
			 {
				threshold=thresh; 
				 
			 }
	public static void main (String[] args) throws Exception{
		//adjacency list to represent graph
		HashMap<String, List<DestValueDS>> adjListInv = new HashMap<String, List<DestValueDS>>();
		
        		
		HashMap<String,Double> pathInfluence = new HashMap<String,Double>();
		
		HashMap<String,Double> nodeInfluence = new HashMap<String,Double>();
		//node influence
		HashMap<String,Double> totalInfluence = new HashMap<String,Double>();
		HashMap<String, Double> outgoingSum = new HashMap<String,Double>();
		
//		TreeMap <Integer,Double> nodelist= new TreeMap<Integer,Double>();
		
		//change path of the file
	
		String filePath = "src/main/resources/astro-ph.gml";
		
		Graph g = new DefaultGraph("g");
		FileSource fs = FileSourceFactory.sourceFor(filePath);

		fs.addSink(g);

		fs.readAll(filePath);

		//initializing the adjacency list with empty lists
		//initializing the outgoing sum to 0 for each node 
		for(Node n : g) {			
			adjListInv.put(n.getId(), new ArrayList<DestValueDS>());
			adjListInf.put(n.getId(), new ArrayList<DestValueInf>());
			outgoingSum.put(n.getId(), 0.0);
					
		}
		

		for(Edge e : g.getEdgeSet()){
			
			List<DestValueDS> destValList = adjListInv.get(e.getSourceNode().getId());
			List<DestValueDS> destValListRev =adjListInv.get(e.getTargetNode().getId());
			/*destValList.addAll(destValListRev);
			
			List<DestValueInf> destValInfList = adjListInf.get(e.getTargetNode().getId());
			List<DestValueInf> destValInfListRev = adjListInf.get(e.getSourceNode().getId());
			destValInfList.addAll(destValInfListRev);*/
			/*DestValueInf destValueInf = new DestValueInf(e.getSourceNode().getId(),0.0);
			destValInfList.add(destValueInf);
			
			DestValueInf destValueInfRev = new DestValueInf(e.getTargetNode().getId(),0.0);
			destValInfListRev.add(destValueInfRev);*/
			
			DestValueDS destVal = new DestValueDS(e.getTargetNode().getId(),e.getAttribute("value").toString(),0.0);
			destValList.add(destVal);  
			
			DestValueDS destValRev = new DestValueDS(e.getSourceNode().getId(),e.getAttribute("value").toString(),0.0);
			destValListRev.add(destValRev);

			
			adjListInv.put(e.getSourceNode().getId(), destValList);
			adjListInv.put(e.getTargetNode().getId(), destValListRev);
//			adjListInf.put(e.getTargetNode().getId(), destValInfList);
			
			outgoingSum.put(e.getSourceNode().getId(), outgoingSum.get(e.getSourceNode().getId())+destVal.getValue());
			outgoingSum.put(e.getTargetNode().getId(), outgoingSum.get(e.getTargetNode().getId())+destVal.getValue());
			
				
		}
		
		
		int h=0;
		
		for(Map.Entry<String, List<DestValueDS>> entry: adjListInv.entrySet())
		{
			Double sum = 0.0;
			
			List<DestValueDS> ll = entry.getValue();
		//	List<DestValueInf> iff =new ArrayList<DestValueInf>();
//			System.out.println("Here:"+ ll.size());
			for(DestValueDS p : ll)
			{
				sum+=p.getValue();
			}
			
			for(DestValueDS p : ll)
			{
				Double inf = p.getValue()/sum;
				
				DestValueInf lp = new DestValueInf(entry.getKey(),inf);
				List<DestValueInf> iff = adjListInf.get(p.getDest());
				iff.add(lp);
				adjListInf.put(p.getDest(),iff);
			}
//		System.out.println("test line:\n");	
		}
		
		
				
	int count=0;
		for(Node n:g)
		{
			Double inf = 0.0;
			for(DestValueInf p : adjListInf.get(n.getId()))
			{
				inf+=p.getInfluence();
			}
			
			nodeInfluence.put(n.getId(), inf);
		
//			System.out.println("Enter step 2:\n");
		}
		
		//Trial
		/*for(Entry<String, List<DestValueInf>> thisEntry : adjListInf.entrySet()){
			System.out.println("Inside here: "+thisEntry.getKey()+"\n");
			for(DestValueInf ds: thisEntry.getValue()){
			//	System.out.print("Node "+ds.dest + " and influence value: " + ds.getInfluence()+" ");
				if(ds.getInfluence()<=0)
				{
					System.out.println("Source is "+thisEntry.getKey());
					System.out.println("Destination is "+ds.getDest());
				}
			}
		}*/
				
		//-------------Trial ends------------------------			
//			
//    		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//    		System.out.println("Enter the depth threshold to end traversal: \n");
//	        String s = br.readLine();
//	        System.out.println("Enter the node Id \n");
//	        try{
//	        node=br.readLine();
//	        threshold = Double.parseDouble(br.readLine());
//	        }catch(NumberFormatException nfe){
//	            System.err.println("Enter only a double type number");
//	        }
 A: for(Entry<String, List<DestValueInf>> n : adjListInf.entrySet())
		{
//	 System.out.println("Enter step 3:\n");
			Double megaSum = 0.0;
		    Double combi_inf = 0.0;
           int temp1=0,sampl=0;
           
			String n1=n.getKey();
			//Push node into stack
			if(!visited.isEmpty()){
			  for(String j:visited){
				
				 if(n1==j)
					continue A;
				 else
					 sampl++;
			  }
				if(sampl==visited.size()){
					if(stack_l.isEmpty())
						 stack_l.push(n1);
						  else{
							  for(String i:stack_l){
								  if(n1==i)					 
									break;
								  else
									temp1++;
							  }
							  if(temp1==stack_l.size())
								  stack_l.push(n1);
					  }
								  
//						number++;

//						if(number<1000)
//					System.out.println("Enter step 4-2:\n");
					    megaSum=calc(n1,combi_inf,threshold);
//						else
//							break A;	
						if(megaSum!=0.0)
						pathInfluence.put(n.getKey(), megaSum);
				}
			 
		  } // if visited is not empty ----ends here----
	
		else{
			if(stack_l.isEmpty())
				 stack_l.push(n1);
				  else{
					  for(String i:stack_l){
						  if(n1==i)					 
							break; //check this condition
						  else
							temp1++;
					  }
					  if(temp1==stack_l.size())
						  stack_l.push(n1);
			  }					  

//				number++;
//				if(number<1000)
//			System.out.println("Enter step 4-1:\n");
				megaSum=calc(n.getKey(),combi_inf,threshold);
//				else
//				   break A;
				if(megaSum!=0.0)
				pathInfluence.put(n.getKey(), megaSum);
		   }
			
		}
		
		
		for(Map.Entry<String, Double> val : pathInfluence.entrySet())
		{		
			totalInfluence.put(val.getKey(), val.getValue() + nodeInfluence.get(val.getKey()));
//			System.out.println("Path influence of:"+val.getKey()+" = "+val.getValue());
			
		}
		
		
		/*for(Map.Entry<String, Double> val : totalInfluence.entrySet())
		{
			System.out.println("Total Influence of Node"+" "+val.getKey()+" "+"is:"+" "+Math.round(val.getValue()*10000.0)/10000.0);
//			nodelist.put(Integer.parseInt(val.getKey()),val.getValue());				
//			total_infl+="Total Influence of Node"+" "+val.getKey()+" "+"is:"+" "+val.getValue()+"\r\n";
			
		}*/
//		System.out.println("total influ test: "+totalInfluence.size());
		//Hashmap before sorting,random order
		 Set<Entry<String, Double>> entries = totalInfluence.entrySet();
		
		 //Sorting the Hashmap by values
		 //Writing custom comparator to arrange them in decreasing order
		 Comparator<Entry<String, Double>> valueComparator = new Comparator<Entry<String,Double>>() {
			 @Override
			 public int compare(Entry<String, Double> e1, Entry<String, Double> e2) { 
				 Double v1 = e1.getValue(); 
				 Double v2 = e2.getValue();
				 return v2.compareTo(v1);
				 } 
			 };
	     //Convert Set to List,as sort method needs a List
			 List<Entry<String,Double >> listOfEntries = new ArrayList<Entry<String, Double>>(entries);
		 //Sorting Hashmap by values using the comparator
			 Collections.sort(listOfEntries,valueComparator);
			 LinkedHashMap<String,Double> sortedByValue = new LinkedHashMap<String,Double>();
			 //copying entries from list to map
			 for(Entry<String, Double> entry : listOfEntries){ 
				 sortedByValue.put(entry.getKey(), entry.getValue());
				 }
	        //Hashmap after sorting entry values
			 Set<Entry<String, Double>> entrySetSortedByValue = sortedByValue.entrySet();
		     String total_infl="";
		     String total_keys="";
		     FileWriter out_X=new FileWriter("src/main/resources/static/images/keyset.txt");
		     FileWriter out_Y=new FileWriter("src/main/resources/static/images/valueset.txt");
		     BufferedWriter bf_X=new BufferedWriter(out_X);
		     BufferedWriter bf_Y=new BufferedWriter(out_Y);
		//Printing the nodes
		    for(Entry<String,Double> nodelist : entrySetSortedByValue )
		      {
			   System.out.println("Total Influence of Node"+" "+nodelist.getKey()+" "+"is:"+" "+Math.round(nodelist.getValue()*10000.0)/10000.0);
			   total_keys+=nodelist.getKey()+"\r\n";
			   total_infl+=Math.round(nodelist.getValue()*1000.0)/1000.0+"\r\n";
		   	  }
		
		
		  bf_X.write(total_keys);
		  bf_Y.write(total_infl);
	      bf_X.close();
	      bf_Y.close();

//	  displayNodeResults(node,threshold);
				
	}

	 //Influence calculation method code 
	
	public static double calc( String nodeId,Double combi_inf,Double threshold){
		Double temp=1.0;	
//		System.out.println("Enter step 5:\n");
//		if(adjListInf.get(nodeId).isEmpty())
//			  System.out.println("Reached here: "+nodeId+ " "+adjListInf.get(nodeId));
	if(adjListInf.get(nodeId)!=null){
		
		/*for(Map.Entry<String,List<DestValueInf>> list: adjListInf.entrySet()){
//			System.out.println("Top code: "+list.getKey()+" ");
			int h=0;
			for(DestValueInf vals:list.getValue()){
//				System.out.println(vals.dest+ " "+vals.influence+ " "+h);h++;
				 } 
			 }*/
		B: for(DestValueInf l:adjListInf.get(nodeId)){
//			System.out.println("Trial code: "+l.dest+" "+l.influence);
			int temp2=0,temp3=0;
					 
			String parent_node=l.getDest();
	//------------------------- Commented code here------------------------
//			if(adjListInf.get(parent_node).isEmpty()){
//
//				System.out.println("Reached inside null condition "+adjListInf.get(parent_node)+" "+adjListInf.get(parent_node).size());
//				visited.add(parent_node); 

			/*	if(!stack_l.isEmpty()){
					for(DestValueInf val:adjListInf.get(nodeId)){
					for(String h:visited){
						if(h==val.getDest())
							temp3++;
					       }
					   }
					if(temp3==adjListInf.get(nodeId).size()){
						System.out.println("Reached inside null condition "+adjListInf.get(parent_node)+" "+adjListInf.get(parent_node).size());
						stack_l.pop();
						String parent=stack_l.peek();
						calc(parent,combi_inf,threshold);
					       }
					
						}*/
//			}

			 if(!visited.isEmpty()) {
	    	   
			 for(String j:visited) {
				 
				if(l.getDest()==j) {
					break;
				}
				else temp3++;
			 }
				
			if(temp3==visited.size()){
			Double k=l.getInfluence();
			 for(String i:stack_l){
				  if(l.getDest()==i)					 
				  {  break; 
				  }
				  else
					temp2++;
			  }
			 if(temp2==stack_l.size()){
//		       		 System.out.println("Inside: "+" "+l.getDest()+" "+k);
		            stack_l.push(l.getDest());
					
					 temp=temp*k*discount;
//					 System.out.println("test parent: "+" "+l.getDest()+" "+k+" temp "+temp);
					 combi_inf=temp;
					 if(adjListInf.get(parent_node)!=null){
					for(DestValueInf no:(adjListInf.get(parent_node))){
						double new_temp=1.0;
						
//						 System.out.println("test : "+" "+no.getDest()+" "+no.getInfluence());
					   new_temp=temp*(no.getInfluence())*discount;
					   
					  /* temp=(double)Math.floor(temp*100)/100;
					   new_temp=(double)Math.floor(new_temp*100)/100;
					   Double dif=Math.abs(temp-new_temp);*/
					  
					  if(Math.abs(temp-new_temp)<threshold){
//						  System.out.println("if test: "+" temp "+temp+" new_temp "+new_temp+" Difference "+Math.abs(temp-new_temp));
						  if(no.getDest()!=null)
							  visited.add(no.getDest());
								 continue B;
					  }
					   else{
						 String l1=l.getDest();
//						 System.out.println("else test: "+" Difference "+Math.abs(temp-new_temp));
						 temp=new_temp;
						 combi_inf+=temp;
//						 System.out.println("Inside calc: "+combi_inf);
					         calc(l1,combi_inf,threshold);
					    }
					
					  } 
			 } 
			
					}
			       }
		         
		       }
			else{
//				System.out.println("Reached here: "+nodeId+ " "+adjListInf.get(nodeId));
				Double k=l.getInfluence();
				 for(String i:stack_l){
					  if(l.getDest()==i)					 
					  {
						  continue B;
					  }
					  else
						temp2++;
				  }
				  if(temp2==stack_l.size()){
//					  System.out.println("Inside: "+" "+l.getDest()+" "+k);
	            stack_l.push(l.getDest());
				
	            temp=temp*k*discount;
//	            System.out.println("test1 parent: "+" "+l.getDest()+" "+k+" temp "+temp);
	            combi_inf=temp;
	            if(adjListInf.get(parent_node)!=null){
				for(DestValueInf no:(adjListInf.get(parent_node))){
			       Double new_temp=1.0;
				   new_temp=temp*(no.getInfluence())*discount;  
//				   temp=(double)Math.floor(temp*100)/100;
//				   new_temp=(double)Math.floor(new_temp*100)/100;
//				   System.out.println(" test1 : "+" "+no.getDest()+" "+no.getInfluence());
//				   Double diff=Math.abs(temp-new_temp);
				  if(Math.abs(temp-new_temp)<threshold){  
//					  System.out.println("if test1: "+" temp "+temp+" new_temp "+new_temp+" Difference "+Math.abs(temp-new_temp));
					 if(no.getDest()!=null)
					  visited.add(no.getDest());
				      continue B;
				  }
				  else{
					 String l1=l.getDest();
//					 System.out.println("else test1: "+" Difference "+Math.abs(temp-new_temp));
					 temp=new_temp;
					 combi_inf+=temp;
//					 System.out.println("Inside calc: "+combi_inf);
				     calc(l1,combi_inf,threshold);
//				         }
				  
				       }
				     }
				  }
			}
			
		  
		}
//			System.out.println("Out of calc: "+combi_inf);		
	}
//	temp6=combi_inf;
//	System.out.println("Outside of out of calc: "+combi_inf+" "+"count"+" "+cut++);
	return combi_inf;
	}
				else {
					
						for(String s:stack_l){
							if((nodeId==s)&&(s==stack_l.peek()))
							{
								stack_l.pop();
								break;
							}
						}
						return 0;
					}
				
//		if(!stack_l.isEmpty())
//		stack_l.pop();
		
		                                
	}
}
	
	
	
	

