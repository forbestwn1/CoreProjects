//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createConfigure;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createTestComplex1Plugin = function(){
	
	var loc_out = {

		createComplexEntityCore : function(complexEntityDef, variableGroupId, bundleCore, configure){
			return loc_createTestComplex1ComponentCore(complexEntityDef, variableGroupId, bundleCore, configure);
		},
			
	};

	return loc_out;
};

var loc_createTestComplex1ComponentCore = function(complexEntityDef, variableGroupId, bundleCore, configure){
	var loc_id = nosliw.generateId();
	var loc_variableGroupId = variableGroupId;
	var loc_complexEntityDef = complexEntityDef;
	var loc_runtimeInterface;
	var loc_bundleCore = bundleCore;
	var loc_configureValue = node_createConfigure(configure).getConfigureValue();
	var loc_parentView;
	var loc_mainView;
	var loc_attributes = {};
	var loc_attributeViews = {};
	
	var loc_out = {

		getDataType: function(){    return  "testComplex1";   },
		getId : function(){  return loc_id;   },
		getConfigure : function(){   return loc_configureValue;    },
		getVariableGroupId : function(){   return loc_variableGroupId;     },
		
		getPreInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("PreInitCoreTextComplex", {}), handlers, request);
			var attrsValue = loc_complexEntityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEX_ATTRIBUTE];
			_.each(attrsValue, function(attrValue, attrName){
				var complexEntityId = attrValue[node_COMMONATRIBUTECONSTANT.EMBEDEDENTITY_ENTITYID];
				var attrEntity = nosliw.runtime.getPackageService().createComplexEntityRuntime(complexEntityId, loc_out, loc_bundleCore, loc_configureValue[attrName]);
				loc_attributes[attrName] = attrEntity;
				out.addRequest(attrEntity.getPreInitRequest());
			});
			return out;
		},		
		
		//call back to provide runtime context : view (during init phase)
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateRuntimeContextCoreTextComplex", {}), handlers, request);

			loc_parentView = $(runtimeContext.view);
			loc_mainView = $('<div class="view view-main" style="overflow-y1: scroll; border-width:thick; border-style:solid; border-color:black">testComplex</div>');
			loc_parentView.append(loc_mainView);
			
			_.each(loc_attributes, function(attr, attrName){
				var attrView = $('<div>attr: '+attrName+'</div>');
				loc_mainView.append(attrView);
				loc_attributeViews[attrName] = attrView;
				
				var attrRuntimeContext = _.extend({}, runtimeContext, {
					view : attrView
				});
				out.addRequest(attr.getUpdateRuntimeContextRequest(attrRuntimeContext));
			});
			return out;
		},

		getUpdateRuntimeInterfaceRequest : function(runtimeInterface, handlers, request){   
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateRuntimeInterfaceInComponentRuntime", {}), handlers, request);
			loc_runtimeInterface = runtimeInterface;
			return out;
		},

		
		
		//execute command
		getExecuteCommandRequest : function(commandName, parm, handlers, requestInfo){},
		getExecuteNosliwCommandRequest : function(commandName, parm, handlers, requestInfo){   this.getExecuteCommandRequest(node_basicUtility.buildNosliwFullName(commandName), parm, handlers, requestInfo);    },

		//get part by id
		getPart : function(partId){ },
		
		//get interface exposed
		getInterface : function(){ return {}; },

		//set state for the component core
		setState : function(state){   },

		//component runtime env
		getRuntimeEnv : function(){   return loc_runtimeEnv;    },
		setRuntimeEnv : function(runtimeEnv){   loc_runtimeEnv = runtimeEnv;     },
		
		//value by name
		getValue : function(name){},
		setValue : function(name, value){},
		
		//lifecycle handler
		getLifeCycleRequest : function(transitName, handlers, request){},
		setLifeCycleStatus : function(status){},
		
		//call back when core embeded into runtime during init phase
		getUpdateRuntimeRequest : function(runtimeEnv, handlers, request){},
		
		registerEventListener : function(listener, handler, thisContext){  },
		unregisterEventListener : function(listener){ },

		registerValueChangeEventListener : function(listener, handler, thisContext){   },
		unregisterValueChangeEventListener : function(listener){ },
		
		startLifecycleTask : function(){},
		endLifecycleTask : function(){},
			
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});

//Register Node by Name
packageObj.createChildNode("createTestComplex1Plugin", node_createTestComplex1Plugin); 

})(packageObj);
