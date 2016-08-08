package com.nosliw.entity.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.exception.HAPServiceDataException;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPWraper;
import com.nosliw.data.info.HAPDataTypeDefInfo;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.event.HAPEntityOperationEvent;
import com.nosliw.entity.event.HAPEvent;
import com.nosliw.entity.expression.HAPAttributeExpressionUtility;
import com.nosliw.entity.operation.HAPEntityOperation;
import com.nosliw.entity.operation.HAPEntityOperationInfo;
import com.nosliw.entity.options.HAPContainerOptionsWraper;
import com.nosliw.entity.options.HAPOptionsComplex;
import com.nosliw.entity.options.HAPOptionsDefinition;
import com.nosliw.entity.utils.HAPAttributeConstant;
import com.nosliw.entity.utils.HAPEntityDataTypeUtility;
import com.nosliw.entity.utils.HAPEntityDataUtility;
import com.nosliw.entity.utils.HAPEntityUtility;
import com.nosliw.entity.validation.HAPValidationInfoExpression;

/*
 * abstract class for all the wraper classes: atom, container, entity, reference
 * wraper play roles of :
 * 		container for data (atom, list, set, map, entity, reference)
 * 		placeholder
 * 		manage empty : empty means there is no value in this wraper
 * 		validation for operation or data
 * 		only event source for all event (implementation in HAPValueEventSource)
 * 			operation event
 * 			update event
 * 			clear event
 *	 	listener for these event, 
 *			operation event : entity
 *			update event : coplext (entity, container)
 *			clear up event : wraper reference to entity
 */
public abstract class HAPDataWraper extends HAPWraper{
	//parent entity
	//for solid entity wraper, this attribute is null
	//for element within container, this attribute is the same as container's parent entity
	private HAPEntityData m_parentEntity = null;
	
	//the attribute name if this wraper is an attribute of a ComplexEntity
	private String m_parentEntityAttributePath = null;
	
	//attribute defnition for this wraper as attribute
	private HAPAttributeDefinition m_attrDef = null;  

	//some attribute have id attribute, for instance, element in container
	private String m_id = null;
	
	private HAPEntityDefinitionManager m_entityDefMan;
	
	//************************  init
	/*
	 * constructor for wrapper
	 * 		type : the data type for this wraper
	 * 		appContext: the application context
	 * some values can be set later:
	 * 		value (empty or data)
	 * 		relation with parent entity
	 */
	HAPDataWraper(HAPDataTypeDefInfo dataType, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan){
		super(dataType, dataTypeMan);
		this.m_entityDefMan = entityDefMan;
	}


	//*************************  Init and Clear up method 

	/*
	 * init the value in the wrap
	 * it is called when create a new entity
	 */
	abstract protected void initAttributeData();

	/*
	 * utility method to use scope as input
	 */
	public void clearUp(int scope){
		this.preClearUp(scope);
		if(scope==HAPConstant.CONS_ENTITYOPERATION_SCOPE_GLOBAL){
			this.breakExternalLink();
		}
		this.clearUPData(createClearupParmsByScope(scope));
		this.breakFromParent(scope);
		this.clearUPBasic(scope);
	}
	
	/*
	 * it is called when the wraper is deleted
	 * clear reference, event, backup ...
	 */
	@Override
	public void clearUp(Map<String, Object> parms){
		int scope = HAPEntityUtility.getScopeFromClearupParms(parms);
		this.clearUp(scope);
	}

	void preClearUp(int scope){}
	void breakExternalLink(){}
	
	/*
	 * clear things (value, attribute speicfic for wraper) in the wraper
	 * 	for container, clear up the value in the container
	 * 	for entity, clear up each attribute 
	 */
	abstract void clearUPData(Map<String, Object> scope);
	
	void breakFromParent(int scope){
		this.m_parentEntity = null;
		this.m_parentEntityAttributePath = null;
		this.m_attrDef = null;
	}

	/*
	 * clear basic information in HAPValueWraper
	 */
	void clearUPBasic(int scope){
		this.clearUp(null);
	}

	/*
	 * create map parms according to scope
	 */
	protected Map<String, Object> createClearupParmsByScope(int scope){
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(HAPConstant.CONS_WRAPECLEARUP_PARM_SCOPE, scope+"");
		return out;
	}
	
	
	//************************  Operation -- need validation and notification
	
	/*
	 * uniformed method for operating the data in wrapper
	 * this operation is restrict because the value will be validated, the event of change will be sent out 
	 * 		1.  backup value
	 * 		2.  do operation
	 *		3.  validation
	 *		4.  if success, inform listener
	 *		5.  
	 *  data:  
	 *		for Atom type, String or HAPData
	 * 		for container type, HAPValueWraper
	 *		for complex type, 
	 */
	public HAPServiceData operate(HAPEntityOperationInfo operation)
	{
		//for critical attribute, rename the operation
		if(HAPEntityDataTypeUtility.isAtomType(this.getDataTypeDefInfo())){
			HAPAtomWraper atomWraper = (HAPAtomWraper)this;
			if(atomWraper.isCritical())  operation.setOperation(HAPEntityOperation.ENTITYOPERATION_ATTR_CRITICAL_SET);
		}

		//do some thing before real operation
		//    check input format
		//    transform/uniform input
		//    ...
		HAPServiceData sdata = this.preOperation(operation);
		if(sdata.isFail())  return sdata;

		//do the actual operation
		List<HAPEntityOperationInfo> extraOps = new ArrayList<HAPEntityOperationInfo>();
		sdata = this.doOperate(operation, extraOps);
		if(sdata.isFail())	return sdata;

		for(HAPEntityOperationInfo extraOp : extraOps){
			this.doAnotherOperation(extraOp);
		}
		
		//validation for this attribute 
		if(operation.isValidation()){
			//all the rules are based on HAPData, therefore, we need to transform the data in the wrap to HAPData first
			HAPServiceData vdata = this.ifValidValue();    
			if(vdata.isFail())		return vdata;
		}
		
		// let parent be aware of the operation
		if (operation.getScope()==HAPConstant.CONS_ENTITYOPERATION_SCOPE_ENTITY ||	operation.getScope()==HAPConstant.CONS_ENTITYOPERATION_SCOPE_GLOBAL) {
			//if this attribute configured for this operation, then create event, and send event
			//to parent Entity
			HAPEvent event = HAPEvent.createEntityOperationEvent(operation);
			if(this.ifTrigureOperationEvent(event)){
				if(this.m_parentEntity!=null){
					HAPServiceData pData = this.m_parentEntity.onEvent(event);
					if(pData.isFail())  return pData;
				}
			}
		}
		
		//let external be aware of the operation
		if(operation.getScope()==HAPConstant.CONS_ENTITYOPERATION_SCOPE_GLOBAL){
			this.externalOperation(operation, sdata);
		}
		
		this.postOperation(operation, sdata);

		return sdata;
	}

	protected HAPServiceData preOperation(HAPEntityOperationInfo operation){return HAPServiceData.createSuccessData();}
	public void prepareReverseOperation(HAPEntityOperationInfo operation, HAPServiceData serviceData){}
	protected void externalOperation(HAPEntityOperationInfo operation, HAPServiceData resule){}
	abstract protected HAPServiceData doOperate(HAPEntityOperationInfo operation, List<HAPEntityOperationInfo> extraOps);
	protected void postOperation(HAPEntityOperationInfo operation, HAPServiceData resule){}
	
//******************parent entity methods
		/*
		 * if this wraper is an attribute of complex entity, return attribute definition
		 * if this wraper is a container element, return the container's attribute definition 
		 */
		public HAPAttributeDefinition getAttributeDefinition(){	return this.m_attrDef;	}

		/*
		 * get parent entity
		 * note : for container element, the parent entity is container's parent, not the container itself
		 */
		public HAPEntityData getParentEntity(){return this.m_parentEntity;	}

		/*
		 * get attribute PATH in parent entity
		 * note : for container element, the attribute name is the container's path + element id
		 */
		public String getParentEntityAttributePath(){return this.m_parentEntityAttributePath;}

		public HAPEntityWraper getRootEntityWraper(){ return HAPEntityDataUtility.getRootEntityParentWraper(this);	}

		public String getRootEntityAttributePath(){	return HAPEntityDataUtility.getRootEntityAttributePath(this);}
		
		public HAPReferenceInfoAbsolute getReferencePath(){	return new HAPReferenceInfoAbsolute(this.getRootEntityWraper().getID(), this.getRootEntityAttributePath());	}

		/*
		 * set the parent entity for this wrapper
		 * 	null, null: clear the data
		 * 		parent entity
		 * 		attribute name in parent entity
		 * 		attribute definition for attribute
		 */
		void setParentEntity(HAPEntityData parentEntity, String attributePath, HAPAttributeDefinition attrDef){
			this.m_parentEntity = parentEntity;
			this.m_parentEntityAttributePath = attributePath;
			
			if(attrDef!=null)  this.m_attrDef = attrDef.cloneDefinition(attrDef.getEntityDefinition());
			else{
				if(this.m_parentEntity==null || this.m_parentEntityAttributePath==null)  this.m_attrDef = null;
				else{
					HAPSegmentParser segs = new HAPSegmentParser(this.m_parentEntityAttributePath);
					this.m_attrDef = parentEntity.getAttributeDefinition(segs.next());
				}
			}
		}
		
		//*******************  Methods related with value
		
		public HAPDataWraper getChildWraperByPath(String attributePath) {
			if(HAPBasicUtility.isStringEmpty(attributePath)) return this;
			
			HAPDataWraper wraper = null;
			HAPSegmentParser pathParser = new HAPSegmentParser(attributePath);
			String firstSeg = pathParser.next();
			String keyword = HAPNamingConversionUtility.getKeyword(firstSeg);
			if(HAPConstant.CONS_ATTRIBUTE_PATH_ENTITY.equals(keyword)){
				//entity keyword
				wraper = this.getParentEntity().getWraper();
			}
			else{
				wraper = this.getChildWraper(firstSeg);
			}
			if(wraper==null) return null;
			else return wraper.getChildWraperByPath(pathParser.getRestPath());
		}

		/*
		 * get child wraper
		 * 		for entity wraper, return the wraper for attribute
		 * 		for container wraper, return the wraper for element
		 */
		abstract protected HAPDataWraper getChildWraper(String pathEle);
		
		public Set<HAPDataWraper> getChildWraperByGenericPath(String attributePath){
			Set<HAPDataWraper> out = new HashSet<HAPDataWraper>();
//			if(HAPUtility.isStringEmpty(attributePath)){
//				out.add(this);
//			}
//			else{
//				HAPPathParser pathParser = new HAPPathParser(attributePath, HAPConstant.SEPERATOR_ATTRPATH);
//				Set<HAPDataWraper> wrapers = this.getGenericChildWraper(pathParser.getFirstSegment());
//				for(HAPDataWraper wraper : wrapers){
//					 out.addAll(wraper.getGenericChildPathWraper(pathParser.getRestPath()));
//				}
//			}
			return out;
		}
		
		abstract protected Set<HAPDataWraper> getGenericChildWraper(String pathEle);

		//*************************  Parse
		
		protected void buildOjbectJsonMap(Map<String, String> map, Map<String, Class> dataTypeMap){
			this.setBasicJsonWraperValue(map);
			this.setWraperJsonValue(map);
		}
		
		protected void setWraperJsonValue(Map<String, String> jsonMap){}
		
		/*
		 * parse string value (json, xml, ...)
		 */
		public void parse(String value){
			HAPData data = this.getDataTypeManager().parseString(value, this.getDataTypeDefInfo().getCategary(), this.getDataTypeDefInfo().getType());
			this.setData(data);
		}
		
		/*
		 * helper class, set common value when create wraper json
		 */
		protected void setBasicJsonWraperValue(Map<String, String> values){
			values.put(HAPAttributeConstant.ATTR_DATAWRAPER_ID, this.getId());
			values.put(HAPAttributeConstant.ATTR_DATAWRAPER_ATTRPATH, this.getParentEntityAttributePath());
			if(this.getParentEntity()==null) values.put(HAPAttributeConstant.ATTR_DATAWRAPER_PARENTENTITYID, null);
			else values.put(HAPAttributeConstant.ATTR_DATAWRAPER_PARENTENTITYID, this.getParentEntity().getWraper().getID().toStringValue(null));
			
			if(this.getAttributeDefinition()!=null)	values.put(HAPAttributeConstant.ATTR_DATAWRAPER_ATTRCONFIGURE, this.getAttributeDefinition().toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));
			else values.put(HAPAttributeConstant.ATTR_DATAWRAPER_ATTRCONFIGURE, "{}");
		}

		public String toString(){return this.getData().toString();	}
		
		
		//************************** Clone
		@Override
		protected void cloneTo(HAPWraper wraper){
			super.cloneTo(wraper);
			HAPDataWraper dataWraper = (HAPDataWraper)wraper;
			dataWraper.m_id = this.m_id;
			dataWraper.m_parentEntityAttributePath = this.m_parentEntityAttributePath;
			if(this.m_attrDef!=null)		dataWraper.m_attrDef = this.m_attrDef.cloneDefinition(this.m_attrDef.getEntityDefinition());
		}
		
		
		//************************** Basic
		public String getId(){return this.m_id;}
		public void setId(String id){this.m_id=id;}

		@Override
		public HAPDataTypeDefInfo getDataTypeDefInfo(){
			if(this.getAttributeDefinition()!=null)  return this.getAttributeDefinition().getDataTypeDefinitionInfo();
			return super.getDataTypeDefInfo();
		}


		//************************** Event
		/*
		 * check if the attribute is configure to triguer the event
		 */
		boolean ifTrigureOperationEvent(HAPEvent event){
			int type = event.getType();
			switch(type){
			case HAPConstant.CONS_EVENTTYPE_ENTITY_OPERATION:
				HAPEntityOperationEvent opEvent = (HAPEntityOperationEvent)event;
				HAPAttributeDefinition attrDef = this.getAttributeDefinition();
				if(opEvent.getOperation().getOperation()==HAPEntityOperation.ENTITYOPERATION_ATTR_CRITICAL_SET)   return true;
				if(attrDef==null)  return false;
				for(String e : this.getAttributeDefinition().getEvents()){
					if(e.equals(opEvent.getOperation().getOperation().getName())){
						return true;
					}
				}
				break;
			case HAPConstant.CONS_EVENTTYPE_ENTITY_MODIFY:
				for(String e : this.getAttributeDefinition().getEvents()){
					if(e.equals(HAPConstant.CONS_EVENT_ENTITY_CHANGE)){
						return true;
					}
				}
				break;
			}
			
			return false;
		}

		//************************  Options
		
		
		
		//************************  Validation
		/*
		 * using rule of the attribute to validate the data
		 */
		protected HAPServiceData ifValidValue()
		{
			HAPServiceData out = HAPServiceData.createSuccessData();
			
			HAPAttributeDefinition attrDef = this.getAttributeDefinition();
			if(attrDef==null)  return out;
			
			for(HAPValidationInfoExpression validationInfo : attrDef.getValidationInfos()){
				try {
					if(!HAPAttributeExpressionUtility.executeAttributeValidationExpression(validationInfo.getExpression(), this)){
						return HAPServiceData.createFailureData(null,validationInfo.getErrorMessage());
					}
				} catch (HAPServiceDataException e) {
					return HAPServiceData.createFailureData(null, "Invalid Rule Return Format : " + e.getServiceData().getMessage());
				}
			}
			return out;
		}
		
		/*
		 * using options of the attribute to validate the data
		 */
		protected HAPServiceData ifValidateOptionsData(){
			HAPServiceData out = HAPServiceData.createSuccessData();

			HAPAttributeDefinition attrDef = this.getAttributeDefinition();
			if(attrDef!=null){
				HAPOptionsDefinition options = attrDef.getOptionsDefinition();
				HAPContainerOptionsWraper optionsDataWraper = null; 
//						options.getOptions(this);  ???
				if(!optionsDataWraper.contains(this)){
					return HAPServiceData.createFailureData(null, "the data is not part of options");
				}
			}
			
			return out;
		}
		
		//************************** Environment Help Method
		protected HAPServiceData getEntityByID(HAPEntityID ID){
			return this.getCurrentTransaction().useEntityByID(ID);
		}
		
		protected HAPEntityDataAccess getCurrentTransaction(){
			HAPEntityWraper rootParentWraper = HAPEntityDataUtility.getRootEntityParentWraper(this);
			return rootParentWraper.getDataAccess();
		}
		
		/*
		 * when do some operation, it lead some extra operation 
		 * this method help to invoke another operation
		 */
		protected HAPServiceData doAnotherOperation(HAPEntityOperationInfo operationInfo){
			return this.getCurrentTransaction().operate(operationInfo);
		}
		
		protected HAPEntityDefinitionManager getEntityDefinitionManager(){return this.m_entityDefMan;}
}
