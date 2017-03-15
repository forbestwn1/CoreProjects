package com.nosliw.data.datatype.importer;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.HAPStringableValueAtomic;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeVersion;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResourceIdOperationImp extends HAPDataTypeIdImp implements HAPResourceId{

	@HAPAttribute
	public static String OPERATION = "operation";
	
	public HAPResourceIdOperationImp(String name, HAPDataTypeVersion version, String operation){
		super(name, version);
		this.setOperation(operation);
	}

	public HAPResourceIdOperationImp(String fullName, String operation){
		super(fullName);
		this.setOperation(operation);
	}

	@Override
	public String getId() {		return this.buildLiterate();	}

	public HAPDataTypeIdImp getDataTypeId(){ return this;  } 
	
	public String getOperation(){  return this.getAtomicAncestorValueString(OPERATION); }
	public void setOperation(String operation){  this.updateAtomicChildStrValue(OPERATION, operation);  }

	@Override
	public String getType() {		return HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPEOPERATION;	}

	@Override
	protected String buildLiterate(){
		return HAPNamingConversionUtility.cascadeSegments(super.buildLiterate(), this.getOperation());
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		String[] segs = HAPNamingConversionUtility.parseSegments(literateValue);
		this.updateAtomicChildStrValue(NAME, segs[0], HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);
		if(segs.length>=2){
			HAPStringableValueAtomic versionValue = new HAPStringableValueAtomic(segs[1], HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeVersionImp.class.getName());
			this.updateChild(VERSION, versionValue);
		}
		if(segs.length>=3)   this.setOperation(segs[2]);
		return true;
	}
}
