package com.googlepages.switch486.MAS.Engine.Image.Filter;

import com.googlepages.switch486.MAS.Engine.Image.AIImage;
import com.googlepages.switch486.MAS.Engine.Image.Filter.Gabor.FilterMatrixToBigForImageException;

public interface CanFilter {
	
	/**
	 * @param imageToTransform - represents an image, that will be transformed with the Filter, that implements this interface
	 * @param xSize - number of pixels in the x axis, that will be used for the filter window
	 * @param ySize - number of pixels in the y axis, that will be used for the filter window
	 * @return - an filtration result
	 * @throws FilterMatrixToBigForImageException - occurs when @param xSize > @param imageToTransform etc.
	 */
	public AIImage filter (AIImage imageToTransform, int xSize, int ySize) throws FilterMatrixToBigForImageException ;
	
	/**
	 * @param imageToTransform - represents an image, that will be transformed with the Filter, that implements this interface
	 * @param filterMatrix - filterMatrix that will be applied to the image
	 * @return - an filtration result
	 * the parameters for this function should be checked in the one above
	 */
	public AIImage filter (AIImage imageToTransform, double [][] filterMatrix);
	
	/**
	 * @param imageToTransform - represents an image, that will be transformed with the Filter, that implements this interface
	 * @return - an filtration result
	 * Uses the specified in the gabor Class FilterMatrix.
	 */
	public AIImage filter (AIImage imageToTransform);
}
