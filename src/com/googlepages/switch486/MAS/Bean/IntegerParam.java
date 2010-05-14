package com.googlepages.switch486.MAS.Bean;

public class IntegerParam extends Param {

	private Integer parameter;
	
	public Integer getParameter() {
		return parameter;
	}

	public void setParameter(Integer parameter) {
		this.parameter = parameter;
	}

	public IntegerParam(Actions action, Integer parameter) {
		super(action);
		this.parameter = parameter;
	}
	
	@Override
	public String toString() {
		return "["+super.toString()+",\t"+parameter+"]";
	}
}
