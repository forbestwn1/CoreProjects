package com.nosliw.core.application.common.scriptexpression.serialize;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.application.common.scriptexpressio.HAPManualSegmentScriptExpression;

public class HAPOutputScriptProcessor {

	private String m_functionBody;
	
	private List<HAPManualSegmentScriptExpression> m_children;
	
	public HAPOutputScriptProcessor() {
		this.m_children = new ArrayList<HAPManualSegmentScriptExpression>();
	}
	
	public String getFunctionBody() {    return this.m_functionBody;    }
	public void setFunctionBody(String body) {    this.m_functionBody = body;     }
	
	public List<HAPManualSegmentScriptExpression> getScriptChildren(){  return this.m_children;    }
	public void addChild(HAPManualSegmentScriptExpression child) {    this.m_children.add(child);    }
	
}
