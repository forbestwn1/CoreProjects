package com.nosliw.data.basic.list;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataOperation;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperationContext;
import com.nosliw.data.HAPOperationInfoAnnotation;
import com.nosliw.data.basic.string.HAPStringData;
import com.nosliw.data.info.HAPDataTypeInfo;

public class HAPListOperation extends HAPDataOperation{

	public HAPListOperation(HAPDataTypeManager man, HAPDataType dataType) {
		super(man, dataType);
	}

	@HAPOperationInfoAnnotation(in = { "list:simple", "expression:simple", "map:simple", "string:simple" }, out = "list:simple", description = "")
	public HAPData literate(HAPData[] parms, HAPOperationContext opContext){
		HAPList listDataType = (HAPList)this.getDataType();
		HAPData out = listDataType.newList();
		
		HAPListData listData = (HAPListData)parms[0];
		for(int i=0; i<listData.getSize(); i++){
			HAPData eleData = listData.getData(i);
			
			HAPData expressionParmsData = parms[2];
			HAPStringData thisNameData = (HAPStringData)parms[3];
			HAPData[] parms2 = {expressionParmsData, HAPDataTypeManager.STRING.createDataByValue(thisNameData.getValue()), eleData};
			expressionParmsData = this.getDataTypeManager().dataOperate(new HAPDataTypeInfo("simple", "map"), "put", parms2, opContext);
			
			HAPData[] parms1 = {parms[1], expressionParmsData};
			HAPData expressionOutData = this.getDataTypeManager().dataOperate(new HAPDataTypeInfo("simple", "expression"), "execute", parms1, opContext);
			
			HAPData[] parms3 = {out, expressionOutData};
			out = this.getDataTypeManager().dataOperate(new HAPDataTypeInfo("list", "simple"), "add", parms3, opContext);
		}
		return out;
	}

	@HAPOperationInfoAnnotation(in = { "list:simple", "expression:simple", "map:simple" }, out = "list:simple", description = "")
	public HAPData each(HAPData[] parms, HAPOperationContext opContext){
		HAPList listDataType = (HAPList)this.getDataType();
		HAPData out = listDataType.newList();
		
		HAPListData listData = (HAPListData)parms[0];
		for(int i=0; i<listData.getSize(); i++){
			HAPData eleData = listData.getData(i);
			
			HAPData expressionParmsData = parms[2];
			HAPData[] parms2 = {expressionParmsData, HAPDataTypeManager.STRING.createDataByValue("this"), eleData};
			expressionParmsData = this.getDataTypeManager().dataOperate(new HAPDataTypeInfo("simple", "map"), "put", parms2, opContext);
			
			HAPData[] parms1 = {parms[1], expressionParmsData};
			HAPData expressionOutData = this.getDataTypeManager().dataOperate(new HAPDataTypeInfo("simple", "expression"), "execute", parms1, opContext);
			
			HAPData[] parms3 = {out, expressionOutData};
			out = this.getDataTypeManager().dataOperate(new HAPDataTypeInfo("list", "simple"), "add", parms3, opContext);
		}
		return out;
	}
	
	@HAPOperationInfoAnnotation(in = {  }, out = "list:simple", description = "")
	public HAPData newList(HAPData[] parms, HAPOperationContext opContext){
		HAPList listDataType = (HAPList)this.getDataType();
		HAPData out = listDataType.newList();
		return out;
	}

}
