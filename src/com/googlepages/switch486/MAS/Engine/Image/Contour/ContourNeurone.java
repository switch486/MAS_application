package com.googlepages.switch486.MAS.Engine.Image.Contour;

public class ContourNeurone {
	
	public static double boost = 0.85;
	public static int radiusRestriction = 4;
	//public static double angleBorder = Math.PI/2;
	public static double angleBorder = 1.4d;
	public static double membranePotential = 0;
	
	private double DIFFERENCE;
	
	double realAngle;
	double theta;
	boolean isSinus;
	boolean isactive;
	double power=0; // -1,1
	private int x, y;

	public ContourNeurone(double theta, double realAngle, boolean isSinus, boolean isActive, int x, int y) {
		this.theta = theta;
		this.realAngle = realAngle;
		this.isSinus = isSinus;
		this.x = x;
		this.y = y;
		this.isactive = isActive;
		this.power = (Math.random()/8);
		//this.power = 0.2001;
	}
	
	public boolean isInBounds (ContourNeurone[][] net, int xx, int yy) {
		if (xx<0 || yy<0 || xx>=net.length || yy>=net[0].length){
			return false;
		}
		return true;
	}
	
	public void actualize() {
		power += DIFFERENCE;
		if (power<0){
			power=0d;
		}else if (power>1d){
			power=1d;
		}
	}
	
	/**
	 * @param net - the network to wirk with
	 * @return - a absolute power difference value for the neurone
	 */
	public double countPower (ContourNeurone[][] net) {
		if (isactive) {
			double addd = 0;
			int ct = 0;
			for (int i = x - radiusRestriction; i <= x + radiusRestriction; i++) {
				for (int j = y - radiusRestriction; j <= y + radiusRestriction; j++) {
					if (isInBounds(net, i, j)) {
						if (net[i][j].isactive) {
							/*if (i == x && j == y) {
								// NOP
							} else {*/
								// addd +=net[i][j].power *
								// getRadiusRestriction(i, j) *
								// getAngleProperty(net, i, j);
								addd += (net[i][j].power
										* getRadiusRestriction(i, j) * getAngleProperty(
										net, i, j)) > 0 ? 1 : -1;
								ct++;
							//}
						}
					}
				}
			}
			DIFFERENCE = addd/ct;
			//if (DIFFERENCE < membranePotential) return 0;
			/*power+=avg;*/
			return DIFFERENCE;
		}
		//a bit against the javadoc description....
		//power = 0d;
		DIFFERENCE = 0d;
		return DIFFERENCE;
	}
	
	private double getRadiusRestriction(int xx, int yy){
		//always <1
		return ((double)getRadius(xx, yy))/radiusRestriction;
	}
	
	private int getRadius(int xx, int yy) {
		//return Math.sqrt((xx-x)*(xx-x)+ (yy-y)*(yy-y));
		return Math.max(Math.abs(xx-x),Math.abs(yy-y));
	}
	
	private double getAngleProperty(ContourNeurone[][] net, int xx, int yy){
		double angle = getAngleDifference(net, xx, yy);
		//if (angleBorder>angle) {
			//return angleBorder-angle;
			return Math.cos((angle/angleBorder)*(Math.PI/2));
		//}
		//return -angle/(Math.PI);
		//return angleBorder-angle;
		//<-1, 1>
	}
	
	/**
	 * @param theta an angle of the second neurone
	 * @return the difference value -- meaning of the contour.
	 */
	private double getAngleDifference (ContourNeurone[][] net, int xx, int yy) {
		// 0-PHI
		return Math.abs(theta-net[xx][yy].theta)/getNearAngleBoost(xx, yy);
	}
	
	private double getNearAngleBoost(int xx, int yy) {
		int one = Math.abs(xx-x);
		int two = Math.abs(yy-y);
		if (one + two <=2 ) {
			return boost;
		}else {
			return 1;
		}
	}
	
}
