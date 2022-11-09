package com.nosliw.data.core.domain.testing.testcomplex1;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public class HAPDefinitionEntityTestComplex1 extends HAPDefinitionEntityInDomainComplex{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX1;

	public static final String ATTR_VARIABLE = "variable";
	
	private String m_variable;
	
	public HAPDefinitionEntityTestComplex1() {
		super(ENTITY_TYPE);
	}

	public String getVariable() {   return this.m_variable;    }
	public void setVariable(String var) {    this.m_variable = var;    }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestComplex1 out = new HAPDefinitionEntityTestComplex1();
		this.cloneToDefinitionEntityInDomain(out);
		out.m_variable = this.m_variable;
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTR_VARIABLE, this.m_variable);
	}

	//expanded json. expand all referenced 
	@Override
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntityDefinitionGlobal entityDefDomain){
		super.buildExpandedJsonMap(jsonMap, typeJsonMap, entityDefDomain);
		jsonMap.put(ATTR_VARIABLE, this.m_variable);
	}

}
