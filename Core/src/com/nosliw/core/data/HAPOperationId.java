package com.nosliw.core.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPOperationId extends HAPDataTypeId{

	@HAPAttribute
	public static String OPERATION = "operation";
	
	private String m_operation;

	public HAPOperationId(String literate){
		this.buildObjectByLiterate(literate);
	}
	
	public HAPOperationId(String name, String version, String operation){
		super(name, version);
		this.setOperation(operation);
	}
		
	public HAPOperationId(String name, HAPDataTypeVersion version, String operation){
		super(name, version);
		this.setOperation(operation);
	}

	public HAPOperationId(String fullName, String operation){
		this.setFullName(fullName);
		this.setOperation(operation);
	}
	
	public HAPOperationId(HAPDataTypeId dataTypeId, String operation){
		super(dataTypeId.getName(), dataTypeId.getVersion());
		this.setOperation(operation);
	}

	public String getOperation(){		return this.m_operation;	}
	public void setOperation(String operation){ this.m_operation = operation;  }
	
	@Override
	protected String buildLiterate(){
		return HAPUtilityNamingConversion.cascadeLevel1(super.buildLiterate(), this.getOperation());
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		int index = literateValue.lastIndexOf(HAPConstantShared.SEPERATOR_LEVEL1);
		this.setOperation(literateValue.substring(index+1));
		super.buildObjectByLiterate(literateValue.substring(0, index));
		
//		this.setName(segs[0]);
//		if(segs.length>=2){
//			this.setVersion(segs[1]);
//		}
//		if(segs.length>=3){
//			this.setOperation(segs[2]);
//		}
		return true;
	}
}
