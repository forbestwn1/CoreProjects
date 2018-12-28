package com.nosliw.data.core.process;

public abstract class HAPExecutableActivityNormal extends HAPExecutableActivity{

	private HAPDefinitionDataAssociationGroupExecutable m_input;

	private HAPDefinitionDataAssociationGroupExecutable m_output;
	
	
	public HAPExecutableActivityNormal(String id, HAPDefinitionActivity activityDef) {
		super(id, activityDef);
	}

	public void setInputDataAssociation(HAPDefinitionDataAssociationGroupExecutable input) {  this.m_input = input;  }

	public void setOutputDataAssociation(HAPDefinitionDataAssociationGroupExecutable output) {  this.m_output = output;  }
	
}
