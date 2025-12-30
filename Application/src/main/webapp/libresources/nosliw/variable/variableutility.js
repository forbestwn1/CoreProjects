//get/create package
var packageObj = library.getChildPackage("variable");    

(function(packageObj){
//get used node
var node_getObjectType;
var node_makeObjectWithType;
var node_CONSTANT;	
var node_createValueInVar;
var node_namingConvensionUtility;
var node_basicUtility;
var node_createPathToBaseVariableInfo;
//*******************************************   Start Node Definition  ************************************** 	
/**
 * 
 */
var node_variableUtility = function(){
	
    var loc_toBaseVariableConvertInfo = function(variable, outcome){
		if(variable.prv_isBase==true){
			outcome.addSegment({
	    		baseVariable : variable
		    });
		}
		else{
    		outcome.addSegment({
	    		adapter : variable.prv_valueAdapter,
		    	pathToParent : variable.prv_getRelativeVariableInfo().path
		    });
			loc_toBaseVariableConvertInfo(variable.prv_getRelativeVariableInfo().parent, outcome);
		}
	};

	return {

        toBaseVariableConvertInfo : function(variable){
    		var out = node_createPathToBaseVariableInfo();
			loc_toBaseVariableConvertInfo(variable, out);
			return out;
		},

        getVariable : function(varObj){
			var out = varObj;
        	var type = node_getObjectType(varObj);
	        if(type==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLEWRAPPER){
		        out = varObj.getVariable();
	        }
	        return out;
		}
		
	};	
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.entity.createValueInVar", function(){node_createValueInVar = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.entity.createPathToBaseVariableInfo", function(){node_createPathToBaseVariableInfo = this.getData();});


//Register Node by Name
packageObj.createChildNode("variableUtility", node_variableUtility); 

})(packageObj);
