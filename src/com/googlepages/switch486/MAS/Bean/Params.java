package com.googlepages.switch486.MAS.Bean;

import java.util.ArrayList;
import java.util.Iterator;

public class Params extends ArrayList<Param> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6078811162699272265L;

	public Object get(Actions a){
		Param p = (new Param(a));
		int j=-1;
		for (int i = 0; i < this.size(); i++){
			Param m = get(i);
			if (p.equals(m))
			    j=i;
		}
		return j!=-1 ? get(j) : null;
	}
	
	public boolean contains(Actions a) {
		return contains(new Param(a));
	}
	
	public String getStringParam(Actions a) {
		return ((StringParam) get(a)).getParameter(); 
	}
	
	public Double getDoubleParam(Actions a) {
		return ((DoubleParam) get(a)).getParameter(); 
	}
	
	public Integer getIntParam(Actions a) {
		return ((IntegerParam) get(a)).getParameter(); 
	}
	
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
