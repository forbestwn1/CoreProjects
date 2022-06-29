//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_makeObjectWithType;

//*******************************************   Start Node Definition  ************************************** 	

//variable domain, it response to value structure domain in complex resource
var nod_createVariableDomain = function(variableDomainDef){

	//value structure definition domain
	var loc_variableDomainDefinition = variableDomainDef;
	
	var loc_rootGroupId;
	
	var loc_variableGroupById = {};

	var loc_groupIdIndex = 0;
	
	var loc_out = {
		//create variable group according to
		//value structure complex
		//parent group
		//return group id
		creatVariableGroup : function(valueStructureComplex, parentVariableGroupId){
			loc_groupIdIndex++;
			return nod_createVariableGroup(loc_groupIdIndex+"", valueStructureComplex, loc_variableDomainDefinition, parentVariableGroup);
		},
		
		getVariableValue : function(groupId, variableId){
			
		},
		
		setVariableValue : function(groupId, variableId, value){
			
		}
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VAIRABLEDOMAIN);

	return loc_out;
};

//variable group responding to value structure complex
//valueStructureComplex value structure complex definition under complex entity
//
//it has parent group, so that some variable is from parent
var loc_createVariableGroup = function(id, valueStructureComplex, variableDomainDef, parentVariableGroup){
	
	//var group id
	var loc_id;
	
	//parent domain which some variable can get from
	var loc_parentVariableGroup = parentVariableGroup;
	
	//variables in this domain
	var loc_variablesById = {};
	
	var loc_init = function(id, valueStructureComplex, variableDomainDef, parentVariableGroup){
		
	};
	
	var loc_out = {
		
		getVariableInfo : function(variableId){
			
		},
		
		
	};
	
	loc_init(id, valueStructureComplex, variableDomainDef, parentVariableGroup);
	return loc_out;
};

//variable info
var loc_createVariableInfo = function(variableId){
	
	//variable id responding to variable id defined in value structure definition
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
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createVariableDomain", nod_createVariableDomain); 

})(packageObj);
