package com.nosliw.common.interpolate;

import java.util.HashSet;
import java.util.Set;

/*
 * class for interpolate process output
 */
public class HAPInterpolateOutput {
	//interpolate result 
	private String m_output;
	//unsolved element, 
	//by checking whether this has element, we know whether interpolate resolve successfully
	private Set<String> m_unsolved;
	
	public HAPInterpolateOutput(){
		this.m_unsolved = new HashSet<String>();
	}
	
	public void addUnsolved(String name){	this.m_unsolved.add(name);	}
	public void addUnsolved(Set<String> names){  this.m_unsolved.addAll(names);}
	
	public String getOutput(){ return this.m_output; }
	public Set<String> getUnsolved(){  return this.m_unsolved; }
	public void setOutput(String output){ this.m_output = output; } 
	
	public boolean isResolved(){  return this.m_unsolved.size()<=0; }
	
	@Override
	public String toString(){  return this.m_output; }
}
