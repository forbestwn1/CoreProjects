package com.nosliw.data.core.structure.reference;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.domain.valueport.HAPIdValuePort;

public class HAPReferenceRootElement extends HAPSerializableImp{

	@HAPAttribute
	public static final String PARENTVALUECONTEXT = "parentValueContext";

	@HAPAttribute
	public static final String VALUESTRUCTUREREFERENCE = "valueStructureReference";

	@HAPAttribute
	public static final String ROOTNAME = "rootName";

	//parent complex name for referred complex, for instance, self, external context
	private HAPIdValuePort m_valuePortId;
	
	//criteria for value structure candidate
	private HAPReferenceValueStructure m_valueStructureReference;
	
	private String m_rootName;

	public HAPReferenceRootElement() {}
	
	public HAPReferenceRootElement(String rootName) {
		this.m_rootName = rootName;
	}

	public HAPReferenceRootElement(String parent, HAPIdValuePort valuePortInfo) {
		this(rootName);
		this.m_valuePortId = valuePortInfo;
	}
	
	public HAPIdValuePort getValuePortId() {
		if(HAPUtilityBasic.isStringNotEmpty(this.m_parentValueContext))   return this.m_parentValueContext;
		return HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;  
	}
	
	public void setParentValueContextName(String parent) {		this.m_parentValueContext = parent;	}
	
	public String getRootName() {   return this.m_rootName;      }
	public void setRootName(String rootName) {    this.m_rootName = rootName;    }

	public HAPIdValuePort getValuePortId() {    return this.m_valuePortId;     }
	
	public HAPReferenceValueStructure getValueStructureReference() {    return this.m_valueStructureReference;     }
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.m_rootName = (String)value;
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			this.m_parentValueContext = (String)jsonValue.opt(PARENTVALUECONTEXT);
			this.setRootName(jsonValue.getString(ROOTNAME));
		}
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ROOTNAME, this.getRootName());
		jsonMap.put(PARENTVALUECONTEXT, this.getParentValueContextName());
	}
	
	public HAPReferenceRootElement cloneRootElementReference() {
		HAPReferenceRootElement out = new HAPReferenceRootElement();
		this.cloneToRootReference(out);
		return out;
	}
	
	protected void cloneToRootReference(HAPReferenceRootElement rootEleRef) {
		rootEleRef.m_parentValueContext = this.getParentValueContextName();
		rootEleRef.m_rootName = this.getRootName();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPReferenceRootElement) {
			HAPReferenceRootElement ele = (HAPReferenceRootElement)obj;
			if(!HAPUtilityBasic.isEquals(this.getRootName(), ele.getRootName()))  return false;
			if(!HAPUtilityBasic.isEquals(this.getParentValueContextName(), ele.getParentValueContextName()))  return false;
			out = true;
		}
		return out;
	}	
}
