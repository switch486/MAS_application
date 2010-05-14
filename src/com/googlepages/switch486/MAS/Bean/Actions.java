package com.googlepages.switch486.MAS.Bean;

public enum Actions {
	/*
	 * Globals that represent annything not mensioned bellow
	 */
	G_FILEPATH_FOR_OUTPUT_FILES,
	G_SOURCE_IMAGE,

	/*
	 * Functions, that represent the actions of the Engine
	 */
	F_GABOR_FILTER_EXPORT, 
	F_GABOR_FILTER_MATRIX_EXPORT,
	F_FILTER_WITH_GABOR_FILTER_ONCE,
	
	/*
	 * Doubles representing the gabor filter parameters
	 */
	D_gabor_sin_phase, 
	D_gabor_sin_magnitude, 
	D_gabor_sin_direction, 
	D_gabor_gaus_scale, 
	D_gabor_gaus_x, 
	D_gabor_gaus_y, 
	D_gabor_gaus_a, 
	D_gabor_gaus_b, 
	D_gabor_gaus_theta,
	
	/*
	 * Ints representing the other Gabor Filter parameters
	 */
	I_gabor_filter_matrix_x,
	I_gabor_filter_matrix_y,
	
	;
	
	public String toString() {return this.name();}
	
}
