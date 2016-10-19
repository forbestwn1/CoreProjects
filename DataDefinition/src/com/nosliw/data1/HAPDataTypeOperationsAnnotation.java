package com.nosliw.data1;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.data.datatype.HAPDataTypeInfoWithVersion;

/*
 * DataTypeOperations that all the operation infor is get from annotation of Operation Object
 * With this class implementaion, for the operation part of a data type, all needed is to defined a well annotated class
 */
public class HAPDataTypeOperationsAnnotation extends HAPDataTypeOperations{

	public HAPDataTypeOperationsAnnotation(Object operationObj, HAPDataTypeInfoWithVersion dataTypeInfo, HAPDataTypeManager dataTypeMan){
		super(dataTypeInfo, dataTypeMan);
		this.setOperationObject(operationObj);
		this.buildOperationInfos();
	}

	/*
	 * get all operation infos through annotations
	 */
	private void buildOperationInfos(){
		Method[] methods = this.getOperationObject().getClass().getMethods();
		
		for(Method method : methods){
			String name = method.getName();
			int i = name.lastIndexOf(HAPConstant.SEPERATOR_SURFIX);
			boolean isScriptDefMethod = i!=-1 && this.getDataTypeManager().getSupportedScripts().contains(name.substring(i+1));
			if(!isScriptDefMethod){
				//this method is for defining java operation
				Annotation[] annotations = method.getDeclaredAnnotations();
				for(Annotation annotation : annotations){
				    if(annotation instanceof HAPOperationInfoAnnotation){
				    	HAPOperationInfoAnnotation op = (HAPOperationInfoAnnotation) annotation;
				    	HAPDataTypeInfo outDataTypeInfo = HAPDataTypeInfo.build(op.out(), this.getDataTypeInfo()); 
				    	
				    	List<HAPDataTypeInfo> inDataTypeInfos = new ArrayList<HAPDataTypeInfo>();
				    	for(String inString : op.in()){
				    		inDataTypeInfos.add(HAPDataTypeInfo.build(inString, this.getDataTypeInfo()));
				    	}
				    	
				    	HAPDataOperationInfo opInfo = new HAPDataOperationInfo(this.getDataType(), name, inDataTypeInfos, outDataTypeInfo, op.description());
				    	this.addDataOperationInfo(opInfo);
				    	break;
				    }
				}		
			}
		}
		
		for(Method method : methods){
			String name = method.getName();
			int i = name.lastIndexOf(HAPConstant.SEPERATOR_SURFIX);
			boolean isScriptDefMethod = i!=-1 && this.getDataTypeManager().getSupportedScripts().contains(name.substring(i+1));
			if(isScriptDefMethod){
				//this method is for defining script
				try{
					//dependent data types
					Set<HAPDataTypeInfo> dependentDataTypeInfos = new HashSet<HAPDataTypeInfo>();
					Annotation[] annotations = method.getDeclaredAnnotations();
					for(Annotation annotation : annotations){
					    if(annotation instanceof HAPScriptOperationInfoAnnotation){
					    	HAPScriptOperationInfoAnnotation op = (HAPScriptOperationInfoAnnotation) annotation;
					    	for(String de : op.dependent()){
						    	HAPDataTypeInfo dependentDataTypeInfo = HAPDataTypeInfo.parseDataTypeInfo(de);
						    	if(dependentDataTypeInfo!=null){
						    		dependentDataTypeInfos.add(dependentDataTypeInfo);
						    	}
					    	}
					    }
					}
					
					//script infor
					String scriptName = name.substring(i+1);
					name = name.substring(0, i);
					Object script = method.invoke(this.getOperationObject());
					if(script instanceof String){
						this.addOperationScript(name, scriptName, (String)script, dependentDataTypeInfos);
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}
