package com.nosliw.data.core.structure.reference;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.structure.HAPReferenceElementInStructure;

//criteria for reference
@HAPEntityWithAttribute
public class HAPReferenceElementInValueContext extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENTPATH = "elementPath";

	@HAPAttribute
	public static final String VALUESTRUCTUREREFERENCE = "valueStructureReference";

	@HAPAttribute
	public static final String PARENTVALUECONTEXT = "parentValueContext";

	//parent complex name for referred complex, for instance, self, external context
	private String m_parentValueContext;
	
	//criteria for value structure candidate
	private HAPReferenceValueStructure m_valueStructureReference;
	
	//path to element (root name + path)
	private String m_elementPath;

	public HAPReferenceElementInValueContext() {}
	
	public HAPReferenceElementInValueContext(String refPath) {
		this.m_elementPath = refPath;
	}

	public HAPReferenceElementInValueContext(String parent, String refPath) {
		this(refPath);
		this.m_parentValueContext = parent;
	}
	
	public String getParentValueContextName() {
		if(HAPUtilityBasic.isStringNotEmpty(this.m_parentValueContext))   return this.m_parentValueContext;
		return HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;  
	}
	
	public void setParentValueContextName(String parent) {		this.m_parentValueContext = parent;	}
	
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
			this.m_parentValueContext = (String)jsonValue.opt(PARENTVALUECONTEXT);
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
		jsonMap.put(PARENTVALUECONTEXT, this.getParentValueContextName());
	}
	
	public HAPReferenceElementInValueContext cloneReferencePathInfo() {
		HAPReferenceElementInValueContext out = new HAPReferenceElementInValueContext();
		out.m_parentValueContext = this.getParentValueContextName();
		out.m_elementPath = this.getElementPath();
		return out;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPReferenceElementInValueContext) {
			HAPReferenceElementInValueContext ele = (HAPReferenceElementInValueContext)obj;
			if(!HAPUtilityBasic.isEquals(this.getElementPath(), ele.getElementPath()))  return false;
			if(!HAPUtilityBasic.isEquals(this.getParentValueContextName(), ele.getParentValueContextName()))  return false;
			out = true;
		}
		return out;
	}	
}
