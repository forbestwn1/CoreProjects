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
import com.nosliw.data.core.expression.HAPExpressionInfo;
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

	private Map<String, HAPExpressionInfo> m_expressionInfos;

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

		this.m_expressionInfos = new LinkedHashMap<String, HAPExpressionInfo>();
		this.m_expressions = new LinkedHashMap<String, HAPExpression>();
	}

	@Override
	public void registerExpressionInfo(HAPExpressionInfo expressionInfo){
		String name = expressionInfo.getName();
		if(HAPBasicUtility.isStringEmpty(name)){
			name = System.currentTimeMillis()+"";
			((HAPExpressionInfoImp)expressionInfo).setName(name);
		}
		this.m_expressionInfos.put(name, expressionInfo);
	}
	
	@Override
	public HAPExpressionInfo getExpressionInfo(String name) {		return this.m_expressionInfos.get(name);	}

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
			         HAPExpressionInfoImp expressionInfo = (HAPExpressionInfoImp)HAPStringableEntityImporterJSON.parseJsonEntity(inputStream, HAPExpressionInfoImp._VALUEINFO_NAME, HAPValueInfoManager.getInstance());
			         this.registerExpressionInfo(expressionInfo);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private HAPExpression processExpression(String expressionName){
		HAPExpressionImp expression = this.buildExpression(expressionName);

		System.out.println("***************** Start to process expression : " + expressionName);
		
		//process reference
		System.out.println("******* Process reference");
		this.processReferences(expression.getOperand(), new LinkedHashMap<String, String>(), expression.getExpressionInfo());
		
		//process constant
		System.out.println("******* Process constant");
		this.processConstants(expression.getOperand(), expression.getExpressionInfo());
		
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
	
	private HAPExpressionImp buildExpression(String expressionName){
		HAPExpressionInfo expressionInfo = getExpressionInfo(expressionName);
		HAPOperand expressionOperand = this.getExpressionParser().parseExpression(expressionInfo.getExpression());
		HAPExpressionImp expression = new HAPExpressionImp(expressionInfo, expressionOperand);
		return expression;
	}

	private void processConstants(HAPOperand operand, final HAPExpressionInfo expressionInfo){
		HAPExpressionUtility.processAllOperand(operand, null, new HAPExpressionTask(){

			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference reference = (HAPOperandReference)operand;
					HAPExpression expression = reference.getExpression();
					processConstants(expression.getOperand(), expression.getExpressionInfo());
				}
				else if(opType.equals(HAPConstant.EXPRESSION_OPERAND_CONSTANT)){
					HAPOperandConstant constantOperand = (HAPOperandConstant)operand;
					constantOperand.setData(expressionInfo.getConstants().get(constantOperand.getName()));
				}
				return true;
			}

			@Override
			public void postPross(HAPOperand operand, Object data) {
			}
		});		
	}
	
	// varsMapping  childVar --- parentVar
	private void processReferences(HAPOperand expressionOperand, Map<String, String> varsMapping, final HAPExpressionInfo expressionInfo) {
		HAPExpressionUtility.processAllOperand(expressionOperand, varsMapping, new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				Map<String, String> reversedVarMap = (Map<String, String>)data;
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference reference = (HAPOperandReference)operand;
					String referenceName = reference.getExpressionName();
					HAPReferenceInfo referenceInfo = expressionInfo.getReferences().get(referenceName);
					
					String refExpName = null;
					Map<String, String> refVarMap = null;
					if(referenceInfo!=null){
						refExpName = referenceInfo.getReference();
						if(refExpName==null)  refExpName = referenceName;
						refVarMap = referenceInfo.getVariableSMap();
						if(refVarMap==null)   refVarMap = new LinkedHashMap<String, String>();
					}
					else{
						refExpName = referenceName;
						refVarMap = new LinkedHashMap<String, String>();
					}
					
					HAPExpressionImp refExpression = buildExpression(refExpName);
					
					//build childRefVarMap for child reference operand
					Map<String, String> childRefReversedVarMap = new LinkedHashMap<String, String>();
					for(String r : refVarMap.keySet()){
						if(reversedVarMap.get(r)!=null)   childRefReversedVarMap.put(refVarMap.get(r), reversedVarMap.get(r));
						else  childRefReversedVarMap.put(refVarMap.get(r), r);
					}
					processReferences(refExpression.getOperand(), childRefReversedVarMap, refExpression.getExpressionInfo());
					
					//update variables info in reference expression
					Map<String, String> reverseRefVarMap = new LinkedHashMap<String, String>();
					for(String parent : refVarMap.keySet())		reverseRefVarMap.put(refVarMap.get(parent), parent);
					
					Map<String, HAPVariableInfo> originalVarInfos = refExpression.getVariables();
					Map<String, HAPVariableInfo> updatedVarInfos = new LinkedHashMap<String, HAPVariableInfo>();
					for(String originalVarName : originalVarInfos.keySet()){
						String mappedVarName = reverseRefVarMap.get(originalVarName);
						if(mappedVarName==null)  mappedVarName = originalVarName;
						updatedVarInfos.put(mappedVarName, originalVarInfos.get(originalVarName));
					}
					refExpression.setVariables(updatedVarInfos);
					
					reference.setExpression(refExpression);
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
