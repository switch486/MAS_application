package com.googlepages.switch486.MAS.IODB;

import java.util.ArrayList;

import com.googlepages.switch486.MAS.Engine.Image.AIImage;

public interface IIODB {

	public void runShellCommandForFilterExport (String[] command);
	
	public void runShellCommandForFilterMatrixExport(String[] command);

	public AIImage getImage(String filePath);

	public String writeImage(AIImage filter, String stringParam);
	
	public void writeFile(String command, String fileName);

	public String runShellCommandForResultsMerge(ArrayList<String> list);

	public void deleteFile(String path);

}
