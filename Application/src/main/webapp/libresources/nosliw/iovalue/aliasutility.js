//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_aliasUtility = function(){
	
	var loc_out = {

		getFlatContextElementByAlias : function(name, flatContext){
			var eleId = flatContext[node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_VARIDBYNAME][name];
			return flatContext[node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT][eleId];
		},
				
		expandNameByFlatContext : function(name, flatContext){
			var namesByVarId = flatContext[node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_NAMESBYVARID];
			var varIdByName = flatContext[node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_VARIDBYNAME];
			return namesByVarId[varIdByName[name]];
		},
			
		expandInputByFlatContext : function(inputValue, flatContext){
			var varIdByName = flatContext[node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_VARIDBYNAME];
			var namesByVarId = flatContext[node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_NAMESBYVARID];
			return this.expandInput(inputValue, varIdByName, namesByVarId);
		},
		
		expandInputByVariablesInfoContainer : function(inputValue, varsInfoContainer){
			var varIdByName = varsInfoContainer[node_COMMONATRIBUTECONSTANT.CONTAINERVARIABLEINFO_VARIDBYNAME];
			var namesByVarId = varsInfoContainer[node_COMMONATRIBUTECONSTANT.CONTAINERVARIABLEINFO_NAMESBYVARID];
			return this.expandInput(inputValue, varIdByName, namesByVarId);
		},
			
		expandInput : function(inputValue, varIdByName, namesByVarId){
			var out = {};
			var valueByVarId = {};
			_.each(inputValue, function(value, name){
				var varId = varIdByName[name];
				if(varId!=undefined)	valueByVarId[varId] = value;
				else out[name] = value;
			});
			_.each(valueByVarId, function(value, varId){
				var names = namesByVarId[varId];
				_.each(names, function(name){
					out[name] = value;
				});
			});
			return out;
		},
		
	};
		
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("aliasUtility", node_aliasUtility); 

})(packageObj);
