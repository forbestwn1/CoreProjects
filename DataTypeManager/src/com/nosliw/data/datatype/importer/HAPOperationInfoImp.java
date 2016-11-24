package com.nosliw.data.datatype.importer;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPOperationInfo;
import com.nosliw.data.HAPOperationOutInfo;
import com.nosliw.data.HAPOperationParmInfo;

public class HAPOperationInfoImp extends HAPStringableValueEntity implements HAPOperationInfo{

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String DATATYPENAME = "dataTypeName";
	
	@HAPAttribute
	public static String DATATYPEVERSION = "dataTypeVersion";
	
	public HAPOperationInfoImp(){}
	
	public HAPOperationInfoImp(String id, String name, String description, String dataTypeName, String dataTypeVersion){
		this.updateAtomicChild(ID, id);
		this.updateAtomicChild(NAME, name);
		this.updateAtomicChild(DESCRIPTION, description);
		this.updateAtomicChild(DATATYPENAME, dataTypeName);
		this.updateAtomicChild(DATATYPEVERSION, dataTypeVersion, HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeVersionImp.class.getName());
	}

	@Override
	public String getName() {		return this.getAtomicAncestorValueString(NAME);	}

	@Override
	public String getDescription() {		return this.getAtomicAncestorValueString(DESCRIPTION);  }

	public String getId(){		return this.getAtomicAncestorValueString(ID);	}

	public String getDataTypeName(){		return this.getAtomicAncestorValueString(DATATYPENAME);	}
	
	public HAPDataTypeVersionImp getDataTypeVersion(){		return (HAPDataTypeVersionImp)this.getAtomicValueAncestorByPath(DATATYPEVERSION);	}
	
	@Override
	public Map<String, HAPOperationParmInfo> getParmsInfo() {
		HAPStringableValueMap map = this.getMapAncestorByPath(PAMRS);
		Map<String, HAPOperationParmInfo> out = (Map<String, HAPOperationParmInfo>)map.getMapValue();
		return out;
	}

	@Override
	public HAPOperationOutInfo getOutputInfo() {		return (HAPOperationOutInfo)this.getEntityAncestorByPath(OUTPUT);	}

}
