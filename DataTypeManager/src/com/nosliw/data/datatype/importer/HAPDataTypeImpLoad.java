package com.nosliw.data.datatype.importer;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.data.HAPOperationInfo;

public class HAPDataTypeImpLoad extends HAPDataTypeImp{

	@HAPAttribute
	public static String OPERATIONS = "operations";

	public HAPDataTypeImpLoad(){}
	
	public HAPDataTypeImpLoad(String Id, String name, String version, String description, String parent, String linked) {
		super(Id, name, version, description, parent, linked);
	}

	public List<HAPOperationInfo> getDataOperationInfos(){
		HAPStringableValueList list = (HAPStringableValueList)this.getListAncestorByPath(OPERATIONS);
		return (List<HAPOperationInfo>)list.getListValue();
	}
}
