package com.nosliw.entity.persistent.xmlfile;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Modifier;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.entity.data.HAPDataWraper;
import com.nosliw.entity.data.HAPDataWraperTask;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.data.HAPReferenceInfoAbsolute;
import com.nosliw.entity.data.HAPReferenceWraper;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;
import com.nosliw.entity.utils.HAPEntityDataTypeUtility;
import com.nosliw.entity.utils.HAPEntityDataUtility;

public abstract class HAPXMLEntityLoader extends HAPEntityLoaderImp{

	public HAPXMLEntityLoader(){
		super();
	}
	
	@Override
	public void clear(){}
	
	@Override
	public void load(){	}

	abstract protected String getFileNameByEntity(HAPEntityWraper entityWraper);
	
	@Override
	public void removeEntityFromPersist(HAPEntityWraper entityWraper){
		String fileName = this.getFileNameByEntity(entityWraper);
		File file = new File(fileName);
		file.delete();
	}

	@Override
	public void persistEntity(HAPEntityWraper entityWraper){
		String fileName = this.getFileNameByEntity(entityWraper);
		
		File theDir = new File(fileName).getParentFile();
		if (!theDir.exists()){
			theDir.mkdir();
		}
		
		HAPXMLEntityExporter.writeData(entityWraper, fileName);		
	}
	
	@Override
	public String getName() {
		return null;
	}

	@Override
	public String toString()
	{
		Gson gson = new GsonBuilder()
	    	.setPrettyPrinting()
	    	.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE)	    	
	    	.disableHtmlEscaping()
	    	.create();		
		
		String json = gson.toJson(this); // serializes target to Json
		return json;
	}

	
	@Override
	public void afterLoad(){
		
		HAPEntityDataUtility.iterateAllEntity(this, null, new HAPDataWraperTask(){
			@Override
			public HAPServiceData process(HAPDataWraper wraper, Object data) {
				HAPServiceData out = HAPServiceData.createSuccessData();
				if(HAPEntityDataTypeUtility.isReferenceType(wraper)){
					HAPReferenceWraper refWraper = (HAPReferenceWraper)wraper;
					if(!refWraper.isEmpty()){
						getReferenceManager().addParentReference(refWraper);
						out = HAPServiceData.createFailureData();
					}
				}
				else if(HAPEntityDataTypeUtility.isAtomType(wraper)){
					out = HAPServiceData.createFailureData();
				}
				return out;
			}

			@Override
			public Object afterProcess(HAPDataWraper wraper, Object data) {
				return null;
			}
			
		});
		
		
		//set full id for each entity
		/*
		for(HAPEntityWraper entityWraper : this.m_allEntities){
			HAPValueUtility.iterateEntityWraper(entityWraper, null, new HAPDataWraperTask(){
				@Override
				public HAPServiceData process(HAPDataWraper wraper, Object data) {
					if(HAPUtility.isSolidEntityType(wraper)){
						HAPEntityWraper complexWraper = (HAPEntityWraper)wraper;
						HAPEntityData complexEntity = complexWraper.getEntityData();
						HAPEntityData parentEntity = wraper.getParentEntity();
						HAPAttributeDefinition attrDef = wraper.getAttributeDefinition();
						if(parentEntity == null){
							complexWraper.setID(complexEntity.getEntityName() + ":" + complexWraper.getId());
							return HAPServiceData.createSuccessData(data);
						}
						else if(HAPUtility.isContainerType(attrDef.getDataType())){
							complexWraper.setID(parentEntity.getWraper().getID()+":"+attrDef.getName()+":"+complexWraper.getId());
						}
						else{
							complexWraper.setID(parentEntity.getWraper().getID()+":"+attrDef.getName());
						}
						return HAPServiceData.createSuccessData(data);
					}
					else if(HAPUtility.isContainerType(wraper)){
						return HAPServiceData.createSuccessData(data);
					}
					return HAPServiceData.createFailureData(data, "");
				}

				@Override
				public Object afterProcess(HAPDataWraper wraper, Object data) {
					return data;
				}
			});
		}
		*/

//		for(HAPEntityWraper entity : this.m_allEntities)
//		{
//			this.addToGroup(entity);
//		}
		
	}
	
	/*
	private void setAllDataTypePath(){
		String[] groups = this.getGroups();
		for(String group : groups){
			HAPServiceData data = this.getEntitiesByGroup(group);
			if(data.isSuccess()){
				HAPEntityValueWraper[] wrapers = (HAPEntityValueWraper[])data.getSuccessData();
				for(HAPEntityValueWraper wraper : wrapers){
					this.setDataTypePath(wraper);
				}
			}
		}
	}
	
	private void setDataTypePath(HAPEntityValueWraper wraper){
		if(wraper.isEmpty())  return;
		
		HAPEntity entity = wraper.getEntityValue();
		
		if(wraper.getType().getType().equals("common.datatype")){
			((HAPDataTypeEntity)entity).createPath("", entity.getID());
		}
		else{
			String[] attributes = entity.getAttributes();
			for(String attr : attributes){
				HAPValueWraper attrWraper = entity.getAttributeValueWraper(attr);
				
				if(HAPUtility.isContainerType(attrWraper.getType())){
					HAPContainerValueWraper containerWraper = (HAPContainerValueWraper)attrWraper;
					Iterator<HAPValueWraper> it = containerWraper.iterator();
					while(it.hasNext()){
						HAPValueWraper eleWraper = it.next();
						if(HAPUtility.isEntityType(eleWraper.getType())){
							if(eleWraper instanceof HAPEntityValueWraper){
								this.setDataTypePath((HAPEntityValueWraper)eleWraper);
							}
						}
					}
				}
				else if(HAPUtility.isEntityType(attrWraper.getType())){
					if(attrWraper instanceof HAPEntityValueWraper){
						this.setDataTypePath((HAPEntityValueWraper)attrWraper);
					}
				}
			}
		}
	}
	*/
	
	public String toJson() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
//		for(String group : this.m_groupedEntities.keySet()){
//			Map<String, HAPEntityWraper> groupEntitys = this.m_groupedEntities.get(group);
//			for(String entityId : groupEntitys.keySet()){
//				sb.append("\n\""+entityId+"\"");
//				sb.append(":");
//				sb.append(groupEntitys.get(entityId).toStringValue("json"));
//				sb.append(",");
//			}
//		}
//		sb.append("}");
		return sb.toString();
	}
	
	@Override
	public HAPEntityWraper parseString(String content, String format){
		HAPEntityWraper out = null;
		try{
			JSONObject jsonObj = new JSONObject(content);
//			out = HAPDataImporter.parseEntityWraperJson(jsonObj);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return out;
	}

	public void readInputStream(InputStream stream)
	{
		InputStream[] streams = {stream}; 
		HAPEntityWraper[] entities = HAPXMLEntityImporter.readEntities(streams);
		for(HAPEntityWraper entity : entities){
			entity.setDataAccess(this);
//			this.getEntityNormalContainer().addEntity(entity);
		}
	}
}
