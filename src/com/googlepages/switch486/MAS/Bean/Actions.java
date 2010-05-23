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
	F_SINUS_FILTER_EXPORT,
	F_SINUS_FILTER_MATRIX_EXPORT,
	F_FILTER_WITH_GABOR_FILTER_ONCE,
	F_FILTER_WITH_GABOR_FILTER,
	F_SINUS_FILTER_FILTER_IMAGE,
	
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
	D_sinus_magnitude,
	
	
	
	B_sinus_filter_phase,
	
	
	/*
	 * Ints representing the other Gabor Filter parameters
	 */
	I_gabor_filter_matrix_x,
	I_gabor_filter_matrix_y,
	I_sinus_filter_matrix_width,
	I_sinus_filter_matrix_height,
	I_sinus_filter_fine, 
	
	;
	
	public String toString() {return this.name();}
	
}
