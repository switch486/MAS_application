package com.googlepages.switch486.MAS.Engine.Image.HistogramEqualizator;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.googlepages.switch486.MAS.Engine.Image.AIImage;


public class HistogramEqualizator {
	/*
	 * http://www.algorytm.org/index.php?option=com_content&task=view&id=140&Itemid=28
	 */
	
	private int width;
	private int height;
	private int mul;
	private int [] histogram=null;
	private double [] H = null;
	
	private int max (int [] t) {
		int val=0;
		for (int i=0; i<255; i++) {
			if (t[i]>val) {
				val = t[i];
			}
		}
		return val;
	}
	
	/**
	 * @param input image that will be transformed using a histogram
	 */
	public HistogramEqualizator(AIImage input) {
		this.histogram= new int[256];
		this.width = input.getWidth();
		this.height = input.getHeight();
		this.mul = width * height;
		for (int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				histogram[new Color(input.getRGB(i, j)).getRed()]++;
			}
		}
	}

	/**
	 * @param i a number in range 0-255
	 * @return cumulative distribution function == accumulated, normalized histogram
	 * D(n); H(i); colour distribution in the image
	 */
	private double cdf (int i) {
		int values=0; 
		for (int j=0; j<=i; j++) {
			values+= this.histogram[j];
		}
		return (double)values/this.mul;
	}
	
	/**
	 * @return a table that represents a cdf table
	 */
	private void countCdfTable () {
		this.H = new double [256];
		this.H[0]=(double)this.histogram[0]/this.mul;
		for (int i=1; i<256; i++) {
			this.H[i] = this.H[i-1]+((double)this.histogram[i]/this.mul);
		}
	}
	
	public static AIImage equalizeImage (AIImage input) {
		HistogramEqualizator he = new HistogramEqualizator(input);
		he.countCdfTable();
		AIImage out = new AIImage(he.width, he.height, BufferedImage.TYPE_INT_ARGB);
		for (int i=0; i<he.width; i++) {
			for (int j=0; j<he.height; j++) {
				int val = (int) (255*he.H[new Color(input.getRGB(i, j)).getRed()]);
				out.setRGB(i, j, new Color(val, val, val).getRGB());
			}
		}
		return out;
	}
	
	public static void showHistogram (AIImage input, String s) {
		HistogramEqualizator he = new HistogramEqualizator(input);
		final AIImage out = new AIImage(he.width, he.height, BufferedImage.TYPE_INT_ARGB);
		int max = he.max(he.histogram);
		for (int i=0; i<255; i++) {
				out.setRGB(i, 255-(int)(255*((float)he.histogram[i]/max)), Color.BLACK.getRGB());
		}
		JFrame f = new JFrame(s);
		f.setLocation(100, 100);
		f.setBounds(100, 100, 300, 300);
		//f.setResizable(false);
		Container content = new JPanel() {
			private static final long serialVersionUID = -2978325628848188017L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(out, 0, 0, this);

			}
		};
		f.setContentPane(content);
		f.setVisible(true);
	}
	
	public static void showDistribution (AIImage input, String s) {
		HistogramEqualizator he = new HistogramEqualizator(input);
		he.countCdfTable();
		final AIImage out = new AIImage(he.width, he.height, BufferedImage.TYPE_INT_ARGB);
		for (int i=0; i<255; i++) {
			out.setRGB(i, 255-(int)(255*(he.H[i])), Color.BLACK.getRGB());
		}
		JFrame f = new JFrame(s);
		f.setLocation(100, 100);
		f.setBounds(100, 100, 300, 300);
		//f.setResizable(false);
		Container content = new JPanel() {
			private static final long serialVersionUID = -2978325628848188017L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(out, 0, 0, this);

			}
		};
		f.setContentPane(content);
		f.setVisible(true);
		
	}
	
}
