package com.googlepages.switch486.MAS.IODB;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.googlepages.switch486.MAS.Engine.Image.AIImage;

public class IODBWorker implements IIODB {
	
	private static final Logger logger = Logger.getLogger(IODBWorker.class.getName());

	public IODBWorker() {
		
	}

	@Override
	public void runShellCommandForFilterExport(String[] command) {
		writeFile(command[1], command[0]);
		
		String ComplexCommand = "gnuplot "+command[0];
		String secondCommand = "montage -geometry 640x480 "+command[2]+" -geometry 640x480 "+command[3]+" "+command[0]+".png";
		runShell(ComplexCommand);
		runShell(secondCommand);
		delete(command[2]);
		delete(command[3]);
	}

	@Override
	public void runShellCommandForFilterMatrixExport(String[] command) {
		writeFile(command[1], command[0]);
		int i=3; 
		while (i < command.length - 1) {
			writeFile(command[i+1], command[i]);
			i+=2;
		}
		
		String ComplexCommand = "gnuplot "+command[0];
		runShell(ComplexCommand);
	}

	public void writeFile(String command, String fileName) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(new File(fileName)));
			bw.append(command);
			bw.flush();
			bw.close();
		}catch(Exception e){
			logger.log(Level.WARNING, "Script export Exception", e);
		}
		logger.fine("The following file was written: ["+fileName+"]");
	}

	private void runShell(String command) {
		try {
			Runtime run = Runtime.getRuntime();
			Process pr = run.exec(command) ;
			pr.waitFor() ;
			BufferedReader normalBuff = new BufferedReader( new InputStreamReader( pr.getInputStream() ) ) ;
			BufferedReader errorBuff = new BufferedReader( new InputStreamReader( pr.getErrorStream() ) ) ;
			String lineError=null, lineNormal=null, goodLine = "", badLine = "";
			while ( (lineError= errorBuff.readLine() ) != null || (lineNormal = normalBuff.readLine() ) != null)
			{
				if (lineError==null) {
					goodLine+=lineNormal+"\n";
				}else {
					badLine+=lineError+"\n";
				}
			}
			if (goodLine.length() != 0)
				logger.fine(goodLine);
			if (badLine.length() != 0)
				logger.warning(badLine);
		} catch (Exception e) {
			logger.log(Level.WARNING, "runShellCommandException", e);
		}
		logger.fine("The following command was executed: ["+command+"]");
	}
	
	private void delete(String imageFilePath) {
	    try {
	      File target = new File(imageFilePath);

	      if (!target.exists()) {
	        logger.info("File " + imageFilePath+ " WAS no more..");
	        return;
	      }
	      if (target.delete())
	        logger.fine("File: " + imageFilePath + " removed succesfully!");
	      else
	    	logger.info("Failed to delete " + imageFilePath);
	    } catch (Exception e) {
	    	logger.log(Level.INFO, "Unable to delete " + imageFilePath ,e);
	    }
	  }

	public AIImage getImage(String filePath) {
		BufferedImage bimg = null;  
		try {  
			bimg = ImageIO.read(new File(filePath));  
			logger.info("File " + filePath+ " was succesfully got.");
		} catch (Exception e) {  
			logger.log(Level.SEVERE, "Unable to read image", e);  
		}  
		return new AIImage(bimg);
	}

	@Override
	public String writeImage(AIImage filter, String stringParam) {
		String res = null;
		try {
			res = stringParam+System.currentTimeMillis()+".png";
			ImageIO.write((BufferedImage)filter, "png", new File(res));
			logger.info("File " + res+ " was succesfully written.");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unable to write image", e);  
		}
		return res;
	}

	@Override
	public void runShellCommandForResultsMerge(ArrayList<String> list) {
		for (int i=1; i< list.size(); i++) {
			runShell("composite -compose Screen -gravity center "+list.get(i-1)+" "+list.get(i)+ " " + list.get(i));
		}
		logger.info("One output image created: "+ list.get(list.size()-1));
		for (int i=0; i< list.size()-1; i++) {
			delete(list.get(i));
		}
	}

	
}
