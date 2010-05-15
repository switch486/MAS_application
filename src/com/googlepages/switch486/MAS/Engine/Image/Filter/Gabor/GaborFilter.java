package com.googlepages.switch486.MAS.Engine.Image.Filter.Gabor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.googlepages.switch486.MAS.Bean.Actions;
import com.googlepages.switch486.MAS.Bean.DoubleParam;
import com.googlepages.switch486.MAS.Bean.Param;
import com.googlepages.switch486.MAS.Bean.Params;
import com.googlepages.switch486.MAS.Engine.Image.AIImage;
import com.googlepages.switch486.MAS.Engine.Image.IsToImageable;
import com.googlepages.switch486.MAS.Engine.Image.Filter.FilterMatrixToBigForImageException;
import com.googlepages.switch486.MAS.Engine.Image.Filter.ICanFilter;

public class GaborFilter implements ICanFilter, IsToImageable {
	
	private static final Logger logger = Logger.getLogger(GaborFilter.class.getName());

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
	 */
	private void setP(double p, Param dp) {
		setP(p);
		if (dp==null) 
			return;
		if (dp.getAction().equals(Actions.D_gabor_sin_phase)) 
			setP(((DoubleParam)dp).getParameter());
	}
	private void setP(double p) {
		if (p < 0 || p > Math.PI*2) {
			logger.warning("Actual value: " + p
					+ " for the argument p out of the bounds [0; 2PI], setting p to 0.");
			p = 0;
		}
		this.p = p;
	}
	
	/**
	 * @param f0 the f0 to set
	 */
	private void setF0(double f0, Param dp) {
		setF0(f0);
		if (dp==null) 
			return;
		if (dp.getAction().equals(Actions.D_gabor_sin_magnitude)) 
			setF0(((DoubleParam)dp).getParameter());
	}
	private void setF0(double f0) {
		if (f0 < 0.001 || f0 > 1) {
			logger.warning("Actual value: " + f0
					+ " for the argument F0 out of the bounds [0.001; 1], setting F0 to 0.2.");
			f0 = 0.2;
		}
		F0 = f0;
	}
	
	/**
	 * @param w0
	 *            - double value in bounds 0.0-1.0 that represents the angle of
	 *            the sin function
	 */
	private void setW0(double w0, Param dp) {
		setW0(w0);
		if (dp==null) 
			return;
		if (dp.getAction().equals(Actions.D_gabor_sin_direction)) 
			setW0(((DoubleParam)dp).getParameter());
	}
	public void setW0(double w0) {
		/*
		 * Sets and counts the parameters for the Gabor Filter ##00048/(30)+(23)
		 */
		if (w0 < 0d || w0 > 1d) { // roundings....
			logger.warning("Actual value: " + w0
					+ " for the argument w0 out of the bounds [0.0d; 1.0d], setting w0 to 0.");
			w0 = 0;
		}
		this.w0 = Math.PI + w0 * Math.PI;
	}
	
	/**
	 * @param k the k to set - at the moment does not have a meaning...
	 */
	private void setK(double k, Param dp) {
		setK(k);
		if (dp==null) 
			return;
		if (dp.getAction().equals(Actions.D_gabor_gaus_scale)) 
			setK(((DoubleParam)dp).getParameter());
	}
	private void setK(double k) {
		if (k < 1 || k > 1) {
			logger.warning("Actual value: " + k
					+ " for the argument K out of the bounds [1; 1], setting K to 1.");
			k = 1;
		}
		K = k;
	}

	/**
	 * @param x0 the x0 to set
	 */
	private void setX0(double x0, Param dp) {
		setX0(x0);
		if (dp==null) 
			return;
		if (dp.getAction().equals(Actions.D_gabor_gaus_x)) 
			setX0(((DoubleParam)dp).getParameter());
	}
	private void setX0(double x0) {
		if (x0 < -4 || x0 > 4) {
			logger.warning("Actual value: " + x0
					+ " for the argument x0 out of the bounds [-4; 4], setting x0 to 0.");
			x0 = 0;
		}
		this.x0 = x0;
	}

	/**
	 * @param y0 the y0 to set
	 */
	private void setY0(double y0, Param dp) {
		setY0(y0);
		if (dp==null) 
			return;
		if (dp.getAction().equals(Actions.D_gabor_gaus_y))
			setY0(((DoubleParam)dp).getParameter());
	}
	private void setY0(double y0) {
		if (y0 < -4 || y0 > 4) {
			logger.warning("Actual value: " + y0
					+ " for the argument y0 out of the bounds [-4; 4], setting y0 to 0.");
			y0 = 0;
		}
		this.y0 = y0;
	}

	/**
	 * @param a the a to set
	 */
	private void setA(double a, Param dp) {
		setA(a);
		if (dp==null) 
			return;
		if (dp.getAction().equals(Actions.D_gabor_gaus_a)) 
			setA(((DoubleParam)dp).getParameter());
	}
	private void setA(double a) {
		if (a < 0 || a > 1) {
			logger.warning("Actual value: " + a
					+ " for the argument a out of the bounds [0; 1], setting a to 0.26.");
			a = 0.26;
		}
		this.a = a;
	}

	/**
	 * @param b the b to set
	 */
	private void setB(double b, Param dp) {
		setB(b);
		if (dp==null) 
			return;
		if (dp.getAction().equals(Actions.D_gabor_gaus_b)) 
			setB(((DoubleParam)dp).getParameter());
	}
	private void setB(double b) {
		if (b < 0 || b > 1) {
			logger.warning("Actual value: " + b
					+ " for the argument b out of the bounds [0; 1], setting b to 0.26.");
			b = 0.26;
		}
		this.b = b;
	}

	/**
	 * @param theta the theta to set
	 */
	private void setTheta(double theta, Param dp) {
		setTheta(theta);
		if (dp==null) 
			return;
		if (dp.getAction().equals(Actions.D_gabor_gaus_theta)) 
			setTheta(((DoubleParam)dp).getParameter());
	}
	private void setTheta(double theta) {
		if (theta < 0 || theta > Math.PI*2) {
			logger.warning("Actual value: " + theta
					+ " for the argument theta out of the bounds [0; 2PI], setting theta to PI.");
			theta = Math.PI;
		}
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
	private void setFilterMatrix(int xSize, int ySize) {
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
				double xfrac = (8 * (double) x / (double) (xSize-1)) - 4;
				double yfrac = (8 * (double) y / (double) (ySize-1)) - 4;

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

	private String filterToString(){
		StringBuilder out = new StringBuilder();
		out.append('(');
		//SinPart
		out.append("sin(");			//sin - B
			out.append("2*pi*"+F0);
			out.append("*(");			////B
				out.append("x*cos("+w0+")");
				out.append("+y*sin("+w0+")");
			out.append(")+"+p);			////E
		out.append(")");			//sin - E
		out.append('*');
		//GausPart
		out.append(K+"*exp(");			//exp - B
			out.append("-pi*(");			//pi - B
				out.append(a+"**2");			//a^2
					out.append("*((x-"+x0+")*cos("+theta+")");
						out.append("+(y-"+y0+")*sin("+theta+"))**2");
					out.append("+");				//+
				out.append(b+"**2");			//b^2
					out.append("*(-(x-"+x0+")*sin("+theta+")");
						out.append("+(y-"+y0+")*cos("+theta+"))**2");
					out.append(")");			//pi - E
			out.append(")");			//exp - E
		return out.append(')').toString();
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
		//double[][] filterMatrixNormalizedInput = normalize(filterMatrixInput);
		double[][] filterMatrixNormalizedInput = (filterMatrixInput);
		int iWidth = imageToTransform.getWidth();
		int iHeight = imageToTransform.getHeight();
		double [][] temporaryImageValues = new double [iWidth][iHeight];
		AIImage out = new AIImage(iWidth, iHeight, BufferedImage.TYPE_INT_ARGB);
		int fXSize = filterMatrixNormalizedInput.length;
		int fYSize = filterMatrixNormalizedInput[0].length;
		int xMid = fXSize/2;
		int yMid = fYSize/2;
		double max=0d;
		//double min=0d;
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
				if (summedValue<0){
					summedValue = -summedValue;
				}
				if (summedValue>max) {
					max = summedValue;
				}
				temporaryImageValues[x][y] = summedValue;
			}
		}
		double delta = max;
		 
		for (int x=0; x<iWidth; x++) {						// foreach pixel in the width
			for (int y=0; y<iHeight; y++){					// foreach pixel in the height
				int val = (int)(255*((temporaryImageValues[x][y]-0)/delta));
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
				liste.add("Filter.length  = " + filterMatrix.length);
				liste.add("Filter[].length  = " + filterMatrix[0].length);
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

	public String listParameters () {
		StringBuilder liste = new StringBuilder();
		liste.append("\n\n# Parameters:\n");
		liste.append("# F0 = " + F0+"\n");
		liste.append("# w0 = " + w0+"\n");
		liste.append("# x0 = " + x0+"\n");
		liste.append("# y0 = " + y0+"\n");
		liste.append("# a  = " + a+"\n");
		liste.append("# b  = " + b+"\n");
		liste.append("# p  = " + p+"\n");
		liste.append("# K  = " + K+"\n");
		liste.append("# theta  = " + theta+"\n");
		liste.append("# Filter.length  = " + filterMatrix.length);
		liste.append("# Filter[].length  = " + filterMatrix[0].length);
		liste.append("# -------------------------------------------------------------------------- \n");
		liste.append("# NOTE: To run this script You need to have installed the Gnuplot application\n");
		liste.append("# For linux users, please run 'gnuplot THIS_FILE_NAME', to recieve results\n");
		return liste.toString();
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
	
	public String getGnuPlotString() {
		StringBuilder s = new StringBuilder();
		s.append("# X\tY\tV\n");
		for (int i = 0; i < filterMatrix.length; i++) {
			for (int j = 0; j < filterMatrix[0].length; j++) {
				s.append(i+"\t"+j+"\t"+filterMatrix[i][j]+"\n");
			}
		}
		return s.toString();
	}

	public void setGfParams(Params p) {
		setGfParams(p, null);
	}
	
	public void setGfParams(Params p, Param dp) {
			this.setA (p.getDoubleParam(Actions.D_gabor_gaus_a), dp);
			this.setB (p.getDoubleParam(Actions.D_gabor_gaus_b), dp);
			this.setF0(p.getDoubleParam(Actions.D_gabor_sin_magnitude), dp);
			this.setP (p.getDoubleParam(Actions.D_gabor_sin_phase), dp);
			this.setW0(p.getDoubleParam(Actions.D_gabor_sin_direction), dp);
			this.setK (p.getDoubleParam(Actions.D_gabor_gaus_scale), dp);
			this.setTheta(p.getDoubleParam(Actions.D_gabor_gaus_theta), dp);
			this.setX0(p.getDoubleParam(Actions.D_gabor_gaus_x), dp);
			this.setY0(p.getDoubleParam(Actions.D_gabor_gaus_y), dp);
			this.setFilterMatrix(p.getIntParam(Actions.I_gabor_filter_matrix_x), p.getIntParam(Actions.I_gabor_filter_matrix_y));
	}

	/**
	 * @param outPutFileLocation
	 * @return String [4]: 	[0] - the location of the script to run
	 * 						[1] - the script content - to pass into a file and run
	 * 						[2] - the filename+path to the first image to join
	 * 						[3] - the filename+path to the second image to join
	 */
	public String[] exportFilter(String outPutFileLocation) {
		long name = System.currentTimeMillis();
		
		String[] out = new String[4];
		StringBuilder s = new StringBuilder();
		out[0] = outPutFileLocation + name;

		char c = '\"';
		s.append("set terminal png;\n");
		s.append("set hidden3d;\n");
		s.append("set pm3d;\n");
		s.append("set isosample 100,100;\n");
		s.append("set xrange [-4:4];\n");
		s.append("set yrange [-4:4];\n");
		s.append("set zrange [-1:1];\n");
		out[2] = outPutFileLocation + name + "_1.png";
		s.append("set output " + c + out[2] + c+ "\n");
		s.append("splot "+this.filterToString()+" title 'Gabor Function'\n");
		s.append("set pm3d map;\n");
		//s.append("set zrange [-1:1];\n");
		s.append("set isosample 100,100;\n");
		out[3] = outPutFileLocation + name + "_2.png";
		s.append("set output " + c + out[3] + c+ "\n");
		s.append("splot "+this.filterToString()+" title 'Gabor Function'\n");
		out[1] = s.toString() +listParameters();
		return out;
	}

	/**
	 * @param outPutFileLocation
	 * @return String [4]: 	[0] - the location of the script to run
	 * 						[1] - the script content - to pass into a file and run
	 * 						[2] - the filename+path to the first image to join
	 * 						[3] - the filename+path to the data file
	 * 						[4] - the data file content
	 */
	public String[] exportFilterMatrix(String outPutFileLocation) {
		long name = System.currentTimeMillis();
		
		String[] out = new String[5];
		StringBuilder s = new StringBuilder();
		out[0] = outPutFileLocation + name;
		
		out[3] = outPutFileLocation + name + ".dat";
		out[4] = this.getGnuPlotString();
		char c = '\"';
		s.append("set terminal png;\n");
		s.append("set hidden3d;\n");
		s.append("set dgrid3d 30,30;\n");
		out[2] = outPutFileLocation + name + ".png";
		s.append("set output " + c + out[2] + c+ "\n");
		s.append("splot "+c+out[3]+c+" u 1:2:3 with lines;\n");
		//-----------------------------
		out[1] = s.toString() + listParameters() + "# NOTE: the "+out[3]+" file also needed to plot!\n";
		return out;
	}

}
