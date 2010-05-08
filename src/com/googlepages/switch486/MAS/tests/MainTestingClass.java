package com.googlepages.switch486.MAS.tests;

import java.util.ArrayList;

import com.googlepages.switch486.MAS.Engine.Image.AIImage;
import com.googlepages.switch486.MAS.Engine.Image.Filter.Gabor.GaborFilter;
import com.googlepages.switch486.MAS.Engine.Image.Filter.Gabor.GaborParameterOutOfBoundsException;
import com.googlepages.switch486.MAS.Engine.Image.HistogramEqualizator.HistogramEqualizator;

public class MainTestingClass {
	
	public static String path="/home/switch486/MAS_TEST/";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<String> lista = new ArrayList<String>();
		lista.add( path+"one.png");
		lista.add( path+"wakacje1.jpg");
		lista.add( path+"lazer.png");
		lista.add( path+"wigi.png");
		lista.add( path+"m1.jpg");
		lista.add( path+"m2.jpg");
		lista.add( path+"m3.jpg");
		lista.add( path+"m4.jpg");
		lista.add( path+"myself.jpg");
		lista.add( path+"circ.png");
		lista.add( path+"circ_plus.png");
		lista.add( path+"circ_plus2.png");
		
		testList(lista, "HisTest");

		//testGaborFilterMax("with max...", s);
		//testGabor();
		//testGaborFilter("Gaborfilter output", s, 0);
		/*testGaborFilter("Gaborfilter output", s, 0.25);
		testGaborFilter("Gaborfilter output", s, 0.5);
		testGaborFilter("Gaborfilter output", s, 0.75);
		testGaborFilter("Gaborfilter output", s, 1);*/
		//testImage(s);
		/*double [][] fm = {{1d/9, 1d/9, 1d/9}, {1d/9, 1d/9, 1d/9}, {1d/9, 1d/9, 1d/9}};
		testFilter(fm, "Only ones here", s);
		double [][] f2 = {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};
		testFilter(f2, "the same as main Image?", s);
		double [][] f3 = {{0.25, 0, 0.25}, {0, 0, 0}, {0.25, 0, 0.25}};
		testFilter(f3, "hmm, consiter something like this", s);
		*/
	}
	
	private static void testList(java.util.List<String> lista, String extra_info){
		for (String s : lista ) {
			System.out.println("Starting for: "+s);
			testGaborFilterMax("with max...", s, extra_info);
		}
	}

	private static void testFilter(double [][] fm, String name, String s) {
		AIImage ai = new AIImage(s);
		GaborFilter g = null;
		try {
			g = new GaborFilter();
			g.setW0(0.5);
		} catch (GaborParameterOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		ai = g.filter(ai, fm);
		ai = g.filter(ai, fm);
		ai = g.filter(ai, fm);
		ai.toImage(name);
	}

	private static void testGabor() {
		GaborFilter g = null;
		
		try {
			g = new GaborFilter();
			g.setW0(0.5);
			g.setFilterMatrix(400, 400);
		} catch (GaborParameterOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		g.toImage();
	}
	
	private static void testGaborFilter(String name, String s, double angle) {
		AIImage ai = new AIImage(s);
		GaborFilter g = null;
		
		try {
			g = new GaborFilter();
			g.setW0(angle);
			g.setFilterMatrix(10, 10);
		} catch (GaborParameterOutOfBoundsException e) {
			e.printStackTrace();
		}
		ai = g.filter(ai);
		ai.toImage(name);
	}
	
	private static void testGaborFilterMax(String name, String s, String extra) {
		long t = System.currentTimeMillis();
		AIImage ai = new AIImage(s);
		GaborFilter g = null;
		ArrayList<AIImage> imagelist = new ArrayList<AIImage>();
		System.out.print("|");
		for (int i = 0; i < 4; i ++) {
				System.out.print(".");
				try {
					g = new GaborFilter();
					g.setW0(i*0.25d);
					g.setFilterMatrix(10, 10);
				} catch (GaborParameterOutOfBoundsException e) {
					e.printStackTrace();
				}
				imagelist.add(g.filter(ai));
				System.out.print(",");
				try {
					g = new GaborFilter();
					g.setW0(i*0.25d);
					g.setFilterMatrix(10, 10);
				} catch (GaborParameterOutOfBoundsException e) {
					e.printStackTrace();
				}
				imagelist.add(g.filter(ai.getInverted()));
		}
		System.out.print("|");
		AIImage result = AIImage.getMaxFromList(imagelist);
		System.out.print("+");
		/*HistogramEqualizator.showHistogram(result, "hisFirst");
		HistogramEqualizator.showDistribution(result, "disFirst");*/
		AIImage his = HistogramEqualizator.equalizeImage(result);
		/*HistogramEqualizator.showHistogram(his, "hislast");
		HistogramEqualizator.showDistribution(his, "dislast");*/
		System.out.print("<");
		//result.toImage(name);
		long tt = System.currentTimeMillis();
		System.out.println("\nPerformedTime: " + (tt-t)/1000 + " seconds");
		System.out.println("Saved under"+AIImage.saveImage(his, path, extra)); //not so good :P
	}
	
	private static void testImage(String s) {
		AIImage ai = new AIImage(s);
		
		ai.toImage();
	}

}
