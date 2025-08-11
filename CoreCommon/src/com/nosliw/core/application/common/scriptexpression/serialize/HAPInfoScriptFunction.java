package com.nosliw.core.application.common.scriptexpression.serialize;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.script.HAPJSScriptInfo;

public class HAPInfoScriptFunction {

	private HAPJSScriptInfo m_main;
	
	private List<HAPJSScriptInfo> m_children;
	
	public HAPInfoScriptFunction() {
		this.m_children = new ArrayList<HAPJSScriptInfo>();
	}
	
	public void setMainScript(HAPJSScriptInfo main) {   this.m_main = main;   }
	public HAPJSScriptInfo getMainScript() {  return this.m_main;   }
	
	public List<HAPJSScriptInfo> getChildren() {   return this.m_children;   }
	public void addChild(HAPJSScriptInfo child) {    this.m_children.add(child);    }
	public void addChildren(List<HAPJSScriptInfo> children) {    this.m_children.addAll(children);    }
	
	public void mergeWith(HAPInfoScriptFunction info) {
		this.addChild(info.getMainScript());
		this.addChildren(info.getChildren());
	}
	
}
