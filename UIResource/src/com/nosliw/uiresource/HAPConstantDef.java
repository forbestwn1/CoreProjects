package com.nosliw.uiresource;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.expression.HAPOperand;

@HAPEntityWithAttribute
public class HAPConstantDef extends HAPSerializableImp{

	private String m_literate;
	
	private Object m_value;
	
	private boolean m_isProcessed = false;
	
	public HAPConstantDef(String literate){
		
	}

	/**
	 * get data after processing the constant
	 * @return
	 */
	public Object getValue(){
		
	}
	
	/**
	 * 
	 */
	public void process(
			Map<String, HAPConstantDef> constantDefs,
			HAPUIResourceIdGenerator idGenerator, 
			HAPExpressionManager expressionMan){
		List<Object> segments = HAPUIResourceParserUtility.parseUIExpression(this.m_literate, idGenerator, expressionMan);
		for(Object segment : segments){
			if(segment instanceof HAPScriptExpression){
				HAPScriptExpression uiExpression = (HAPScriptExpression)segment;
				//find all required constants names in ui expressions
				Set<String> constantNames = new HashSet<String>();
				List<Object> uiExpEles = uiExpression.getElements();
				for(Object uiExpEle : uiExpEles){
					if(uiExpEle instanceof HAPExpressionDefinition){
						HAPExpressionDefinition expDef = (HAPExpressionDefinition)uiExpEle;
						HAPOperand expOperand = expDef.getOperand();
						constantNames.addAll(HAPExpressionUtility.discoveryUnsolvedConstants(expOperand));
					}
				}
				
				//build parms
				Map<String, HAPData> parms = new LinkedHashMap<String, HAPData>();
				for(String constantName : constantNames){
					HAPConstantDef constantDef = constantDefs.get(constantName);
					constantDef.process(constantDefs, idGenerator, expressionMan);
					HAPData parm = constantDef.getDataValue();
					parms.put(constantName, parm);
				}
				
				//
			}
		}
		
	}
	
	private HAPData getDataValue(){
		
	}
}
