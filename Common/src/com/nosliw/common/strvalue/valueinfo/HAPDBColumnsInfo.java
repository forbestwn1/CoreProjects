package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.utils.HAPConstant;

public class HAPDBColumnsInfo  extends HAPStringableValueEntity{

	//whether the name in column info is relative or not
	public static final String RELATIVE = "relative";
	
	public static final String COLUMNS = "columns";
	
	public HAPDBColumnsInfo(){
		this.updateComplexChild(COLUMNS, HAPConstant.STRINGALBE_VALUEINFO_LIST);
	}
	
	
	public boolean getRelative(){  return this.getAtomicAncestorValueBoolean(RELATIVE);  }
	
	public HAPStringableValueList<HAPDBColumnInfo> getColumns(){
		return (HAPStringableValueList<HAPDBColumnInfo>)this.getListChild(COLUMNS);
	}
	
	public void addDbColumnInfo(HAPDBColumnInfo columnInfo){		this.getColumns().addChild(columnInfo);	}

}
