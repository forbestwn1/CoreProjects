package com.nosliw.data.core.structure.reference;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.HAPReferenceElementInStructure;

@HAPEntityWithAttribute
public class HAPReferenceElementInStructureComplex extends HAPSerializableImp{

	@HAPAttribute
	public static final String REFERENCEPATH = "referencePath";

	@HAPAttribute
	public static final String PARENT = "parent";

	//parent name for referred complex, for instance, self, external context
	private String m_parent;
	
	//criteria for value structure candidate
	private HAPCriteriaValueStructure m_valueStructureCriteria;
	
	//definition of the path (reference + path)
	private String m_referencePath;

	public HAPReferenceElementInStructureComplex() {}
	
	public HAPReferenceElementInStructureComplex(String refPath) {
		this.m_referencePath = refPath;
	}

	public HAPReferenceElementInStructureComplex(String parent, String refPath) {
		this(refPath);
		this.m_parent = parent;
	}
	
	public String getParent() {
		if(HAPBasicUtility.isStringNotEmpty(this.m_parent))   return this.m_parent;
		return HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;  
	}
	
	public void setParent(String parent) {		this.m_parent = parent;	}
	
	public String getReferencePath() {   return this.m_referencePath;    }
	public void setReferencePath(String path) {  this.m_referencePath = path;	}

	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.m_referencePath = (String)value;
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			this.m_parent = (String)jsonValue.opt(PARENT);
			Object referencePathObj = jsonValue.get(REFERENCEPATH);
			
			if(referencePathObj instanceof String)	this.setReferencePath((String)referencePathObj);
			else if(referencePathObj instanceof JSONObject){
				HAPReferenceElementInStructure contextPath = new HAPReferenceElementInStructure();
				contextPath.buildObject(referencePathObj, HAPSerializationFormat.JSON);
				this.setReferencePath(contextPath.toStringValue(HAPSerializationFormat.LITERATE));
			}
		}
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(REFERENCEPATH, this.getReferencePath());
		jsonMap.put(PARENT, this.getParent());
	}
	
	public HAPReferenceElementInStructureComplex cloneReferencePathInfo() {
		HAPReferenceElementInStructureComplex out = new HAPReferenceElementInStructureComplex();
		out.m_parent = this.getParent();
		out.m_referencePath = this.getReferencePath();
		return out;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPReferenceElementInStructureComplex) {
			HAPReferenceElementInStructureComplex ele = (HAPReferenceElementInStructureComplex)obj;
			if(!HAPBasicUtility.isEquals(this.getReferencePath(), ele.getReferencePath()))  return false;
			if(!HAPBasicUtility.isEquals(this.getParent(), ele.getParent()))  return false;
			out = true;
		}
		return out;
	}	
}
