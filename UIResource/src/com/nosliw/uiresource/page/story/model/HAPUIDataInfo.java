package com.nosliw.uiresource.page.story.model;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.structure.HAPReferenceRootInStrucutre;
import com.nosliw.core.application.common.structure.HAPUtilityStructureReference;
import com.nosliw.core.application.common.variable.HAPVariableDataInfo;

@HAPEntityWithAttribute
public class HAPUIDataInfo extends HAPSerializableImp{

	@HAPAttribute
	public static final String DATATYPE = "dataType";

	@HAPAttribute
	public static final String IDPATH = "idPath";

	@HAPAttribute
	public static final String ROOTREFERENCE = "rootReference";

	private HAPVariableDataInfo m_dataType;
	
	private HAPComplexPath m_idPath;
	
	private HAPReferenceRootInStrucutre m_rootReference;
	
	public HAPVariableDataInfo getDataType() {	return this.m_dataType;	}
	public void setDataType(HAPVariableDataInfo dataTypeCriteria) {    this.m_dataType = dataTypeCriteria;      }

	public HAPComplexPath getIdPath() {   return this.m_idPath;   }
	public void setIdPath(HAPComplexPath contextPath) {    this.m_idPath = contextPath;    }
	
	public HAPReferenceRootInStrucutre getRootReference() {  return this.m_rootReference;    }
	public void setRootReference(HAPReferenceRootInStrucutre rootRef) {    this.m_rootReference = rootRef;     }
	
	public HAPUIDataInfo cloneUIDataInfo() {
		HAPUIDataInfo out = new HAPUIDataInfo();
		out.m_dataType = this.m_dataType.cloneVariableDataInfo();
		out.m_idPath = this.m_idPath.cloneComplexPath();
		out.m_rootReference = this.m_rootReference.cloneStructureRootReference();
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		JSONObject dataTypeObj = jsonObj.optJSONObject(DATATYPE);
		if(dataTypeObj!=null) {
			this.m_dataType = new HAPVariableDataInfo();
			this.m_dataType.buildObject(dataTypeObj, HAPSerializationFormat.JSON);
		}
		Object contextPathObj = jsonObj.opt(IDPATH);
		if(contextPathObj!=null) {
			if(contextPathObj instanceof String) {
				this.m_idPath = new HAPComplexPath((String)contextPathObj);
			}
			else {
				this.m_idPath = new HAPComplexPath();
				this.m_idPath.buildObject(contextPathObj, HAPSerializationFormat.JSON);
			}
		}
		
		this.m_rootReference = HAPUtilityStructureReference.parseRootReferenceJson(jsonObj.getJSONObject(ROOTREFERENCE));
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATATYPE, this.m_dataType.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(IDPATH, this.m_idPath.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ROOTREFERENCE, this.m_rootReference.toStringValue(HAPSerializationFormat.JSON));
	}
}
