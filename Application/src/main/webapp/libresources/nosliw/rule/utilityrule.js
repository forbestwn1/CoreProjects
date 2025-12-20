//get/create package
var packageObj = library;    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONATRIBUTECONSTANT;
var node_COMMONCONSTANT;
var node_TaskResult;

//*******************************************   Start Node Definition  **************************************

var node_ruleUtility = function(){

	var loc_out = {
        
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

//Register Node by Name
packageObj.createChildNode("ruleUtility", node_ruleUtility); 

})(packageObj);
