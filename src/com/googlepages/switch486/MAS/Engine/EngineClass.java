package com.googlepages.switch486.MAS.Engine;

import com.googlepages.switch486.MAS.Engine.Image.Filter.Gabor.GaborFilter;
import com.googlepages.switch486.MAS.Engine.Image.Filter.Gabor.GaborParameterOutOfBoundsException;

public class EngineClass {
	
	public GaborFilter gf;
	
	public EngineClass() {
		gf = new GaborFilter();
	}
	
	/**
	 * @param p the p to set
	 */
	public void setP(double p) {
		gf.setP(p);
	}

	/**
	 * @param f0 the f0 to set
	 *             - is thrown when a parameter is out of bounds
	 */
	public void setF0(double f0) {
		gf.setF0(f0);
	}

	/**
	 * @param w0
	 *            - double value in bounds 0.0-2.0 that represents the angle of
	 *            the sin function
	 */
	public void setW0(double w0) {
		try {
			gf.setW0(w0);
		} catch (GaborParameterOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param k the k to set
	 */
	public void setK(double k) {
		gf.setK(k);
	}

	/**
	 * @param x0 the x0 to set
	 */
	public void setX0(double x0) {
		gf.setX0(x0);
	}
	
	/**
	 * @param y0 the y0 to set
	 */
	public void setY0(double y0) {
		gf.setY0(y0);
	}
	
	/**
	 * @param a the a to set
	 */
	public void setA(double a) {
		gf.setA(a);
	}
	
	/**
	 * @param b the b to set
	 */
	public void setB(double b) {
		gf.setB(b);
	}
	
	/**
	 * @param theta the theta to set
	 */
	public void setTheta(double theta) {
		gf.setTheta(theta);
	}

}
