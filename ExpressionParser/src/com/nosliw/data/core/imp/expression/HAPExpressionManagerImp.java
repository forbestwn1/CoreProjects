package com.nosliw.data.core.imp.expression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.io.HAPStringableEntityImporterJSON;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSuite;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPExpressionParser;
import com.nosliw.data.core.expression.HAPOperandTask;
import com.nosliw.data.core.expression.HAPOperandVariable;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.expression.HAPOperandConstant;
import com.nosliw.data.core.expression.HAPOperandReference;
import com.nosliw.data.core.expression.HAPProcessVariablesContext;
import com.nosliw.data.core.expression.HAPReferenceInfo;
import com.nosliw.data.core.expression.HAPVariableInfo;

public class HAPExpressionManagerImp implements HAPExpressionManager{

	private Map<String, HAPExpressionDefinitionSuiteImp> m_expressionDefinitionSuites;

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
		this.m_expressionDefinitionSuites = new LinkedHashMap<String, HAPExpressionDefinitionSuiteImp>();
		this.m_idIndex = 1;
	}

	@Override
	public HAPExpressionDefinitionSuite getExpressionDefinitionSuite(String suiteName){		return this.m_expressionDefinitionSuites.get(suiteName);	}
	
	@Override
	public Set<String> getExpressionDefinitionSuites() {		return this.m_expressionDefinitionSuites.keySet();	}
	
	@Override
	public void addExpressionDefinitionSuite(HAPExpressionDefinitionSuite expressionDefinitionSuite){
		HAPExpressionDefinitionSuiteImp suite = (HAPExpressionDefinitionSuiteImp)this.getExpressionDefinitionSuite(expressionDefinitionSuite.getName());
		if(suite==null){
			this.m_expressionDefinitionSuites.put(expressionDefinitionSuite.getName(), (HAPExpressionDefinitionSuiteImp)expressionDefinitionSuite);
		}
		else{
			suite.merge((HAPExpressionDefinitionSuiteImp)expressionDefinitionSuite);
		}
	}
	
	@Override
	public HAPExpressionDefinition getExpressionDefinition(String suite, String name) {		return this.getExpressionDefinitionSuite(suite).getExpressionDefinition(name);	}

	@Override
	public HAPExpression processExpression(String suite, String expressionName, Map<String, HAPDataTypeCriteria> variableCriterias){
		System.out.println("***************** Start to process expression : " + expressionName);
		System.out.println("******* Parse expression");
		HAPExpressionImp expression = this.parseExpressionDefinition(suite, expressionName);

		//set expression name, every expression instance has a unique name
		expression.setId(expressionName + "_no" + this.m_idIndex++);
		
		//discover variables
		this.discoverVariables(expression);
		
		//process reference
		System.out.println("******* Process reference");
		this.processReference(suite, expression, null);
		
		//process constant
		System.out.println("******* Process constant");
		this.processConstants(expression);
		
		//discover variables
		HAPProcessVariablesContext context = new HAPProcessVariablesContext();
		Map<String, HAPVariableInfo> parentVariableInfos = new LinkedHashMap<String, HAPVariableInfo>();
		if(variableCriterias!=null){
			for(String varName : variableCriterias.keySet())		parentVariableInfos.put(varName, new HAPVariableInfo(variableCriterias.get(varName)));
		}
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
			         HAPExpressionDefinitionSuiteImp expressionDefinitionSuite = (HAPExpressionDefinitionSuiteImp)HAPStringableEntityImporterJSON.parseJsonEntity(inputStream, HAPExpressionDefinitionSuiteImp._VALUEINFO_NAME, HAPValueInfoManager.getInstance());
			         this.addExpressionDefinitionSuite(expressionDefinitionSuite);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void discoverVariables(HAPExpressionImp expression){
		//process all child references
		HAPExpressionUtility.processAllOperand(expression.getOperand(), expression, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_VARIABLE)){
					HAPOperandVariable variableOperand = (HAPOperandVariable)operand;
					HAPExpressionImp expression = (HAPExpressionImp)data;
					Map<String, HAPVariableInfo> varsInfo = expression.getLocalVarsInfo();
					if(varsInfo.get(variableOperand.getVariableName())==null){
						varsInfo.put(variableOperand.getVariableName(), null);
					}
				}
				return true;
			}
		});		
	}

	
	/**
	 * Process all references in expression
	 * Update variables in expression
	 * All the operation above also operate on child referenced expression
	 */
	private void processReference(final String suite, final HAPExpressionImp expression, Map<String, String> varMapping){
		//process all child references
		HAPExpressionUtility.processAllOperand(expression.getOperand(), null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand;
					String referenceName = referenceOperand.getExpressionReference();
					HAPReferenceInfo referenceInfo = expression.getExpressionDefinition().getReferences().get(referenceName);
					
					String refExpName = null;   //referenced expression name, by default, use referenceName as expression name
					if(referenceInfo!=null)		refExpName = referenceInfo.getReference();
					if(refExpName==null)  refExpName = referenceName;
					
					HAPExpressionImp refExpression = (HAPExpressionImp)expression.getReference(refExpName);
					if(refExpression==null){
						//if referenced expression has not been processed, parse it
						refExpression = parseExpressionDefinition(suite, refExpName);
						discoverVariables(refExpression);
						expression.addReference(referenceName, refExpression);
						
						Map<String, String> refVarMap = new LinkedHashMap<String, String>();   //variable mapping from parent to reference expression
						if(referenceInfo!=null){
							refVarMap = referenceInfo.getVariablesMap();
						}
						
						processReference(suite, refExpression, HAPBasicUtility.reverseMapping(refVarMap));
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
	private HAPExpressionImp parseExpressionDefinition(String suite, String expressionName){
		HAPExpressionDefinitionImp expressionDefinition = (HAPExpressionDefinitionImp)getExpressionDefinition(suite, expressionName);
		HAPOperand expressionOperand = this.getExpressionParser().parseExpression(expressionDefinition.getExpression());
		//add cloned definition to expression
		HAPExpressionImp out = new HAPExpressionImp(expressionDefinition.clone(), expressionOperand);
		
		//process preference info in definition to add reference name to mapped variable name
		HAPExpressionDefinitionImp expDef = (HAPExpressionDefinitionImp)out.getExpressionDefinition();
		Map<String, HAPReferenceInfo> expReferences = expDef.getReferences();
		for(String ref : expReferences.keySet()){
			HAPReferenceInfoImp refInfo = (HAPReferenceInfoImp)expReferences.get(ref);
			Map<String, String> newVarMapping = new LinkedHashMap<String, String>();
			Map<String, String> varMapping = refInfo.getVariablesMap();
			for(String varName : varMapping.keySet()){
				newVarMapping.put(varName, HAPExpressionUtility.buildFullVariableName(ref, varMapping.get(varName)));
			}
			refInfo.setVariableMap(newVarMapping);
		}
		
		return out;
	}

	private void processConstants(final HAPExpression expression){
		HAPExpressionUtility.processAllOperand(expression.getOperand(), expression.getExpressionDefinition(), new HAPOperandTask(){
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
