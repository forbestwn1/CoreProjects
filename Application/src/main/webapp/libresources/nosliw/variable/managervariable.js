//get/create package
var packageObj = library.getChildPackage("variable");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_getObjectType;
var node_ChildVariableInfo;
var node_newVariable;

//*******************************************   Start Node Definition  **************************************

var node_createVariableManager = function(){
	//all variables
	var loc_variables = {};
	//variable usage
	var loc_varUsage = {};

	var loc_newVariable = function(data1, data2, adapterInfo, info){
		var variable = node_newVariable(data1, data2, adapterInfo, info);
		loc_variables[variable.prv_id] = variable;
		loc_varUsage[variable.prv_id] = 0;
		return variable;
	};

	var loc_useVariable = function(variableId){	
		loc_varUsage[variableId]++;
		return loc_variables[variableId];
	};
	
	var loc_releaseVariable = function(variableId){
		loc_varUsage[variableId]--;
		if(loc_varUsage[variableId]<=0){
			loc_variables[variableId].destroy();
			delete loc_variables[variableId];
			delete loc_varUsage[variableId];
		}
		else return loc_variables[variableId];
	};
	
	var loc_exportVariable = function(variable){
		var out = {
			id : variable.prv_id,
			info : variable.info,
			children : []
		};
		
		_.each(variable.prv_getChildren(), function(childInfo, id){
			var child = {
				path : childInfo.path,
				variable : loc_exportVariable(childInfo.variable)
			};
			out.children.push(child);
		});
		
		return out;
	};
	
	var loc_out = {
		
		export : function(){
			var out = [];
			
			//find all root variable
			var rootVarIds = [];
			_.each(loc_variables, function(variable, varId){
				var relativeVarInfo = variable.prv_getRelativeVariableInfo();
				if(relativeVarInfo==undefined){
					rootVarIds.push(varId);
				}
			});
			
			_.each(rootVarIds, function(varId, i){
				out.push(loc_exportVariable(loc_variables[varId]));
			});
			return out;
		},
		
		getVariableInfo : function(id){		
			return { 
				id :id,
				variable :loc_variables[id],
				usage : loc_varUsage[id]
			};	
		},
		
		createVariable : function(data1, data2, adapterInfo, info){
			var data1Type = node_getObjectType(data1);
			if(data1Type==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE){
				//if data1 is variable, then use crete child variable 
				return this.createChildVariable(data1, data2, adapterInfo, info);
			}
			else{
				return loc_newVariable(data1, data2, adapterInfo, info);
			}
		},

		createChildVariable : function(variable, path, adapterInfo, info){
			var out;
			if(adapterInfo==undefined&&info==undefined){
				//normal child, try to reuse existing one
				var childVarInfo;
				if(path==undefined || path=="")  childVarInfo = new node_ChildVariableInfo(variable, "");
				else childVarInfo = variable.prv_childrenVariable[path];
				
				if(childVarInfo==undefined){
					out = loc_newVariable(variable, path, adapterInfo, info);
				}
				else{
					out = childVarInfo.variable;
				}
			}
			else{
				//child with extra info
				out = loc_newVariable(variable, path, adapterInfo, info);
			}
			return out;
		},
		
		useVariable : function(variable){
			return loc_useVariable(variable.prv_id);  
		},
		
		releaseVariable : function(variable){  return loc_releaseVariable(variable.prv_id);  },
		
		destroyVariable : function(variable) {  
			loc_varUsage[variable.prv_id]--;
			this.releaseVariable(variable);
		}
		
	};
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("variable.variable.ChildVariableInfo", function(){node_ChildVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("variable.variable.newVariable", function(){node_newVariable = this.getData();});


//Register Node by Name
packageObj.createChildNode("createVariableManager", node_createVariableManager); 

})(packageObj);
