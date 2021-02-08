package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPDBColumnsInfo  extends HAPStringableValueEntity{

	//whether the name in column info is relative or not
	public static final String PATHTYPE = "pathType";

	//whether include column defined in reference
	public static final String INCLUDECOLUMN = "includeColumn";
	
	//prefix to add to column name defined in referenced column info
	public static final String PREFIX = "prefix";
	
	public static final String COLUMNS = "columns";
	
	public HAPDBColumnsInfo(){
		this.updateComplexChild(COLUMNS, HAPConstantShared.STRINGALBE_VALUEINFO_LIST);
		this.updateAtomicChildStrValue(PATHTYPE, HAPConstantShared.STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_ABSOLUTE);
		this.updateAtomicChildStrValue(INCLUDECOLUMN, "no", HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_BOOLEAN);
	}
	
	public String getPathType(){  return this.getAtomicAncestorValueString(PATHTYPE);  }
	
	public void setPathType(String pathType){ this.updateAtomicChildStrValue(PATHTYPE, pathType);  }
	
	public HAPStringableValueList<HAPDBColumnInfo> getColumns(){
		return (HAPStringableValueList<HAPDBColumnInfo>)this.getListChild(COLUMNS);
	}
	
	public boolean isIncludeColumn(){  return this.getAtomicAncestorValueBoolean(INCLUDECOLUMN);  }
	
	public String getPrefix(){  return this.getAtomicAncestorValueString(PREFIX);  }
	
	public void addDbColumnInfo(HAPDBColumnInfo columnInfo){		this.getColumns().addChild(columnInfo);	}

}
