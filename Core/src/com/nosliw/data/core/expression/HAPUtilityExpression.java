package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.updatename.HAPUpdateNamePrefix;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentReference;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityDataComponent;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.dataassociation.HAPParserDataAssociation;

public class HAPUtilityExpression {

	public static Map<String, HAPData> getDataConstants(HAPDefinitionExpressionGroup expressionGroupDef, HAPContext context){
		Map<String, HAPData> out = new LinkedHashMap<String, HAPData>();
		for(HAPDefinitionConstant constantDef : getDataConstantsDefinition(expressionGroupDef, context)) {
			HAPData data = constantDef.getData();
			if(data!=null)	out.put(constantDef.getId(), data);
		}
		return out;
	}
	
	public static Set<HAPDefinitionConstant> getDataConstantsDefinition(HAPDefinitionExpressionGroup expressionGroupDef, HAPContext context){
		Set<HAPDefinitionConstant> out = null;
		out = expressionGroupDef.getConstantDefinitions();
		if(out==null) {
			//try to build constant from attachment and context
			if(expressionGroupDef instanceof HAPWithAttachment) {
				out = HAPUtilityDataComponent.buildDataConstantDefinition(((HAPWithAttachment)expressionGroupDef).getAttachmentContainer(), context);
			}
		}
		
		if(out==null)   throw new RuntimeException();
		return out;
	}

	public static HAPContext getContext(Object expressionGroupDef, HAPContext extraContext, HAPRuntimeEnvironment runtimeEnv) {
		return HAPUtilityComponent.getContext(expressionGroupDef, extraContext, HAPUtilityExpressionProcessConfigure.getContextProcessConfigurationForExpression(), runtimeEnv);
	}
	
	public static HAPUpdateName getUpdateExpressionVariableName(HAPExecutableExpressionGroup expression) {
		return new HAPUpdateNamePrefix(expression.getId()+"_");
	}
	
	public static String getBeforeUpdateName(HAPExecutableExpressionGroup expression, String name) {
		return name.substring((expression.getId()+"_").length());
	}

	//build reference definition from attachment
	public static Map<String, HAPDefinitionReference> buildReferenceDefinition(HAPOperandWrapper operand, HAPContainerAttachment attContainer) {
		Map<String, HAPDefinitionReference> out = new LinkedHashMap<String, HAPDefinitionReference>();
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					String refName = referenceOperand.getReferenceName();
					String referenceTo = null;
					String eleName = null;
					
					String[] segs = HAPNamingConversionUtility.splitTextByComponents(refName, "AT");
					if(segs.length==1) {
						referenceTo = segs[0];
					}
					else if(segs.length == 2) {
						referenceTo = segs[0];
						eleName = segs[1];
					}

					HAPDefinitionReference refDef = new HAPDefinitionReference();
					refDef.setName(refName);

					HAPAttachment refAttachment = null; 
					if(attContainer!=null)		refAttachment = attContainer.getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_EXPRESSION, referenceTo);
					if(refAttachment==null) {
						//not found in attachment
						throw new RuntimeException();
//						refDef.setResourceId(HAPUtilityResource.buildLocalReferenceResourceId(referenceTo));
//						refDef.setElementName(eleName);
//						refDef.setInputMapping(null);
					}
					else {
						//build ref definition from attachment
						//parse input mapping
						JSONObject adaptorJson = (JSONObject)refAttachment.getAdaptor();
						if(adaptorJson!=null)  refDef.setInputMapping(HAPParserDataAssociation.buildDefinitionByJson(adaptorJson.optJSONObject(HAPDefinitionReference.INPUTMAPPING)));
						else refDef.setInputMapping(null);
						
						if(refAttachment.getType().equals(HAPConstantShared.ATTACHMENT_TYPE_ENTITY)) {
							//solid reference
							refDef.setResourceId(HAPUtilityResourceId.buildLocalReferenceResourceId(referenceTo));
							refDef.setElementName(eleName);
						}
						else if(refAttachment.getType().equals(HAPConstantShared.ATTACHMENT_TYPE_REFERENCEEXTERNAL)||refAttachment.getType().equals(HAPConstantShared.ATTACHMENT_TYPE_REFERENCELOCAL)){
							refDef.setResourceId(((HAPAttachmentReference)refAttachment).getReferenceId());
							if(eleName==null && adaptorJson!=null) {
								//if no element in ref name, then try to find elename from adaptor
								eleName = (String)adaptorJson.opt(HAPDefinitionReference.ELEMENTNAME);
							}
							refDef.setElementName(eleName);
						}
					}
					
					out.put(refName, refDef);
				}
				return true;
			}
		});
		return out;
		
	}
	
	public static Map<String, HAPDefinitionReference> normalizeReferenceDefinition(HAPOperandWrapper operand, Map<String, HAPDefinitionReference> referenceDef) {
		Map<String, HAPDefinitionReference> out = new LinkedHashMap<String, HAPDefinitionReference>();
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					String refName = referenceOperand.getReferenceName();
					HAPDefinitionReference refDef = referenceDef.get(refName);
					if(refDef==null) {
						refDef = new HAPDefinitionReference();
						String referenceTo = null;
						String eleName = null;
						String[] segs = HAPNamingConversionUtility.parsePaths(refName);
						if(segs.length==1) {
							referenceTo = segs[0];
						}
						else if(segs.length == 2) {
							referenceTo = segs[0];
							eleName = segs[1];
						}
						
						refDef.setName(referenceTo);
						refDef.setElementName(eleName);
						refDef.setResourceId(HAPUtilityResourceId.buildLocalReferenceResourceId(refName));
						refDef.setInputMapping(null);
					}
					out.put(refName, refDef);
				}
				return true;
			}
		});
		return out;
	}

}
