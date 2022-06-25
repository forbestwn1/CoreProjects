//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_getComponentLifecycleInterface;
	var node_basicUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_createBundleService = function() {
	
	var loc_complexEntityPlugins = {};

	//create component runtime object
	var loc_getCreateComponentRuntimeRequest = function(complexEntityId, bundleDefinition, parentVariableGroup, configure, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createComplexRuntime"), handlers, request);

		//get component definition
		var componentDef = null;

		//new complexCore through complex plugin
		var complexEntityPlugin = loc_complexEntityPlugins[complexEntityId.entityType];
		var componentCore = complexEntityPlugin.createComplexCore(componentDef);
		
		//build variableGroup
		var variableGroup = null;
		
		//build decorationInfos
		var decorationInfos = null;
		
		//create runtime
		var runtime = node_createComponentRuntime(componentCore, decorationInfos, variableGroup, configure, state, request);
		
		//runtime init
		out.addRequest(runtime.getInitRequest({
			success : function(request){
				return request.getData();
			}
		}).withData(runtime));

	};

	var loc_newBundleRuntimeRequest = function(resourceType, resourceId, bundleDefinition, configure){
		//create bundle runtime
		var bundleRuntime = node_createBundleRuntime(bundleDefinition);
		
		//get complex entity id
		var mainEntityId;
		
		//create root component
		loc_getCreateComponentRuntimeRequest(mainEntityId, bundleDefinition, variableDomain, configure, handlers, request);
		
	};
	
	var loc_createPackageExe = function(resourceType, resourctId, packageDef, inputValue, setting, handlers, request){
		//create package runtime
		node_createPackageRuntime(packageDef)
		
		//get main entity id
		var mainEntityId;
		
		
	};
	
	var loc_out = {

		createComplexObject : function(complexEntityId, bundleDefinition, parentVariableGroup, configure, handlers, request){
		},

			
		createPackageExe : function(resourceType, resourctId, inputValue, setting, handlers, request){
			
		},
		
		registerComplexEntityPlugin : function(entityType, complexEntityPlugin){
			loc_complexEntityPlugins[entityType] = complexEntityPlugin;
		}
		
	};

	return loc_out;
};

//plug in for complex entity,
//it create component core object
//
var loc_complexEntityPlugin ={
	
	//create component core object
	createComponentCore : function(complexDef, configure){
		intoRuntimeRequest();
	}
	
};

var node_createPackageRuntime = function(packageDef){

	var loc_packageDef = packageDef;
	
	var loc_bundleById = {};
	
};

//bundle object
var node_createBundleRuntime = function(bundleDef){
	
	//bundle definition
	var loc_bundleDefinition = bundleDef;
	
	//variable domain for this bundle
	var loc_variableDomain = nod_createVariableDomain(loc_bundleDefinition.valueStructureDomain);
	
	//root component runtime
	var loc_rootComponent;
	
	var loc_out = {
		
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
