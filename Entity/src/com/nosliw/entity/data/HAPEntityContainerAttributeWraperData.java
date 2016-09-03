package com.nosliw.entity.data;

import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataImp;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.info.HAPDataTypeDefInfo;
import com.nosliw.entity.definition.HAPAttributeDefinitionContainer;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.utils.HAPEntityDataTypeUtility;

public class HAPEntityContainerAttributeWraperData extends HAPBaseData{

	private Map<String, HAPDataWraper> m_elements;
	
	private HAPEntityContainerAttributeWraper m_wraper;
	
	HAPEntityContainerAttributeWraperData(HAPEntityContainerAttributeWraper wraper, HAPDataType dataType, HAPEntityDefinitionManager entityDefMan){
		super(dataType, entityDefMan);
		this.m_wraper = wraper;
		this.m_elements = new LinkedHashMap<String, HAPDataWraper>();
	}

	//******************************  Clear Up
	protected void doClearUp(int scope){
		for(String key : this.m_elements.keySet()){
			HAPDataWraper eleWraper = this.m_elements.get(key);
			eleWraper.clearUp(scope);
		}
		this.m_elements.clear();
	}
	
	/**************************  Container Basic ***************************/
	private String createId(){
		return String.valueOf(System.currentTimeMillis());
	}
	
	public Iterator<HAPDataWraper> iterate() {
		return this.m_elements.values().iterator();
	}
	
	public int getSize(){return this.m_elements.size();}

	public HAPDataWraper getElementWraper(String id){return this.m_elements.get(id);}

	public HAPDataWraper[] getElementWrapers(){
		return this.m_elements.values().toArray(new HAPDataWraper[0]);
	}
	
	@Override
	public boolean isEmpty() {return this.getSize()==0;}
	
	/*******************************  manage element ****************************/
	public HAPDataWraper removeElementData(String id) {
		HAPDataWraper wraper = this.getElementWraper(id);
		this.m_elements.remove(id);
		return wraper;
	}

	public void removeAllElementDatas(){
		for(String id :this.m_elements.keySet()){
			HAPDataWraper wraper = this.getElementWraper(id);
			wraper.clearUp(HAPConstant.ENTITYOPERATION_SCOPE_OPERATION);
			this.m_elements.remove(id);
		}
	}
	
	/*
	 * add value wraper to the container
	 * no event
	 */
	public HAPDataWraper addElementData(HAPData data, String id){
		HAPDataTypeDefInfo dataTypeInfo = this.m_wraper.getChildDataTypeDefInfo();

		HAPDataWraper eleWraper = null;
		if(HAPEntityDataTypeUtility.isAtomType(dataTypeInfo)){
			eleWraper = new HAPAtomWraper(dataTypeInfo, this.getDataTypeManager(), this.getEntityDefinitionManager());
		}
		else if(HAPEntityDataTypeUtility.isReferenceType(dataTypeInfo)){
			eleWraper = new HAPReferenceWraper(dataTypeInfo, this.getDataTypeManager(), this.getEntityDefinitionManager());
		}
		else if(HAPEntityDataTypeUtility.isEntityType(dataTypeInfo)){
			eleWraper = new HAPEntityWraper(dataTypeInfo, this.getDataTypeManager(), this.getEntityDefinitionManager());
		}
		
		eleWraper.setData(data);
		String dataId = id;
		if(HAPBasicUtility.isStringEmpty(id))  dataId = this.createId();
		eleWraper.setId(dataId);

		//set AttrDef for container to its element 
		HAPAttributeDefinitionContainer attrDef = (HAPAttributeDefinitionContainer)this.m_wraper.getAttributeDefinition();
		eleWraper.setParentEntity(this.m_wraper.getParentEntity(), HAPNamingConversionUtility.cascadePath(this.m_wraper.getParentEntityAttributePath(), dataId), attrDef.getChildAttributeDefinition());
		
		this.m_elements.put(dataId, eleWraper);
		return eleWraper;
	}

	
	/**************************  Clone  ***************************/
	
	@Override
	public HAPData cloneData() {
		return null;
	}

	public void cloneFrom(HAPEntityContainerAttributeWraperData data){
		for(HAPDataWraper ele : data.getElementWrapers()){
			this.addElementData(ele.getData().cloneData(), ele.getId());
		}
	}

	
	/**************************  Parse  ***************************/
	@Override
	public String toDataStringValue(String format) {
		if(format.equals(HAPConstant.SERIALIZATION_JSON)) 
		{
			Map<String, String> jsonMap = new LinkedHashMap<String, String>();
			Iterator<HAPDataWraper> it = this.iterate();
			while(it.hasNext()){
				HAPDataWraper eleWraper = it.next();
				if(eleWraper!=null){
					jsonMap.put(eleWraper.getId(), eleWraper.toStringValue(HAPConstant.SERIALIZATION_JSON));
				}
			}
			return HAPJsonUtility.getMapJson(jsonMap);
		}
		return null;
	}

}
