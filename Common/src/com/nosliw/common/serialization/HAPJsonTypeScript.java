package com.nosliw.common.serialization;

public class HAPJsonTypeScript extends HAPSerializableImp implements HAPJsonTypeAsItIs{

	private String m_script;
	
	public HAPJsonTypeScript(String script){
		this.m_script = script;
	}
	
	public String getScript(){		return this.m_script;	}
	
	@Override
	protected String buildFullJson(){ return this.m_script; }
	@Override
	protected String buildJson(){ return this.m_script; }
	
	@Override
	protected String buildJavascript(){ return this.m_script; }

	@Override
	public String toString(){  return this.m_script;  }
}
