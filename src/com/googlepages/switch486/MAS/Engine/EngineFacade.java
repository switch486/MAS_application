package com.googlepages.switch486.MAS.Engine;

import java.util.logging.Logger;

import com.googlepages.switch486.MAS.Bean.Params;
import com.googlepages.switch486.MAS.Engine.Image.Filter.Gabor.GaborFilter;

public class EngineFacade implements IEngine {
	
	private static final Logger logger = Logger.getLogger(EngineFacade.class.getName());
	
	public GaborFilter gf;
	
	public EngineFacade() {
		gf = new GaborFilter();
	}
	
	@Override
	public void executeParameters(Params p) {
		logger.fine("Execute passed parameters");
		
		logger.fine(p.toString());
	}

	
	
}
