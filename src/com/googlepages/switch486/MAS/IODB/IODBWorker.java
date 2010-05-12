package com.googlepages.switch486.MAS.IODB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IODBWorker implements IIODB {
	
	private static final Logger logger = Logger.getLogger(IODBWorker.class.getName());

	public IODBWorker() {
		
	}

	@Override
	public void runShellCommand(String command) {
		String ComplexCommand = "echo '"+command+"' > /home/switch486/out; sh /home/switch486/out;";
		try {
			Runtime run = Runtime.getRuntime();
			Process pr = run.exec("") ;
			pr.waitFor() ;
			BufferedReader buf = new BufferedReader( new InputStreamReader( pr.getInputStream() ) ) ;
			String line ;
			while ( (line = buf.readLine() ) != null )
			{
			System.out.println(line) ;
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "runShellCommandException", e);
		}
		logger.fine("The following command was succesfully executed");
		logger.fine("["+command+"] \nTHROUGH: ["+ComplexCommand+"]");
	}
	
}
