package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapper;

public class HAPContextNodeRootConstant extends HAPSerializableImp implements HAPContextNodeRoot{

	@HAPAttribute
	public static final String VALUE = "value";

	private Object m_value;
	
	private HAPContextNodeRootInfo m_info;
	
	public HAPContextNodeRootConstant() {
		this.m_info = new HAPContextNodeRootInfo();
	}
	
	@Override
	public String getType() {		return HAPConstant.UIRESOURCE_ROOTTYPE_CONSTANT;	}

	@Override
	public HAPContextNodeRootInfo getInfo() {	return this.m_info; 	}
	
	public void setValue(Object value){		this.m_value = value;	}

	public Object getValue(){   return this.m_value;  }
	
	/**
	 * Get data value of value
	 * if not data, then return null
	 * if is data, then return data object
	 */
	public HAPData getDataValue(){
		HAPDataWrapper out = new HAPDataWrapper();
		boolean isData = out.buildObjectByLiterate(this.m_value.toString());
		if(isData)  return out;
		else return null;
	}
	
	@Override
	public HAPContextNodeRoot toSolidContextNodeRoot(Map<String, Object> constants,
			HAPEnvContextProcessor contextProcessorEnv) {
		HAPContextNodeRootConstant out = new HAPContextNodeRootConstant();
		out.m_info = this.m_info.toSolidContextNode(constants, contextProcessorEnv);
		out.m_value = this.m_value;
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE, this.m_value.toString());
		typeJsonMap.put(VALUE, this.m_value.getClass());
	}

	@Override
	public HAPContextNodeRoot cloneContextNodeRoot() {
		HAPContextNodeRootConstant out = new HAPContextNodeRootConstant();
		out.m_info = this.m_info.clone();
		out.m_value = this.m_value;
		return out;
	}

}
