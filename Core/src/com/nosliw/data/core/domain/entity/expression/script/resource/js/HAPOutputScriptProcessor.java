package com.nosliw.data.core.domain.entity.expression.script.resource.js;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableSegmentExpression;

public class HAPOutputScriptProcessor {

	private String m_functionBody;
	
	private List<HAPExecutableSegmentExpression> m_children;
	
	public HAPOutputScriptProcessor() {
		this.m_children = new ArrayList<HAPExecutableSegmentExpression>();
	}
	
	public String getFunctionBody() {    return this.m_functionBody;    }
	public void setFunctionBody(String body) {    this.m_functionBody = body;     }
	
	public List<HAPExecutableSegmentExpression> getScriptChildren(){  return this.m_children;    }
	public void addChild(HAPExecutableSegmentExpression child) {    this.m_children.add(child);    }
	
}
