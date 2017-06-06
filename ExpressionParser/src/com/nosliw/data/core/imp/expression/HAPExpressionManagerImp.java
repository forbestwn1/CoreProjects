package com.nosliw.data.core.imp.expression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.strvalue.io.HAPStringableEntityImporterJSON;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPExpressionParser;
import com.nosliw.data.core.expression.HAPExpressionTask;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.expression.HAPOperandConstant;
import com.nosliw.data.core.expression.HAPOperandReference;
import com.nosliw.data.core.expression.HAPOperandVariable;
import com.nosliw.data.core.expression.HAPProcessVariablesContext;
import com.nosliw.data.core.expression.HAPReferenceInfo;
import com.nosliw.data.core.expression.HAPVariableInfo;

public class HAPExpressionManagerImp implements HAPExpressionManager{

	private Map<String, HAPExpressionDefinition> m_expressionDefinitions;

	private Map<String, HAPExpression> m_expressions;
	
	private HAPExpressionParser m_expressionParser;
	
	private HAPDataTypeHelper m_dataTypeHelper;
	
	public HAPExpressionManagerImp(HAPExpressionParser expressionParser, HAPDataTypeHelper dataTypeHelper){
		this.m_expressionParser = expressionParser;
		this.m_dataTypeHelper = dataTypeHelper;
		this.init();
	}
	
	private void init(){
		String fileFolder = HAPFileUtility.getClassFolderPath(this.getClass()); 
		HAPValueInfoManager.getInstance().importFromFolder(fileFolder, false);

		this.m_expressionDefinitions = new LinkedHashMap<String, HAPExpressionDefinition>();
		this.m_expressions = new LinkedHashMap<String, HAPExpression>();
	}

	@Override
	public void registerExpressionDefinition(HAPExpressionDefinition expressionDefinition){
		String name = expressionDefinition.getName();
		if(HAPBasicUtility.isStringEmpty(name)){
			name = System.currentTimeMillis()+"";
			((HAPExpressionDefinitionImp)expressionDefinition).setName(name);
		}
		this.m_expressionDefinitions.put(name, expressionDefinition);
	}
	
	@Override
	public HAPExpressionDefinition getExpressionDefinition(String name) {		return this.m_expressionDefinitions.get(name);	}

	@Override
	public HAPExpression getExpression(String expressionName) {
		HAPExpression out = this.m_expressions.get(expressionName);
		if(out==null){
			out = this.processExpression(expressionName);
		}
		return out;
	}

	public void importExpressionFromFolder(String folder){
		Set<File> files = HAPFileUtility.getAllFiles(folder);
		for(File file : files){
			if(file.getName().endsWith(".expression")){
				try {
					InputStream inputStream = new FileInputStream(file);
			         HAPExpressionDefinitionImp expressionDefinition = (HAPExpressionDefinitionImp)HAPStringableEntityImporterJSON.parseJsonEntity(inputStream, HAPExpressionDefinitionImp._VALUEINFO_NAME, HAPValueInfoManager.getInstance());
			         this.registerExpressionDefinition(expressionDefinition);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private HAPExpression processExpression(String expressionName){
		System.out.println("***************** Start to process expression : " + expressionName);
		System.out.println("******* Parse expression");
		HAPExpressionImp expression = this.parseExpression(expressionName);

		//process reference
		System.out.println("******* Process reference");
		this.processReference(expression);
		
		//process reference
		System.out.println("******* Process reference variable mapping");
		this.processReferenceVarMappings(expression, new LinkedHashMap<String, String>());
		
		//process constant
		System.out.println("******* Process constant");
		this.processConstants(expression.getOperand(), expression.getExpressionDefinition());
		
		//discover variables
		Map<String, HAPVariableInfo> expectVariablesInfo = new LinkedHashMap<String, HAPVariableInfo>(); 
		HAPProcessVariablesContext context = new HAPProcessVariablesContext();
		expression.discover(expectVariablesInfo, null, context, this.getDataTypeHelper());
		
		if(!context.isSuccess()){
			System.out.println("******* Error");
			expression.addErrorMessages(context.getMessages());
		}
		this.m_expressions.put(expressionName, expression);
		return expression;
	}
	
	private HAPExpressionImp parseExpression(String expressionName){
		HAPExpressionDefinitionImp expressionDefinition = (HAPExpressionDefinitionImp)getExpressionDefinition(expressionName);
		HAPOperand expressionOperand = this.getExpressionParser().parseExpression(expressionDefinition.getExpression());
		return new HAPExpressionImp(expressionDefinition.clone(), expressionOperand);
	}

	/**
	 * Find all the reference
	 * Parse referenced expression
	 * Rename the variables 
	 */
	private void processReference(final HAPExpressionImp expression){
		HAPExpressionUtility.processAllOperand(expression.getOperand(), null, new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference reference = (HAPOperandReference)operand;
					String referenceName = reference.getExpressionReference();
					HAPReferenceInfo referenceInfo = expression.getExpressionDefinition().getReferences().get(referenceName);
					
					String refExpName = null;   //referenced expression name, by default, use referenceName as expression name
					Map<String, String> refVarMap = null;   //variable mapping from parent to reference expression
					if(referenceInfo!=null){
						refExpName = referenceInfo.getReference();
						if(refExpName==null)  refExpName = referenceName;
						refVarMap = referenceInfo.getVariablesMap();
						if(refVarMap==null)   refVarMap = new LinkedHashMap<String, String>();
					}
					else{
						refExpName = referenceName;
						refVarMap = new LinkedHashMap<String, String>();
					}
					
					if(expression.getReference(refExpName)==null){
						//parse referenced expression
						HAPExpressionImp refExpression = parseExpression(refExpName);
						expression.addReference(referenceName, refExpression);
						processReference(refExpression);
					}					
				}
				return true;
			}

			@Override
			public void postPross(HAPOperand operand, Object data) {
			}
		});		
	}

	private void processConstants(HAPOperand operand, final HAPExpressionDefinition expressionDefinition){
		HAPExpressionUtility.processAllOperand(operand, null, new HAPExpressionTask(){

			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference reference = (HAPOperandReference)operand;
					HAPExpression expression = reference.getExpression();
					processConstants(expression.getOperand(), expression.getExpressionDefinition());
				}
				else if(opType.equals(HAPConstant.EXPRESSION_OPERAND_CONSTANT)){
					HAPOperandConstant constantOperand = (HAPOperandConstant)operand;
					constantOperand.setData(expressionDefinition.getConstants().get(constantOperand.getName()));
				}
				return true;
			}

			@Override
			public void postPross(HAPOperand operand, Object data) {
			}
		});		
	}
	
	private void processReferenceVarMappings1(final HAPExpressionImp expression, Map<String, String> reversedVarMap) {
		//process variables in reference expression
		HAPExpressionUtility.processAllOperand(expression.getOperand(), reversedVarMap, new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				Map<String, String> reversedVarMap = (Map<String, String>)data;
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_VARIABLE)){
					//Replace all variable operand's var name to parent name according to mapping
					HAPOperandVariable variableChild = (HAPOperandVariable)operand;
					String mappedVarName = reversedVarMap.get(variableChild.getVariableName());
					if(mappedVarName!=null){
						variableChild.setVariableName(mappedVarName);
					}
				}
				return true;
			}

			@Override
			public void postPross(HAPOperand operand, Object data) {
			}});
		
		Map<String, HAPExpression> references = expression.getReferences();
		for(String referenceName : references.keySet()){
			HAPExpressionImp refExpression = (HAPExpressionImp)references.get(referenceName);
			
			//build childRefVarMap for child reference operand
			Map<String, String> childRefReversedVarMap = new LinkedHashMap<String, String>();
			Map<String, String> refVarMap = expression.getExpressionDefinition().getReferences().get(referenceName).getVariablesMap();   //variable mapping from parent to reference expression
			for(String r : refVarMap.keySet()){
				if(reversedVarMap.get(r)!=null)   childRefReversedVarMap.put(refVarMap.get(r), reversedVarMap.get(r));
				else  childRefReversedVarMap.put(refVarMap.get(r), r);
			}
			processReferenceVarMappings(refExpression, childRefReversedVarMap);
			
			//update variables definition info in reference expression
			Map<String, String> reverseRefVarMap = new LinkedHashMap<String, String>();
			for(String parentVar : refVarMap.keySet())		reverseRefVarMap.put(refVarMap.get(parentVar), parentVar);
			
			Map<String, HAPVariableInfo> originalVarInfos = refExpression.getVariables();
			Map<String, HAPVariableInfo> updatedVarInfos = new LinkedHashMap<String, HAPVariableInfo>();
			for(String originalVarName : originalVarInfos.keySet()){
				String mappedVarName = reverseRefVarMap.get(originalVarName);
				if(mappedVarName==null)  mappedVarName = originalVarName;
				updatedVarInfos.put(mappedVarName, originalVarInfos.get(originalVarName));
			}
			refExpression.setVariables(updatedVarInfos);
		}
	}
	
	
	/**
	 * Process reference variable mappings: 
	 * 		replace variables name in variable operand in reference expression according to mapping
	 * 		update varaible defintion info in reference expression
	 * @param expressionOperand
	 * @param varsMapping    childVar --- rootParentVar
	 * @param expressionDefinition
	 */
	private void processReferenceVarMappings(final HAPExpressionImp expression, Map<String, String> varsMapping) {
		HAPExpressionUtility.processAllOperand(expression.getOperand(), varsMapping, new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				Map<String, String> reversedVarMap = (Map<String, String>)data;
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference reference = (HAPOperandReference)operand;
					String referenceName = reference.getExpressionReference();
					HAPReferenceInfo referenceInfo = expression.getExpressionDefinition().getReferences().get(referenceName);
					
					String refExpName = null;   //referenced expression name, by default, use referenceName as expression name
					Map<String, String> refVarMap = null;   //variable mapping from parent to reference expression
					if(referenceInfo!=null){
						refExpName = referenceInfo.getReference();
						if(refExpName==null)  refExpName = referenceName;
						refVarMap = referenceInfo.getVariablesMap();
						if(refVarMap==null)   refVarMap = new LinkedHashMap<String, String>();
					}
					else{
						refExpName = referenceName;
						refVarMap = new LinkedHashMap<String, String>();
					}
					
					if(expression.getReference(refExpName)==null){
						//parse referenced expression
						HAPExpressionImp refExpression = parseExpression(refExpName);
						
						//build childRefVarMap for child reference operand
						Map<String, String> childRefReversedVarMap = new LinkedHashMap<String, String>();
						for(String r : refVarMap.keySet()){
							if(reversedVarMap.get(r)!=null)   childRefReversedVarMap.put(refVarMap.get(r), reversedVarMap.get(r));
							else  childRefReversedVarMap.put(refVarMap.get(r), r);
						}
						processReferenceVarMappings(refExpression, childRefReversedVarMap);
						
						//update variables definition info in reference expression
						Map<String, String> reverseRefVarMap = new LinkedHashMap<String, String>();
						for(String parentVar : refVarMap.keySet())		reverseRefVarMap.put(refVarMap.get(parentVar), parentVar);
						
						Map<String, HAPVariableInfo> originalVarInfos = refExpression.getVariables();
						Map<String, HAPVariableInfo> updatedVarInfos = new LinkedHashMap<String, HAPVariableInfo>();
						for(String originalVarName : originalVarInfos.keySet()){
							String mappedVarName = reverseRefVarMap.get(originalVarName);
							if(mappedVarName==null)  mappedVarName = originalVarName;
							updatedVarInfos.put(mappedVarName, originalVarInfos.get(originalVarName));
						}
						refExpression.setVariables(updatedVarInfos);
						
						expression.addReference(referenceName, refExpression);
					}
					return false;
				}
				else if(opType.equals(HAPConstant.EXPRESSION_OPERAND_VARIABLE)){
					//Replace all variable operand's var name to parent name according to mapping
					HAPOperandVariable variableChild = (HAPOperandVariable)operand;
					String mappedVarName = reversedVarMap.get(variableChild.getVariableName());
					if(mappedVarName!=null){
						variableChild.setVariableName(mappedVarName);
					}
				}
				return true;
			}

			@Override
			public void postPross(HAPOperand operand, Object data) {
			}});
	}
	
	protected HAPExpressionParser getExpressionParser(){		return this.m_expressionParser;	}
	protected HAPDataTypeHelper getDataTypeHelper(){   return this.m_dataTypeHelper;   }
}
