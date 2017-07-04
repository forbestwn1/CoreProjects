package com.nosliw.data.core.imp;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeOperation;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.HAPOperationParmInfo;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.HAPRelationshipPath;
import com.nosliw.data.core.HAPRelationshipPathSegment;

public class HAPDataTypeOperationImp extends HAPOperationImp implements HAPRelationship, HAPDataTypeOperation{

	public static String _VALUEINFO_NAME;
	
	@HAPAttribute
	public static String OPERATIONID = "operationId";

	public HAPDataTypeOperationImp(){}
	
	public HAPDataTypeOperationImp(HAPOperationImp targetOperation, HAPRelationshipImp relationship){
		this.init(targetOperation, relationship);
	}
	
	public HAPDataTypeOperationImp(HAPOperationImp targetOperation){
		this.init(targetOperation, null);
	}
	
	private void init(HAPOperationImp targetOperation, HAPRelationshipImp relationship){
		this.cloneFrom(targetOperation);
		this.setOperationId(targetOperation.getId());
		this.setTarget(targetOperation.getDataTypeName());
		if(relationship!=null){
			this.setSource(relationship.getSource());
			this.updateAtomicChildObjectValue(PATH, relationship.getPath());
		}
		else{
			this.setSource(targetOperation.getDataTypeName());
			this.updateAtomicChildObjectValue(PATH, new HAPRelationshipPathImp());
		}
	}
	
	//methods from HAPDataTypeOperation
	@Override
	public HAPOperation getOperationInfo() {  return this;  }
	@Override
	public HAPRelationship getTargetDataType() {		return this;	}

	//methods from HAPRelationship
	@Override
	public HAPDataTypeId getTarget() {	return (HAPDataTypeId)this.getAtomicAncestorValueObject(TARGET, HAPDataTypeId.class);	}
	@Override
	public HAPDataTypeId getSource(){   return (HAPDataTypeId)this.getAtomicAncestorValueObject(SOURCE, HAPDataTypeId.class);  }
	@Override
	public HAPRelationshipPath getPath() {		return (HAPRelationshipPathImp)this.getAtomicAncestorValueObject(PATH, HAPRelationshipPathImp.class);	}

	public void setTarget(HAPDataTypeId dataTypeName){ this.updateAtomicChildObjectValue(TARGET, dataTypeName); }
	
	public void setSource(HAPDataTypeId dataTypeName){  this.updateAtomicChildObjectValue(SOURCE, dataTypeName);  }
	
	public String getOperationId(){ return this.getAtomicAncestorValueString(OPERATIONID);  }
	public void setOperationId(String opId){ this.updateAtomicChildStrValue(OPERATIONID, opId);  }
	
	public HAPDataTypeOperationImp extendPathSegment(HAPRelationshipPathSegment segment, HAPDataTypeId sourceId){
		HAPDataTypeOperationImp out = this.clone(HAPDataTypeOperationImp.class);
		out.getPath().insert(segment);
		out.setSource(sourceId);
		return out;
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		
		jsonMap.put(TARGET, HAPSerializeManager.getInstance().toStringValue(this.getTarget(), HAPSerializationFormat.LITERATE));
		jsonMap.put(SOURCE, HAPSerializeManager.getInstance().toStringValue(this.getSource(), HAPSerializationFormat.LITERATE));
		jsonMap.put(PATH, HAPSerializeManager.getInstance().toStringValue(this.getPath(), HAPSerializationFormat.LITERATE));
	}

}
