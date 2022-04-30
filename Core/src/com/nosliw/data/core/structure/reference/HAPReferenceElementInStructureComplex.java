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
	public static final String ELEMENTPATH = "elementPath";

	@HAPAttribute
	public static final String VALUESTRUCTUREREFERENCE = "valueStructureReference";

	@HAPAttribute
	public static final String PARENTCOMPLEX = "parentComplex";

	//parent complex name for referred complex, for instance, self, external context
	private String m_parentComplex;
	
	//criteria for value structure candidate
	private HAPReferenceValueStructure m_valueStructureReference;
	
	//path to element (root name + path)
	private String m_elementPath;

	public HAPReferenceElementInStructureComplex() {}
	
	public HAPReferenceElementInStructureComplex(String refPath) {
		this.m_elementPath = refPath;
	}

	public HAPReferenceElementInStructureComplex(String parent, String refPath) {
		this(refPath);
		this.m_parentComplex = parent;
	}
	
	public String getParentComplexName() {
		if(HAPBasicUtility.isStringNotEmpty(this.m_parentComplex))   return this.m_parentComplex;
		return HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;  
	}
	
	public void setParentComplexName(String parent) {		this.m_parentComplex = parent;	}
	
	public String getElementPath() {   return this.m_elementPath;    }
	public void setElementPath(String path) {  this.m_elementPath = path;	}

	public HAPReferenceValueStructure getValueStructureReference() {    return this.m_valueStructureReference;     }
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.m_elementPath = (String)value;
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			this.m_parentComplex = (String)jsonValue.opt(PARENTCOMPLEX);
			Object referencePathObj = jsonValue.get(ELEMENTPATH);
			
			if(referencePathObj instanceof String)	this.setElementPath((String)referencePathObj);
			else if(referencePathObj instanceof JSONObject){
				HAPReferenceElementInStructure contextPath = new HAPReferenceElementInStructure();
				contextPath.buildObject(referencePathObj, HAPSerializationFormat.JSON);
				this.setElementPath(contextPath.toStringValue(HAPSerializationFormat.LITERATE));
			}
		}
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENTPATH, this.getElementPath());
		jsonMap.put(PARENTCOMPLEX, this.getParentComplexName());
	}
	
	public HAPReferenceElementInStructureComplex cloneReferencePathInfo() {
		HAPReferenceElementInStructureComplex out = new HAPReferenceElementInStructureComplex();
		out.m_parentComplex = this.getParentComplexName();
		out.m_elementPath = this.getElementPath();
		return out;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPReferenceElementInStructureComplex) {
			HAPReferenceElementInStructureComplex ele = (HAPReferenceElementInStructureComplex)obj;
			if(!HAPBasicUtility.isEquals(this.getElementPath(), ele.getElementPath()))  return false;
			if(!HAPBasicUtility.isEquals(this.getParentComplexName(), ele.getParentComplexName()))  return false;
			out = true;
		}
		return out;
	}	
}
