package com.googlepages.switch486.MAS.Bean;

public abstract class Param {
	
	private Actions action; 
	
	public Param (Actions action ) {
		this.action = action;
	}

	public Actions getAction() {
		return action;
	}

	public void setAction(Actions action) {
		this.action = action;
	}
	
	@Override
	public String toString() {
		return action.toString();
	}
	
}
