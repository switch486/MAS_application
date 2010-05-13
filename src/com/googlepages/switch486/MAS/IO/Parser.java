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
		
		CmdLineParser.Option gaborFilter = parser.addHelp(parser
				.addStringOption('G', "GABOR"),
				"Gabor: Perform calculations using the Gabor Filter, possibilities follow:",
				"EXPORT :\tExport the Filter into a graphic file, under the location for the logs in the defaults.properties " +
				"\n\t\tor - if specified - into the --LOCATION filepath\n" +
				"\t\t./CandA -G EXPORT");
		
		CmdLineParser.Option gaborPhase = parser.addHelp(parser
				.addDoubleOption('p', "sin_phase"),
				"Gabor: Set the phase of the complex sinus function",
				"./CandA -p 12,3");
		CmdLineParser.Option gaborF0 = parser.addHelp(parser.addDoubleOption(
				'f', "sin_magnitude"),
				"Gabor: Set the magnitude of the complex sinus function",
				"./CandA --gaus_magnitude 4");
		CmdLineParser.Option gaborW0 = parser.addHelp(parser.addDoubleOption(
				'w', "sin_direction"),
				"Gabor: Set the direction of the complex sinus function",
				"./CandA -w 3,14");
		CmdLineParser.Option gaborK = parser.addHelp(parser.addDoubleOption(
				'k', "gaus_scale"), "Gabor: Set the Gaussian envelope scale",
				"./CandA -k 1");
		CmdLineParser.Option gaborX = parser.addHelp(parser.addDoubleOption(
				'x', "gaus_x"), "Gabor: Set the Gaussian envelope x position",
				"./CandA -x 0");
		CmdLineParser.Option gaborY = parser.addHelp(parser.addDoubleOption(
				'y', "gaus_y"), "Gabor: Set the Gaussian envelope y position",
				"./CandA -y 0");
		CmdLineParser.Option gaborA = parser.addHelp(parser.addDoubleOption(
				'a', "gaus_a"), "Gabor: Set the Gaussian envelope scale a",
				"./CandA -a 0,26");
		CmdLineParser.Option gaborB = parser.addHelp(parser.addDoubleOption(
				'b', "gaus_b"), "Gabor: Set the Gaussian envelope scale b",
				"./CandA -b 0,26");
		CmdLineParser.Option gaborTheta = parser.addHelp(parser
				.addDoubleOption('t', "gaus_theta"),
				"Gabor: Set the Gaussian envelope rotation angle",
				"./CandA -t 3,14");

		CmdLineParser.Option verbose = parser.addHelp(parser.addBooleanOption(
				'v', "verbose"), "Show some more info when @ work (all log levels!)",
				"./CandA --verbose");
		CmdLineParser.Option help = parser.addHelp(parser.addBooleanOption('h',
				"help"), "Show this help message", "./CandA -h");

		
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
		p.add(new StringParam(Actions.G_FILEPATH_FOR_OUTPUT_FILES, LOCATION));

		String GABOR = (String) parser.getOptionValue(gaborFilter);
		if (GABOR != null) {
			if (GABOR.equals("EXPORT")){
				p.add(new StringParam(Actions.F_GABOR_FILTER_EXPORT, GABOR));
			}else {
				logger.info("Unexpected parameter found: "+GABOR+"; skipping");
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

		Boolean canHasVerbose = (Boolean) parser.getOptionValue(verbose);
		if (canHasVerbose != null && canHasVerbose.booleanValue() == true){			
			File f = new File(Parser.userDir+Parser.projectDir+"loggerVerbose.properties");
			try {
				Parser.logger.fine("reading the configuration file");
				LogManager.getLogManager().readConfiguration(new FileInputStream(f));
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


		System.exit(0);
	}
	
}
