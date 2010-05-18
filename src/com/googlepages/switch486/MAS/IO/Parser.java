package com.googlepages.switch486.MAS.IO;

import jargs.gnu.CmdLineParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import com.googlepages.switch486.MAS.Bean.Actions;
import com.googlepages.switch486.MAS.Bean.DoubleParam;
import com.googlepages.switch486.MAS.Bean.IntegerParam;
import com.googlepages.switch486.MAS.Bean.Params;
import com.googlepages.switch486.MAS.Bean.StringParam;
import com.googlepages.switch486.MAS.Engine.IEngine;

/**
 * @author switch486 based on the jargs example
 */
public class Parser extends CmdLineParser {
	List<String> optionHelpStrings = new ArrayList<String>();
	private final String DocumentDetailsName = "appHelp.pdf";
	private final String seeDetails = "\n\tFor more details, see: "
			+ DocumentDetailsName;
	private static final String userDir = System.getProperty("user.dir");
	private static final String projectDir = "/src/com/googlepages/switch486/MAS/IO/";
	private static final String beanConfigDir = "/res/";

	private static final Logger logger = Logger.getLogger("com.googlepages.switch486.MAS");

	private File f = new File(userDir+projectDir+"defaults.properties");
	private Properties properties = new Properties();
	private FileHandler fh;
	
	private IEngine engineFacade;

	public void setEngine(IEngine engineFacade) {
		this.engineFacade = engineFacade;
	}

	public void loadProperties() {
		InputStream is;
		try {
			is = new FileInputStream(f);
			properties.load(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loggerSettings(String loggerFilePath) {
		try {
			File f = new File(loggerFilePath + System.currentTimeMillis()
					+ ".log");
			boolean success = f.createNewFile();
			if (!success) {
				throw new Exception("File was not created!");
			}
			fh = new FileHandler(f.getAbsolutePath(), true);
			logger.addHandler(fh);
			logger.setLevel(Level.ALL);
			fh.setFormatter(new SimpleFormatter());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Parser() {
		System.setProperty("file.encoding", "UTF-8");
	}

	/**
	 * @param option
	 * @param helpString
	 * @param example
	 * @return
	 */
	public Option addHelp(Option option, String helpString, String example) {
		optionHelpStrings.add(" -" + option.shortForm() + "/--"
				+ option.longForm() + " :\t" + helpString + "\n\t\t\'" + example
				+ "\'");
		return option;
	}

	/**
	 * 
	 */
	public void printUsage() {
		System.err.println("usage: prog [options]");
		for (Iterator<String> i = optionHelpStrings.iterator(); i.hasNext();) {
			System.err.println(i.next());
		}
		System.err.println(seeDetails);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BeanFactory beanFactory =
			new XmlBeanFactory(
					new FileSystemResource(
					userDir+beanConfigDir+"bean-config.xml"));
		
		Parser parser = (Parser) beanFactory.getBean(Parser.class.getName());
		//parser.engineFacade.setLoggerParrent(logger);
		/*logger.info(parser.toString());*/
		parser.loadProperties();
		parser.loggerSettings(parser.properties
				.getProperty("logger.outFilePath"));
		
		Params p = new Params();
		
		
		
		/*
		 * CmdLineParser.Option verbose =
		 * parser.addHelp(parser.addBooleanOption('v', "verbose"),
		 * "Print extra information while @ work"); CmdLineParser.Option size =
		 * parser.addHelp(parser.addIntegerOption('s', "size"),
		 * "The extent of the thing"); CmdLineParser.Option name =
		 * parser.addHelp(parser.addStringOption ('n', "name"),
		 * "Name given to the widget"); CmdLineParser.Option help =
		 * parser.addHelp(parser.addBooleanOption('h', "help"),
		 * "Show this help message");
		 */
		Parser.logger.fine("Construct the help.");
		CmdLineParser.Option filePath = parser.addHelp(parser
				.addStringOption('L', "LOCATION"),
				"Location: Specify a location for output files of anny kind (Logs locations are defined in the defaults.properties!)",
				"./CandA -L \"/home/Martin/myFiles/\"");
		
		CmdLineParser.Option sourceImage = parser.addHelp(parser
				.addStringOption('S', "SOURCE"),
				"Location: Specify a location for the image that has to transformed with the Gabor Filter",
				"./CandA -S \"/home/Martin/myFiles/adam.png\" -G FILTER");
		
		CmdLineParser.Option gaborFilter = parser.addHelp(parser
				.addStringOption('G', "GABOR"),
				"Gabor: Perform calculations using the Gabor Filter, possibilities follow:",
				"EXPORT :\tExport the Filter into a graphic file, under the location for the logs in the defaults.properties " +
				"\n\t\t\t\tor - if specified - into the --LOCATION filepath\n" +
				"\t\tEXPORTFM :\tExport the Filter Matrix of the created Gabor Filter into a graphic file\n" +
				"\t\tFILTER :\tExport the Filtered image file under the --SOURCE filepath using the filter ONCE\n" +
				"\t\tJOINFILTER :\tExport the Filtered images under the --SOURCE filepath using the filter 10 times, and joining the results into one.\n" +
				"\t\t./CandA -G EXPORT");
		
		CmdLineParser.Option gaborPhase = parser.addHelp(parser
				.addDoubleOption('p', "gabor_sin_phase"),
				"Gabor: Set the phase of the complex sinus function",
				"./CandA -p 12,3");
		CmdLineParser.Option gaborF0 = parser.addHelp(parser.addDoubleOption(
				'f', "gabor_sin_magnitude"),
				"Gabor: Set the magnitude of the complex sinus function",
				"./CandA --gabor_sin_magnitude 4");
		CmdLineParser.Option gaborW0 = parser.addHelp(parser.addDoubleOption(
				'w', "gabor_sin_direction"),
				"Gabor: Set the direction of the complex sinus function",
				"./CandA -w 3,14");
		CmdLineParser.Option gaborK = parser.addHelp(parser.addDoubleOption(
				'k', "gabor_gaus_scale"), "Gabor: Set the Gaussian envelope scale",
				"./CandA -k 1");
		CmdLineParser.Option gaborX = parser.addHelp(parser.addDoubleOption(
				'x', "gabor_gaus_x"), "Gabor: Set the Gaussian envelope x position",
				"./CandA -x 0");
		CmdLineParser.Option gaborY = parser.addHelp(parser.addDoubleOption(
				'y', "gabor_gaus_y"), "Gabor: Set the Gaussian envelope y position",
				"./CandA -y 0");
		CmdLineParser.Option gaborA = parser.addHelp(parser.addDoubleOption(
				'a', "gabor_gaus_a"), "Gabor: Set the Gaussian envelope scale a",
				"./CandA -a 0,26");
		CmdLineParser.Option gaborB = parser.addHelp(parser.addDoubleOption(
				'b', "gabor_gaus_b"), "Gabor: Set the Gaussian envelope scale b",
				"./CandA -b 0,26");
		CmdLineParser.Option gaborTheta = parser.addHelp(parser
				.addDoubleOption('t', "gabor_gaus_theta"),
				"Gabor: Set the Gaussian envelope rotation angle",
				"./CandA -t 3,14");
		CmdLineParser.Option gaborFilterMatrixX = parser.addHelp(parser.addIntegerOption(
				's', "gabor_filter_matrix_s"), "Gabor: Set width of the filter matrix.",
				"./CandA -s 7");
		CmdLineParser.Option gaborFilterMatrixY = parser.addHelp(parser.addIntegerOption(
				'd', "gabor_filter_matrix_d"), "Gabor: Set height of the filter matrix",
				"./CandA -d 7");

		
		CmdLineParser.Option sinusFilter = parser.addHelp(parser
				.addStringOption('H', "SINUS"),
				"Sinus: Perform calculations using the Sinus 'Filter', possibilities follow:",
				"EXPORT :\tExport the Sinus Filters into a multiimage \n" +
				"\t\tEXPORTFM :\tExport the Sinus Filter matrix into a set of images \n" +
				"\t\tFILTER :\tFilters the output of the Gabor filter(SOURCE) with a _Sinus_ filter\n" +
				"\t\t./CandA -H EXPORT");
		
		CmdLineParser.Option sinusI = parser.addHelp(parser.addIntegerOption(
				'i', "sinus_i"), "Sinus: Set the sinus filter matrix height",
				"./CandA -i 10");
		CmdLineParser.Option sinusU = parser.addHelp(parser.addIntegerOption(
				'u', "sinus_u"), "Sinus: Set the sinus filter matrix width",
				"./CandA -u 10");
		CmdLineParser.Option sinusO = parser.addHelp(parser.addIntegerOption(
				'o', "sinus_o"), "Sinus: Set the sinus fine level - how many thics are going to be in the image chosen.",
				"./CandA -o 8");
		CmdLineParser.Option sinusMagnitude = parser.addHelp(parser.addDoubleOption(
				'm', "sin_magnitude"),
				"Sinus: Set the magnitude of the complex sinus function",
				"./CandA --sin_magnitude 4");
		
		
		CmdLineParser.Option verbose = parser.addHelp(parser.addBooleanOption(
				'v', "verbose"), "Show some more info when @ work (all log levels!)",
		"./CandA --verbose");
		CmdLineParser.Option help = parser.addHelp(parser.addBooleanOption('h',
		"help"), "Show this help message", "./CandA -h");
		
		/*
		 * ---------------------------------------------------------------------------------------
		 */
		Parser.logger.fine("Begin parsing.");
		try {
			parser.parse(args);
		} catch (CmdLineParser.OptionException e) {
			Parser.logger.log(Level.SEVERE, "Parser Exception", e);
			parser.printUsage();
			System.exit(2);
		}

		if (Boolean.TRUE.equals(parser.getOptionValue(help))) {
			Parser.logger.fine("Print help and exit.");
			parser.printUsage();
			System.exit(0);
		}

		// Extract the values entered for the various options -- if the
		// options were not specified, the corresponding values will be
		// null.

		logger.fine("Get parsed values.");
		String LOCATION = (String) parser.getOptionValue(filePath);
		if (LOCATION == null) {
			LOCATION = parser.properties.getProperty("logger.outFilePath");
		}
		LOCATION = LOCATION.endsWith("/") ? LOCATION : LOCATION+"/";
		p.add(new StringParam(Actions.G_FILEPATH_FOR_OUTPUT_FILES, LOCATION));
		
		String SOURCE = (String) parser.getOptionValue(sourceImage);
		if (SOURCE != null) {
			p.add(new StringParam(Actions.G_SOURCE_IMAGE, SOURCE));
		}

		String GABOR = (String) parser.getOptionValue(gaborFilter);
		if (GABOR != null) {
			if (GABOR.equals("EXPORT")) {
				p.add(new StringParam(Actions.F_GABOR_FILTER_EXPORT, GABOR));
			} else if (GABOR.equals("EXPORTFM")) {
				p.add(new StringParam(Actions.F_GABOR_FILTER_MATRIX_EXPORT,	GABOR));
			} else if (GABOR.equals("FILTER")) {
				p.add(new StringParam(Actions.F_FILTER_WITH_GABOR_FILTER_ONCE, GABOR));
			} else if (GABOR.equals("JOINFILTER")) {
				p.add(new StringParam(Actions.F_FILTER_WITH_GABOR_FILTER, GABOR));
			} else {
				logger.info("Unexpected parameter found: " + GABOR	+ "; skipping");
			}
		}
		
		Double gaborPhaseValue = (Double) parser.getOptionValue(gaborPhase);
		if (gaborPhaseValue == null)
			gaborPhaseValue = Double.parseDouble(parser.properties
					.getProperty("gabor.sin_phase"));
		p.add(new DoubleParam(Actions.D_gabor_sin_phase, gaborPhaseValue));

		Double gaborF0Value = (Double) parser.getOptionValue(gaborF0);
		if (gaborF0Value == null)
			gaborF0Value = Double.parseDouble(parser.properties
					.getProperty("gabor.sin_magnitude"));
		p.add(new DoubleParam(Actions.D_gabor_sin_magnitude, gaborF0Value));

		Double gaborW0Value = (Double) parser.getOptionValue(gaborW0);
		if (gaborW0Value == null)
			gaborW0Value = Double.parseDouble(parser.properties
					.getProperty("gabor.sin_direction"));
		p.add(new DoubleParam(Actions.D_gabor_sin_direction, gaborW0Value));

		Double gaborKValue = (Double) parser.getOptionValue(gaborK);
		if (gaborKValue == null)
			gaborKValue = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_scale"));
		p.add(new DoubleParam(Actions.D_gabor_gaus_scale, gaborKValue));

		Double gaborXValue = (Double) parser.getOptionValue(gaborX);
		if (gaborXValue == null)
			gaborXValue = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_x"));
		p.add(new DoubleParam(Actions.D_gabor_gaus_x, gaborXValue));

		Double gaborYValue = (Double) parser.getOptionValue(gaborY);
		if (gaborYValue == null)
			gaborYValue = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_y"));
		p.add(new DoubleParam(Actions.D_gabor_gaus_y, gaborYValue));

		Double gaborAValue = (Double) parser.getOptionValue(gaborA);
		if (gaborAValue == null)
			gaborAValue = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_a"));
		p.add(new DoubleParam(Actions.D_gabor_gaus_a, gaborAValue));

		Double gaborBValue = (Double) parser.getOptionValue(gaborB);
		if (gaborBValue == null)
			gaborBValue = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_b"));
		p.add(new DoubleParam(Actions.D_gabor_gaus_b, gaborBValue));

		Double gaborThetaValue = (Double) parser.getOptionValue(gaborTheta);
		if (gaborThetaValue == null)
			gaborThetaValue = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_theta"));
		p.add(new DoubleParam(Actions.D_gabor_gaus_theta, gaborThetaValue));
		
		Integer gaborFilterMatriX = (Integer) parser.getOptionValue(gaborFilterMatrixX);
		if (gaborFilterMatriX == null)
			gaborFilterMatriX = Integer.parseInt(parser.properties
					.getProperty("gabor.filter_matrix_x"));
		p.add(new IntegerParam(Actions.I_gabor_filter_matrix_x, gaborFilterMatriX));
		
		Integer gaborFilterMatriY = (Integer) parser.getOptionValue(gaborFilterMatrixY);
		if (gaborFilterMatriY == null)
			gaborFilterMatriY = Integer.parseInt(parser.properties
					.getProperty("gabor.filter_matrix_y"));
		p.add(new IntegerParam(Actions.I_gabor_filter_matrix_y, gaborFilterMatriY));
		
		
		String SINUS = (String) parser.getOptionValue(sinusFilter);
		if (SINUS != null) {
			if (SINUS.equals("EXPORT")) {
				p.add(new StringParam(Actions.F_SINUS_FILTER_EXPORT, SINUS));
			}else if (SINUS.equals("EXPORTFM")) {
				p.add(new StringParam(Actions.F_SINUS_FILTER_MATRIX_EXPORT, SINUS));
			}else if (SINUS.equals("FILTER")) {
				p.add(new StringParam(Actions.F_SINUS_FILTER_FILTER_IMAGE, SINUS));
			} else {
				logger.info("Unexpected parameter found: " + SINUS	+ "; skipping");
			}
		}
		
		Integer sinusFilterSinusI = (Integer) parser.getOptionValue(sinusI);
		if (sinusFilterSinusI == null)
			sinusFilterSinusI = Integer.parseInt(parser.properties
					.getProperty("sinus.filter_matrix_height"));
		p.add(new IntegerParam(Actions.I_sinus_filter_matrix_height, sinusFilterSinusI));
		
		Integer sinusFilterSinusU = (Integer) parser.getOptionValue(sinusU);
		if (sinusFilterSinusU == null)
			sinusFilterSinusU = Integer.parseInt(parser.properties
					.getProperty("sinus.filter_matrix_width"));
		p.add(new IntegerParam(Actions.I_sinus_filter_matrix_width, sinusFilterSinusU));
		
		Integer sinusFilterSinusO = (Integer) parser.getOptionValue(sinusO);
		if (sinusFilterSinusO == null)
			sinusFilterSinusO = Integer.parseInt(parser.properties
					.getProperty("sinus.fine_level"));
		p.add(new IntegerParam(Actions.I_sinus_filter_fine, sinusFilterSinusO));

		Double sinusMagnitudeValue = (Double) parser.getOptionValue(sinusMagnitude);
		if (sinusMagnitudeValue == null)
			sinusMagnitudeValue = Double.parseDouble(parser.properties
					.getProperty("sinus.magnitude"));
		p.add(new DoubleParam(Actions.D_sinus_magnitude, sinusMagnitudeValue));

		Boolean canHasVerbose = (Boolean) parser.getOptionValue(verbose);
		if (canHasVerbose != null && canHasVerbose.booleanValue() == true){			
			File f = new File(Parser.userDir+Parser.projectDir+"loggerVerbose.properties");
			try {
				Parser.logger.fine("reading the configuration file");
				LogManager.getLogManager().readConfiguration(new FileInputStream(f));
				parser.loggerSettings(parser.properties
						.getProperty("logger.outFilePath"));
				Parser.logger.fine("DONE reading the configuration file");
			} catch (SecurityException e) {
				Parser.logger.log(Level.SEVERE, "Logger readConfiguration fail", e);
			} catch (FileNotFoundException e) {
				Parser.logger.log(Level.SEVERE, "Logger readConfiguration fail", e);
			} catch (IOException e) {
				Parser.logger.log(Level.SEVERE, "Logger readConfiguration fail", e);
			}
		}
		
		parser.engineFacade.executeParameters(p);

		Parser.logger.finest("END application");

		System.exit(0);
	}
	
}
