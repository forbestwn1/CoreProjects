package com.nosliw.core.application.common.scriptexpression.serialize;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.application.common.scriptexpressio.HAPSegmentScriptExpression;

public class HAPOutputScriptProcessor {

	private String m_functionBody;
	
	private List<HAPSegmentScriptExpression> m_children;
	
	public HAPOutputScriptProcessor() {
		this.m_children = new ArrayList<HAPSegmentScriptExpression>();
	}
	
	public String getFunctionBody() {    return this.m_functionBody;    }
	public void setFunctionBody(String body) {    this.m_functionBody = body;     }
	
	public List<HAPSegmentScriptExpression> getScriptChildren(){  return this.m_children;    }
	public void addChild(HAPSegmentScriptExpression child) {    this.m_children.add(child);    }
	
}
