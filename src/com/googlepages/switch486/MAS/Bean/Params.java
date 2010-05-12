package com.googlepages.switch486.MAS.Bean;

import java.util.ArrayList;
import java.util.Iterator;

public class Params extends ArrayList<Param> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6078811162699272265L;

	@Override
	public String toString() {
		Iterator<Param> i = this.iterator();
		if (! i.hasNext())
		    return "[]";

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (;;) {
			Param e = i.next();
		    sb.append("\n"+e);
		    if (! i.hasNext())
			return sb.append(']').toString();
		    sb.append(", ");
		}
	}
	
}
