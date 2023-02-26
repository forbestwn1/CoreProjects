//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;

//*******************************************   Start Node Definition  **************************************
	
//interface for plug in for complex entity,
//it create component core object
var node_buildComplexEntityPlugInObject = function(rawPluginObj){

	var interfaceDef = {
		//create component core object
		createComplexEntityCore : function(id, complexEntityDef, variableGroupId, bundleCore, configure){
			return {};
		}
	};
		
	return _.extend({}, interfaceDef, rawPluginObj);	
};

var node_buildSimpleEntityPlugInObject = function(rawPluginObj){

	var interfaceDef = {
		//create component core object
		createComplexEntityCore : function(id, complexEntityDef, variableGroupId, bundleCore, configure){
			return {};
		}
	};
		
	return _.extend({}, interfaceDef, rawPluginObj);	
};

var node_buildComplexEntityCoreObject = function(rawComplexEntityCore, valueContextId, bundleCore){
	
	var loc_rawComplexEntityCore = rawComplexEntityCore;
	
	var loc_valueContextId = valueContextId;
	
	
	
	var interfaceDef = {

		getComplexEntityInitRequest : function(handlers, request){   return loc_rawComplexEntityCore.getComplexEntityInitRequest==undefined?undefined:loc_rawComplexEntityCore.getComplexEntityInitRequest(handlers, request);     },
			
		getValueContextId : function(){   return loc_valueContextId;   },
		
		
		getChildrenComplexEntityNames : function(){},
		
		getChildComplexEntity : function(attrName){},
		
		
			
	};
		
	return _.extend({}, interfaceDef, rawComplexEntityCore);	
};


var node_createComplexEntityEnvInterface = function(complexEntityCore, valueContextId, bundleCore){
	
	var loc_complexEntityCore = complexEntityCore;
	
	var loc_bundleCore = bundleCore;
	
	var loc_valueContextId = valueContextId;
	
	var loc_out = {
		complexUtility : {
			createChildComplexRuntimeRequest : function(complexEntityId, configure, handlers, request){
				return nosliw.runtime.getPackageService().getCreateComplexEntityRuntimeRequest(complexEntityId, loc_complexEntityCore, loc_bundleCore, configure, handlers, request);
			},
		}	
	};
	
	return loc_out;
};
		

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("buildComplexEntityPlugInObject", node_buildComplexEntityPlugInObject); 
packageObj.createChildNode("buildSimpleEntityPlugInObject", node_buildSimpleEntityPlugInObject); 
packageObj.createChildNode("buildComplexEntityCoreObject", node_buildComplexEntityCoreObject); 
packageObj.createChildNode("createComplexEntityEnvInterface", node_createComplexEntityEnvInterface); 

})(packageObj);
