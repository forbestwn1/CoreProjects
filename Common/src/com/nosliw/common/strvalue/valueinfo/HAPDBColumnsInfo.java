package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.utils.HAPConstant;

public class HAPDBColumnsInfo  extends HAPStringableValueEntity{

	//whether the name in column info is relative or not
	public static final String PATHTYPE = "pathType";
	
	public static final String COLUMNS = "columns";
	
	public HAPDBColumnsInfo(){
		this.updateComplexChild(COLUMNS, HAPConstant.STRINGALBE_VALUEINFO_LIST);
		this.updateAtomicChildStrValue(PATHTYPE, HAPConstant.STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_ABSOLUTE);
	}
	
	public String getPathType(){  return this.getAtomicAncestorValueString(PATHTYPE);  }
	
	public HAPStringableValueList<HAPDBColumnInfo> getColumns(){
		return (HAPStringableValueList<HAPDBColumnInfo>)this.getListChild(COLUMNS);
	}
	
	public void addDbColumnInfo(HAPDBColumnInfo columnInfo){		this.getColumns().addChild(columnInfo);	}

}
