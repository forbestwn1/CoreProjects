package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPRoot extends HAPEntityInfoWritableImp{

	public static final String INHERIT_MODE = "inherit";
	public static final String INHERIT_MODE_FINAL = "final";

	@HAPAttribute
	public static final String LOCALID = "localId";

	@HAPAttribute
	public static final String GLOBALID = "globalId";

	public static final String DEFINITION = "definition";
	
	@HAPAttribute
	public static final String DEFAULT = "defaultValue";

	//unique id within context structure
	private String m_localId;
	
	//unique id within system
	private String m_globalId;
	
	//default value for the element, used in runtime when no value is set
	private Object m_defaultValue;

	//context element definition
	private HAPElement m_definition;
	
	//calculated, discover all the relative element with path to it: path --- element
	private Map<String, HAPElementLeafRelative> m_relativeEleInfo;
	
	public HAPRoot() {}
	
	public HAPRoot(HAPElement definition) {  this.m_definition = definition;  }

	public Object getDefaultValue(){   
		Object value;
		if(this.isConstant()) {
			//for constant, default value is constant value
			HAPElementLeafConstant constantEle = (HAPElementLeafConstant)this.getDefinition();
			value = constantEle.getValue();
		}
		else {
			value = this.m_defaultValue;
		}
		return value;  
	}
	
	public String getLocalId() {  return this.m_localId;    }
	public void setLocalId(String id) {  this.m_localId = id;    }
	
	public String getGlobalId() {   return this.m_globalId;   }
	public void setGlobalId(String globalId) {    this.m_globalId = globalId;     }

	public void setDefaultValue(Object defaultValue){		this.m_defaultValue = defaultValue;	}

	public boolean isConstant() {		return HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(this.m_definition.getType());	}
	
	public Map<String, HAPElementLeafRelative> getRelativeInfo() {
		if(this.isConstant())  return null;
		if(this.m_relativeEleInfo==null) {
			this.m_relativeEleInfo = HAPUtilityStructure.discoverRelativeElement(this);
		}
		if(this.m_relativeEleInfo==null || this.m_relativeEleInfo.isEmpty())  return null;
		return this.m_relativeEleInfo;
	}
	
	public boolean isAbsolute() {  return !(this.isConstant() || this.getRelativeInfo()!=null);      }

	public HAPElement getDefinition() {   return this.m_definition;   }

	public void setDefinition(HAPElement definition) {   this.m_definition = definition;  }
	
	public HAPRoot cloneRootBase() {
		HAPRoot out = new HAPRoot();
		this.cloneToEntityInfo(out);
		out.m_localId = this.m_localId;
		out.m_globalId = this.m_globalId;
		return out;
	}
	
	public HAPRoot cloneExceptElement() {
		HAPRoot out = new HAPRoot();
		this.cloneToEntityInfo(out);
		out.m_localId = this.m_localId;
		out.m_globalId = this.m_globalId;
		out.m_definition = this.m_definition.cloneStructureElement();
		out.m_defaultValue = this.m_defaultValue;
		return out;
	}

	public HAPRoot cloneRoot() {
		HAPRoot out = this.cloneExceptElement();
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DEFINITION, this.m_definition.toStringValue(HAPSerializationFormat.JSON));
		
		if(this.m_defaultValue!=null){
			jsonMap.put(DEFAULT, this.m_defaultValue.toString());
			typeJsonMap.put(DEFAULT, this.m_defaultValue.getClass());
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPRoot) {
			HAPRoot root = (HAPRoot)obj;
			if(!super.equals(obj))  return false;
			if(!HAPBasicUtility.isEquals(this.m_defaultValue, root.m_defaultValue))  return false;
			if(!HAPBasicUtility.isEquals(this.m_definition, root.m_definition)) return false;
			out = true;
		}
		return out;
	}

}
