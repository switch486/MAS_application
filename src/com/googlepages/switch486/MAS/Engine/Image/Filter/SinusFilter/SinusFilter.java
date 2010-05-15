package com.googlepages.switch486.MAS.Engine.Image.Filter.SinusFilter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import com.googlepages.switch486.MAS.Bean.Actions;
import com.googlepages.switch486.MAS.Bean.DoubleParam;
import com.googlepages.switch486.MAS.Bean.IntegerParam;
import com.googlepages.switch486.MAS.Bean.Param;
import com.googlepages.switch486.MAS.Bean.Params;
import com.googlepages.switch486.MAS.Engine.Image.AIImage;
import com.googlepages.switch486.MAS.Engine.Image.Filter.FilterMatrixToBigForImageException;
import com.googlepages.switch486.MAS.Engine.Image.Filter.ICanFilter;

public class SinusFilter implements ICanFilter {
	
	private static final Logger logger = Logger.getLogger(SinusFilter.class.getName());
	
	private double[][][] filterMatrix = null;
	
	/*
	 * The number represents the count of border thics, that will be in an output image
	 * example: 1 -> {|}
	 * 			2 -> {|, -}
	 * 			4 -> {|, /, -, \}
	 * 			...
	 */
	private int fineLevel;
	private double magnitude;
	//private double direction;
	
	/**
	 * @param a the a to set
	 */
	private void setFineLevel(int fineLevel, Param pa) {
		setFineLevel(fineLevel);
		if (pa==null) 
			return;
		if (pa.getAction().equals(Actions.I_sinus_filter_fine)) 
			setFineLevel(((IntegerParam)pa).getParameter());
	}
	private void setFineLevel(int fineLevel) {
		if (fineLevel < 0) {
			logger.warning("Actual value: " + fineLevel
					+ " for the argument a out of the bounds [0; +\\inf], setting a to 1.");
			fineLevel = 1;
		}
		this.fineLevel = fineLevel;
	}
	
	/**
	 * @param magnitude the magnitude to set
	 */
	private void setMagnitude(double magnitude, Param dp) {
		setMagnitude(magnitude);
		if (dp==null) 
			return;
		if (dp.getAction().equals(Actions.D_sinus_magnitude)) 
			setMagnitude(((DoubleParam)dp).getParameter());
	}
	private void setMagnitude(double magnitude) {
		if (magnitude < 0.001 || magnitude > 1) {
			logger.warning("Actual value: " + magnitude
					+ " for the argument magnitude out of the bounds [0.001; 1], setting magnitude to 0.2.");
			//magnitude = 0.2;
			logger.warning("Please set the magnitude borders!!!!");
		}
		this.magnitude = magnitude;
	}
	
	/**
	 * @param direction
	 *            - double value in bounds 0.0-1.0 that represents the angle of
	 *            the sin function
	 */
	/*private void setDirection(double direction, Param dp) {
		setDirection(direction);
		if (dp==null) 
			return;
		if (dp.getAction().equals(Actions.D_sinus_direction)) 
			setDirection(((DoubleParam)dp).getParameter());
	}*/
	/*@Deprecated
	public void setDirection(double direction) {
		if (direction < 0d || direction > 1d) { // roundings....
			logger.warning("Actual value: " + direction
					+ " for the argument direction out of the bounds [0.0d; 1.0d], setting direction to 0.");
			direction = 0;
		}
		//this.direction = direction;
		//this.direction = Math.PI + direction * Math.PI;
	}*/
	
	private double getDirection(int num) {
		return ((double)num/(double)fineLevel) * Math.PI + Math.PI;
	}
	

	/**
	 *
	 */
	public SinusFilter() {

	}


	

	/**
	 * @param xSize
	 *            the size of x array
	 * @param ySize
	 *            the size of y array The function fills the array with the
	 *            representing values according to the Sinus Filter
	 */
	private void setFilterMatrix(int xSize, int ySize) {
		//fineLevel = nOOfThics;
		filterMatrix = new double[fineLevel][xSize][ySize];
		for (int t = 0; t < fineLevel; t++) {
			double direction =  getDirection(t);
			for (int x = 0; x < xSize; x++) {
				for (int y = 0; y < ySize; y++) {
					double xfrac = (2*(double) x / (double) (xSize-1)) - 1;
					double yfrac = (2*(double) y / (double) (ySize-1)) - 1;

					double sinValue = Math.cos(2 * Math.PI * magnitude
							* (xfrac * Math.cos(direction) + yfrac * Math.sin(direction)));

					filterMatrix[t][x][y] = sinValue;

				}
			}
		}
	}

	private String filterToString(){
		StringBuilder out = new StringBuilder();
		out.append('(');
		//SinPart
		//TODO
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

		//TODO
		return filter(imageToTransform, filterMatrix[0]);
	}
	
	/* (non-Javadoc)
	 * @see com.googlepages.switch486.MAS.Engine.Image.Filter.Gabor.IGaborFilter#filter(com.googlepages.switch486.MAS.Engine.Image.AIImage)
	 */
	@Override
	public AIImage filter(AIImage imageToTransform) {
		return filter(imageToTransform, this.filterMatrix[0]);
		///TODO
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
		double max=0d;
		//double min=0d;
		for (int x=0; x<iWidth; x++) {						// foreach pixel in the width
			for (int y=0; y<iHeight; y++){					// foreach pixel in the height
				//double ValuesInsideTheImageBounds = 0d;			// for the better counting... 
				double summedValue = 0d;
				for (int i=0; i<fXSize; i++){				// for every point in the filterMatrix - x
					for (int j=0; j<fYSize; j++){			// for every point in the filterMatrix - x
						///TODO

					}
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
	
	public String listParameters () {
		StringBuilder liste = new StringBuilder();
		liste.append("\n\n# Parameters:\n");
		liste.append("# Magnitude  = " + magnitude+"\n");
		liste.append("# Fine level  = " + fineLevel+"\n");
		liste.append("# Filter[].length  = " + filterMatrix[0].length+"\n");
		liste.append("# Filter[][].length  = " + filterMatrix[0][0].length+"\n");
		liste.append("# -------------------------------------------------------------------------- \n");
		liste.append("# NOTE: To run this script You need to have installed the Gnuplot application\n");
		liste.append("# For linux users, please run 'gnuplot THIS_FILE_NAME', to recieve results\n");
		return liste.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		this.setFilterMatrix(20, 20);
		for (int i = 0; i < filterMatrix[0].length; i++) {
			//TODO
			for (int j = 0; j < filterMatrix[0].length; j++) {
				s.append("" + filterMatrix[0][i][j] + "\t");
			}
			s.append("\n");
		}
		return s.toString();
	}
	
	public String getGnuPlotString(int i2) {
		StringBuilder s = new StringBuilder();
		s.append("# X\tY\tV\n");
		for (int i = 0; i < filterMatrix[0].length; i++) {
			for (int j = 0; j < filterMatrix[0][0].length; j++) {
				s.append(i+"\t"+j+"\t"+filterMatrix[i2][i][j]+"\n");
			}
		}
		return s.toString();
	}

	public void setSfParams(Params p) {
		setSfParams(p, null);
	}
	
	public void setSfParams(Params p, Param dp) {
		this.setFineLevel(p.getIntParam(Actions.I_sinus_filter_fine), dp);
		//this.setDirection(p.getIntParam(Actions.D_sinus_direction), dp);
		this.setMagnitude(p.getDoubleParam(Actions.D_sinus_magnitude), dp);
			/*this.setA (p.getDoubleParam(Actions.D_gabor_gaus_a), dp);
			this.setF0(p.getDoubleParam(Actions.D_gabor_sin_magnitude), dp);
			this.setP (p.getDoubleParam(Actions.D_gabor_sin_phase), dp);
			this.setW0(p.getDoubleParam(Actions.D_gabor_sin_direction), dp);
			this.setK (p.getDoubleParam(Actions.D_gabor_gaus_scale), dp);
			this.setTheta(p.getDoubleParam(Actions.D_gabor_gaus_theta), dp);
			this.setX0(p.getDoubleParam(Actions.D_gabor_gaus_x), dp);
			this.setY0(p.getDoubleParam(Actions.D_gabor_gaus_y), dp);*/
		this.setFilterMatrix(p.getIntParam(Actions.I_sinus_filter_matrix_width), p.getIntParam(Actions.I_sinus_filter_matrix_height));
		//todo
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
	 * @return String [n]: 	[0] - the location of the script to run
	 * 						[1] - the script content - to pass into a file and run
	 * 						[2] - the filename+path to the first image to join
	 * 						[3] - the filename+path to the data file
	 * 						[4] - the data file content  
	 * 						[5] - the filename+path to the data file
	 * 						[6] - the data file content  
	 * 						[7] - the filename+path to the data file
	 * 						[8] - the data file content
	 * 						  ....
	 * 						[n-1] - the filename+path to the data file
	 * 						[n] - the data file content  
	 * where 4+6+8+ ... +n 
	 * 		 1+1+1+ ... +1 = fineLevel
	 */
	public String[] exportFilterMatrix(String outPutFileLocation) {
		long name = System.currentTimeMillis();
		
		String[] out = new String[3+2*fineLevel];
		StringBuilder s = new StringBuilder();
		out[0] = outPutFileLocation + name;
		char c = '\"';
		s.append("set terminal png;\n");
		s.append("set hidden3d;\n");
		s.append("set dgrid3d 40,40;\n");
		
		for (int i=0; i<fineLevel; i++) {
			s.append("# Fine Level "+i+" of "+fineLevel+"\n");
			//name = System.currentTimeMillis();
			out[3+2*i] = outPutFileLocation + name+ "_"+i+".dat";
			out[4+2*i] = this.getGnuPlotString(i);
			out[2] = outPutFileLocation + name + "_"+i+".png";
			s.append("set output " + c + out[2] + c+ "\n");
			s.append("splot "+c+out[3+2*i]+c+" u 1:2:3 with lines;\n");
		}
		//-----------------------------
		out[1] = s.toString() + listParameters() + "# NOTE: the *.dat filea are also needed to plot!\n";
		return out;
	}

}
