package com.nosliw.data.core.valuestructure;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.HAPReferenceRootInStrucutre;

@HAPEntityWithAttribute
public class HAPReferenceRootInExecutable extends HAPSerializableImp implements HAPReferenceRootInStrucutre{

	@HAPAttribute
	public static final String ID = "id";

	@HAPAttribute
	public static final String NAME = "name";

	private String m_name;
	
	private String m_id;
	
	private final static String IDSYMBOL = "@";
	
	public HAPReferenceRootInExecutable() {}
	
	public HAPReferenceRootInExecutable(String nameOrId, boolean isId) {
		if(isId)   this.m_id = nameOrId;
		else this.m_name = nameOrId;
	}
	
	public HAPReferenceRootInExecutable(String literate) {
		this.buildObjectByLiterate(literate);
	}
	
	public String getName() {   return this.m_name;   }
	
	public String getId() {    return this.m_id;     }
	
	@Override
	public String getStructureType() {   return HAPConstantShared.STRUCTURE_TYPE_VALUEEXECUTABLE;  }

	@Override
	public HAPReferenceRootInStrucutre cloneStructureRootReference() {  
		HAPReferenceRootInExecutable out = new HAPReferenceRootInExecutable();
		out.m_id = this.m_id;
		out.m_name = this.m_name;
		return out;
	}

	@Override
	public String toString() {   return this.buildLiterate();	}

	@Override
	protected String buildLiterate(){  
		if(this.m_id!=null)   return IDSYMBOL+this.m_id;
		if(this.m_name!=null)  return this.m_name;
		return null;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(STRUCTURETYPE, this.getStructureType());
		jsonMap.put(ID, this.m_id);
		jsonMap.put(NAME, this.m_name);
	}

	@Override
	protected boolean buildObjectByJson(Object json){  
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = (String)jsonObj.opt(ID);
		this.m_name = (String)jsonObj.opt(NAME);
		return true;  
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		if(literateValue.startsWith(IDSYMBOL))   this.m_id = literateValue.substring(IDSYMBOL.length());
		else this.m_name = literateValue;
		return true;  
	}

}
