//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_getComponentLifecycleInterface;
	var node_basicUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_createVariableManager = function(){
	
	
	
	var loc_out = {
		
	};
	
	return loc_out;
	
};

//variable domain, it response to value structure domain in complex resource
var nod_createVariableDomain = function(valueDomainDef){

	//value structure definition domain
	var loc_valueDomainDef = valueDomainDef;
	
	var loc_rootGroupId;
	
	var loc_variableGroupById = {};
	
	var loc_out = {
		//create variable domain according
		//value structure complex id
		//parent domain
		//return domain id
		creatVariableGroup : function(valueStructureComplexId, parentDomain){
			return groupId;
		},
		
		getVariableValue : function(groupId, variableId){
			
		},
		
		setVariableValue : function(groupId, variableId, value){
			
		}
		
	};
	
	return loc_out;
};

//variable group responding to value structure complex
//valueStructureDefs all value structures from complex
//it has parent group, so that some variable is from parent
var nod_createVariableGroup = function(valueStructureDefs, parentGroup){
	
	//parent domain which some variable can get from
	var loc_parentGroup = parentGroup;
	
	//variables in this domain
	var loc_variablesById = {};
	
	var loc_init = function(valueStructureDefs){
		
	};
	
	var loc_out = {
		
		getVariableInfo : function(variableId){
			
		},
		
		
	};
	
	loc_init(valueStructureDefs);
	return loc_out;
};

//variable info
var node_createVariableInfo = function(variableId){
	
	//variable id responding to variable id defined in value structure defintion
	var loc_variableId = variableId;
	
	//current value for variable
	var loc_value;
	
	var loc_usedCount = 1;
	
	var loc_out = {
		
		getVariableId : function(){   return loc_variableId;    },
	
		getValue : function(){   return loc_value;    },
		
		setValue : function(value){  loc_value = value;    },
		
		use : function(){   loc_usedCount++;   },
		
		unUse : function(){  loc_usedCount--;  }
	};
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("componentUtility", node_componentUtility); 

})(packageObj);
