package edu.sjsu.cmpe295.influencers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.sjsu.cmpe295.influencers.model.DestValueDS;
import edu.sjsu.cmpe295.influencers.model.DestValueInf;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;


/**
 * Created by vignesh on 4/18/2017.
 */

@Component
public class NodeService {
	
	public static List<DestValueInf> cloneList(HashMap<String,List<DestValueInf>> hm,String p)
	{
		List<DestValueInf> aList=new ArrayList<DestValueInf>(hm.get(p).size());
		
		for(DestValueInf pp : hm.get(p))
		{
		aList.add(new DestValueInf(pp));
		}
		
		return aList;
	}
	
	
	public static ArrayList<DestValueInf> cloneList(List<DestValueInf> dList)
	{
		
		ArrayList<DestValueInf> clone=new ArrayList<DestValueInf>();
		
		for(DestValueInf dv : dList)
		{
			clone.add(new DestValueInf(dv));
		}
		
		return clone;
	}
	
	public static Double depthInf(HashMap<String, List<DestValueInf>> adjListInf, HashMap<String, List<DestValueDS>> adjListInv, String d,int depth,Double threshold)
	{
		
		List<Double> dList = new ArrayList<Double>();
		boolean[] visited = new boolean[35];
		
		for(int i=0;i<visited.length;i++)
		{
			visited[i]=false;
		}
		
		int tries=0;
		Stack<String> s = new Stack<>();
		
		s.push(d);
		int v = Integer.parseInt(d);
		visited[v] = true;
		int de = 0;
		
		
	
		
//		for(Map.Entry<String, List<DestValueInf>> entry: adjListInf.entrySet())
//		{
//			List<DestValueInf> k = cloneList(adjListInf,entry.getKey());
//			listNeighbors.put(entry.getKey(),k);
//		}
		
	//	if(listNeighbors==null)
		//	System.out.println("Null da");
		HashMap<String, List<DestValueInf>> listNeighbors = new HashMap<>();
		for(Map.Entry<String, List<DestValueInf>> entry : adjListInf.entrySet())
		{
			listNeighbors.put(entry.getKey(),cloneList(entry.getValue()));
		}
		
		Double temp = 1.0, discount;
		Double pathInfluence = 0.0;
		
		
		Double A = 0.0;
		while(!s.isEmpty())
		{
		//	System.out.println("Inside stack ");
			String p = s.peek();
		//	T y= new T(adjListInf);
			
			int listSize=adjListInf.get(p).size();
				
			List<DestValueInf> ll = listNeighbors.get(p);
		
			if(ll==null)
				System.out.println("Null da");
	//		else
		//	System.out.println("Top stack "+p+" "+ll.size());
			
			
			if(ll!=null && !ll.isEmpty() && visited[Integer.parseInt(ll.get(0).getDest())])
			{
				ll.remove(0);
			}
			
			if(ll!=null && !ll.isEmpty() && de<depth)
			{
			
				s.push(ll.get(0).getDest());
				visited[Integer.parseInt(ll.get(0).getDest())] = true;
		//		System.out.println("Adding Destination "+ll.get(0).getDest());
				
					
				discount = (1-(0.1 * (de+1)));
				dList.add(ll.get(0).getInfluence() * discount);
				
		//		if(depth==2)
		//		System.out.println(" Summing edge influence "+A);
			
				temp *= dList.get(dList.size()-1);
				tries++;
				de++;
				
				
				
		//		System.out.println("Removing "+ll.get(0));
				DestValueInf g = ll.get(0);
			
				listNeighbors.get(p).remove(g);
				
				if(de == depth)
				{
					if(de>0)
					de--;
							
					//Threshold
				//	if(depth==2) 
				//	System.out.println("Adding "+temp);
					
					if(temp>threshold)
					pathInfluence+=temp;
				//	System.out.println("Addinging Influence is "+temp);
					s.pop();
					if(dList.size()>0)
					{
					temp/=dList.get(dList.size()-1);
					dList.remove(dList.size()-1);
					}
					else
					{
						System.out.println();
					}
				} 
		
				
			}
			else if(ll!=null && ll.isEmpty())
			{
	//			while(listSize>0)
	//			{
	//				if(dList.size()>0)
	//				dList.remove(dList.size()-1);
//					listSize--;
//				}
				
				if(dList.size()>0)
				{
				temp/=dList.get(dList.size()-1);
				dList.remove(dList.size()-1);
				}
				else
				{
					System.out.println();
				}
				
	if(de>0)
		de--;
	
	if(!s.isEmpty())
	{
	String pp = s.peek();
	
	visited[Integer.parseInt(pp)]=false;
	
				s.pop();
	}
			}
			
			
			
			if(s.size() == 1)
			{
				if(listNeighbors.get(s.peek()) == null || listNeighbors.get(s.peek()).isEmpty())
				{
					s.pop();
				}
				
			}
		}
		
		
		
		
	//	System.out.println("Tries "+tries);
		return pathInfluence;
		
	}
	
	
	/*

	public static Double influentialPath(String source, String destination, Double threshold)
	{
		Double inf = 0.0;
		
		Stack<String> s = new Stack<>();
		s.push(source);
		
		HashMap<String, List<DestValueInf>> listNeighbors = new HashMap<>();
		
		for(Map.Entry<String, List<DestValueInf>> entry : adjListInf.entrySet())
		{
			listNeighbors.put(entry.getKey(),cloneList(entry.getValue()));
		}
		
		while(!s.isEmpty())
		{
			String top = s.peek();
			
			
		}
		
	}*/
	
	public static Double mostInfluentialPath(HashMap<String, List<DestValueInf>> adjListInf, HashMap<String, List<DestValueDS>> adjListInv, String source,String destination,Double threshold)
	{
		List<String> tempPath = new ArrayList<String>();
		List<String> maxPath = new ArrayList<String>();
		
		boolean[] visited = new boolean[35];
		
		for(int i=0;i<visited.length;i++)
		{
			visited[i]=false;
		}
		
		List<Double> dList = new ArrayList<Double>();
		
		int tries=0;
		Stack<String> s = new Stack<>();
		
		s.push(source);
		visited[Integer.parseInt(source)]=true;
		tempPath.add(source);
		int de = 0;
		
		
		

		HashMap<String, List<DestValueInf>> listNeighbors = new HashMap<>();

		for(Map.Entry<String, List<DestValueInf>> entry : adjListInf.entrySet())
		{
			listNeighbors.put(entry.getKey(),cloneList(entry.getValue()));
		}
		
		Double temp = 1.0, discount;
		Double pathInfluence = 0.0;
		
		
		Double A = 0.0;
		while(!s.isEmpty())
		{

			String p = s.peek();

			
			int listSize=adjListInf.get(p).size();
				
			List<DestValueInf> ll = listNeighbors.get(p);
		
			while(ll!=null && !ll.isEmpty() && visited[Integer.parseInt(ll.get(0).getDest())])
			{
				ll.remove(0);
			}
			
			
			if(ll==null)
				System.out.println("Null da");

			
			if(ll!=null && !ll.isEmpty())
			{
				
				String d = ll.get(0).getDest();
				
				s.push(d);
				
				visited[Integer.parseInt(d)]=true;
				
			
				tempPath.add(d);
		
				
					
				discount = (1-(0.1 * (de+1)));
				dList.add(ll.get(0).getInfluence() * discount);
				

			
				temp *= dList.get(dList.size()-1);
				tries++;
				de++;
				
	
				DestValueInf g = ll.get(0);
			
				listNeighbors.get(p).remove(g);
				
				if(d.equals(destination))
				{

					
					if(temp>pathInfluence)
					{
						pathInfluence = temp;
						
						for(String ss : tempPath)
						{
							System.out.print("Traversing  "+ss+"\t");
						}
						
						maxPath.clear();
						maxPath = new ArrayList<String>(tempPath);
						
					
						
						System.out.println();
		
					}
					
					if(s.size()>0)
					{
					String tt = s.peek();
					visited[Integer.parseInt(tt)]=false;
					s.pop();
					}
					
					if(dList.size()>0)
					{
					temp/=dList.get(dList.size()-1);
					dList.remove(dList.size()-1);
					}
					else
					{
						System.out.println();
					}
					
					if(tempPath.size()>0)
					tempPath.remove(tempPath.size()-1);
				
					
					
				} 
		
				
			}
			else if(ll!=null && ll.isEmpty())
			{
				
				if(dList.size()>0)
				{
				temp/=dList.get(dList.size()-1);
				dList.remove(dList.size()-1);
				}
				else
				{
					System.out.println();
				}
				
	if(de>0)
		de--;
	
	if(s.size()>0)
	{
	String tt = s.peek();
	visited[Integer.parseInt(tt)]=false;
	s.pop();
	}
	
				
				if(tempPath.size()>0)
				{
					tempPath.remove(tempPath.get(tempPath.size()-1));
				}
				
			}
			
			
			
			if(s.size() == 1)
			{
				if(listNeighbors.get(s.peek()) == null || listNeighbors.get(s.peek()).isEmpty())
				{
					s.pop();
				}
				
			}
		}
		
		System.out.println("Most influential path is");
		for(String ss : maxPath)
		{
			System.out.print("Traversing  "+ss+"\t");
		}
		
		return pathInfluence;
		
	}
	
	 public static HashMap sortByValues(HashMap map) { 
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       }.reversed());

	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }


	
	}
	
