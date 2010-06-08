package com.googlepages.switch486.MAS.Engine.Image.Contour;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import com.googlepages.switch486.MAS.Bean.Actions;
import com.googlepages.switch486.MAS.Bean.Param;
import com.googlepages.switch486.MAS.Bean.Params;
import com.googlepages.switch486.MAS.Engine.Image.AIImage;

public class ContourIntegrator {
	
	private ContourNeurone[][] network;
	private int sX;
	private int sY;

	public ContourIntegrator() {
		
	}
	
	public void setX (int x) {
		sX = x;		
	}
	
	public void setY (int y) {
		sY = y;		
	}
	
	public void setCiParams(Params p) {
		setCiParams(p, null);
	}
	
	public void setCiParams(Params p, Param dp) {
		this.setX(p.getIntParam(Actions.I_sinus_filter_matrix_width));
		this.setY(p.getIntParam(Actions.I_sinus_filter_matrix_height));
		//todo
	}
	
	public AIImage printModelOutput(int iWidth, int iHeight){
		AIImage out = new AIImage(iWidth, iHeight, BufferedImage.TYPE_INT_ARGB);
		int beX;// \ that are those values that represent the x and y of the image marking the point (0, 0), because not every image will have sizes matching int*filtermatrix[0].length etc.
		int beY;// /
		int fmx = sX;
		int fmy = sY;
		beX = 0; 
		beY = 0; 
		int xTimes = network.length;
		int yTimes = network[0].length;
		
		for (int i=0; i<xTimes; i++) {
			for (int j=0; j<yTimes; j++) {
				if (inBounds(out, i * fmx + beX + fmx , j * fmy + beY + fmy)){
					printOneContourWithPower(out, i * fmx + beX, j * fmy + beY, fmx, fmy, i, j);
				}else {
					toString();
				}
			}
		}
		return out;
	}
	
	private void printOneContourWithPower(AIImage out, int i2, int j2, int fmx, int fmy, int ii, int jj) {
		for (int i = i2, cc = 0; i < i2 + fmx; i++, cc++) {
			for (int j = j2, dd = 0; j < j2 + fmy; j++, dd++) {
				out.setRGB(i, j, Color.white.getRGB());			//ff, ff, ff
			}
		}
		if (network[ii][jj].isactive) {
			// /////if (network[ii][jj].isSinus){//omg

			// /////}else {//less complicated!
			// network[ii][jj].theta;
			double x = -Math.sin(network[ii][jj].theta);
			double y = Math.cos(network[ii][jj].theta);
			for (int i = -10; i < 10; i++) {
				if (inBounds(i2, j2, i2 + fmx, j2 + fmy,
						(int) ((i2 + (fmx / 2)) + i * x),
						(int) ((j2 + (fmy / 2)) + i * y))) {
					float d = (float) (network[ii][jj].power);
					out.setRGB((int) ((i2 + (fmx / 2)) + i * x),
							(int) ((j2 + (fmy / 2)) + i * y), new Color(d, 0f, 0f).getRGB());
				}
			}
			// /////}
			// network[ii][jj].power;
			// PRINT CONTOUR WITH POWER!!!!
		}
	}
	
	private boolean inBounds (AIImage out, int x, int y){
		if (x<0 || y<0 || x>=out.getWidth() || y>=out.getHeight()) {
			return false;
		}
		return true;
	}
	
	private boolean inBounds (int x, int y, int xx, int yy, int a, int b){
		if (a<x || a>xx || b<y || b>yy) {
			return false;
		}
		return true;
	}

	public void calculate(ContourNeurone[][] netWork) {
		this.network = netWork;
		int x = network.length;
		int y = network[0].length;
		boolean koniec = false;
		int ct = 0;
		
		while(ct<100) {
		
			LinkedList<Double> lld = new LinkedList<Double>();
			for (int i=0; i<x; i++) {
				for (int j=0; j<y; j++) {
					
					lld.add(network[i][j].countPower(network));
				}
			}
			for (int i=0; i<x; i++) {
				for (int j=0; j<y; j++) {
					network[i][j].actualize();
				}
			}
			
			double avg = average(lld);
			if (avg<0.0001){
				koniec=true;
			}
			ct++;
		}
	}

	private double average(LinkedList<Double> lld) {
		double avg=0;
		int l = lld.size();
		for (Double d : lld) {
			avg +=d;
		}
		avg/=l;
		return avg;
	}
	
}