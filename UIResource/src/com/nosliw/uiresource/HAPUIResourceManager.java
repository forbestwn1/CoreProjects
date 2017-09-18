package com.nosliw.uiresource;

import java.util.Map;

import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.runtime.HAPRuntime;

public class HAPUIResourceManager {

	private HAPExpressionManager m_expressionMan; 
	
	private HAPRuntime m_runtime;
	
	public HAPUIResourceManager(HAPExpressionManager expressionMan, HAPRuntime runtime){
		this.m_expressionMan = expressionMan;
		this.m_runtime = runtime;
	}
	
	public void processUIResource(String file){
		HAPUIResourceIdGenerator idGengerator = new HAPUIResourceIdGenerator(1);
		HAPUIResourceParser uiResourceParser = new HAPUIResourceParser(null, this.m_expressionMan, idGengerator);
		HAPUIResource uiResource = uiResourceParser.parseFile(file);
		
		Map<String, HAPConstantDef> constantDefs = uiResource.getConstants();
		for(String name : constantDefs.keySet()){
			HAPConstantDef constantDef = constantDefs.get(name);
			constantDef.process(constantDefs, idGengerator, m_expressionMan, this.m_runtime);
		}
		
	}
	
	
}
