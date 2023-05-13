package com.nosliw.data.core.domain.entity.adapter.dataassociation.mapping;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.HAPReferenceRootInStrucutre;

@HAPEntityWithAttribute
public class HAPReferenceRootInMapping extends HAPSerializableImp implements HAPReferenceRootInStrucutre{

	@HAPAttribute
	public static final String NAME = "name";

	private String m_name;
	
	public HAPReferenceRootInMapping() {}
	
	public HAPReferenceRootInMapping(String name) {
		this.m_name = name;
	}
	
	@Override
	public String getStructureType() {   return HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT;  }

	public String getName() {   return this.m_name;   }

	@Override
	public String toString() {   return this.buildLiterate();	}

	@Override
	protected String buildLiterate(){  return this.m_name; }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(STRUCTURETYPE, this.getStructureType());
		jsonMap.put(NAME, this.m_name);
	}

	@Override
	protected boolean buildObjectByJson(Object json){  
		JSONObject jsonObj = (JSONObject)json;
		this.m_name = (String)jsonObj.opt(NAME);
		return true;  
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		this.m_name = literateValue;
		return true;  
	}

	@Override
	public HAPReferenceRootInStrucutre cloneStructureRootReference() {   return new HAPReferenceRootInMapping(this.m_name);  }
}
