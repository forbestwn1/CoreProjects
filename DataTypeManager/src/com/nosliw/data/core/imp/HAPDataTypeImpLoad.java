package com.nosliw.data.core.imp;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.core.data.HAPOperation;

public class HAPDataTypeImpLoad extends HAPDataTypeImp{

	public static String _VALUEINFO_NAME;
	
	@HAPAttribute
	public static String OPERATIONS = "operations";

	public HAPDataTypeImpLoad(){	}
	
	public HAPDataTypeImpLoad(String Id, String name, String version, String description, String parent, String linked) {
		super(Id, name, version, description, parent, linked);
	}

	public List<HAPOperation> getDataOperationInfos(){
		HAPStringableValueList list = (HAPStringableValueList)this.getListAncestorByPath(OPERATIONS);
		return (List<HAPOperation>)list.getListValue();
	}
}
