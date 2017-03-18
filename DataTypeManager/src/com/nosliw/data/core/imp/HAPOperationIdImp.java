package com.nosliw.data.core.imp;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.HAPStringableValueAtomic;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeVersion;

public class HAPOperationIdImp extends HAPDataTypeIdImp{

	@HAPAttribute
	public static String OPERATION = "operation";
	
	public HAPOperationIdImp(String name, String version, String operation){
		super(name, version);
		this.setOperation(operation);
	}
		
	public HAPOperationIdImp(String name, HAPDataTypeVersion version, String operation){
		super(name, version);
		this.setOperation(operation);
	}

	public HAPOperationIdImp(String fullName, String operation){
		this.setFullName(fullName);
		this.setOperation(operation);
	}
	
	public HAPOperationIdImp(HAPDataTypeId dataTypeId, String operation){
		super(dataTypeId.getName(), dataTypeId.getVersion());
		this.setOperation(operation);
	}
	
	public String getOperation(){		return this.getAtomicAncestorValueString(OPERATION);	}
	public void setOperation(String operation){ this.updateAtomicChildStrValue(OPERATION, operation);  }
	
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
		if(segs.length>=3){
			this.setOperation(segs[2]);
		}
		return true;
	}

}
