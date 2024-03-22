package com.nosliw.core.application.common.valueport;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPUtilityBasic;

@HAPEntityWithAttribute
public class HAPReferenceRootElement extends HAPSerializableImp{

	@HAPAttribute
	public static final String VALUEPORTID = "valuePortId";

	@HAPAttribute
	public static final String VALUEPORTNAME = "valuePortName";

	@HAPAttribute
	public static final String VALUESTRUCTUREREFERENCE = "valueStructureReference";

	@HAPAttribute
	public static final String ROOTNAME = "rootName";

	//value port Id
	private HAPRefValuePort m_valuePortRef;
	
	//sometimes use value port name, need to translate name to value port id
	private String m_valuePortName;
	
	//criteria for value structure candidate
	private HAPReferenceValueStructure m_valueStructureReference;
	
	private String m_rootName;

	public HAPReferenceRootElement() {}
	
	public HAPReferenceRootElement(String rootName) {
		this.m_rootName = rootName;
	}

	public HAPReferenceRootElement(String rootName, HAPRefValuePort valuePortRef) {
		this(rootName);
		this.m_valuePortRef = valuePortRef;
	}
	
	public HAPRefValuePort getValuePortRef() {    return this.m_valuePortRef;     }
	public void setValuePortRef(HAPRefValuePort valuePortRef) {    this.m_valuePortRef = valuePortRef;     }
	
	public String getValuePortName() {    return this.m_valuePortName;    }
	
	public HAPReferenceValueStructure getValueStructureReference() {    return this.m_valueStructureReference;     }
	
	public String getRootName() {   return this.m_rootName;      }
	public void setRootName(String rootName) {    this.m_rootName = rootName;    }

	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.m_rootName = (String)value;
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			JSONObject valuePortIdJson = jsonValue.optJSONObject(VALUEPORTID);
			if(valuePortIdJson!=null) {
				this.m_valuePortRef = new HAPRefValuePort();
				this.m_valuePortRef.buildObject(valuePortIdJson, HAPSerializationFormat.JSON);
			}
			JSONObject valueStructureRefJson = jsonValue.optJSONObject(VALUESTRUCTUREREFERENCE);
			if(valueStructureRefJson!=null) {
				this.m_valueStructureReference = new HAPReferenceValueStructure();
				this.m_valueStructureReference.buildObject(valueStructureRefJson, HAPSerializationFormat.JSON);
			}
			this.m_valuePortName = (String)jsonValue.opt(VALUEPORTNAME);
			this.m_rootName = (String)jsonValue.opt(ROOTNAME); 
		}
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEPORTID, HAPSerializeManager.getInstance().toStringValue(this.m_valuePortRef, HAPSerializationFormat.JSON));
		jsonMap.put(VALUEPORTNAME, this.m_valuePortName);
		jsonMap.put(ROOTNAME, this.getRootName());
		jsonMap.put(VALUESTRUCTUREREFERENCE, HAPSerializeManager.getInstance().toStringValue(this.m_valueStructureReference, HAPSerializationFormat.JSON));
	}
	
	public HAPReferenceRootElement cloneRootElementReference() {
		HAPReferenceRootElement out = new HAPReferenceRootElement();
		this.cloneToRootReference(out);
		return out;
	}
	
	protected void cloneToRootReference(HAPReferenceRootElement rootEleRef) {
		rootEleRef.m_valuePortName = this.m_valuePortName;
		if(this.m_valuePortRef!=null) {
			rootEleRef.m_valuePortRef = (HAPRefValuePort)this.m_valuePortRef.cloneValue();
		}
		rootEleRef.m_rootName = this.getRootName();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPReferenceRootElement) {
			HAPReferenceRootElement ele = (HAPReferenceRootElement)obj;
			if(!HAPUtilityBasic.isEquals(this.m_valuePortRef, ele.m_valuePortRef)) {
				return false;
			}
			if(!HAPUtilityBasic.isEquals(this.m_valuePortName, ele.m_valuePortName)) {
				return false;
			}
			if(!HAPUtilityBasic.isEquals(this.m_valueStructureReference, ele.m_valueStructureReference)) {
				return false;
			}
			if(!HAPUtilityBasic.isEquals(this.getRootName(), ele.getRootName())) {
				return false;
			}
			out = true;
		}
		return out;
	}
}
