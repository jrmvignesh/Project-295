package edu.sjsu.cmpe295.influencers.model;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;


public class NodesvsInfluence {
	
	static DefaultCategoryDataset dataset=new DefaultCategoryDataset();
  static int noofnodes=0;
  public NodesvsInfluence(int number){
	  noofnodes=number;
  }
	public static void main(String[] args) throws Exception{
		
		
	  InputStream keys=new FileInputStream(new File("src/main/resources/static/images/keyset.txt"));
	  InputStream values=new FileInputStream(new File("src/main/resources/static/images/valueset.txt"));
	  
	 /* InputStream keysnext=new FileInputStream(new File("./keyset0.002.txt"));
	  InputStream valuesnext=new FileInputStream(new File("./valueset0.002.txt"));*/
	  
	  BufferedReader keyreader= new BufferedReader(new InputStreamReader(keys));
	  BufferedReader valuereader= new BufferedReader(new InputStreamReader(values));

//	  noofnodes=20;
	  DefaultCategoryDataset finalset=graphGen(keyreader,valuereader,noofnodes);
	  
	  keyreader.close();
	  valuereader.close();
	  
	 JFreeChart barChartObjects = ChartFactory.createBarChart(
			 "NODES Vs INFLUENCE VALUES", 
			 "NODEID",
			 "INFLUENCE VALUE",
			 finalset, 
			 PlotOrientation.VERTICAL,
			 true, 
			 true, false);
	 int width= 2040;
	 int height= 880;
	 File barChart = new File("src/main/resources/static/images/influencevsnodes.jpeg");
	 ChartUtilities.saveChartAsJPEG(barChart,barChartObjects , width, height);
	  
	}
	static DefaultCategoryDataset graphGen(BufferedReader keyreader,BufferedReader valuereader,int noofnodes) throws NumberFormatException, IOException{
		int count=0;
		String k=null,v=null;
		 while(((k=keyreader.readLine())!=null)&&((v=valuereader.readLine())!=null)){    
		        if(count<noofnodes){
		       	dataset.addValue(Double.parseDouble(v), "INFLUENCE",k);
		        }
		count++;
			  }
		return dataset;
	}
	
}


