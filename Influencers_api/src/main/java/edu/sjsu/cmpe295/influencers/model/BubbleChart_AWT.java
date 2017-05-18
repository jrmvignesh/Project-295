package edu.sjsu.cmpe295.influencers.model;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class BubbleChart_AWT extends ApplicationFrame {

	public BubbleChart_AWT( String s ) throws IOException {
		super( s );                 
		JPanel jpanel = createDemoPanel( );                 
		jpanel.setPreferredSize(new Dimension( 560 , 370 ) );                 
		setContentPane( jpanel ); 
	}
	
	
	private static JFreeChart createChart( XYZDataset xyzdataset ) {
		JFreeChart jfreechart = ChartFactory.createBubbleChart(
				"LEVEL vs INFLUENCE vs COVERAGE",                    
				"Influence",                    
				"Level",                    
				xyzdataset,                    
				PlotOrientation.HORIZONTAL,                    
				true, true, false);

		XYPlot xyplot = ( XYPlot )jfreechart.getPlot( );                 
		xyplot.setForegroundAlpha( 0.65F );                 
		XYItemRenderer xyitemrenderer = xyplot.getRenderer( );
		xyitemrenderer.setSeriesPaint( 0 , Color.red ); 
		xyitemrenderer.setSeriesPaint( 1 , Color.blue ); 
		xyitemrenderer.setSeriesPaint( 2 , Color.green);
		xyitemrenderer.setSeriesPaint( 3 , Color.yellow ); 
		NumberAxis numberaxis = ( NumberAxis )xyplot.getDomainAxis( );                 
		numberaxis.setLowerMargin( 0.2 );                 
		numberaxis.setUpperMargin( 0.5 );                 
		NumberAxis numberaxis1 = ( NumberAxis )xyplot.getRangeAxis( );                 
		numberaxis1.setLowerMargin( 0.8 );                 
		numberaxis1.setUpperMargin( 0.9 );

		return jfreechart;
	}

	public static XYZDataset createDataset( ) throws IOException {
		DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset(); 
		final String LEVEL3 = "Level 3";
		final String LEVEL4 = "Level 4";
		GraphDS g1 = new GraphDS(3,"EXPONENTIAL");
		//GraphDS g2 = new GraphDS(4,"CONSTANT_DECAY");
		//GraphDS g3 = new GraphDS(5,"CONSTANT_DECAY");
		//GraphDS g4 = new GraphDS(6,"CONSTANT_DECAY");
		//GraphDS g2 = new GraphDS(4,"CONSTANT_DECAY");
//		      double ad[ ] = { 30 , 40 , 50 , 60 , 70 , 80 };                 
//		      double ad1[ ] = { 10 , 20 , 30 , 40 , 50 , 60 };                 
//		      double ad2[ ] = { 4 , 5 , 10 , 8 , 9 , 6 };      
//		ArrayList<Double> a1 = new ArrayList<Double>();
//		ArrayList<Double> a2 = new ArrayList<Double>();
//		ArrayList<Double> a3 = new ArrayList<Double>();
		

		double ad[] = new double[100],ad2[] = new double[100];
		double ad1[] = new double[100];
		int i=0;
		for(Map.Entry<String, Double> eachPair : g1.nodeInfluences.entrySet()){
			ad[i] = eachPair.getValue();
			//ad1[i] = Double.parseDouble(eachPair.getKey());
			ad1[i] =(double)(g1.numNodes.get(eachPair.getKey())/(double)g1.numNodes.size());
			ad2[i] = 1.0;
			//ad2[i] = g1.numNodes.get(eachPair.getKey());
			//System.out.println("ad " + eachPair.getValue() + " ---ad1: "+ad1[i] + " ---ad2 "+ad2[i]);
			i++;
		}
		double ad3[][] = { ad , ad2 , ad1 };
		
		defaultxyzdataset.addSeries( "Coverage @ Level 1", ad3 );
		
		// for level 4
		double ad_4[] = new double[100],ad2_4[] = new double[100];
		double ad1_4[] = new double[100];
		
		GraphDS g2 = new GraphDS(2,"CONSTANT_DECAY");
		i=0;
		for(Map.Entry<String, Double> eachPair : g2.nodeInfluences.entrySet()){
			ad_4[i] = eachPair.getValue();
			//ad1[i] = Double.parseDouble(eachPair.getKey());
			ad1_4[i] =(double)(g2.numNodes.get(eachPair.getKey())/(double)g2.numNodes.size() );
			ad2_4[i] = 2.0;
			//ad2[i] = g1.numNodes.get(eachPair.getKey());
			i++;
		}
		double ad3_4[][] = { ad_4 , ad2_4 , ad1_4 };
		
		defaultxyzdataset.addSeries( "Coverage @ Level 2", ad3_4 );
		
		//level 5
		double ad_5[] = new double[100],ad2_5[] = new double[100];
		double ad1_5[] = new double[100];
		
		GraphDS g3 = new GraphDS(3,"CONSTANT_DECAY");
		i=0;
		for(Map.Entry<String, Double> eachPair : g3.nodeInfluences.entrySet()){
			ad_5[i] = eachPair.getValue();
			//ad1[i] = Double.parseDouble(eachPair.getKey());
			ad1_5[i] =(double)(g3.numNodes.get(eachPair.getKey())/(double)g3.numNodes.size()  );
			ad2_5[i] = 3.0;
			//ad2[i] = g1.numNodes.get(eachPair.getKey());
			i++;
		}
		double ad3_5[][] = { ad_5 , ad2_5 , ad1_5 };
		
		defaultxyzdataset.addSeries( "Coverage @ Level 3", ad3_5 );
		
		//level 6
				double ad_6[] = new double[100],ad2_6[] = new double[100];
				double ad1_6[] = new double[100];
				
				GraphDS g4 = new GraphDS(4,"CONSTANT_DECAY");
				i=0;
				for(Map.Entry<String, Double> eachPair : g4.nodeInfluences.entrySet()){
					ad_6[i] = eachPair.getValue();
					//ad1[i] = Double.parseDouble(eachPair.getKey());
					ad1_6[i] =(double)(g4.numNodes.get(eachPair.getKey())/(double)g4.numNodes.size() );
					ad2_6[i] = 4.0;
					//ad2[i] = g1.numNodes.get(eachPair.getKey());
					i++;
				}
				double ad3_6[][] = { ad_6 , ad2_6 , ad1_6 };
				
				defaultxyzdataset.addSeries( "Coverage @ Level 4", ad3_6 );
		

		return defaultxyzdataset; 
	}

	public static JPanel createDemoPanel( ) throws IOException {
		JFreeChart jfreechart = createChart( createDataset( ) );                 
		ChartPanel chartpanel = new ChartPanel( jfreechart );

		chartpanel.setDomainZoomable( true );                 
		chartpanel.setRangeZoomable( true );

		return chartpanel;
	}

}