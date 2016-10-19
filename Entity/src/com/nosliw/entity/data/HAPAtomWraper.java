package com.nosliw.entity.data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.datatype.HAPDataTypeDefInfo;
import com.nosliw.data1.HAPAtomData;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.definition.HAPAttributeDefinitionAtom;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.operation.HAPEntityOperationInfo;

/*
 * atom value wraper
 */
public class HAPAtomWraper extends HAPDataWraper{

	
	//**************************** Init
	/*
	 * operations supported by AtomValueWraper
	 * OPERATION_CHANGEVALUE: 
	 * 		change the internal value of wraper (both normal and critical atom attribute)
	 * OPERATION_CRITICALVALUE:
	 * 		when CHANGEVALUE operation is on critical attribute, then the operation is changed to CRITICALVALUE
	 * 		atom attribute wraper, just set the value
	 * 		entity wraper do the rest part: change entity attribute
	 */
	
	public HAPAtomWraper(HAPDataTypeDefInfo type, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan) {
		super(type, dataTypeMan, entityDefMan);
		this.setEmpty();
	}
	
	@Override
	protected void initAttributeData(){
		HAPAttributeDefinitionAtom attrDef = (HAPAttributeDefinitionAtom)this.getAttributeDefinition();
		if(attrDef==null){
			this.setEmpty();
		}
		else if(attrDef.getDefaultValue()!=null){
			//use default value configured
			this.setData(attrDef.getDefaultValue());
		}
		else if(!attrDef.getIsEmptyOnInit()){
			//if mandatory, set default value for this type
			HAPData value = this.getDataTypeManager().getDefaultValue(this.getDataTypeDefInfo());
			this.setData(value);
		}
		else{
			//if empty on init, set empty
			this.setEmpty();
		}
	}
	
	
	//**************************** Clear UP
	@Override
	void clearUPData(Map<String, Object> parms) {
		HAPData data = this.getData();
		if(data!=null)  data.clearUp(parms);
		this.setEmpty();
	}

	//*****************************  Attribute Value
	@Override
	protected HAPDataWraper getChildWraper(String attribute){return this;}

	@Override
	protected Set<HAPDataWraper> getGenericChildWraper(String pathEle){return new HashSet<HAPDataWraper>();}
	
	
	//****************************  Operation	
	@Override
	protected HAPServiceData doOperate(HAPEntityOperationInfo operation, List<HAPEntityOperationInfo> extraOps) {
		HAPData outData = this.getData();
		switch(operation.getOperation()){
		case ENTITYOPERATION_ATTR_ATOM_SET:
			this.setData((HAPData)operation.getData());
			break;
		case ENTITYOPERATION_ATTR_CRITICAL_SET:
			this.setData((HAPData)operation.getData());
			break;
		}
		return HAPServiceData.createSuccessData(outData);
	}

	@Override
	public void prepareReverseOperation(HAPEntityOperationInfo operation, HAPServiceData serviceData){
		operation.setExtra(this.getData());
	}

	@Override
	protected HAPServiceData preOperation(HAPEntityOperationInfo operation){
		String value = operation.getValue();
		HAPData d = operation.getData();
		HAPAtomData data = null;
		if(d!=null){
			data = (HAPAtomData)d;
		}
		else{
			data = (HAPAtomData)this.getDataTypeManager().parseString(value, this.getDataTypeDefInfo().getCategary(), this.getDataTypeDefInfo().getType());
			operation.setData(data);
		}
		return HAPServiceData.createSuccessData();
	}
	
	/*
	 * if this atom wraper attribute is critical attribute
	 */
	public boolean isCritical(){return ((HAPAttributeDefinitionAtom)this.getAttributeDefinition()).getIsCritical();}
	
	
	@Override
	public boolean equals(Object o){
		return HAPBasicUtility.isEquals(this.getData(), o); 
	}
}
