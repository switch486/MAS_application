package com.googlepages.switch486.MAS.IO;

import jargs.gnu.CmdLineParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import com.googlepages.switch486.MAS.Engine.Image.Filter.Gabor.GaborFilter;
import com.googlepages.switch486.MAS.Engine.Image.Filter.Gabor.IGaborFilter;

/**
 * @author switch486 based on the jargs example
 */
public class IOClass extends CmdLineParser {
	List<String> optionHelpStrings = new ArrayList<String>();
	private final String DocumentDetailsName = "appHelp.pdf";
	private final String seeDetails = "\n\tFor more details, see: "
			+ DocumentDetailsName;
	private static final String userDir = System.getProperty("user.dir");
	private static final String projectDir = "/src/com/googlepages/switch486/MAS/IO/";
	private static final String beanConfigDir = "/res/";

	private static final Logger logger = Logger.getLogger(IOClass.class
			.getName());

	private File f = new File(userDir+projectDir+"defaults.properties");
	private Properties properties = new Properties();
	private FileHandler fh;
	
	private IGaborFilter gaborFilter;

	public void setGaborFilter(IGaborFilter gaborFilter) {
		this.gaborFilter = gaborFilter;
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

	public IOClass() {
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
				+ option.longForm() + ":\t" + helpString + "\n\t\t\'" + example
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
		
		IOClass parser = (IOClass) beanFactory.getBean(IOClass.class.getName());
		/*logger.info(parser.toString());
		logger.info(parser.gaborFilter.toString());*/
		parser.loadProperties();
		parser.loggerSettings(parser.properties
				.getProperty("logger.outFilePath"));
		
		//TODO -- sciezka
		
		

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

		IOClass.logger.fine("Construct the help.");
		CmdLineParser.Option gaborPhase = parser.addHelp(parser
				.addDoubleOption('p', "gaus_phase"),
				"Gabor: Set the phase of the complex sinus function",
				"./CandA -p 12,3");
		CmdLineParser.Option gaborF0 = parser.addHelp(parser.addDoubleOption(
				'f', "gaus_magnitude"),
				"Gabor: Set the magnitude of the complex sinus function",
				"./CandA --gaus_magnitude 4");
		CmdLineParser.Option gaborW0 = parser.addHelp(parser.addDoubleOption(
				'w', "gaus_direction"),
				"Gabor: Set the direction of the complex sinus function",
				"./CandA -w 3,14");
		CmdLineParser.Option gaborK = parser.addHelp(parser.addDoubleOption(
				'K', "gaus_scale"), "Gabor: Set the Gaussian envelope scale",
				"./CandA -K 1");
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
				.addDoubleOption('T', "gaus_theta"),
				"Gabor: Set the Gaussian envelope rotation angle",
				"./CandA -T 3,14");

		CmdLineParser.Option verbose = parser.addHelp(parser.addBooleanOption(
				'v', "verbose"), "Show some more info when @ work (all log levels!)",
				"./CandA --verbose");
		CmdLineParser.Option help = parser.addHelp(parser.addBooleanOption('h',
				"help"), "Show this help message", "./CandA -h");

		IOClass.logger.fine("Begin parsing.");
		try {
			parser.parse(args);
		} catch (CmdLineParser.OptionException e) {
			IOClass.logger.log(Level.SEVERE, "Parser Exception", e);
			parser.printUsage();
			System.exit(2);
		}

		if (Boolean.TRUE.equals(parser.getOptionValue(help))) {
			parser.printUsage();
			IOClass.logger.fine("Print help and exit.");
			System.exit(0);
		}

		// Extract the values entered for the various options -- if the
		// options were not specified, the corresponding values will be
		// null.

		IOClass.logger.fine("Get parsed values.");

		Double gaborPhaseValue = (Double) parser.getOptionValue(gaborPhase);
		if (gaborPhaseValue == null)
			gaborPhaseValue = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_phase"));

		Double gaborF0Value = (Double) parser.getOptionValue(gaborF0);
		if (gaborF0Value == null)
			gaborF0Value = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_magnitude"));

		Double gaborW0Value = (Double) parser.getOptionValue(gaborW0);
		if (gaborW0Value == null)
			gaborW0Value = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_direction"));

		Double gaborKValue = (Double) parser.getOptionValue(gaborK);
		if (gaborKValue == null)
			gaborKValue = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_scale"));

		Double gaborXValue = (Double) parser.getOptionValue(gaborX);
		if (gaborXValue == null)
			gaborXValue = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_x"));

		Double gaborYValue = (Double) parser.getOptionValue(gaborY);
		if (gaborYValue == null)
			gaborYValue = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_y"));

		Double gaborAValue = (Double) parser.getOptionValue(gaborA);
		if (gaborAValue == null)
			gaborAValue = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_a"));

		Double gaborBValue = (Double) parser.getOptionValue(gaborB);
		if (gaborBValue == null)
			gaborBValue = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_b"));

		Double gaborThetaValue = (Double) parser.getOptionValue(gaborTheta);
		if (gaborThetaValue == null)
			gaborThetaValue = Double.parseDouble(parser.properties
					.getProperty("gabor.gaus_theta"));

		Boolean canHasVerbose = (Boolean) parser.getOptionValue(verbose);
		if (canHasVerbose != null && canHasVerbose.booleanValue() == true){			
			File f = new File(parser.userDir+parser.projectDir+"loggerVerbose.properties");
			try {
				IOClass.logger.fine("reading the configuration file");
				LogManager.getLogManager().readConfiguration(new FileInputStream(f));
			} catch (SecurityException e) {
				IOClass.logger.log(Level.SEVERE, "Logger readConfiguration fail", e);
			} catch (FileNotFoundException e) {
				IOClass.logger.log(Level.SEVERE, "Logger readConfiguration fail", e);
			} catch (IOException e) {
				IOClass.logger.log(Level.SEVERE, "Logger readConfiguration fail", e);
			}
		}
		

		/*
		 * String[] otherArgs = parser.getRemainingArgs();
		 * System.out.println("remaining args: "); for ( int i = 0; i <
		 * otherArgs.length; ++i ) { System.out.println(otherArgs[i]); }
		 */

		// In a real program, one would pass the option values and other
		// arguments to a function that does something more useful.

		System.exit(0);
	}
	
	private void print(PrintStream out) {
		out.println("asdf: ");
		LogManager logManager = LogManager.getLogManager();
		Enumeration<String> loggerNames = logManager.getLoggerNames();
		Set<String> names = new TreeSet<String>();
		Set<Handler> handlerSet = new HashSet<Handler>();
		while (loggerNames.hasMoreElements()) {
			String name = loggerNames.nextElement();
			names.add(name);
		}
		for (String name : names) {
			Logger logger = logManager.getLogger(name);
			String nome = logger.getName();
			if (nome.length() == 0) nome = "<NONAME>";
//			boolean inheritedLevel = false;
			Level level = logger.getLevel();
//			boolean inheritedHandler = false;
			Handler[] handlers = logger.getHandlers();

			Logger parentLevelLogger = null;
			if (level == null) {
				parentLevelLogger = logger.getParent();
				while (parentLevelLogger != null && level == null) {
					Level parentLevel = parentLevelLogger.getLevel();
					if (parentLevel != null) {
						level = parentLevel;
					} else {
						parentLevelLogger = parentLevelLogger.getParent();
					}
				}
			}
			Logger parentHandlerLogger = null;
			if (handlers == null) {
				parentHandlerLogger = logger.getParent();
				while (parentHandlerLogger != null && handlers == null) {
					Handler[] parentHandlers = parentHandlerLogger.getHandlers();
					if (parentHandlers != null) {
						handlers = parentHandlers;
					} else {
						parentHandlerLogger = parentHandlerLogger.getParent();
					}
				}
			}
			String nivel = "?";
			if (level != null) nivel = level.getName();
			if (parentLevelLogger != null) {
				if (parentLevelLogger.getName().length() == 0)
					nivel = "<root>:"+nivel;
				else
					nivel = parentLevelLogger.getName()+":"+nivel;
			}
			String lista = "?";
			if (handlers != null) {
				List<Handler> list = Arrays.asList(handlers);
				if (list.size() == 0) {
					lista = "";
				} else {
					lista = null;
					for (Handler handler : list) {
						if (lista == null) lista = handler.getClass().getName();
						else lista += ", " + handler.getClass().getSimpleName();
					}
				}
			}
			if (parentHandlerLogger != null) {
				if (parentHandlerLogger.getName().length() == 0)
					nivel = "<root>:"+lista;
				else
					nivel = parentHandlerLogger.getName()+":"+lista;
			}
			out.format(" - %s (%s): %s\n", nome, nivel, lista);
		}
		for (Handler handler : handlerSet) {
			String nome = handler.getClass().getSimpleName();
			String nivel = handler.getLevel().getName();
			out.format(" - %s (%s)\n", nome, nivel);
		}
	}


}
