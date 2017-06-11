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
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPExpressionParser;
import com.nosliw.data.core.expression.HAPExpressionTask;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.expression.HAPOperandConstant;
import com.nosliw.data.core.expression.HAPOperandReference;
import com.nosliw.data.core.expression.HAPProcessVariablesContext;
import com.nosliw.data.core.expression.HAPReferenceInfo;
import com.nosliw.data.core.expression.HAPVariableInfo;

public class HAPExpressionManagerImp implements HAPExpressionManager{

	private Map<String, HAPExpressionDefinition> m_expressionDefinitions;

	private HAPExpressionParser m_expressionParser;
	
	private HAPDataTypeHelper m_dataTypeHelper;
	
	private int m_idIndex;
	
	public HAPExpressionManagerImp(HAPExpressionParser expressionParser, HAPDataTypeHelper dataTypeHelper){
		this.m_expressionParser = expressionParser;
		this.m_dataTypeHelper = dataTypeHelper;
		this.init();
	}
	
	private void init(){
		String fileFolder = HAPFileUtility.getClassFolderPath(this.getClass()); 
		HAPValueInfoManager.getInstance().importFromFolder(fileFolder, false);
		this.m_expressionDefinitions = new LinkedHashMap<String, HAPExpressionDefinition>();
		this.m_idIndex = 1;
	}

	@Override
	public void registerExpressionDefinition(HAPExpressionDefinition expressionDefinition){
		String name = expressionDefinition.getName();
		if(HAPBasicUtility.isStringEmpty(name)){
			name = System.currentTimeMillis()+"";
			((HAPExpressionDefinitionImp)expressionDefinition).setName(name);
		}
		
		//update mapping variable name in "to" part, 
		Map<String, HAPReferenceInfo> references = expressionDefinition.getReferences();
		for(String referenceName : references.keySet()){
			Map<String, String> oldVarMap = references.get(referenceName).getVariablesMap();
			Map<String, String> newVarMap = new LinkedHashMap<String, String>();
			for(String fromVar : oldVarMap.keySet()){
				String toVar = oldVarMap.get(fromVar);
				toVar = HAPExpressionUtility.updateVaraible(referenceName, toVar);
				newVarMap.put(fromVar, toVar);
			}
		}
		
		this.m_expressionDefinitions.put(name, expressionDefinition);
	}
	
	@Override
	public HAPExpressionDefinition getExpressionDefinition(String name) {		return this.m_expressionDefinitions.get(name);	}

	@Override
	public HAPExpression processExpression(String expressionName, Map<String, HAPDataTypeCriteria> variableCriterias){
		System.out.println("***************** Start to process expression : " + expressionName);
		System.out.println("******* Parse expression");
		HAPExpressionImp expression = this.parseExpressionDefinition(expressionName);

		//set expression name
		expression.setName(expressionName + this.m_idIndex++);
		
		//process reference
		System.out.println("******* Process reference");
		this.processReference(expression, null);
		
		//process constant
		System.out.println("******* Process constant");
		this.processConstants(expression);
		
		//discover variables
		HAPProcessVariablesContext context = new HAPProcessVariablesContext();
		Map<String, HAPVariableInfo> parentVariableInfos = new LinkedHashMap<String, HAPVariableInfo>();
		for(String varName : variableCriterias.keySet())		parentVariableInfos.put(varName, new HAPVariableInfo(variableCriterias.get(varName)));
		expression.discover(parentVariableInfos, null, context, this.getDataTypeHelper());
		
		if(!context.isSuccess()){
			System.out.println("******* Error");
			expression.addErrorMessages(context.getMessages());
		}
		return expression;
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

	/**
	 * Process all references in expression
	 * Update variables in expression
	 * All the operation above also operate on child referenced expression
	 */
	private void processReference(final HAPExpressionImp expression, Map<String, String> varMapping){
		//process all child references
		HAPExpressionUtility.processAllOperand(expression.getOperand(), null, new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand;
					String referenceName = referenceOperand.getExpressionReference();
					HAPReferenceInfo referenceInfo = expression.getExpressionDefinition().getReferences().get(referenceName);
					
					String refExpName = null;   //referenced expression name, by default, use referenceName as expression name
					Map<String, String> refVarMap = null;   //variable mapping from parent to reference expression
					if(referenceInfo!=null){
						refExpName = referenceInfo.getReference();
						refVarMap = referenceInfo.getVariablesMap();
					}
					if(refExpName==null)  refExpName = referenceName;
					
					HAPExpressionImp refExpression = (HAPExpressionImp)expression.getReference(refExpName);
					if(refExpression==null){
						//if referenced expression has not been processed, parse it
						refExpression = parseExpressionDefinition(refExpName);
						expression.addReference(referenceName, refExpression);
						processReference(refExpression, HAPBasicUtility.reverseMapping(refVarMap));
					}
					referenceOperand.setExpression(refExpression);
				}
				return true;
			}
		});		
		
		//update variables in current expression
		if(varMapping!=null){
			expression.updateVariablesName(varMapping);
		}
	}
	
	//parse expression definition according to its name
	private HAPExpressionImp parseExpressionDefinition(String expressionName){
		HAPExpressionDefinitionImp expressionDefinition = (HAPExpressionDefinitionImp)getExpressionDefinition(expressionName);
		HAPOperand expressionOperand = this.getExpressionParser().parseExpression(expressionDefinition.getExpression());
		//add cloned definition to expression
		return new HAPExpressionImp(expressionDefinition.clone(), expressionOperand);
	}

	private void processConstants(final HAPExpression expression){
		HAPExpressionUtility.processAllOperand(expression.getOperand(), expression.getExpressionDefinition(), new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_CONSTANT)){
					HAPOperandConstant constantOperand = (HAPOperandConstant)operand;
					HAPExpressionDefinition expressionDefinition = (HAPExpressionDefinition)data;
					constantOperand.setData(expressionDefinition.getConstants().get(constantOperand.getName()));
				}
				return true;
			}
		});	
		
		//process constant in referenced expression
		Map<String, HAPExpression> references = expression.getReferences();
		for(String referenceName : references.keySet()){
			HAPExpressionImp refExpression = (HAPExpressionImp)references.get(referenceName);
			processConstants(refExpression);
		}
	}
	
	protected HAPExpressionParser getExpressionParser(){		return this.m_expressionParser;	}
	protected HAPDataTypeHelper getDataTypeHelper(){   return this.m_dataTypeHelper;   }
}
