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
			out = this.processExpressionDefinition(expressionName);
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
	
	private HAPExpression processExpressionDefinition(String expressionName){
		System.out.println("***************** Start to process expression : " + expressionName);
		System.out.println("******* Parse expression");
		HAPExpressionImp expression = this.parseExpressionDefinition(expressionName);

		//process reference
		System.out.println("******* Process reference");
		this.processReference(expression);
		
		//process reference, replace the variable name in referenced expression with the name from parent/root
		System.out.println("******* Process reference variable mapping");
		this.processReferenceVarMappings(expression, new LinkedHashMap<String, String>());
		
		//process constant
		System.out.println("******* Process constant");
		this.processConstants(expression);
		
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
	
	//parse expression definition according to its name
	private HAPExpressionImp parseExpressionDefinition(String expressionName){
		HAPExpressionDefinitionImp expressionDefinition = (HAPExpressionDefinitionImp)getExpressionDefinition(expressionName);
		HAPOperand expressionOperand = this.getExpressionParser().parseExpression(expressionDefinition.getExpression());
		//add cloned definition to expression
		return new HAPExpressionImp(expressionDefinition.clone(), expressionOperand);
	}

	/**
	 * Process reference variable mappings: 
	 * 		replace variables name in variable operand in reference expression according to mapping
	 * @param expressionOperand
	 * @param varsMapping    childVar --- rootParentVar
	 * @param expressionDefinition
	 */
	private void processReferenceVarMappings(final HAPExpressionImp expression, Map<String, String> reversedVarMap) {
		//update variables' name in expression
		expression.updateVariablesName(reversedVarMap);

		//process referenced expression
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
		}
	}
	
	/**
	 * Find all the reference
	 * Parse referenced expression
	 * Rename variables by append parent name to variables in referred expression (when add reference to expression) 
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
						refVarMap = referenceInfo.getVariablesMap();
					}
					if(refExpName==null)  refExpName = referenceName;
					if(refVarMap==null)   refVarMap = new LinkedHashMap<String, String>();
					
					if(expression.getReference(refExpName)==null){
						//if referenced expression has not been processed, parse it
						HAPExpressionImp refExpression = parseExpressionDefinition(refExpName);
						expression.addReference(referenceName, refExpression);
						processReference(refExpression);
					}					
				}
				return true;
			}
		});		
	}

	private void processConstants(final HAPExpression expression){
		HAPExpressionUtility.processAllOperand(expression.getOperand(), null, new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_CONSTANT)){
					HAPOperandConstant constantOperand = (HAPOperandConstant)operand;
					constantOperand.setData(expression.getExpressionDefinition().getConstants().get(constantOperand.getName()));
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
