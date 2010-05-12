package com.googlepages.switch486.MAS.Bean;

public class StringParam extends Param{

	private String parameter;

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public StringParam(Actions action, String parameter) {
		super(action);
		this.parameter = parameter;
	}
	
	@Override
	public String toString() {
		return "["+super.toString()+",\t"+parameter+"]";
	}
	
}
