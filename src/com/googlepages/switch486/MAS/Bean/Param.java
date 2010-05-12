package com.googlepages.switch486.MAS.Bean;

public class Param {
	
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Param thi = (Param)this;
		Param other = (Param) obj;
		if (thi.action!=other.action)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return action.toString();
	}
	
}
