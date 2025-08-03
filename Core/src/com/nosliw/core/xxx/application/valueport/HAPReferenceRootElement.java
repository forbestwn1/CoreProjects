package com.nosliw.core.xxx.application.valueport;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
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

	@HAPAttribute
	public static final String IODIRECTION = "ioDirection";


	//value port Id
	private HAPIdValuePortInBundle m_valuePortId;
	
	//sometimes use value port name, need to translate name to value port id
	private String m_valuePortName;
	
	//criteria for value structure candidate
	private HAPReferenceValueStructure m_valueStructureReference;
	
	private String m_rootName;

	private String m_ioDirection;
	
	public HAPReferenceRootElement() {}
	
	public HAPReferenceRootElement(String rootName) {
		this.m_rootName = rootName;
	}

	public HAPReferenceRootElement(String rootName, HAPIdValuePortInBundle valuePortId) {
		this(rootName);
		this.m_valuePortId = valuePortId;
	}
	
	public HAPIdValuePortInBundle getValuePortId() {    return this.m_valuePortId;     }
	public void setValuePortId(HAPIdValuePortInBundle valuePortRef) {    this.m_valuePortId = valuePortRef;     }
	
	public String getValuePortName() {    return this.m_valuePortName;    }
	
	public HAPReferenceValueStructure getValueStructureReference() {    return this.m_valueStructureReference;     }
	
	public String getRootName() {   return this.m_rootName;      }
	public void setRootName(String rootName) {    this.m_rootName = rootName;    }

	public String getIODirection() {    return this.m_ioDirection;      }
	public void setIODirection(String ioDirection) {    this.m_ioDirection = ioDirection;      }
	
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.m_rootName = (String)value;
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			JSONObject valuePortIdJson = jsonValue.optJSONObject(VALUEPORTID);
			if(valuePortIdJson!=null) {
				this.m_valuePortId = new HAPIdValuePortInBundle();
				this.m_valuePortId.buildObject(valuePortIdJson, HAPSerializationFormat.JSON);
			}
			JSONObject valueStructureRefJson = jsonValue.optJSONObject(VALUESTRUCTUREREFERENCE);
			if(valueStructureRefJson!=null) {
				this.m_valueStructureReference = new HAPReferenceValueStructure();
				this.m_valueStructureReference.buildObject(valueStructureRefJson, HAPSerializationFormat.JSON);
			}
			this.m_valuePortName = (String)jsonValue.opt(VALUEPORTNAME);
			this.m_rootName = (String)jsonValue.opt(ROOTNAME); 
			this.m_ioDirection = (String)jsonValue.opt(IODIRECTION);
		}
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEPORTID, HAPManagerSerialize.getInstance().toStringValue(this.m_valuePortId, HAPSerializationFormat.JSON));
		jsonMap.put(VALUEPORTNAME, this.m_valuePortName);
		jsonMap.put(ROOTNAME, this.getRootName());
		jsonMap.put(VALUESTRUCTUREREFERENCE, HAPManagerSerialize.getInstance().toStringValue(this.m_valueStructureReference, HAPSerializationFormat.JSON));
		jsonMap.put(IODIRECTION, this.getIODirection());
	}
	
	public HAPReferenceRootElement cloneRootElementReference() {
		HAPReferenceRootElement out = new HAPReferenceRootElement();
		this.cloneToRootReference(out);
		return out;
	}
	
	protected void cloneToRootReference(HAPReferenceRootElement rootEleRef) {
		rootEleRef.m_valuePortName = this.m_valuePortName;
		if(this.m_valuePortId!=null) {
			rootEleRef.m_valuePortId = this.m_valuePortId.cloneValue();
		}
		rootEleRef.m_rootName = this.getRootName();
		rootEleRef.m_ioDirection = this.m_ioDirection;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPReferenceRootElement) {
			HAPReferenceRootElement ele = (HAPReferenceRootElement)obj;
			if(!HAPUtilityBasic.isEquals(this.m_valuePortId, ele.m_valuePortId)) {
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
			if(!HAPUtilityBasic.isEquals(this.m_ioDirection, ele.m_ioDirection)) {
				return false;
			}
			out = true;
		}
		return out;
	}
}
