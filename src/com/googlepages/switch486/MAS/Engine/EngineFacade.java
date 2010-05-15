package com.googlepages.switch486.MAS.Engine;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.googlepages.switch486.MAS.Bean.Actions;
import com.googlepages.switch486.MAS.Bean.DoubleParam;
import com.googlepages.switch486.MAS.Bean.Param;
import com.googlepages.switch486.MAS.Bean.Params;
import com.googlepages.switch486.MAS.Engine.Image.AIImage;
import com.googlepages.switch486.MAS.Engine.Image.Filter.Gabor.GaborFilter;
import com.googlepages.switch486.MAS.Engine.Image.Filter.SinusFilter.SinusFilter;
import com.googlepages.switch486.MAS.IODB.IIODB;

public class EngineFacade implements IEngine {
	
	private IIODB iODBWorker;

	public void setIODBWorker(IIODB iODBWorker) {
		this.iODBWorker = iODBWorker;
	}
	
	private static final Logger logger = Logger.getLogger(EngineFacade.class.getName());
	
	private GaborFilter gf;
	private SinusFilter sf;
	
	private String OutPutFileLocation;
	
	public EngineFacade() {
		gf = new GaborFilter();
		sf = new SinusFilter();
	}
	
	@Override
	public void executeParameters(Params p) {
		logger.fine("Execute passed parameters");
		OutPutFileLocation = p
				.getStringParam(Actions.G_FILEPATH_FOR_OUTPUT_FILES);
		gf.setGfParams(p);
		sf.setSfParams(p);
		if (p.contains(Actions.F_GABOR_FILTER_EXPORT)) {
			logger.info("<> Export Gabor Filter Action");
			iODBWorker.runShellCommandForFilterExport(gf.exportFilter(OutPutFileLocation));
		}
		if (p.contains(Actions.F_GABOR_FILTER_MATRIX_EXPORT)) {
			logger.info("<> Export Gabor Filter Matrix Action");
			iODBWorker.runShellCommandForFilterMatrixExport(gf.exportFilterMatrix(OutPutFileLocation));
		}
		if (p.contains(Actions.F_FILTER_WITH_GABOR_FILTER_ONCE)) {
			logger.info("<> Filter Image with the predefined Gabor Filter");
			if (p.contains(Actions.G_SOURCE_IMAGE)) {
				iODBWorker.writeImage(gf.filter(iODBWorker.getImage(
						p.getStringParam(Actions.G_SOURCE_IMAGE))), 
						p.getStringParam(Actions.G_FILEPATH_FOR_OUTPUT_FILES));
			} else {
				logger.warning("There was no source image to start the filtering with");
			}
		}
		if (p.contains(Actions.F_FILTER_WITH_GABOR_FILTER)) {
			logger.info("<> Filter Image with the predefined Gabor Filter and join results");
			if (p.contains(Actions.G_SOURCE_IMAGE)) {
				ArrayList<String> list = new ArrayList<String>();
				AIImage filterImage =  iODBWorker.getImage(p.getStringParam(Actions.G_SOURCE_IMAGE));
				for (double w0 = 0; w0<1; w0+=0.1d){
					gf.setGfParams(p, new DoubleParam(Actions.D_gabor_sin_direction, w0));
					list.add(iODBWorker.writeImage(gf.filter(filterImage), 
						p.getStringParam(Actions.G_FILEPATH_FOR_OUTPUT_FILES)));
				}
				logger.fine("Images created, joining and removing temp images...");
				iODBWorker.runShellCommandForResultsMerge(list);
			} else {
				logger.warning("There was no source image to start the filtering with");
			}
		}
		if (p.contains(Actions.F_SINUS_FILTER_EXPORT)) {
			logger.info("<> Export Sinus Filter Matrix Action");
			iODBWorker.runShellCommandForFilterMatrixExport(sf.exportFilterMatrix(OutPutFileLocation));
		}
		
		//logger.fine(p.toString());
	}

	

	
	
}
