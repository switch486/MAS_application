package com.googlepages.switch486.MAS.Engine.Image;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AIImage extends BufferedImage implements IsToImageable { 
	
	/**
	 * the same as in the parrent class
	 * @param width 
	 * @param height
	 * @param imagetype
	 */
	public AIImage(int width, int height, int imagetype) {
		super(width, height, imagetype);
	}
	
	/**
	 * @param ima the image the object will be created WITH
	 */
	public AIImage(BufferedImage ima) {
		super(ima.getColorModel(), ima.getRaster(), false, null);
		//setting the image into grayscale
		int iWidth = getWidth();
		int iHeight = getHeight();
		for (int x=0; x<iWidth; x++) {// foreach pixel in the width
			for (int y=0; y<iHeight; y++){// foreach pixel in the height
				Color c = new Color(getRGB(x, y));
				int sum = c.getRed() + c.getGreen() + c.getBlue();
				sum/=3;
				this.setRGB(x, y, new Color(sum, sum, sum).getRGB());
			}
		}
	}
	
	public void toImage() {
		toImage("ToImage()");
	}
	
	public void toImage(String s) {
		
		JFrame f = new JFrame(s);
		f.setLocation(100, 100);
		int x = getWidth();
		int y = getHeight();
		f.setBounds(100, 100, x, y);
		//f.setResizable(false);
		Container content = new JPanel( ){
			/**
			 * 
			 */
			private static final long serialVersionUID = -2978325628848188017L;

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if (this!=null) {
				g.drawImage(AIImage.this, 0, 0, this);
			}
		}};
		f.setContentPane(content);
		f.setVisible(true);
		
	}
	
	/**
	 * @param imag - the secomd image 
	 * Transformation goes as follows. the max is performed on THIS image: this = max{this, imag}
	 */
	public void setThisMax (AIImage imag) {
		int xsize = this.getWidth();
		int ysize = this.getHeight();
		for (int x=0; x<xsize; x++) {
			for (int y=0; y<ysize; y++) {
				this.setRGB(x, y, Math.max(this.getRGB(x, y), imag.getRGB(x, y)));
			}
		}
	}
	
	/**
	 * @param list - list that holds AIImages
	 * @return AIImage, that is a setThisMax() on 'all' list elements
	 */
	public static AIImage getMaxFromList (ArrayList<AIImage> list) {
		if (list.size()<2) {
			throw new RuntimeException("list has not much elements...");
		}
		for (int i=1; i<list.size(); i++) {
			list.get(0).setThisMax(list.get(i));
		}
		return list.get(0);
	}
	
}
