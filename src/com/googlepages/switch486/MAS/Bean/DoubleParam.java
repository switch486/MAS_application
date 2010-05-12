package com.googlepages.switch486.MAS.Bean;

public class DoubleParam extends Param {

	private Double parameter;
	
	public Double getParameter() {
		return parameter;
	}

	public void setParameter(Double parameter) {
		this.parameter = parameter;
	}

	public DoubleParam(Actions action, Double parameter) {
		super(action);
		this.parameter = parameter;
	}
	
	@Override
	public String toString() {
		return "["+super.toString()+",\t"+parameter+"]";
	}
	
}
