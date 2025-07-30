package com.nosliw.data.core.runtime;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.container.HAPContainer;
import com.nosliw.common.container.HAPItemWrapper;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.scriptexpression.HAPExpressionScript;

@HAPEntityWithAttribute
public class HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup extends HAPSerializableImp{

	@HAPAttribute
	public static String CONTAINER = "container";
	
	@HAPAttribute
	public static String SCRIPTEXPRESSION = "scriptExpression";
	
	@HAPAttribute
	public static String CONSTANT = "constant";
	
	private HAPContainer<HAPItemWrapper> m_scriptExpressionInfoContainer;
	
	public HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup() {
		this.m_scriptExpressionInfoContainer = new HAPContainer<HAPItemWrapper>(); 
	}
	
	public String addScriptExpressionInfo(String name, HAPExpressionScript exprssionScript, Map<String, Object> constants) {
		HAPItemWrapper item = new HAPItemWrapper();
		item.setValue(new HAPItem(exprssionScript, constants));
		item.setName(name);
		return this.m_scriptExpressionInfoContainer.addItem(item);
	}
	
	public List<HAPItemWrapper> getItems(){
		return this.m_scriptExpressionInfoContainer.getItems();
	}
	
	public boolean isEmpty() {   return this.m_scriptExpressionInfoContainer.isEmpty();    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTAINER, this.m_scriptExpressionInfoContainer.toStringValue(HAPSerializationFormat.JAVASCRIPT));
	}
	
	class HAPItem extends HAPSerializableImp{
		
		private HAPExpressionScript m_exprssionScript;
		
		private Map<String, Object> m_constants;
		
		public HAPItem(HAPExpressionScript exprssionScript, Map<String, Object> constants) {
			this.m_exprssionScript = exprssionScript;
			this.m_constants = new LinkedHashMap<String, Object>();
			if(constants!=null) {
				this.m_constants.putAll(constants);
			}
		}

		@Override
		protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
			super.buildJSJsonMap(jsonMap, typeJsonMap);
			jsonMap.put(HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup.SCRIPTEXPRESSION, this.m_exprssionScript.toStringValue(HAPSerializationFormat.JAVASCRIPT));
			jsonMap.put(HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup.CONSTANT, HAPManagerSerialize.getInstance().toStringValue(m_constants, HAPSerializationFormat.JAVASCRIPT));
		}
	}
}
