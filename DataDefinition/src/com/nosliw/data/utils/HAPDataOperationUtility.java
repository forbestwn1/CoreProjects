package com.nosliw.data.utils;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeImp;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataOperationInfo;

public class HAPDataOperationUtility {

	public static boolean isNewOperation(String opName){
		return opName.startsWith(HAPConstant.DATAOPERATION_NEWDATA);
	}
	
	
	public static HAPServiceData dataOperate(HAPDataTypeImp dataType, String operation, HAPData[] parms){
		HAPDataOperationInfo operationInfo = null;
		operationInfo = dataType.getOperationInfoByName(operation);
		
		if(operationInfo==null){
			//cannot find by name, means does not defined in anywhere (older version, parent)
			return HAPDataErrorUtility.createDataOperationNotDefinedError(dataType.getDataTypeInfo(), operation, null); 
		}
		
		String convertPath = operationInfo.getConvertPath();
		if(HAPBasicUtility.isStringEmpty(convertPath)){
			//within current version, no convertion
			return dataType.localOperate(operation, parms);
		}
		else{
			//do convertion of the parms
			HAPData[] oldParms = parms;
			HAPDataTypeImp newDataType = dataType;
			String[] convertPathSegs = HAPNamingConversionUtility.parsePathSegs(convertPath); 
			for(String convertPathSeg : convertPathSegs){
				HAPData[] newParms = new HAPData[parms.length];
				String[] convertPathSegDetails = HAPNamingConversionUtility.parseDetailInfos(convertPathSeg);
				switch(convertPathSegDetails[0]){
				case HAPConstant.OPERATIONDEF_PATH_PARENT:
				{
					//convert parms to parent type
					for(int i=0; i<oldParms.length; i++){
						HAPData d = oldParms[i];
						if(newDataType.getDataTypeInfo().equalsWithoutVersion(HAPDataUtility.getDataTypeInfo(d))){ // just convert all the parms of the same data type (not considering the version)
							//conver to parent type
							HAPServiceData s = d.getDataType().operate(HAPConstant.DATAOPERATION_TOPARENTTYPE, new HAPData[]{d});
							if(s.isSuccess())  newParms[i] = (HAPData)s.getData();	
							else	return s;
						}
						else{
							newParms[i] = d;
						}
					}
					newDataType = newDataType.getParentDataType();
					break;
				}
				case HAPConstant.OPERATIONDEF_PATH_VERSION:
				{
					int newVersion = Integer.parseInt(convertPathSegDetails[1]);
					newDataType = dataType.getDataTypeByVersion(newVersion);
					for(int i=0; i<oldParms.length; i++){
						HAPData d = oldParms[i];
						if(newDataType.getDataTypeInfo().equalsWithoutVersion(HAPDataUtility.getDataTypeInfo(d))){ // just convert all the parms of the same data type (not considering the version)
							//conver to parent type
							HAPServiceData s = dataType.operate(HAPConstant.DATAOPERATION_TOVERSION, new HAPData[]{d, HAPDataTypeManager.INTEGER.createDataByValue(newVersion)});
							if(s.isSuccess())	newParms[i] = (HAPData)s.getData();
							else	return s;
						}
						else{
							newParms[i] = d;
						}
					}
					break;
				}
				}
				oldParms = newParms; 
			}
			HAPServiceData out = newDataType.operate(operation, oldParms);
			if(out.isSuccess() && out.getData()!=null){
				HAPData outData = (HAPData)out.getData();
				if(HAPDataUtility.getDataTypeInfoWithVersion(outData).equalsWithoutVersion(dataType.getDataTypeInfo())){
					//convert the result back to current version
					HAPServiceData s = dataType.operate(HAPConstant.DATAOPERATION_FROMVERSION, new HAPData[]{outData});
					if(s.isSuccess())	out.setData(s.getData());
					else	return s;
				}
			}
			return out;
		}
	}
	
}
