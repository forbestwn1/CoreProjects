package com.nosliw.data.core.imp.runtime.js;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.imp.io.HAPDBSource;
import com.nosliw.data.core.imp.io.HAPDataAccess;
import com.nosliw.data.core.imp.runtime.js.resource.HAPResourceDataHelperImp;
import com.nosliw.data.core.imp.runtime.js.resource.HAPResourceDataJSConverterImp;
import com.nosliw.data.core.imp.runtime.js.resource.HAPResourceDataJSOperationImp;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPDataAccessRuntimeJS extends HAPDataAccess{

	public HAPDataAccessRuntimeJS(HAPValueInfoManager valueInfoMan, HAPDBSource dbSource) {
		super(valueInfoMan, dbSource);
	}

	public HAPResourceDataJSConverterImp getDataTypeConverter(HAPDataTypeConverter converter){
		return (HAPResourceDataJSConverterImp)this.queryEntityFromDB(HAPResourceDataJSConverterImp._VALUEINFO_NAME, "dataTypeName=? AND converterType=?", new Object[]{converter.getFullName(), converter.getOperation()}, this.getConnection());
	}

	public HAPResourceDataHelperImp getResourceHelper(String id){
		return (HAPResourceDataHelperImp)this.queryEntityFromDB(HAPResourceDataHelperImp._VALUEINFO_NAME, "id=?", new Object[]{id}, this.getConnection());
	}
	
	public HAPJSResourceDependency getJSResourceDependency(HAPResourceId resourceId){
		String resourceIdStr = resourceId.toStringValue(HAPSerializationFormat.LITERATE);
		return (HAPJSResourceDependency)this.queryEntityFromDB(
				HAPJSResourceDependency._VALUEINFO_NAME, 
				HAPJSResourceDependency.RESOURCEID+"=?",
				new Object[]{resourceIdStr}, this.getConnection());
	}
	
	public HAPResourceDataJSOperationImp getJSOperation(HAPOperationId operationId){
		return (HAPResourceDataJSOperationImp)this.queryEntityFromDB(
				HAPResourceDataJSOperationImp._VALUEINFO_NAME, 
				HAPResourceDataJSOperationImp.DATATYPENAME+"=? AND "+HAPResourceDataJSOperationImp.OPERATIONNAME+"=?",
				new Object[]{operationId.getFullName(), operationId.getOperation()}, this.getConnection());
		
	}
}
