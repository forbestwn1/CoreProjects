package com.nosliw.data.core.runtime.js.util.script.expressionscrip2;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.resource.imp.js.HAPJSScriptInfo;

public class HAPScriptFunctionInfo {

	private HAPJSScriptInfo m_main;
	
	private List<HAPJSScriptInfo> m_children;
	
	public HAPScriptFunctionInfo() {
		this.m_children = new ArrayList<HAPJSScriptInfo>();
	}
	
	public void setMainScript(HAPJSScriptInfo main) {   this.m_main = main;   }
	public HAPJSScriptInfo getMainScript() {  return this.m_main;   }
	
	public List<HAPJSScriptInfo> getChildren() {   return this.m_children;   }
	public void addChild(HAPJSScriptInfo child) {    this.m_children.add(child);    }
	public void addChildren(List<HAPJSScriptInfo> children) {    this.m_children.addAll(children);    }
	
	public void mergeWith(HAPScriptFunctionInfo info) {
		this.addChild(info.getMainScript());
		this.addChildren(info.getChildren());
	}
	
}
