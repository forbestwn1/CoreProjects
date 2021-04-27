package com.nosliw.data.core.structure.value;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.structure.HAPReferenceRoot;

@HAPEntityWithAttribute
public class HAPReferenceRootInGroup extends HAPSerializableImp implements HAPReferenceRoot{

	@HAPAttribute
	public static final String CATEGARY = "categary";

	@HAPAttribute
	public static final String NAME = "name";

	@HAPAttribute
	public static final String PATHFORMAT = "pathFormat";


	private String m_categary;
	
	private String m_name;
	
	public HAPReferenceRootInGroup(String categary, String name) {
		this.m_categary = categary;
		this.m_name = name;
	}
	
	public HAPReferenceRootInGroup(String literateValue) {
		this.buildObjectByLiterate(literateValue);
	}

	@Override
	public String getStructureType() {   return HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP;  }

	public String getCategary() {		return this.m_categary;	}
	public void setCategary(String categary) {  this.m_categary = categary;    }
	
	public String getName() {   return this.m_name;   }

	public String getFullName() {  return HAPNamingConversionUtility.cascadeElements(new String[] {this.m_name, this.m_categary}, HAPConstantShared.SEPERATOR_CONTEXT_CATEGARY_NAME);   }

	public String getPathFormat() {  return HAPNamingConversionUtility.buildPath(m_categary, m_name);   }
	
	@Override
	public HAPReferenceRoot cloneStructureRootReference() {  return new HAPReferenceRootInGroup(this.m_categary, this.m_name);  }

	@Override
	public String toString() {   return this.buildLiterate();	}

	@Override
	protected String buildLiterate(){  return this.getFullName(); }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(STRUCTURETYPE, this.getStructureType());
		jsonMap.put(CATEGARY, this.m_categary);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(PATHFORMAT, this.getPathFormat());
	}

	@Override
	protected boolean buildObjectByJson(Object json){  
		JSONObject jsonObj = (JSONObject)json;
		this.m_categary = (String)jsonObj.opt(CATEGARY);
		this.m_name = (String)jsonObj.opt(NAME);
		return true;  
	}
	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		String[] segs = HAPNamingConversionUtility.splitTextByElements(literateValue, HAPConstantShared.SEPERATOR_CONTEXT_CATEGARY_NAME);
		if(segs.length>=1)   this.m_name = segs[0];
		if(segs.length>=2)   this.m_categary = segs[1];
		return true;  
	}

}
