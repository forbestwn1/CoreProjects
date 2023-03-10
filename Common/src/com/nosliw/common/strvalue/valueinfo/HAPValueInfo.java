package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueEntityBasic;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public abstract class HAPValueInfo extends HAPStringableValueEntityBasic{

	public static final String TYPE = "type";

	public static final String DBCOLUMNINFOS = "dbColumnInfos";

	public static final String DBCOLUMNLINFO = "dbColumnInfo";
	
	abstract public HAPValueInfo clone();
	
	abstract public HAPStringableValue newValue();
	
	public HAPValueInfo(){
	}
	
	public HAPValueInfo getChildByPath(String path){
		HAPValueInfo out = this;
		String[] pathSegs = HAPUtilityNamingConversion.parsePaths(path);
		for(String pathSeg: pathSegs){
			out = out.getElement(pathSeg);
		}
		return out;
	}
	
	protected HAPValueInfo getElement(String name){		return this;	}
	
	public HAPDBColumnsInfo getDBColumnsInfo(){		return (HAPDBColumnsInfo)this.getEntityAncestorByPath(DBCOLUMNINFOS);	}
	public void setDBColumnsInfo(HAPDBColumnsInfo columnsInfo){		this.updateChild(DBCOLUMNINFOS, columnsInfo);	}
	
	public String getValueInfoType(){	return this.getAtomicAncestorValueString(HAPValueInfo.TYPE);	}

	public HAPValueInfo getSolidValueInfo(){		return this;	}
	public String getSolidValueInfoType(){		return this.getValueInfoType();	}
	
	@Override
	public void init(){		super.init();	}

	protected HAPValueInfoManager getValueInfoManager(){ return HAPValueInfoManager.getInstance(); }
	
	protected void cloneFrom(HAPValueInfo valueInfo){		super.cloneFrom(valueInfo);	}
	
}
