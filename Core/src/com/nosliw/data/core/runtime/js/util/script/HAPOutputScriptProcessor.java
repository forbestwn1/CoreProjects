package com.nosliw.data.core.runtime.js.util.script;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.script.expression.HAPExecutableScript;

public class HAPOutputScriptProcessor {

	private String m_functionBody;
	
	private List<HAPExecutableScript> m_children;
	
	public HAPOutputScriptProcessor() {
		this.m_children = new ArrayList<HAPExecutableScript>();
	}
	
	public String getFunctionBody() {    return this.m_functionBody;    }
	public void setFunctionBody(String body) {    this.m_functionBody = body;     }
	
	public List<HAPExecutableScript> getScriptChildren(){  return this.m_children;    }
	public void addChildren(HAPExecutableScript script) {    this.m_children.add(script);    }
	
}
