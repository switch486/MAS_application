package com.googlepages.switch486.MAS.Engine.Image.Filter.Gabor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.googlepages.switch486.MAS.Engine.Image.AIImage;
import com.googlepages.switch486.MAS.Engine.Image.IsToImageable;
import com.googlepages.switch486.MAS.Engine.Image.Filter.CanFilter;

public class GaborFilter implements CanFilter, IsToImageable, IGaborFilter {

	private double p;
	private double F0;
	private double w0;
	private double K;
	private double x0;
	private double y0;
	private double a;
	private double b;
	private double theta;
	
	private double[][] filterMatrix = null;

	/**
	 *
	 */
	public GaborFilter() {

	}

	/**
	 * @param p the p to set
	 * @throws GaborParameterOutOfBoundsException
	 *             - is thrown when a parameter is out of bounds
	 */
	public void setP(double p) {
		this.p = p;
	}
	



	/**
	 * @param f0 the f0 to set
	 * @throws GaborParameterOutOfBoundsException
	 *             - is thrown when a parameter is out of bounds
	 */
	public void setF0(double f0) {
		F0 = f0;
	}
	



	/**
	 * @param w0
	 *            - double value in bounds 0.0-2.0 that represents the angle of
	 *            the sin function
	 * @throws GaborParameterOutOfBoundsException
	 *             - is thrown when a parameter is out of bounds
	 */
	public void setW0(double w0) throws GaborParameterOutOfBoundsException {
		/*
		 * Sets and counts the parameters for the Gabor Filter ##00048/(30)+(23)
		 */
		if (w0 < 0d || w0 > 2.0d) {
			throw new GaborParameterOutOfBoundsException("Actual value: " + w0
					+ " for the argument w0 out of the bounds [0.0d; 2.0d].");
		}
		this.w0 = Math.PI + w0 * Math.PI;
	}
	



	/**
	 * @param k the k to set
	 * @throws GaborParameterOutOfBoundsException
	 *             - is thrown when a parameter is out of bounds
	 */
	public void setK(double k) {
		K = k;
	}
	



	/**
	 * @param x0 the x0 to set
	 * @throws GaborParameterOutOfBoundsException
	 *             - is thrown when a parameter is out of bounds
	 */
	public void setX0(double x0) {
		this.x0 = x0;
	}
	



	/**
	 * @param y0 the y0 to set
	 * @throws GaborParameterOutOfBoundsException
	 *             - is thrown when a parameter is out of bounds
	 */
	public void setY0(double y0) {
		this.y0 = y0;
	}
	



	/**
	 * @param a the a to set
	 * @throws GaborParameterOutOfBoundsException
	 *             - is thrown when a parameter is out of bounds
	 */
	public void setA(double a) {
		this.a = a;
	}
	



	/**
	 * @param b the b to set
	 * @throws GaborParameterOutOfBoundsException
	 *             - is thrown when a parameter is out of bounds
	 */
	public void setB(double b) {
		this.b = b;
	}
	



	/**
	 * @param theta the theta to set
	 * @throws GaborParameterOutOfBoundsException
	 *             - is thrown when a parameter is out of bounds
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}
	



	/**
	 * @param xSize
	 *            the size of x array
	 * @param ySize
	 *            the size of y array The function fills the array with the
	 *            representing values according to the Gabor Filter
	 *            
	 * Please take into account that the FilterMatrix that is made here will have a size represented as a odd number (2x+1)
	 */
	public void setFilterMatrix(int xSize, int ySize) {
		if (xSize%2==0) {
			xSize++;
		}
		if (ySize%2==0) {
			ySize++;
		}
		filterMatrix = new double[xSize][ySize];
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				// x, y have to be in bounds -4, +4 therfore
				double xfrac = (8 * (double) x / (double) xSize) - 4;
				double yfrac = (8 * (double) y / (double) ySize) - 4;

				double sinValue = Math.sin(2 * Math.PI * F0
						* (xfrac * Math.cos(w0) + yfrac * Math.sin(w0)) + p);

				double xx0r = ((xfrac - x0) * Math.cos(theta) + (yfrac - y0)
						* Math.sin(theta));
				double yy0r = (-(xfrac - x0) * Math.sin(theta) + (yfrac - y0)
						* Math.cos(theta));

				double gausValue = K
						* Math.exp(-Math.PI
								* (a * a * xx0r * xx0r + b * b * yy0r * yy0r));

				filterMatrix[x][y] = sinValue * gausValue;

			}
		}
	}

	@Override
	public AIImage filter(AIImage imageToTransform, int xSize,
			int ySize) throws FilterMatrixToBigForImageException {
		if (filterMatrix == null) {
			if (xSize > imageToTransform.getWidth()
					|| ySize > imageToTransform.getHeight()) {
				throw new FilterMatrixToBigForImageException();
			}
			setFilterMatrix(xSize, ySize);
		} else if (xSize != filterMatrix.length
				|| ySize != filterMatrix[0].length) {
			setFilterMatrix(xSize, ySize);
		}

		return filter(imageToTransform, filterMatrix);
	}
	
	/* (non-Javadoc)
	 * @see com.googlepages.switch486.MAS.Engine.Image.Filter.Gabor.IGaborFilter#filter(com.googlepages.switch486.MAS.Engine.Image.AIImage)
	 */
	@Override
	public AIImage filter(AIImage imageToTransform) {
		return filter(imageToTransform, this.filterMatrix);
	}

	@Override
	public AIImage filter(AIImage imageToTransform,
			double[][] filterMatrixInput) {
		double[][] filterMatrixNormalizedInput = normalize(filterMatrixInput);
		int iWidth = imageToTransform.getWidth();
		int iHeight = imageToTransform.getHeight();
		double [][] temporaryImageValues = new double [iWidth][iHeight];
		AIImage out = new AIImage(iWidth, iHeight, BufferedImage.TYPE_INT_ARGB);
		int fXSize = filterMatrixNormalizedInput.length;
		int fYSize = filterMatrixNormalizedInput[0].length;
		int xMid = fXSize/2;
		int yMid = fYSize/2;
		double max=0d;
		double min=0d;
		for (int x=0; x<iWidth; x++) {						// foreach pixel in the width
			for (int y=0; y<iHeight; y++){					// foreach pixel in the height
				//double ValuesInsideTheImageBounds = 0d;			// for the better counting... 
				double summedValue = 0d;
				for (int i=0; i<fXSize; i++){				// for every point in the filterMatrix - x
					for (int j=0; j<fYSize; j++){			// for every point in the filterMatrix - x
						if (x - xMid + i < 0 || x - xMid + i >= iWidth) {
							//NOP
						} else if (y - yMid + j < 0 || y - yMid + j >= iHeight) {
							//NOP
						} else {

							int rgb = imageToTransform.getRGB(x - xMid + i, y
									- yMid + j);
							summedValue += filterMatrixNormalizedInput[i][j]*new Color(rgb).getRed(); // because
																	// all other
																	// values
																	// are the
																	// same!
							//ValuesInsideTheImageBounds+=filterMatrixNormalizedInput[i][j];
						}

					}
				}
				if (summedValue>max) {
					max = summedValue;
				}else if (summedValue<min) {
					min = summedValue;
				}
				temporaryImageValues[x][y] = summedValue;
			}
		}
		double delta = max-min;
		 
		for (int x=0; x<iWidth; x++) {						// foreach pixel in the width
			for (int y=0; y<iHeight; y++){					// foreach pixel in the height
				int val = (int)(255*((temporaryImageValues[x][y]-min)/delta));
				out.setRGB(x, y, new Color(val, val, val).getRGB());
			}
		}
		return out;
	}

	public void toImage( boolean useExistingMatrix){
		if (useExistingMatrix){
			if(filterMatrix==null) {
				throw new RuntimeException("filterMatrix is null!");
			}else {
				toImage();
			}
		}else {
			this.setFilterMatrix(300, 300);
			toImage();
		}
	}
	
	public void toImage() {
		
		final int xmax= filterMatrix.length;
		final int ymax= filterMatrix[0].length;

		JFrame f = new JFrame("The GaborFilterOutput");
		f.setLocation(100, 100);
		f.setBounds(100, 100, xmax+200, ymax);
		//f.setResizable(false);
		Container content = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2978325628848188017L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				for (int i = 0; i < xmax; i++) {
					for (int j = 0; j < ymax; j++) {
						int value = (int) ((filterMatrix[i][j]+1) * 127);
						g.setColor(new Color(value, 0, 256 - value));
						g.fillRect(i, j, 1, 1);
					}
				}
				ArrayList<String> liste = new ArrayList<String>();
				liste.add("Parameters:");
				liste.add("F0 = " + F0);
				liste.add("w0 = " + w0);
				liste.add("x0 = " + x0);
				liste.add("y0 = " + y0);
				liste.add("a  = " + a);
				liste.add("b  = " + b);
				liste.add("p  = " + p);
				liste.add("K  = " + K);
				liste.add("theta  = " + theta);
				for (int i = 0, j = 10; i < liste.size(); i++, j += 20) {
					g.drawString(liste.get(i), ymax+10, j);
				}
				int xpos = xmax+20;
				int ypos = ymax-150;
				g.drawString("MAX", xmax+10, ypos - 10);
				for (int i = 0; i < 80; i++) {
					double value = (double) (80 - i) / (double) 80;
					int v = (int) (value * 255);
					g.setColor(new Color(v, 0, 255 - v));
					g.fillRect(xpos, ypos + i, 60, 1);
				}
				g.drawString("MIN", xmax+10, ypos + 100);

			}
		};
		f.setContentPane(content);
		f.setVisible(true);

	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		this.setFilterMatrix(20, 20);
		for (int i = 0; i < filterMatrix.length; i++) {

			for (int j = 0; j < filterMatrix[0].length; j++) {
				s.append("" + filterMatrix[i][j] + "\t");
			}
			s.append("\n");
		}
		return s.toString();
	}

	/**
	 * @return returns a new filter matrix, that values sum up to 1
	 */
	public double[][] normalize(double [][] matrixIn) {
		double sum = 0d;
		int xsize = matrixIn.length;
		int ysize = matrixIn[0].length;
		for (int i=0; i<xsize; i++) {
			for (int j=0; j<ysize; j++) {
				sum +=matrixIn[i][j];
			}
		}
		double [][] out = new double [xsize][ysize];
		for (int i=0; i<xsize; i++) {
			for (int j=0; j<ysize; j++) {
				out[i][j]=matrixIn[i][j]/sum;
			}
		}
		return out;
	}

}
