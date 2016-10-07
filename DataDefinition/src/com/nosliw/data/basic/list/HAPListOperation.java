package com.nosliw.data.basic.list;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataOperation;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperationContext;
import com.nosliw.data.HAPOperationInfoAnnotation;
import com.nosliw.data.basic.bool.HAPBooleanData;
import com.nosliw.data.basic.string.HAPStringData;
import com.nosliw.data.info.HAPDataTypeInfo;

public class HAPListOperation extends HAPDataOperation{

	public HAPListOperation(HAPDataTypeManager man, HAPDataType dataType) {
		super(man, dataType);
	}

	@HAPOperationInfoAnnotation(in = { "list:simple", "any" }, out = "list:simple", description = "")
	public HAPData add(HAPData[] parms, HAPOperationContext opContext){
		HAPListData out = (HAPListData)parms[0];
		out.addData(parms[1]);
		return out;
	}

	@HAPOperationInfoAnnotation(in = { "list:simple", "any" }, out = "boolean:simple", description = "")
	public HAPData contains(HAPData[] parms, HAPOperationContext opContext){
		HAPListData listData = (HAPListData)parms[0];
		for(int i=0; i<listData.getSize(); i++){
			HAPData eleData = listData.getData(i);

			List<HAPData> expressParms = new ArrayList<HAPData>();
			expressParms.add(eleData);
			expressParms.add(parms[1]);

			HAPBooleanData expressionOutData = (HAPBooleanData)this.getDataTypeManager().dataOperate(parms[1].getDataType(), "equals", expressParms.toArray(new HAPData[0]), opContext);
			if(expressionOutData.getValue()){
				return expressionOutData;
			}
		}
		return HAPDataTypeManager.BOOLEAN.createDataByValue(false);
	}
	
	
	@HAPOperationInfoAnnotation(in = { "list:simple", "expression:simple" }, out = "list:simple", description = "")
	public HAPData filter(HAPData[] parms, HAPOperationContext opContext){
		HAPList listDataType = (HAPList)this.getDataType();
		HAPData out = listDataType.newList();
		
		String thisVarName = ((HAPStringData)parms[2]).getValue();
		
		HAPListData listData = (HAPListData)parms[0];
		for(int i=0; i<listData.getSize(); i++){
			HAPData eleData = listData.getData(i);

			Map<String, HAPData> vars = new LinkedHashMap<String, HAPData>();
			vars.putAll(opContext.getVariables());
			vars.put(thisVarName, eleData);
			
			HAPOperationContext opContext1 = new HAPOperationContext();
			opContext1.setVariables(vars);
			
			List<HAPData> expressParms = new ArrayList<HAPData>();
			expressParms.add(parms[1]);
			expressParms.add(HAPDataTypeManager.MAP.newMap(vars));

			HAPBooleanData expressionOutData = (HAPBooleanData)this.getDataTypeManager().dataOperate(new HAPDataTypeInfo("simple", "expression"), "execute", expressParms.toArray(new HAPData[0]), opContext1);
			if(expressionOutData.getValue()){
				HAPData[] parms3 = {out, eleData};
				out = this.getDataTypeManager().dataOperate(new HAPDataTypeInfo("simple", "list"), "add", parms3, opContext1);
			}
		}
		return out;
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
			out = this.getDataTypeManager().dataOperate(new HAPDataTypeInfo("simple", "list"), "add", parms3, opContext);
		}
		return out;
	}

	@HAPOperationInfoAnnotation(in = { "list:simple", "expression:simple", "string:simple", "string:simple" }, out = "list:simple", description = "")
	public HAPData each(HAPData[] parms, HAPOperationContext opContext){
		HAPList listDataType = (HAPList)this.getDataType();
		HAPData out = null;

		String thisVarName = ((HAPStringData)parms[2]).getValue();
		String outVarName = ((HAPStringData)parms[3]).getValue();
		
		Map<String, HAPData> vars = new LinkedHashMap<String, HAPData>();
		vars.putAll(opContext.getVariables());
		
		HAPListData listData = (HAPListData)parms[0];
		for(int i=0; i<listData.getSize(); i++){
			HAPData eleData = listData.getData(i);
			vars.put(outVarName, out);
			vars.put(thisVarName, eleData);
			
			List<HAPData> expressParms = new ArrayList<HAPData>();
			expressParms.add(parms[1]);
			expressParms.add(HAPDataTypeManager.MAP.newMap(vars));
			
			out = this.getDataTypeManager().dataOperate(new HAPDataTypeInfo("simple", "expression"), "execute", expressParms.toArray(new HAPData[0]), opContext);
			
			vars.put(outVarName, out);
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
