package com.nosliw.entity.data;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPWraper;
import com.nosliw.data.datatype.HAPDataTypeDefInfo;
import com.nosliw.data.datatype.HAPDataTypeInfoWithVersion;
import com.nosliw.data.imp.HAPDataTypeImp;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;

public class HAPReference  extends HAPBaseDataType{

	final public static int REFERENCE_ID = 1;
	final public static int REFERENCE_PATH = 2;
	
	public static HAPDataType dataType;
	public HAPReference(HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan) {
		super(new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_REFERENCE, HAPConstant.DATATYPE_TYPE_REFERENCE_NORMAL), null, null, null, "", dataTypeMan, entityDefMan);
	}

	@Override
	public HAPData toData(Object value, String format) {
		return null;
	}

	@Override
	public HAPServiceData validate(HAPData data) {
		return HAPServiceData.createSuccessData();
	}

	public HAPWraper createDataWraper(HAPEntityID ID, HAPDataTypeDefInfo dataTypeDefInfo){
		return new HAPReferenceWraper(ID, dataTypeDefInfo, this.getDataTypeManager(), this.getEntityDefinitionManager());
	}

	public HAPWraper createDataWraper(String path, HAPDataTypeDefInfo dataTypeDefInfo){
		return new HAPReferenceWraper(path, dataTypeDefInfo, this.getDataTypeManager(), this.getEntityDefinitionManager());
	}
	
	public HAPReferenceWraperData createReferenceData(HAPEntityID ID){
		return  new HAPReferenceWraperData(ID, this);
	}

	public HAPReferenceWraperData createReferenceData(String path){
		return  new HAPReferenceWraperData(path, this);
	}
	
	@Override
	public HAPData getDefaultData() {
		return null;
	}
}
