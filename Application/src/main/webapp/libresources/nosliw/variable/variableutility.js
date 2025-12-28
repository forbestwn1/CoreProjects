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
//*******************************************   Start Node Definition  ************************************** 	
/**
 * 
 */
var node_variableUtility = function(){
	
    var loc_toBaseVariableConvertInfo = function(variable, pathInfoArray){
		if(variable.prv_isBase==true){
    		pathInfoArray.push({
	    		baseVariable : variable
		    });
		}
		else{
    		pathInfoArray.push({
	    		adapter : variable.prv_valueAdapter,
		    	pathToParent : variable.prv_getRelativeVariableInfo().path
		    });
			loc_toBaseVariableConvertInfo(variable.prv_getRelativeVariableInfo().parent, pathInfoArray);
		}
	};

	return {

        toBaseVariableConvertInfo : function(variable){
			var out = [];
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


//Register Node by Name
packageObj.createChildNode("variableUtility", node_variableUtility); 

})(packageObj);
