package edu.sjsu.cmpe295.influencers.controller;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;
import org.hibernate.hql.internal.ast.ErrorReporter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import edu.sjsu.cmpe295.influencers.model.BubbleChart_AWT;
import edu.sjsu.cmpe295.influencers.model.DestValueDS;
import edu.sjsu.cmpe295.influencers.model.DestValueInf;
import edu.sjsu.cmpe295.influencers.model.EdgeGraphDS;
import edu.sjsu.cmpe295.influencers.model.ErrorResponse;
import edu.sjsu.cmpe295.influencers.model.GraphDS;
import edu.sjsu.cmpe295.influencers.model.NodeInfluencer;
import edu.sjsu.cmpe295.influencers.model.NodesvsInfluence;
import edu.sjsu.cmpe295.influencers.service.NodeService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by vignesh on 4/18/2017.
 */

@RestController
public class NodeController {

	@Autowired
	NodeService nodeService;
	
	static HashMap<String, List<DestValueInf>> adjListInf = new HashMap<String, List<DestValueInf>>();
	static HashMap<String, List<DestValueDS>> adjListInv = new HashMap<String, List<DestValueDS>>();
	static HashMap<String, List<DestValueInf>> n = new HashMap<String, List<DestValueInf>>();
	
/*<!---------------------------------------- Shibunath Shanker Algorithm----------------------!>*/
	
	@RequestMapping(value = "/thresholdbasedInfluence/{startNodeId}/{thresholdValue}/", method = RequestMethod.GET)
	public NodeInfluencer getThresholdPathInfluence(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("startNodeId") String NodeId, @PathVariable("thresholdValue") String thresholdValue)
			throws IOException {

		String dir = "src/main/resources/static/images";
		File folder = new File(dir);
        File fList[] = folder.listFiles();
        System.out.println("shibu "+ folder);
        for (File f : fList) {
        	
            if (f.getName().endsWith(".jpeg") || f.getName().endsWith(".txt") ) {
                f.delete(); 
            }
            }
		
			int trial=0;
		
			System.out.println("Welcome Shibu");
			System.out.println(NodeId);
			System.out.println(thresholdValue);
		
		
		//adjacency list to represent graph
	//	HashMap<String, List<DestValueDS>> adjListInv = new HashMap<String, List<DestValueDS>>();
	//	HashMap<String, List<DestValueInf>> adjListInf = new HashMap<String, List<DestValueInf>>();
		List<Edge> pl = new ArrayList<Edge>();
		HashMap<String,List<List<Edge>>> paths = new HashMap<>();
		Double discount = 0.9;
		HashMap<String,Double> pathInfluence = new HashMap<String,Double>();
		HashMap<String,Double> totalInfluence = new HashMap<String,Double>();
		HashMap<String,Double> nodeInfluence = new HashMap<String,Double>();
		
		HashMap<String,Double> totalInfluenceValues = new HashMap<String,Double>();
		HashMap<String,Double> totalInfluenceValues_1 = new HashMap<String,Double>();
		//node influence
		HashMap<String, Double> outgoingSum = new HashMap<String,Double>();
		
		//change path of the file
		//String filePath = "D:/eclipse workspace/Influencers_api/src/main/resources/astro-ph.gml";
		String filePath = "src/main/resources/karate.gml";
		
		Graph g = new DefaultGraph("g");
		
		
	//	System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
	/*	Graph gg = new DefaultGraph("p");
	
		gg.addNode("A");
		gg.addNode("B");
		gg.addNode("C");
		gg.addNode("D");
		gg.addNode("E");
		gg.addNode("F");
		gg.addEdge("t", "B", "A");
		gg.addEdge("r", "C", "B");
		gg.addEdge("u", "D", "B");
		gg.addEdge("j", "E", "C");
		gg.addEdge("m", "F", "C");
		gg.addEdge("n", "E", "D");
		gg.addEdge("b", "F", "D");*/
	
		
		FileSource fs = FileSourceFactory.sourceFor(filePath);

		fs.addSink(g);
		//FileReader fr = new FileReader("astro-ph.gml");

//fs.begin(filePath);



	fs.readAll(filePath);


	


		//initializing the adjacency list with empty lists
		//initializing the outgoing sum to 0 for each node 
		for(Node n : g) {			
			adjListInv.put(n.getId(), new ArrayList<DestValueDS>());
			adjListInf.put(n.getId(), new ArrayList<DestValueInf>());
		//	listNeighbors.put(n.getId(), new ArrayList<DestValueInf>());
			outgoingSum.put(n.getId(), 0.0);
					
		}
		
		System.out.println("number of nodes :"+adjListInv.size());
int count = g.getEdgeCount();

int pp = 0;

		for(Edge e : g.getEdgeSet()){
			
			List<DestValueDS> destValList = adjListInv.get(e.getSourceNode().getId());
			List<DestValueDS> sourceValList = adjListInv.get(e.getTargetNode().getId());
			
		//	List<DestValueInf> destValInfList = adjListInf.get(e.getTargetNode().getId());
			
	//		DestValueInf destValueInf = new DestValueInf(e.getSourceNode().getId(),0.0);
		//	destValInfList.add(destValueInf);
			String cmp = "775";
			if(e.getSourceNode().getId().trim().equals(cmp))
			{
	//			System.out.println();
			}
			
			
			//DestValueDS sourceVal = new DestValueDS(e.getSourceNode().getId(),e.getAttribute("value"),0.0);

			DestValueDS sourceVal = new DestValueDS(e.getSourceNode().getId(),"1",0.0);
			DestValueDS destVal = new DestValueDS(e.getTargetNode().getId(),"1",0.0);
//		
			
		//DestValueDS destVal = new DestValueDS(e.getTargetNode().getId(),e.getAttribute("value"),0.0);

		
			
			destValList.add(destVal);
			sourceValList.add(sourceVal);
			
			adjListInv.put(e.getSourceNode().getId(), destValList);
			adjListInv.put(e.getTargetNode().getId(),sourceValList);
			
	//		adjListInf.put(e.getTargetNode().getId(), destValInfList);
			
			outgoingSum.put(e.getSourceNode().getId(), outgoingSum.get(e.getSourceNode().getId())+destVal.getValue());
			
			//Double thisoutgoingSum = outgoingSum.get(e.getSourceNode().getId()) + destVal.getValue();
			//pp++;	
		}
		
	//	System.out.println("Count is "+count);
	//	System.out.println("actual is "+(pp));
		
	
		
		for(Map.Entry<String, List<DestValueDS>> entry: adjListInv.entrySet())
		{
			Double sum = 0.0;
		
			List<DestValueDS> ll = entry.getValue();
		//	List<DestValueInf> iff =new ArrayList<DestValueInf>();
			
			for(DestValueDS p : ll)
			{
			
				sum+=p.getValue();
			}
			
			for(DestValueDS p : ll)
			{
	/*			if(p.getDest().equals("18"))
				{
					System.out.println("The source is "+ entry.getKey());
					System.out.println("Destination is "+p.getDest());
					System.out.println("Value is "+p.getValue());
				}*/
				Double inf = p.getValue()/sum;
				
				DestValueInf lp = new DestValueInf(entry.getKey(),inf);
				
				List<DestValueInf> iff = adjListInf.get(p.getDest());
		//		List<DestValueInf> ifff =  listNeighbors.get(p.getDest());
			//	DestValueInf lp = iff.
				
				if(p.getDest().equals("1451") && entry.getKey().equals("980"))
				{
					System.out.println();
				}
				
				iff.add(lp);
				adjListInf.put(p.getDest(),iff);
			//	listNeighbors.put(p.getDest(),ifff);
			}
			
		}
		
		for(Map.Entry<String, List<DestValueInf>> entry : adjListInf.entrySet())
		{
	//		System.out.println("Source node is "+entry.getKey());
			
			List<DestValueInf> listt = adjListInf.get(entry.getKey());
			
			for(DestValueInf d : listt)
			{
		//		System.out.println("Destination "+d.getDest()+"  Influence is "+d.getInfluence());
			}
		}
		
		Double depthOfN=0.0;
		String influenceOfN="";
		String NID="";
		
		
		
	int counts = 0;
		for(Node n : g)
		{
			for(DestValueInf p : adjListInf.get(n.getId()))
			{
		//		System.out.println("Influence of "+n.getId()+" on "+p.getDest()+" is "+p.getInfluence());
			}
			counts++;
			if(counts == 10)
				break;
		}
		
		for(Node n : g)
		{
			//System.out.println("Node is "+n);
			Double inf = 0.0;
			
			
			for(DestValueInf p : adjListInf.get(n.getId()))
			{
				if(n.getId().equals("14"))
				{
		System.out.println();
				}
				inf+=p.getInfluence();
			}
			
			if(n.getId().equals("0"))
			{
	//			System.out.println("Influence of  "+n.getId() +inf);
				
	//			System.out.println("Count is "+trial);
			}
			nodeInfluence.put(n.getId(), inf);
		
		}
		
	//	if(adjListInf.get("18").isEmpty()){
	//		System.out.println("Empty");
	//	}
		
		int c = 0;
	//	System.out.println("Two level paths");
		
		File file = new File("src/main/resources/static/images/InfluenceResultsKarateFinal.txt");
		File file_ranking = new File("src/main/resources/static/images/InfluenceRanksKarateFinal.txt");
		file.createNewFile();
		file_ranking.createNewFile();
		
		FileWriter fw =new FileWriter(file);
		FileWriter fw1 = new FileWriter(file_ranking);
		fw1.write("Ranking\n");
		  DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
		  DefaultCategoryDataset bar_char_dataset = new DefaultCategoryDataset();
		  DefaultCategoryDataset bar_char_dataset_karate = new DefaultCategoryDataset();
		  
		  
		  
		  
		  Double min_value=Double.MAX_VALUE,max_value=Double.MIN_VALUE;
		for(Node n : g)
		{

			if(n.getId().equals(NodeId))
			{
				Double k=0.01;
				
				while(k<0.21)
				{
					int xx=2;
					Double sumInfluence=0.0;
					while(nodeService.depthInf(adjListInf,adjListInv,n.getId(),xx,k)>0)
					{
						sumInfluence+=nodeService.depthInf(adjListInf,adjListInv,n.getId(),xx,k);
						xx++;
					}
					sumInfluence+=nodeInfluence.get(NodeId);
					String f = String.format("%.2f", k);
					
					line_chart_dataset.addValue(sumInfluence, "Influence of Node "+NodeId, f);
					k+=0.01;
				}
			}
			
		
			Double nodeIn = nodeInfluence.get(n.getId());
	System.out.println(thresholdValue);
			Double t = Double.parseDouble(thresholdValue);
			Double t1 = 0.05;
			
				int x=2,y=2;
				Double sum=0.0;
				Double sum1=0.0;
			while(nodeService.depthInf(adjListInf,adjListInv,n.getId(),x,t)>0)
			{

				sum+=nodeService.depthInf(adjListInf,adjListInv,n.getId(),x,t);
				
				x++;
			}
			
			String yy = String.format("%.2f", t);
					
			if(sum==0 && x==2)
			{
				x=1;
			}
			else if(x>2)
				x--;
			
			sum += nodeIn;
			System.out.println("Influence at "+yy+" is "+sum);
			sum1 += nodeIn;
	
			if(sum<min_value)
				min_value=sum;
			
			if(sum>max_value)
			max_value=sum;
			
			bar_char_dataset.addValue(sum, "Influence at threshold "+thresholdValue, n.getId());

			
		
			totalInfluenceValues.put(n.getId(), sum);
		
			 if(n.getId().equals(NodeId))
			 {
				 NID=NodeId;
				 influenceOfN = String.format("%.2f", sum);
				 depthOfN = Double.parseDouble(x+"");
			 }
		
			 
			 String totInfluence=String.format("%.2f", sum);
				if(sum>0)
				System.out.println("Total influence for nodeID "+n.getId() +" is " +totInfluence+" traversing paths upto depth "+x);
				else
					System.out.println("Total influence for nodeID "+n.getId() +" is " +totInfluence+" traversing no paths");
				if(sum>0)
				fw.write("Total influence for nodeID "+n.getId() +" is " +totInfluence+" traversing paths upto depth "+x+"\n");
				else
					fw.write("Total influence for nodeID "+n.getId() +" is "  +totInfluence+" traversing no paths \n");	
	//		}
		
			
	//closing			
			
	
		
		//System.out.println("Size of totalInfluenceValues is "+totalInfluenceValues_1.size());
	
	
		}
		
		totalInfluenceValues = nodeService.sortByValues(totalInfluenceValues);
		
//	finalResults.putAll(totalInfluenceValues);
//	System.out.println("Size of final results is "+finalResults.size());
	int i = 1;
	
	for(Map.Entry<String, Double> entry : totalInfluenceValues.entrySet())
	{

		
		fw1.write("NodeID is "+entry.getKey()+" Rank is "+i +" Influence is "+entry.getValue()+"\n");
		System.out.println(String.format("%.4f", entry.getValue()));
	//	bar_char_dataset.addValue(entry.getValue(), "Influence at threshold "+thresholdValue, entry.getKey());
		
		i++;		
	
	}
		
		
	//      line_chart_dataset.addValue( 15 , "schools" , "1970" );
	 //     line_chart_dataset.addValue( 30 , "schools" , "1980" );
	  //    line_chart_dataset.addValue( 60 , "schools" , "1990" );
	   //   line_chart_dataset.addValue( 120 , "schools" , "2000" );
	    //  line_chart_dataset.addValue( 240 , "schools" , "2010" ); 
	     // line_chart_dataset.addValue( 300 , "schools" , "2014" );
		
		int a[]={16,
				9,
				10,
				6,
				3,
				4,
				4,
				4,
				5,
				2,
				3,
				1,
				2,
				5,
				2,
				2,
				2,
				2,
				2,
				3,
				2,
				2,
				2,
				5,
				3,
				3,
				2,
				4,
				3,
				4,
				4,
				6,
				12,
				17,
};
		for(int ii=1;ii<=34;ii++)
		{
			
			Double val = ((a[ii-1]-1)*(max_value-min_value)/(16))+min_value;
			bar_char_dataset.addValue(val, "Number of Friendships", ii+"");
		}
	     
			JFreeChart barChartObject = ChartFactory.createBarChart("NodeID vs Influence", "NodeID", "Influence", bar_char_dataset);
			JFreeChart barChartObject_karate = ChartFactory.createBarChart("NodeID vs Influence", "NodeID", "Influence", bar_char_dataset_karate);
	      JFreeChart lineChartObject = ChartFactory.createLineChart(
	         "Threshold vs Influence","NodeID",
	         "Influence",
	         line_chart_dataset,PlotOrientation.VERTICAL,
	         true,true,false);

	      int width = 1280;    /* Width of the image */
	      int height = 960;   /* Height of the image */ 
	      File lineChart = new File( "src/main/resources/static/images/InfluenceLineGraph.jpeg" ); 
	      File barChart = new File( "src/main/resources/static/images/InfluenceBarGraph.jpeg" ); 
	      File barChart_karate = new File( "src/main/resources/static/images/InfluenceForKarate_Ground_Truth.jpeg" ); 
	      ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, width ,height);
	      ChartUtilities.saveChartAsJPEG(barChart ,barChartObject, width ,height);
	    //  ChartUtilities.saveChartAsJPEG(barChart_karate ,barChartObject_karate, width ,height);
	//	fw.flush();
		fw.close();
	     fw1.close();
		
		for(Map.Entry<String, Double> val : pathInfluence.entrySet())
		{
		//	System.out.println("Node is "+val.getKey());
		//	System.out.println("Value is "+val.getValue());
			
			totalInfluence.put(val.getKey(), val.getValue() + nodeInfluence.get(val.getKey()));
		//	System.out.println("Total Influence for node "+" "+val.getKey() +"   "+totalInfluence.get(val.getKey()));
		}
		
		
		//Printing the nodes
		for(Map.Entry<String, Double> val : totalInfluence.entrySet())
		{
			//System.out.println("Total Node is "+val.getKey());
				//System.out.println("Value is "+val.getValue());
		}
		
		Double result = nodeService.mostInfluentialPath(adjListInf,adjListInv,"34","33",0.05);
		System.out.println("Result is "+result);
		NodeInfluencer nodeInfluencer = new NodeInfluencer();
		nodeInfluencer.setNodeId(NID);
	
		nodeInfluencer.setTotalInfluence(influenceOfN);
		nodeInfluencer.setDepth(depthOfN);
		System.out.println("Setting "+NID+influenceOfN+depthOfN);
		return nodeInfluencer;

	}
	/*<!---------------------------------------- Dhanya Ramesh Algorithm----------------------!>*/
	
	
	//Visualization Class
	@RequestMapping(value = "/strongesthPathInfluence/{startNodeId}/{thresholdValue}/", method = RequestMethod.GET)
	public NodeInfluencer getStrongestPathInfluence(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("startNodeId") String NodeId, @PathVariable("thresholdValue") String thresholdValue)
			throws IOException {
		

		String dir = "src/main/resources/static/images";
		File folder = new File(dir);
        File fList[] = folder.listFiles();
        System.out.println("DHANYA "+ folder);
        for (File f : fList) {
        	
            if (f.getName().endsWith(".jpeg") || f.getName().endsWith(".txt") ) {
                f.delete(); 
            }
            }
        
		final String LEVEL3 = "Level 3";
		final String LEVEL4 = "Level 4";


		DefaultCategoryDataset dataset_influence = new DefaultCategoryDataset( );
		DefaultCategoryDataset dataset_number = new DefaultCategoryDataset( );

		GraphDS g1 = new GraphDS(3,"EXPONENTIAL");
		GraphDS g2 = new GraphDS(4,"EXPONENTIAL");
		GraphDS g5 = new GraphDS(5,"EXPONENTIAL");
		GraphDS g6 = new GraphDS(6,"EXPONENTIAL");
		
		
		
				
		System.out.println("----------");
		System.out.println("num nodes size"+g1.numNodes.size());
		Iterator it1 = g1.nodeInfluences.entrySet().iterator();


		while(it1.hasNext()){
			Map.Entry<String,Double> pair = (Map.Entry)it1.next();
			//Iterator it2 = g2.nodeInfluences.entrySet().iterator();

			if(g2.nodeInfluences.containsKey(pair.getKey()) && pair.getValue()>0.0){
				dataset_number.addValue((double)( 100.00*g1.numNodes.get(pair.getKey())/(double)g1.numNodes.size() /*getNumberOfNodes(g1.nodeInfluences.keySet(),g1.numNodes)*/), "Level 3", pair.getKey());
				dataset_number.addValue((double)( 100.00*g2.numNodes.get(pair.getKey())/(double)g1.numNodes.size()  /*getNumberOfNodes(g2.nodeInfluences.keySet(),g2.numNodes)*/), "Level 4", pair.getKey());
				dataset_number.addValue((double)( 100.00*g5.numNodes.get(pair.getKey())/(double)g1.numNodes.size()  /*getNumberOfNodes(g1.nodeInfluences.keySet(),g1.numNodes)*/), "level 5", pair.getKey());
				dataset_number.addValue((double)(100.00*g6.numNodes.get(pair.getKey())/(double)g1.numNodes.size()  /*getNumberOfNodes(g2.nodeInfluences.keySet(),g2.numNodes)*/), "level 6", pair.getKey());
				
				dataset_influence.addValue(pair.getValue(), "Level 3", pair.getKey());
				dataset_influence.addValue(g2.nodeInfluences.get(pair.getKey()), "Level 4", pair.getKey());
				dataset_influence.addValue(g5.nodeInfluences.get(pair.getKey()), "level 5", pair.getKey());
				dataset_influence.addValue(g6.nodeInfluences.get(pair.getKey()), "level 6", pair.getKey());
				
			}

		}
		int width = 1280;    // Width of the image 
		int height = 960;    //Height of the image  
		//chart for influence

		JFreeChart barChart = ChartFactory.createBarChart(
				"NODE INFLUENCE BASED ON LEVEL", 
				"Node ID", "Influence", 
				dataset_influence,PlotOrientation.VERTICAL, 
				true, true, false);

		

	
		File BarChart = new File("src/main/resources/static/images/ap_influence.jpeg"); 
		
		ChartUtilities.saveChartAsJPEG( BarChart , barChart , width , height );

		//chart for number of influenced nodes

		JFreeChart barChart2 = ChartFactory.createBarChart(
				"NUMBER OF INFLUENCED NODES", 
				"Node ID", "Coverage", 
				dataset_number,PlotOrientation.VERTICAL, 
				true, true, false);



		File BarChart2 = new File("src/main/resources/static/images/ap_number.jpeg"); 
		ChartUtilities.saveChartAsJPEG( BarChart2 , barChart2 , width , height );
		
		
		
		/*
		 * Graphs for decay factor changes
		 */

		DefaultCategoryDataset dataset_df = new DefaultCategoryDataset();
		g1 = new GraphDS(4,"EXPONENTIAL");
		g2 = new GraphDS(4,"CONSTANT_DECAY");
		GraphDS g3 = new GraphDS(4," ");
		
		final String EXPONENTIAL = "EXPONENTIAL";
		final String CONSTANT_DECAY = "CONSTANT_DECAY";
		final String STATIC = "STATIC";
		
		it1 = g1.nodeInfluences.entrySet().iterator();


		while(it1.hasNext()){
			
			Map.Entry<String,Double> pair = (Map.Entry)it1.next();
			Iterator it2 = g2.nodeInfluences.entrySet().iterator();

			if(g2.nodeInfluences.containsKey(pair.getKey()) && g3.nodeInfluences.containsKey(pair.getKey()) && pair.getValue()>0.0){
				
				dataset_df.addValue(pair.getValue()/*/g1.numNodes.get(pair.getKey())*/, EXPONENTIAL, pair.getKey());
				dataset_df.addValue(g2.nodeInfluences.get(pair.getKey())/*/g2.numNodes.get(pair.getKey())*/ , CONSTANT_DECAY, pair.getKey());
				dataset_df.addValue(g3.nodeInfluences.get(pair.getKey())/*/g3.numNodes.get(pair.getKey())*/ , STATIC, pair.getKey());
				
			}

		}

		JFreeChart barChart_df = ChartFactory.createBarChart(
				"NODE INFLUENCE BASED ON DECAY FACTOR FOR LEVEL 4", 
				"Category", "Score", 
				dataset_df,PlotOrientation.VERTICAL, 
				true, true, false);

	
		
		
		File BarChart_df = new File("src/main/resources/static/images/ap_decayFactor.jpeg"); 
		ChartUtilities.saveChartAsJPEG( BarChart_df , barChart_df , width , height );
		

		
		return null;
		

	}
	
	public static double getNumberOfNodes(Set<String> nodeInf,Map<String, Integer> numNodes){
		double totalNodes=0.0;
		//calculating the number of nodes in numnodes
		for(String thisNode:nodeInf){
			totalNodes += numNodes.get(thisNode);
		}
		return totalNodes;
	}
	
	/*<!---------------------------------------- Samhita Pyla Shanker Algorithm----------------------!>*/
	
	@RequestMapping(value = "/edgethresholdInfluence/{nodeCount}/{thresholdValue}/", method = RequestMethod.GET)
	
	public void getEdgeBasedInfluence(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("nodeCount") String NodeId, @PathVariable("thresholdValue") String thresholdValue)
			throws Exception {
		

		String dir = "src/main/resources/static/images";
		File folder = new File(dir);
        File fList[] = folder.listFiles();
        System.out.println(" SAMHITA "+ folder);
        for (File f : fList) {
        	
            if (f.getName().endsWith(".jpeg") || f.getName().endsWith(".txt") ) {
                f.delete(); 
            }
            }
		
		Double threshold = Double.parseDouble(thresholdValue);	
		 int number = Integer.parseInt(NodeId);
		
		EdgeGraphDS graph=new EdgeGraphDS(threshold); // Call this constructor to pass first input   
		 graph.main(null);    // Call the main method to run the GraphDS file first

		 NodesvsInfluence no=new  NodesvsInfluence(number); // Call this constructor to pass second input   
	        NodesvsInfluence.main(null); // Call the main method to run this file only after the GraphDS file is run
		

		
	
	}
	
	
	
	
	

	
}
