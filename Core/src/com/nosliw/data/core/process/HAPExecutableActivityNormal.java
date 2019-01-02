package com.nosliw.data.core.process;

import com.nosliw.data.core.script.context.HAPContextFlat;

public abstract class HAPExecutableActivityNormal extends HAPExecutableActivity{

	private HAPDefinitionDataAssociationGroupExecutable m_input;

	private HAPDefinitionDataAssociationGroupExecutable m_output;
	
	
	public HAPExecutableActivityNormal(String id, HAPDefinitionActivity activityDef) {
		super(id, activityDef);
	}

	public void setInputDataAssociation(HAPDefinitionDataAssociationGroupExecutable input) {  this.m_input = input;  }

	public HAPContextFlat getInputContext() {
		if(this.m_input==null)   return null;
		return this.m_input.getContext();   
	}
	
	public void setOutputDataAssociation(HAPDefinitionDataAssociationGroupExecutable output) {  this.m_output = output;  }

	public HAPContextFlat getOutputContext() {
		if(this.m_output==null)   return null;
		return this.m_output.getContext();
	}
	
}
