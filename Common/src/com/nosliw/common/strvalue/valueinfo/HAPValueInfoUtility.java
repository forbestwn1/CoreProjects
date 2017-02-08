package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueAtomic;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoUtility {

	public static HAPStringableValueEntity validateStringableValueEntity(HAPValueInfoEntity entityValueInfo, HAPStringableValueEntity entity){
		HAPStringableValueEntity out = null;
		boolean isMandatory = entityValueInfo.getAtomicAncestorValueBoolean(HAPValueInfoEntity.MANDATORY);
		if(!isMandatory && entity.isEmpty())   return null;
		return entity;
	}

	/**
	 * Get class name used by value info
	 * @param valueInfo
	 * @return
	 */
	public static String getEntityClassNameFromValueInfo(HAPValueInfo valueInfo){
		String className = null;
		if(valueInfo.getValueInfoType().equals(HAPConstant.STRINGALBE_VALUEINFO_ENTITY)){
			className = ((HAPValueInfoEntity)valueInfo).getClassName();
		}
		else if(valueInfo.getValueInfoType().equals(HAPConstant.STRINGALBE_VALUEINFO_ATOMIC)){
			if(((HAPValueInfoAtomic)valueInfo).getDataType().equals(HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT)){
				className = ((HAPValueInfoAtomic)valueInfo).getSubDataType();
			}
		}
		return className;
	}

	/**
	 * Get real object 
	 * @param valueInfo
	 * @return
	 */
	public static Object getObjectFromStringableValue(HAPStringableValue value){
		Object out = null;
		
		if(value==null || value.getStringableStructure()==null){
			int kkkk = 555;
			kkkk++;
		}
		
		if(value.getStringableStructure().equals(HAPConstant.STRINGALBE_VALUEINFO_ENTITY)){
			out = value;
		}
		else if(value.getStringableStructure().equals(HAPConstant.STRINGABLE_VALUESTRUCTURE_ATOMIC)){
			HAPStringableValueAtomic atomicValue = (HAPStringableValueAtomic)value;
			if(atomicValue.getLiterateType().getType().equals(HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT)){
				out = atomicValue.getValue();
			}
		}
		return out;
	}

}
