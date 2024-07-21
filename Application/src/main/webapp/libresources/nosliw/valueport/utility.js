//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	
	var loc_getValuePort = function(withValuePort, varId){
		var valuePortId = varId[node_COMMONATRIBUTECONSTANT.IDELEMENT_ROOTELEMENTID][node_COMMONATRIBUTECONSTANT.IDROOTELEMENT_VALUEPORTID][node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBUNDLE_VALUEPORTID];
		return withValuePort.getValuePort(valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_GROUP], valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_NAME]);
	};
	
	var loc_out = {
		
		getValuePortValueRequest : function(withValuePort, varId, handlers, request){        
			var valuePort = loc_getValuePort(withValuePort, varId);
			var eleInfo = node_createValuePortElementInfo(varId);
			return valuePort.getValueRequest(eleInfo, handlers, request);
		},

		setValuePortValueRequest : function(withValuePort, varId, value, handlers, request){        
			var valuePort = loc_getValuePort(withValuePort, varId);
			var eleInfo = node_createValuePortElementInfo(varId);
			return valuePort.setValueRequest(eleInfo, value, handlers, request);
		},
		
		setValuePortValuesRequest : function(withValuePort, varValues, handlers, request){        
			var varValueByValuePort = {};
			
			_.each(varValues, function(varValue, i){
				var varId = varValue.variableId;
				var byGroup = varValueByValuePort[varId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_GROUP]];
				if(byGroup==undefined){
					byGroup = {};
					varValueByValuePort[varId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_GROUP]] = byGroup;
				}
				var byName = byGroup[varId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_NAME]];
				if(byName==undefined){
					byName = [];
					byGroup[varId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_NAME]] = byName;
				}
				byName.push(varValue);
			});
			
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			_.each(varValueByValuePort, function(byGroup, group){
				_.each(byGroup, function(byName, name){
					var valuePort = withValuePort.getValuePort(group, name);
					var eleValue = [];
					_.each(byName, function(varValue, i){
						eleValue.push(node_ElementIdValuePair(node_createValuePortElementInfo(varValue.variableId), varValue.value));
					});
					out.addRequest( valuePort.setValuesRequest(eleValue));
				});
			});
			return out;			
		},

		createValuePortVariable : function(withValuePort, varId){
			var valuePort = loc_getValuePort(withValuePort, varId);
			var eleInfo = node_createValuePortElementInfo(varId);
			return valuePort.createVariable(eleInfo);
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
packageObj.createChildNode("valuePortUtility", node_utility); 

})(packageObj);
