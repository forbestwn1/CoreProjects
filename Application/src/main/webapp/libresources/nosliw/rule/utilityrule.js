//get/create package
var packageObj = library;    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONATRIBUTECONSTANT;
var node_COMMONCONSTANT;
var node_TaskResult;
var node_variableUtility;
var node_pathUtility;

//*******************************************   Start Node Definition  **************************************

var node_createRuleInfoItem = function(ruleDef){
	return {
		ruleDef : ruleDef
	};
};

var node_ruleUtility = function(){

    var loc_getRuleDefinitionsFromVariable = function(variable){
     	var definition = variable.prv_info==undefined?undefined:variable.prv_info.definition;
	    if(definition!=undefined){
		    var dataDefinition;
			
		    var defType = definition[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_TYPE];
    		if(defType==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_DATA){
	   			dataDefinition = definition[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DATA];
	    	}
		    else if(defType==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE&&definition[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_INHERITDEFINITION]==false){
                dataDefinition = definition[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DEFINITION][node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DATA];
	   		}
			
	    	if(dataDefinition!=undefined){
	        	return dataDefinition[node_COMMONATRIBUTECONSTANT.DATADEFINITION_RULE];
    		}
	    }
    };

    var loc_collectRuleInfo = function(variable, path, allRuleInfo){
   			var dataRuleDefs = loc_getRuleDefinitionsFromVariable(variable);
   			
   			_.each(dataRuleDefs, function(dataRuleDef, i){
				var rulePath = dataRuleDef[node_COMMONATRIBUTECONSTANT.DEFINITIONDATARULE_PATH];
				var comparePath = node_pathUtility.comparePath(rulePath, path);
                if(comparePath.compare==0){
     				allRuleInfo.push(node_createRuleInfoItem(dataRuleDef));
   				}
			});	
			
			var childrenVars = variable.prv_getChildren();
            _.each(childrenVars, function(childVarInfo, id){
    			 var compareWithChildPath = node_pathUtility.comparePath(path, childVarInfo.path);
				 if(compareWithChildPath.compare==0){
					loc_collectRuleInfo(childVarInfo.variable, undefined, allRuleInfo);
				 }
				 else if(compareWithChildPath.compare==1){
					loc_collectRuleInfo(childVarInfo.variable, compareWithChildPath.subPath, allRuleInfo);
				 }
			});

	};

	var loc_out = {

        collectRuleInfo : function(variable){
			var allRuleInfo = [];
			var toRootPathInfo = node_variableUtility.toBaseVariableConvertInfo(node_variableUtility.getVariable(variable));
			loc_collectRuleInfo(toRootPathInfo.getBaseVariable(), toRootPathInfo.getPath(), allRuleInfo);
			return allRuleInfo;
		},
        
        getRuleDefinitionsFromVariable : function(variable){
			return loc_getRuleDefinitionsFromVariable(variable);
    	},
        
        createRuleValidationSuccessResult : function(){
			return new node_TaskResult(node_COMMONCONSTANT.TASK_RESULT_SUCCESS);
		},

        createRuleValidationFailResult : function(failData){
			return new node_TaskResult(node_COMMONCONSTANT.TASK_RESULT_FAIL, failData);
		},
		
		isRuleValidationSuccess : function(result){
			return result.resultName == node_COMMONCONSTANT.TASK_RESULT_SUCCESS;
		}
		
	};
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("task.TaskResult", function(){node_TaskResult = this.getData();});
nosliw.registerSetNodeDataEvent("variable.variable.variableUtility", function(){node_variableUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.path.pathUtility", function(){node_pathUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("ruleUtility", node_ruleUtility); 

})(packageObj);
