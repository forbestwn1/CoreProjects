package com.nosliw.uiresource;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.runtime.HAPRuntime;

@HAPEntityWithAttribute
public class HAPConstantDef extends HAPSerializableImp{

	private String m_literate;
	
	private Object m_value;
	
	private boolean m_isProcessed = false;
	
	public HAPConstantDef(String literate){
		this.m_literate = literate;
	}

	/**
	 * get data after processing the constant
	 * @return
	 */
	public Object getValue(){
		return this.m_value;
	}
	
	public boolean processed(){  return this.m_isProcessed; }
	
	/**
	 * process to get data of constant
	 */
	public void process(
			Map<String, HAPConstantDef> constantDefs,
			HAPUIResourceIdGenerator idGenerator, 
			HAPExpressionManager expressionMan, 
			HAPRuntime runtime){
		if(!this.processed()){
			StringBuffer value = new StringBuffer();
			List<Object> segments = HAPUIResourceParserUtility.parseUIExpression(this.m_literate, idGenerator, expressionMan);
			for(Object segment : segments){
				if(segment instanceof HAPScriptExpression){
					HAPScriptExpression sciptExpression = (HAPScriptExpression)segment;
					//find all required constants names in script expressions
					Set<String> constantNames = new HashSet<String>();
					List<Object> uiExpEles = sciptExpression.getElements();
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
						constantDef.process(constantDefs, idGenerator, expressionMan, runtime);
						HAPData parm = constantDef.getDataValue();
						parms.put(constantName, parm);
					}
					
					//process script scriptExpression
					sciptExpression.process(parms, null);
					
					//execute script expression
					HAPRuntimeTaskExecuteScriptExpression task = new HAPRuntimeTaskExecuteScriptExpression(sciptExpression, null);
					HAPServiceData serviceData = runtime.executeTaskSync(task);
					value.append(serviceData.getData().toString());
				}
				else{
					value.append(segment.toString());
				}
			}
			this.m_value = value.toString();
			this.m_isProcessed = true;
		}
	}
	
	private HAPData getDataValue(){
		return new HAPDataWrapper(this.m_value.toString());
	}
}
