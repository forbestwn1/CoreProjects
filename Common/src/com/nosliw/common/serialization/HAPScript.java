package com.nosliw.common.serialization;

public class HAPScript extends HAPSerializableImp implements HAPJsonTypeAsItIs{

	private String m_script;
	
	public HAPScript(String script){
		this.m_script = script;
	}
	
	public String getScript(){		return this.m_script;	}
	
	@Override
	protected String buildFullJson(){ return this.m_script; }
	@Override
	protected String buildJson(){ return "Empty"; }

	@Override
	public String toString(){  return this.m_script;  }
}
