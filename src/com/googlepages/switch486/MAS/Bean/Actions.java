package com.googlepages.switch486.MAS.Bean;

public enum Actions {

	F_GABOR_FILTER_EXPORT, //exports the filter with a gnuplot command
	F_FILTER_WITH_GABOR_FILTER,
	
	D_gabor_sin_phase, 
	D_gabor_sin_magnitude, 
	D_gabor_sin_direction, 
	D_gabor_gaus_scale, 
	D_gabor_gaus_x, 
	D_gabor_gaus_y, 
	D_gabor_gaus_a, 
	D_gabor_gaus_b, 
	D_gabor_gaus_theta,
	;
	
	public String toString() {return this.name();}
	
}
