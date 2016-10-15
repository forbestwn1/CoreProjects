package com.nosliw.entity.data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPWraper;
import com.nosliw.data.datatype.HAPDataTypeDefInfo;
import com.nosliw.data.utils.HAPDataUtility;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.definition.HAPEntityDefinitionCritical;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.event.HAPEntityClearupEvent;
import com.nosliw.entity.event.HAPEntityOperationEvent;
import com.nosliw.entity.event.HAPEvent;
import com.nosliw.entity.operation.HAPEntityOperation;
import com.nosliw.entity.operation.HAPEntityOperationFactory;
import com.nosliw.entity.operation.HAPEntityOperationInfo;
import com.nosliw.entity.utils.HAPAttributeConstant;
import com.nosliw.entity.utils.HAPEntityDataUtility;

public class HAPEntityWraper extends HAPDataWraper{

	private HAPEntityID m_ID;

	private int m_status;
	private HAPEntityDataAccess m_dataAccess;

	public HAPEntityWraper(HAPDataTypeDefInfo dataTypeDefInfo, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan){
		super(dataTypeDefInfo, dataTypeMan, entityDefMan);
	}

	public HAPEntityWraper(String type, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan){
		this(new HAPDataTypeDefInfo(HAPConstant.DATATYPE_CATEGARY_ENTITY, type), dataTypeMan, entityDefMan);
	}
	
	public HAPEntityWraper(HAPEntityData entity, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan){
		super(new HAPDataTypeDefInfo(HAPDataUtility.getDataTypeInfo(entity)), dataTypeMan, entityDefMan);
		this.setData(entity);
	}

	@Override
	protected void initAttributeData(){
		HAPAttributeDefinition attrDef = this.getAttributeDefinition();
		if(attrDef==null){
			this.setEmpty();
		}
		else{
			HAPEntityData entity = this.getEntityData();
			if(entity == null){
				HAPEntity entityDataType = HAPEntityDataUtility.getEntityDataType(this.getEntityType(), this.getDataTypeManager());
				entity = entityDataType.newEntity();
				this.setData(entity);
			}
			else{
//				entity.reset();
//				entity.initialize();
			}
		}
	}
	
	
	@Override
	protected HAPServiceData doOperate(HAPEntityOperationInfo operation, List<HAPEntityOperationInfo> extraOps) {
		return HAPServiceData.createFailureData();
	}
	
	
	//*************************  clear up method 
	public HAPServiceData preRemove(){return HAPServiceData.createSuccessData();}
	
	@Override
	void breakExternalLink(){
		HAPEvent event = new HAPEntityClearupEvent(this);
		this.getCurrentTransaction().onEvent(event);
	}
	
	@Override
	void clearUPData(Map<String, Object> scope) {
		if(!this.isEmpty()){
			HAPEntityData entity = this.getEntityData();
			entity.clearUp(scope);
		}
	}
	
	
	//*************************  Event 
	/*
	 * handle event 
	 * 		critical value event:
	 * 			
	 *  	other event:
	 *  		wraper the event into a event modify event
	 * 
	 */
	public HAPServiceData onEvent(HAPEvent event) {
		HAPServiceData out = HAPServiceData.createSuccessData();
		
		int scope = HAPConstant.ENTITYOPERATION_SCOPE_GLOBAL;

		out = this.getEntityData().handleEvent(event);
		if(out.isFail())  return out;

		if(event.getType()==HAPConstant.EVENTTYPE_ENTITY_OPERATION)
		{
			HAPEntityOperationEvent attrOpEvent = (HAPEntityOperationEvent)event;
			HAPEntityOperationInfo operation = attrOpEvent.getOperation();
			scope = operation.getScope();
			if(operation.getOperation()==HAPEntityOperation.ENTITYOPERATION_ATTR_CRITICAL_SET){

				HAPEntityDefinitionCritical entityDefinition = this.getEntityDefinitionManager().getEntityDefinition(this.getEntityType());

				String criticalValue1 = operation.getExtra().toString();
				Map<String, HAPAttributeDefinition> attrs1 = entityDefinition.getCriticalValueAttributeDefinitions(criticalValue1);
				for(String attName : attrs1.keySet()){
					HAPAttributeDefinition attr = attrs1.get(attName); 
					HAPDataWraper attrWraper = this.getEntityData().getAttributeValueWraper(attr.getName());
					HAPEntityOperationInfo op = HAPEntityOperationFactory.createEntityAttributeRemoveOperation(this.getID(), attr.getName());
					this.getCurrentTransaction().operate(op);
				}

				String criticalValue = (String)operation.getData().toString();
				Map<String, HAPAttributeDefinition> attrs = entityDefinition.getCriticalValueAttributeDefinitions(criticalValue);
				for(String attrName : attrs.keySet()){
					HAPAttributeDefinition attr = attrs.get(attrName);
					HAPEntityOperationInfo op = HAPEntityOperationFactory.createEntityAttributeAddOperation(this.getID(), attr);
					this.getCurrentTransaction().operate(op);
				}
			}
		}

		//configure > programming
		boolean ifEvent = false;
		if(this.isRootEntity())  ifEvent = true;   //for root entity, always send our modification
		else{
			if(this.ifTrigureOperationEvent(event))  ifEvent = true;
			else{
				if(out.getCode()==HAPConstant.SERVICECODE_ENTITYOPERATION_FORWARD)  ifEvent = true;
			}
		}

		if(ifEvent)
		{
			HAPEntityData parentEntity = this.getParentEntity();
			HAPEvent modifyEvent = HAPEvent.createEntityModifyEvent(this, event);
			//inform the parent
			if(parentEntity!=null){
				out = parentEntity.onEvent(modifyEvent);
			}
			if(scope==HAPConstant.ENTITYOPERATION_SCOPE_GLOBAL){
				//entity manager handle rest : inform reference, options, ...
				this.getCurrentTransaction().onEvent(modifyEvent);
			}
		}
		
		return out;
	}

	//*************************  Reference 

	
	//*************************  Data / value 
	public void setData(HAPData value){
		super.setData(value);
		if(value!=null)  ((HAPEntityData)value).setWraper(this);
	}

	public HAPEntityData getEntityData(){return (HAPEntityData)this.getData();}
	
	@Override
	protected HAPDataWraper getChildWraper(String attribute){
		if(!this.isEmpty())		return this.getEntityData().getAttributeValueWraper(attribute);
		else return null;
	}

	@Override
	protected Set<HAPDataWraper> getGenericChildWraper(String pathEle){
		Set<HAPDataWraper> out = new HashSet<HAPDataWraper>();
		if(pathEle.charAt(0)=='['){
			String exAttr = pathEle.substring(0, pathEle.length()-1).trim();
			out.addAll(this.getDataAccess().getReferenceManager().searchByReverseAttribute(this.getID(), exAttr));
		}
		else{
			out.add(this.getChildWraper(pathEle));
		}
		return out;
	}
	

	//*************************  Clone 
	public HAPEntityWraper cloneEntityWraper(HAPEntityDataAccess transaction){
		HAPEntityWraper out = (HAPEntityWraper)this.cloneWraper();
		out.setDataAccess(transaction);
		return out;
	}

	@Override
	protected void cloneTo(HAPWraper wraper){
		super.cloneTo(wraper);
		HAPEntityWraper dataWraper = (HAPEntityWraper)wraper;
		if(this.m_ID!=null)		dataWraper.setID(this.m_ID.clone());
		dataWraper.setStatus(this.m_status);
	}
	
	//*************************  Parse 
	@Override
	protected void setWraperJsonValue(Map<String, String> jsonMap) {
		jsonMap.put(HAPAttributeConstant.DATAWRAPER_ENTITYID, this.getID().toString());
	}

	//*************************  Basic method 
	public boolean isRootEntity(){
		return this.getParentEntity()==null;
	}
	
	/*
	 * set ID for entity
	 * valid only when entity is in transit status
	 * because in persistent status, id is not allow to reset to other value (immutable)
	 */
	public void setID(HAPEntityID ID){this.m_ID=ID;}
	public HAPEntityID getID(){
		if(this.m_ID==null){
			HAPEntityID parentID = this.getParentEntity().getWraper().getID();
			this.m_ID = new HAPEntityID(parentID, this.getParentEntityAttributePath());
		}
		return this.m_ID;
	}
	
	public String getEntityType(){	return this.getDataTypeDefInfo().getType();	}
	public String getGroup(){return this.getGroup();}
	
	public int getStatus(){return this.m_status;}
	public void setStatus(int status){this.m_status=status;}
	
	public HAPEntityDataAccess getDataAccess(){
		if(this.m_dataAccess!=null)		return this.m_dataAccess;
		else{
			return this.getParentEntity().getWraper().getDataAccess();
		}
	}
	public void setDataAccess(HAPEntityDataAccess trans){this.m_dataAccess=trans;}
	
}
