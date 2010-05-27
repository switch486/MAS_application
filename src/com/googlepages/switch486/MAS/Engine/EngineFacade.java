package com.googlepages.switch486.MAS.Engine;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.googlepages.switch486.MAS.Bean.Actions;
import com.googlepages.switch486.MAS.Bean.DoubleParam;
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
	
	public void setGf(GaborFilter gf) {
		this.gf = gf;
	}

	public void setSf(SinusFilter sf) {
		this.sf = sf;
	}

	private String OutPutFileLocation;
	
	public EngineFacade() {
	}
	
	@Override
	public void executeParameters(Params p) {
		logger.fine("Execute passed parameters");
		OutPutFileLocation = p
				.getStringParam(Actions.G_FILEPATH_FOR_OUTPUT_FILES);
		gf.setGfParams(p);
		sf.setSfParams(p);
		if (p.contains(Actions.F_GABOR_FILTER_EXPORT)) {
			f_gabor_filter_export();
		}
		if (p.contains(Actions.F_GABOR_FILTER_MATRIX_EXPORT)) {
			f_gabor_filter_matrix_export();
		}
		if (p.contains(Actions.F_FILTER_WITH_GABOR_FILTER_ONCE)) {
			f_filter_with_gabor_filter_once(p);
		}
		if (p.contains(Actions.F_FILTER_WITH_GABOR_FILTER)) {
			f_filter_with_gabor_filter(p);
		}
		if (p.contains(Actions.F_SINUS_FILTER_MATRIX_EXPORT)) {
			f_sinus_filter_matrix_export();
		}
		if (p.contains(Actions.F_SINUS_FILTER_EXPORT)) {
			f_sinus_filter_export();
		}
		if (p.contains(Actions.F_SINUS_FILTER_FILTER_IMAGE)) {
			f_sinus_filter_filter_image(p);
		}
		logger.info("Work done!");
		//logger.fine(p.toString());
	}

	private void f_sinus_filter_filter_image(Params p) {
		logger.info("<> Filter Image with the predefined Gabor Filter, join results and filter it with the sinus filter");
		if (p.contains(Actions.G_SOURCE_IMAGE)) {
			ArrayList<String> list = new ArrayList<String>();
			AIImage filterImage =  iODBWorker.getImage(p.getStringParam(Actions.G_SOURCE_IMAGE));
			logger.info("(1/2) Image created, joining and removing temp images...");
			for (double w0 = 0; w0<1; w0+=0.1d){
				gf.setGfParams(p, new DoubleParam(Actions.D_gabor_sin_direction, w0));
				list.add(iODBWorker.writeImage(gf.filter(filterImage), 
					p.getStringParam(Actions.G_FILEPATH_FOR_OUTPUT_FILES)));
			}
			String result = iODBWorker.runShellCommandForResultsMerge(list);
			p.resetParameterForAction(Actions.G_SOURCE_IMAGE, result);
			logger.info("(2/2) Filtering with Sinus...");
			AIImage i = sf.filter(iODBWorker.getImage(p.getStringParam(Actions.G_SOURCE_IMAGE)));
			iODBWorker.writeImage(i, p.getStringParam(Actions.G_FILEPATH_FOR_OUTPUT_FILES));
			//iODBWorker.deleteFile(result);
			logger.fine("Done...");
		} else {
			logger.warning("There was no SOURCE image to start the filterings with");
		}
	}

	private void f_sinus_filter_export() {
		logger.info("<> Export Sinus Filter Action");
		iODBWorker.runShellCommandForFilterExport(sf.exportFilter(OutPutFileLocation));
	}

	private void f_sinus_filter_matrix_export() {
		logger.info("<> Export Sinus Filter Matrix Action");
		iODBWorker.runShellCommandForFilterMatrixExport(sf.exportFilterMatrix(OutPutFileLocation));
	}

	private void f_filter_with_gabor_filter(Params p) {
		logger.info("<> Filter Image with the predefined Gabor Filter and join results");
		if (p.contains(Actions.G_SOURCE_IMAGE)) {
			ArrayList<String> list = new ArrayList<String>();
			AIImage filterImage =  iODBWorker.getImage(p.getStringParam(Actions.G_SOURCE_IMAGE));
			for (double w0 = 0; w0<1; w0+=0.25d){
				gf.setGfParams(p, new DoubleParam(Actions.D_gabor_sin_direction, w0));
				list.add(iODBWorker.writeImage(gf.filter(filterImage), 
					p.getStringParam(Actions.G_FILEPATH_FOR_OUTPUT_FILES)));
			}
			logger.fine("Images created, joining and removing temp images...");
			iODBWorker.runShellCommandForResultsMerge(list);
		} else {
			logger.warning("There was no SOURCE image to start the filtering with");
		}
	}

	private void f_filter_with_gabor_filter_once(Params p) {
		logger.info("<> Filter Image with the predefined Gabor Filter");
		if (p.contains(Actions.G_SOURCE_IMAGE)) {
			iODBWorker.writeImage(gf.filter(iODBWorker.getImage(
					p.getStringParam(Actions.G_SOURCE_IMAGE))), 
					p.getStringParam(Actions.G_FILEPATH_FOR_OUTPUT_FILES));
		} else {
			logger.warning("There was no source image to start the filtering with");
		}
	}

	private void f_gabor_filter_matrix_export() {
		logger.info("<> Export Gabor Filter Matrix Action");
		iODBWorker.runShellCommandForFilterMatrixExport(gf.exportFilterMatrix(OutPutFileLocation));
	}

	private void f_gabor_filter_export() {
		logger.info("<> Export Gabor Filter Action");
		iODBWorker.runShellCommandForFilterExport(gf.exportFilter(OutPutFileLocation));
	}

	

	
	
}
