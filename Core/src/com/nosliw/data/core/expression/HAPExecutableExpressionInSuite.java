package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.script.context.HAPContext;

@HAPEntityWithAttribute(baseName="EXPRESSION")
public class HAPExecutableExpressionInSuite extends HAPExecutableExpressionImp{

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	@HAPAttribute
	public static String EXPRESSIONS = "expressions";

	private String m_id;
	
	private HAPContext m_context;
	
	private Map<String, HAPVariableInfo> m_localVarsInfo;

	private Map<String, HAPExecutableExpressionItem> m_expressionItem;
	
	public HAPExecutableExpressionInSuite(String id) {
		this.m_expressionItem = new LinkedHashMap<String, HAPExecutableExpressionItem>();
		this.m_id = id;
		this.m_localVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
	}
	
	@Override
	public void addExpression(String name, HAPOperandWrapper operand) {
		this.m_expressionItem.put(name, new HAPExecutableExpressionItem(operand.cloneWrapper()));
	}

	@Override
	public Map<String, HAPExecutableExpressionItem> getExpressions(){
		return this.m_expressionItem;
	}

	@Override
	public String getId() {  return this.m_id;  }
	public void setId(String id) {   this.m_id = id;    }
	
	@Override
	public HAPContext getContext() {   return this.m_context;  }

	@Override
	public void setContext(HAPContext context) {   this.m_context = context;  }
	
	@Override
	public Map<String, HAPVariableInfo> getVarsInfo() {  return this.m_localVarsInfo;  }
	public void setVarsInfo(Map<String, HAPVariableInfo> varsInfo) {   this.m_localVarsInfo = varsInfo;  }

	@Override
	public void updateVariableName(HAPUpdateName nameUpdate) {
		Map<String, HAPVariableInfo> localVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		for(String name : this.m_localVarsInfo.keySet()) {
			localVarsInfo.put(nameUpdate.getUpdatedName(name), this.m_localVarsInfo.get(name));
		}
		this.m_localVarsInfo = localVarsInfo;
		
		HAPContext updatedContext = new HAPContext();
		for(String name : this.m_context.getElementNames()) {
			updatedContext.addElement(nameUpdate.getUpdatedName(name), this.m_context.getElement(name));
		}
		this.m_context = updatedContext;
		
		for(String name : this.m_expressionItem.keySet()) {
			this.m_expressionItem.get(name).updateVariableName(nameUpdate);
		}
	}
	
	@Override
	public void discover(Map<String, HAPDataTypeCriteria> expectOutput, HAPProcessTracker processTracker) {
		Map<String, HAPVariableInfo> discoveredVarsInf = new LinkedHashMap<String, HAPVariableInfo>();
		List<String> names = new ArrayList<String>();
		List<HAPOperand> operands = new ArrayList<HAPOperand>();
		List<HAPDataTypeCriteria> outPutCriteria = new ArrayList<HAPDataTypeCriteria>();
		List<HAPMatchers> matchers = new ArrayList<HAPMatchers>();
		
		for(String name : this.m_expressionItem.keySet()) {
			names.add(name);
			if(expectOutput==null)  outPutCriteria.add(null);
			else outPutCriteria.add(expectOutput.get(name));
			operands.add(this.m_expressionItem.get(name).getOperand().getOperand());
		}
		
		HAPOperandUtility.discover(
				operands,
				outPutCriteria,
				this.m_localVarsInfo,
				discoveredVarsInf,
				matchers,
				processTracker);
		
		for(int i=0; i<names.size(); i++) {
			this.m_expressionItem.get(names.get(i)).setOutputMatchers(matchers.get(i));
		}
		this.m_localVarsInfo.clear();
		this.m_localVarsInfo.putAll(discoveredVarsInf);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSIONS, HAPJsonUtility.buildJson(this.getExpressions(), HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLEINFOS, HAPJsonUtility.buildJson(this.m_localVarsInfo, HAPSerializationFormat.JSON));
	}
}
