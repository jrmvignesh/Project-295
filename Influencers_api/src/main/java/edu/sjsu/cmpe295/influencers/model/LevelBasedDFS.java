package edu.sjsu.cmpe295.influencers.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LevelBasedDFS {
	Double Influence;
	Integer NumberOfNodes;

	public LevelBasedDFS(HashMap<String, List<DestValueDS>> adjList, String key, int level,String decayFactor) {
		this.DFS(adjList,key,level,decayFactor);
	}

	public void DFS(HashMap<String, List<DestValueDS>> adjList,String source,int level,String decayFactor){

		ArrayList<String> stack = new ArrayList<String>();
		ArrayList<DestValueDS> removedNodes = new ArrayList<DestValueDS>();
		int thislevel=0;
		double pathInf = (double)1;
		double thisPathInf = (double)0;
		stack.add(source);
		List<DestValueDS> paths = new ArrayList<DestValueDS>();
		while(level>=thislevel){
			thisPathInf=0;
			List<DestValueDS> neighboursList = adjList.get(source);
			if(neighboursList!=null && !neighboursList.isEmpty()){
				double thisEdgeInf = neighboursList.get(0).getValue();

				stack.add(neighboursList.remove(0).getDest());

				source = stack.get(stack.size()-1);

				thislevel ++;
				
				//calculate path influence with decay factor as 1/depth
				if(decayFactor == "EXPONENTIAL"){
					thisPathInf = Math.pow(thislevel, -1)* thisEdgeInf;
				}
				else if(decayFactor == "CONSTANT_DECAY"){
					thisPathInf = thisEdgeInf * (1 - thislevel*0.1);
				}
				else
					thisPathInf = thisEdgeInf * 0.8;
				
				pathInf = pathInf * thisPathInf;
				
				// adding to path only if greater than or equal to already existing value
				DestValueDS temp = new DestValueDS(source,pathInf);
				
				if(!PathContainsNode(paths,source)){
					paths.add(temp);
				}
				else{

					updateStrongestValue(paths,source,pathInf);
					//					int prevPathInfIndex = paths.indexOf(source);
					//					paths.
					//					
					//					System.out.println("prev index" + prevPathInfIndex);
					//					double prevPathInf = paths.get(prevPathInfIndex).getValue();
					//					if(prevPathInf< pathInf){
					//						paths.add(temp);
					//					}
					//					else{
					//						System.out.println("not strongest path");
					//					}
				}

			}
			else{
				removedNodes.add(new DestValueDS(stack.remove(stack.size()-1),pathInf));
				if(stack.size()>0)
					pathInf = getInfValueOfNode(paths, stack.get(stack.size()-1));
				//pathInf = pathInf - getInfValueOfNode(paths,paths.get(paths.size()-1).getDest())-getInfValueOfNode(paths,removedNodes.get(removedNodes.size()-1).getDest());//(removedNodes.get(removedNodes.size()-1).getValue() - removedNodes.get(removedNodes.size()-2).getValue());
				//pathInf = pathInf - thisPathInf;
				thislevel--;
			}

			if(thislevel > level){
				removedNodes.add(new DestValueDS(stack.remove(stack.size()-1),pathInf));
				//pathInf = pathInf - getInfValueOfNode(paths,removedNodes.get(removedNodes.size()-1).getDest());
				pathInf = pathInf - thisPathInf;
				paths.remove(paths.size()-1);
				thislevel--;
			}

			if(stack.size()>0)
				source = stack.get(stack.size()-1);
			else{
				double inf=0;
				for(DestValueDS thisDestValue: paths){
					//System.out.println("Node " +thisDestValue.getDest() + " Influence "+ thisDestValue.getValue());
					inf += thisDestValue.getValue();

				}
				//System.out.println("Number of nodes influenced "+ paths.size() );
				//System.out.println("Influence of Node "+ inf);
				Influence = inf;
				NumberOfNodes=paths.size();
				break;

			}
		}
		
		//return (double)0;
	}

	private static double getInfValueOfNode(List<DestValueDS> paths, String dest) {
		for(DestValueDS destValue:paths){
			if(destValue.getDest()==dest){
				return destValue.getValue();
			}
		}
		
		
		return 0;
	}

	private static boolean PathContainsNode(List<DestValueDS> paths, String source) {
		
		for(DestValueDS thisDestValue: paths){
			if(thisDestValue.getDest() == source){
				return true;
			}
		}
		return false;
	}

	private static void updateStrongestValue(List<DestValueDS> paths, String source, double pathInf) {
		
		for(DestValueDS thisDestValue: paths){
			if(thisDestValue.getDest() == source){
				if(thisDestValue.getValue() < pathInf){
					thisDestValue.value = pathInf;
				}
				else{
					//System.out.println("existing value of "+ thisDestValue.getDest() + " value "+ thisDestValue.getValue()+ " greater than current " +pathInf);
				}


			}
		}


	}

}

