//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createValuePortElementInfo;
//*******************************************   Start Node Definition  ************************************** 	

var node_utilityNamedVariable = function(){
	
	var loc_out = {
		
		getValuePortValueRequest : function(valuePortContainer, valuePortGroupName, valuePortName, varName, handlers, request){
			var eleInfo = node_createValuePortElementInfo(undefined, varName);
			return valuePortContainer.createValuePort(valuePortGroupName, valuePortName).getValueRequest(eleInfo, handlers, request);
		},

		setValuePortValueRequest : function(valuePortContainer, valuePortGroupName, valuePortName, value, handlers, request){        
			var eleInfo = node_createValuePortElementInfo(undefined, varName);
			return valuePortContainer.createValuePort(valuePortGroupName, valuePortName).setValueRequest(eleInfo, value, handlers, request);
		},
		
		createValuePortVariable : function(valuePortContainer, valuePortGroupName, valuePortName){
			var eleInfo = node_createValuePortElementInfo(undefined, varName);
			return valuePortContainer.createValuePort(valuePortGroupName, valuePortName).createVariable(eleInfo);
		},
		
	};
	
	return loc_out;		
		
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.createValuePortElementInfo", function(){node_createValuePortElementInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("utilityNamedVariable", node_utilityNamedVariable); 

})(packageObj);
