//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
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
			var varGroup = loc_createVariableGroup(loc_groupIdIndex+"", valueStructureComplex, loc_variableDomainDefinition, parentVariableGroupId, loc_out);
			loc_variableGroupById[varGroup.getId()] = varGroup;
			return varGroup;
		},
		
		getVariableGroup : function(variableGroupId){   return loc_variableGroupById[variableGroupId];  },
		
		getVariableValue : function(groupId, variableId){
			
		},
		
		setVariableValue : function(groupId, variableId, value){
			
		},
		
		getVariableDomainDefinition : function(){   return loc_variableDomainDefinition;	}
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VAIRABLEDOMAIN);

	return loc_out;
};

//variable group responding to value structure complex
//valueStructureComplex value structure complex definition under complex entity
//
//it has parent group, so that some variable is from parent
var loc_createVariableGroup = function(id, valueStructureComplex, variableDomainDef, parentVariableGroupId, variableDomain){
	
	var loc_variableDomain;
	
	//var group id
	var loc_id;
	
	//parent domain which some variable can get from
	var loc_parentVariableGroupId;
	
	//valueStructures in the group
	var loc_valueStructures = {};
	
	var loc_init = function(id, valueStructureComplex, variableDomainDef, parentVariableGroupId, variableDomain){
		loc_id = id;
		loc_parentVariableGroupId = parentVariableGroupId;
		loc_variableDomain = variableDomain;

		var parentVariableGroup = parentVariableGroupId==undefined ? undefined : variableDomain.getVariableGroup(parentVariableGroupId);
		var valueStructureIds = valueStructureComplex==undefined?[] : valueStructureComplex[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXVALUESTRUCTURE_VALUESTRUCTURE];
		_.each(valueStructureIds, function(valueStructureId){
			var variableDomainDef = variableDomain.getVariableDomainDefinition();
			var valueStructureRuntimeId = variableDomainDef[node_COMMONATRIBUTECONSTANT.DOMAINVALUESTRUCTURE_DEFINITIONBYRUNTIME][valueStructureId];
			var valueStructureDefinitionInfo = variableDomainDef[node_COMMONATRIBUTECONSTANT.DOMAINVALUESTRUCTURE_VALUESTRUCTURE][valueStructureRuntimeId];
			if(parentVariableGroup==undefined || parentVariableGroup.getVariableInfosByValueStructure(valueStructureRuntimeId)==undefined){
				//value structure not found in parent, then build in current group
				var valueStructure = loc_createValueStructure(valueStructureRuntimeId, true);
				_.each(valueStructureDefinitionInfo[node_COMMONATRIBUTECONSTANT.INFOVALUESTRUCTURE_VALUESTRUCTURE], function(rootDef, rootName){
					valueStructure.addVariable(loc_createVariableInfo(rootName, rootDef));
				});
				loc_valueStructures[valueStructure.getId()] = valueStructure;
			}
			else{
				//value structure from parent
				var valueStructure = loc_createValueStructure(valueStructureRuntimeId, false);
				loc_valueStructures[valueStructure.getId()] = valueStructure;
			}
		});
	};
	
	var loc_out = {
			
		getId : function(){  return loc_id;   },
		
		getValueStructure : function(valueStructureId){      },
		
		getVariableInfosByValueStructure : function(valueStructureId){   return loc_variablesByValueStructure[valueStructureId];  },
		
		getVariableInfo : function(variableId){
			
		},
	};
	
	loc_init(id, valueStructureComplex, variableDomainDef, parentVariableGroupId, variableDomain);
	return loc_out;
};

var loc_createValueStructure = function(id, isSolid){
	
	var loc_id = id;
	
	var loc_isSolid = isSolid;

	var loc_usedCount = 1;
	
	var loc_variables = {};
	
	var loc_out = {
		
		getId : function(){   return loc_id;    },
		
		isSolid : function(){   return loc_isSolid;   },

		addVariable : function(varInfo){   loc_variables[varInfo.getName()] = varInfo;    },
		
		use : function(){   loc_usedCount++;   },
		
		unUse : function(){  loc_usedCount--;  }
	};
	
	return loc_out;
};

//variable info
var loc_createVariableInfo = function(name, variableInfo){
	var loc_name = name;
	
	//variable info
	var loc_variableInfo;
	
	//current value for variable
	var loc_value;
	
	var loc_init = function(variableInfo){
		loc_variableInfo = variableInfo;
		loc_value = loc_variableInfo[node_COMMONATRIBUTECONSTANT.ROOTSTRUCTURE_DEFAULT];
	};
	
	var loc_out = {
		
		getName : function(){   return loc_name;    },
			
		getVariableInfo : function(){   return loc_variableInfo;    },
	
		getValue : function(){   return loc_value;    },
		
		setValue : function(value){  loc_value = value;    },
	};
	
	loc_init(variableInfo);
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createVariableDomain", nod_createVariableDomain); 

})(packageObj);
