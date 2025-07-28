package com.nosliw.entity.operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data1.HAPWraper;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPReferenceInfoAbsolute;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.definition.HAPEntityDefinitionCritical;
import com.nosliw.entity.query.HAPQueryEntityWraper;
import com.nosliw.entity.utils.HAPAttributeConstant;
import com.nosliw.entity.utils.HAPEntityEnvironment;

/*
 * class that store all the information for operation
 */
public class HAPEntityOperationInfo {
	
	/*
	 * every operation has unique id
	 * this id is not only used within same data access, it also used within different layer of data access in order to merge operation between layers
	 * it is easy to be implemented for root operation
	 * how to implemented for extended operation?? 
	 */
	private HAPEntityOperationId m_operationId;
	
	/*
	 * root operation, in most case, is the operation request from client
	 * not root operation, is the operation created during the process of running another operation
	 */
	private boolean m_isRootOperation = false;
	
	/*
	 * whether this operation should be submitted during transaction commit
	 */
	private boolean m_isSubmitable = true;
	
	/*
	 * there are three scopes which defines the boundary of operation
	 * 		operation :  only this operation
	 * 		entity :	 current entity boundary
	 * 		global	:	 everywhere: referenced entity, query, ...
	 */
	private int m_scope = -1;

	/*
	 * for the operation that require create a new transaction, whether to commit that transaction after finish the operation
	 */
	private boolean m_isAutoCommit = true;
	
	//whether this operation need go through validation stage
	private boolean m_isValidation = true;

	//operation object
	private HAPEntityOperation m_operation;
	
	private List<HAPEntityOperationInfo> m_entityOperations;
	private HAPEntityID m_entityID;
	private String m_attributePath;
	private HAPReferenceInfoAbsolute m_referencePath;
	private HAPData m_data;
	private HAPWraper m_wraper;
	private HAPEntityDefinitionCritical m_entityDefinition;
	private HAPAttributeDefinition m_attributeDefinition;
	private String m_entityType;
	private String m_elementId;
	private String m_queryName;
	private HAPQueryEntityWraper m_queryEntityWraper;
	private String m_transactionId;
	private String m_value;
	private HAPEntityID m_refEntityID;
	private String m_queryEntityId;
	private int m_queryPosition;
	
	private Map<String, String> m_parms;
	
	private Object m_extra;
	
	public HAPEntityOperationInfo(){
		this.m_parms = new LinkedHashMap<String, String>();
		this.m_operationId = new HAPEntityOperationId();
	}

	public HAPEntityOperationInfo cloneOperationInfo(){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		
		out.m_operationId = this.m_operationId;
		out.m_operation = this.m_operation;
		out.m_isRootOperation = this.m_isRootOperation;
		out.m_isSubmitable = this.m_isSubmitable;
		out.m_scope = this.m_scope;
		out.m_isAutoCommit = this.m_isAutoCommit;
		out.m_isValidation = this.m_isValidation;
		
		if(this.m_entityID!=null)		out.m_entityID = this.m_entityID.clone();
		out.m_attributePath = this.m_attributePath;
		out.m_referencePath = this.m_referencePath;
		if(this.m_data!=null)		out.m_data = this.m_data.cloneData();
		if(this.m_wraper!=null)		out.m_wraper = this.m_wraper.cloneWraper();
		if(this.m_entityDefinition!=null)		out.m_entityDefinition = (HAPEntityDefinitionCritical)this.m_entityDefinition.cloneDefinition();
		if(this.m_attributeDefinition!=null)		out.m_attributeDefinition = this.m_attributeDefinition.cloneDefinition(null);
		out.m_entityType = this.m_entityType;
		out.m_elementId = this.m_elementId;
		out.m_queryName = this.m_queryName;
		out.m_transactionId = this.m_transactionId;
		out.m_value = this.m_value;
		if(this.m_refEntityID!=null)		out.m_refEntityID = this.m_refEntityID.clone();
		
		out.m_extra = this.m_extra;
		
		for(String parm : this.m_parms.keySet()){
			out.m_parms.put(parm, this.m_parms.get(parm));
		}
		
		return out;
	}

	@Override
	public String toString(){
		return this.toStringValue(HAPSerializationFormat.JSON);
	}
	
	public String toStringValue(String format){
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_OPERATIONID, String.valueOf(this.getOperationId()));
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_OPERATION, String.valueOf(this.getOperation().getName()));
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_AUTOCOMMIT, String.valueOf(this.isAutoCommit()));
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_VALIDATION, String.valueOf(this.isValidation()));
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_SCOPE, String.valueOf(this.getScope()));
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_ROOTOPERATION, String.valueOf(this.isRootOperation()));
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_SUBMITABLE, String.valueOf(this.isSubmitable()));
		
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_ATTRIBUTEPATH, this.getAttributePath());
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_ENTITYTYPE, this.getEntityType());
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_ELEMENTID, this.getElementId());
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_QUERYNAME, this.getQueryName());
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_TRANSACTIONID, this.getTransactionId());
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_VALUE, this.getValue());
		
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_ENTITYID, this.getEntityID()==null?null:this.getEntityID().toStringValue(format));
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_REFERENCEPATH, this.getReferencePath()==null?null:this.getReferencePath().toStringValue(format));
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_DATA, this.getData()==null?null:this.getData().toStringValue(format));
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_WRAPER, this.getWraper()==null?null:this.getWraper().toStringValue(format));
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_ENTITYDEFINITION, this.getEntityDefinition()==null?null:this.getEntityDefinition().toStringValue(format));
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_ATTRIBUTEDEFINITION, this.getAttributeDefinition()==null?null:this.getAttributeDefinition().toStringValue(format));
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_REFENTITYID, this.getRefEntityID()==null?null:this.getRefEntityID().toStringValue(format));
		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_QUERYENTITYWRAPER, this.getQueryEntityWraper()==null?null:this.getQueryEntityWraper().toStringValue(format));

		if(this.m_entityOperations!=null){
			jsonMap.put(HAPAttributeConstant.OPERATIONINFO_ENTITYOPERATIONS, HAPJsonUtility.getListObjectJson(m_entityOperations));
		}
		
		if(this.getExtra()!=null){
			if(this.getExtra() instanceof HAPSerializable){
				jsonMap.put(HAPAttributeConstant.OPERATIONINFO_EXTRA, ((HAPSerializable)this.getExtra()).toStringValue(format));
			}
			else if(this.getExtra() instanceof String){
				jsonMap.put(HAPAttributeConstant.OPERATIONINFO_EXTRA, (String)this.getExtra());
			}
		}

		jsonMap.put(HAPAttributeConstant.OPERATIONINFO_PARMS, HAPJsonUtility.buildMapJson(this.m_parms));
		
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
	
	public static HAPEntityOperationInfo parseJson(JSONObject jsonOperation, HAPEntityEnvironment entityEvr){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();

		String operationId = jsonOperation.optString(HAPAttributeConstant.OPERATIONINFO_OPERATIONID);
		if(HAPBasicUtility.isStringNotEmpty(operationId))  out.setOperationId(new HAPEntityOperationId(Long.parseLong(operationId)));
		
		try{
			out.m_operation = HAPEntityOperation.getOperationByName(jsonOperation.getString(HAPAttributeConstant.OPERATIONINFO_OPERATION));
			out.m_isRootOperation = jsonOperation.optBoolean(HAPAttributeConstant.OPERATIONINFO_ROOTOPERATION, out.isRootOperation());
			out.m_isSubmitable = jsonOperation.optBoolean(HAPAttributeConstant.OPERATIONINFO_SUBMITABLE, out.isSubmitable());
			out.m_scope = jsonOperation.optInt(HAPAttributeConstant.OPERATIONINFO_SCOPE, out.getScope()); 
			out.m_isAutoCommit = jsonOperation.optBoolean(HAPAttributeConstant.OPERATIONINFO_AUTOCOMMIT, out.m_isAutoCommit);
			out.m_isValidation = jsonOperation.optBoolean(HAPAttributeConstant.OPERATIONINFO_VALIDATION, out.m_isValidation);
			
			out.m_attributePath = jsonOperation.optString(HAPAttributeConstant.OPERATIONINFO_ATTRIBUTEPATH, out.m_attributePath);
			out.m_entityType = jsonOperation.optString(HAPAttributeConstant.OPERATIONINFO_ENTITYTYPE, out.m_entityType);
			out.m_elementId = jsonOperation.optString(HAPAttributeConstant.OPERATIONINFO_ELEMENTID, out.m_elementId); 
			out.m_queryName = jsonOperation.optString(HAPAttributeConstant.OPERATIONINFO_QUERYNAME, out.m_queryName);
			out.m_transactionId = jsonOperation.optString(HAPAttributeConstant.OPERATIONINFO_TRANSACTIONID, out.m_transactionId); 
			out.m_value = jsonOperation.optString(HAPAttributeConstant.OPERATIONINFO_VALUE, out.m_value); 

			JSONObject entityIDJson = jsonOperation.optJSONObject(HAPAttributeConstant.OPERATIONINFO_ENTITYID);
			if(entityIDJson!=null)		out.m_entityID = HAPEntityID.parseJson(entityIDJson);

			JSONObject refPathJson = jsonOperation.optJSONObject(HAPAttributeConstant.OPERATIONINFO_REFERENCEPATH);
			if(refPathJson!=null) out.m_referencePath = HAPReferenceInfoAbsolute.parseJson(refPathJson);

			JSONObject dataJson = jsonOperation.optJSONObject(HAPAttributeConstant.OPERATIONINFO_DATA);
			if(dataJson!=null)  out.m_data = entityEvr.getDataTypeManager().parseJson(dataJson, null, null);
			else{
				String dataString = jsonOperation.optString(HAPAttributeConstant.OPERATIONINFO_DATA);
				out.m_data = entityEvr.getDataTypeManager().parseString(dataString, null, null);
			}

			JSONObject wraperJson = jsonOperation.optJSONObject(HAPAttributeConstant.OPERATIONINFO_WRAPER);
			if(wraperJson!=null)  out.m_wraper = entityEvr.getDataTypeManager().parseWraper(wraperJson);
			
			JSONObject entityDefJson = jsonOperation.optJSONObject(HAPAttributeConstant.OPERATIONINFO_ENTITYDEFINITION);
			if(entityDefJson!=null)		out.m_entityDefinition = entityEvr.getEntityDefinitionManager().parseEntityDefinitionJson(entityDefJson);

			JSONObject attrDefJson = jsonOperation.optJSONObject(HAPAttributeConstant.OPERATIONINFO_ATTRIBUTEDEFINITION); 
			if(attrDefJson!=null)   out.m_attributeDefinition = entityEvr.getEntityDefinitionManager().parseAttributeDefinitionJson(attrDefJson);
			
			JSONObject refEntityIDJson = jsonOperation.optJSONObject(HAPAttributeConstant.OPERATIONINFO_REFENTITYID);
			if(refEntityIDJson!=null)  out.m_refEntityID = HAPEntityID.parseJson(refEntityIDJson);
			
			String entityId = jsonOperation.optString(HAPAttributeConstant.OPERATIONINFO_ENTITYID);
			if(HAPBasicUtility.isStringNotEmpty(entityId))		out.m_entityID = new HAPEntityID(entityId);

			JSONArray entityOperationJsons = jsonOperation.optJSONArray(HAPAttributeConstant.OPERATIONINFO_ENTITYOPERATIONS);
			if(entityOperationJsons!=null){
				for(int j=0; j<entityOperationJsons.length(); j++){
					JSONObject entityOperationJson = entityOperationJsons.getJSONObject(j);
					out.addEntityOperation(HAPEntityOperationInfo.parseJson(entityOperationJson, entityEvr));
				}
			}
			
			JSONObject parmsJson = jsonOperation.optJSONObject("parms");
			if(parmsJson!=null){
				Iterator<String> it = parmsJson.keys();
				while(it.hasNext()){
					String parm = it.next();
					String value = parmsJson.optString(parm);
					out.m_parms.put(parm, value);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	public HAPEntityOperationId getOperationId(){return this.m_operationId;}
	public void setOperationId(HAPEntityOperationId id){this.m_operationId=id;}
	public HAPEntityOperation getOperation(){return this.m_operation;}
	public void setOperation(HAPEntityOperation op){this.m_operation = op;}
	public boolean isRootOperation(){return this.m_isRootOperation;}
	public void setIsRootOperation(boolean isRoot){	this.m_isRootOperation=isRoot;	}
	public boolean isSubmitable(){return this.m_isSubmitable;}
	public void setIsSubmitable(boolean sub){this.m_isSubmitable=sub;}
	public int getScope(){return this.m_scope;}
	public void setScope(int scope){this.m_scope=scope;}
	public boolean isAutoCommit(){return this.m_isAutoCommit;}
	public void setIsAutoCommit(boolean auto){this.m_isAutoCommit=auto;}
	public boolean isValidation(){return this.m_isValidation;}
	public void setIsValidation(boolean valid){this.m_isValidation=valid;}

	public HAPEntityID getEntityID(){return this.m_entityID;}
	public void setEntityID(HAPEntityID ID){this.m_entityID=ID;}
	public String getAttributePath(){return this.m_attributePath;}
	public void setAttributePath(String path){this.m_attributePath=path;}
	public HAPReferenceInfoAbsolute getReferencePath(){return this.m_referencePath;}
	public void setReferencePath(HAPReferenceInfoAbsolute path){this.m_referencePath=path;}
	public HAPData getData(){return this.m_data;}
	public void setData(HAPData data){this.m_data=data;}
	public HAPWraper getWraper(){return this.m_wraper;}
	public void setWraper(HAPWraper wraper){this.m_wraper=wraper;}
	public HAPEntityDefinitionCritical getEntityDefinition(){return this.m_entityDefinition;}
	public void setEntityDefinition(HAPEntityDefinitionCritical def){this.m_entityDefinition=def;}
	public HAPAttributeDefinition getAttributeDefinition(){return this.m_attributeDefinition;}
	public void setAttributeDefinition(HAPAttributeDefinition attrDef){this.m_attributeDefinition=attrDef;}
	public String getEntityType(){return this.m_entityType;}
	public void setEntityType(String type){this.m_entityType=type;}
	public String getElementId(){return this.m_elementId;}
	public void setElementId(String id){this.m_elementId=id;}
	public String getQueryName(){return this.m_queryName;}
	public void setQueryName(String query){this.m_queryName=query;}
	public Map<String, String> getParms(){return this.m_parms;}
	public void setQueryEntityId(String queryEntityId){this.m_queryEntityId=queryEntityId;}
	public String getQueryEntityId(){ return this.m_queryEntityId; }
	public void setQueryPosition(int position){this.m_queryPosition=position;}
	public int getQueryPosition(){return this.m_queryPosition;}
	
	public List<HAPEntityOperationInfo> getEntityOperations(){return this.m_entityOperations;}
	public void addEntityOperation(HAPEntityOperationInfo operation){
		if(this.m_entityOperations==null)  this.m_entityOperations = new ArrayList<HAPEntityOperationInfo>();
		this.m_entityOperations.add(operation);
	}
	
	public HAPQueryEntityWraper getQueryEntityWraper(){return this.m_queryEntityWraper;}
	public void setQueryEntityWraper(HAPQueryEntityWraper queryEntityWraper){this.m_queryEntityWraper=queryEntityWraper;}
	
	public String getTransactionId(){return this.m_transactionId;}
	public void setTransactionId(String transactionId){this.m_transactionId=transactionId;}
	public String getValue(){return this.m_value;}
	public void setValue(String value){this.m_value=value;}
	public HAPEntityID getRefEntityID(){return this.m_refEntityID;}
	public void setRefEntityID(HAPEntityID ID){this.m_refEntityID=ID;}

	public Object getExtra(){return this.m_extra;}
	public void setExtra(Object data){this.m_extra=data;}
	
	public boolean equals(HAPEntityOperationInfo info){
		return this.m_operationId.equals(info);
	}
}

