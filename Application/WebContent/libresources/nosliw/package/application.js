//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_makeObjectWithType;
	var nod_createVariableDomain;
	var node_createPackageDebugView;
	var node_createConfigure;
	var node_basicUtility;
	var node_componentUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_PACKAGE_NAME = "package";	
	
var node_createApplication = function(resourceId, configure){

	var loc_id;
	
	var loc_resourceId = resourceId;
	
	var loc_configure = configure;
	var loc_configureValue = node_createConfigure(configure).getConfigureValue();
	
	var loc_packageRuntime;
	
	var loc_runtimeContext;
	var loc_runtimeContextForPackage;
	
	var loc_lifecycleEntity;
	var loc_backupState;
	
	var loc_getInitRequest = function(runtimeContext, handlers, request){
		loc_runtimeContext = runtimeContext;
		loc_runtimeContextForPackage = node_componentUtility.makeChildRuntimeContext(loc_runtimeContext, loc_PACKAGE_NAME, loc_packageRuntime); 

		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("initApplication", {}), handlers, request);
		out.addRequest(node_packageService.getCreatePackageRuntimeRequest(loc_resourceId, loc_configure, loc_runtimeContextForPackage, undefined, {
			success : function(request, packageRuntime){
				loc_packageRuntime = packageRuntime;
			}
		}));
		return out;
	};

	var loc_out = {

		getDataType: function(){    return  "application";   },
		getId : function(){  return loc_id;   },
		setId : function(id){   loc_id = id;    },

		getInitRequest : function(runtimeContext, handlers, request){	return loc_getInitRequest(runtimeContext, handlers, request);	},
		
		getLifeCycleRequest : function(transitName, handlers, request){
//			return loc_packageRuntime.getLifeCycleRequest(transitName, handlers, request);
		},
		
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_APPLICATION);
	loc_out.id = loc_id;
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("package.createVariableDomain", function(){nod_createVariableDomain = this.getData();});
nosliw.registerSetNodeDataEvent("component.debug.createPackageDebugView", function(){node_createPackageDebugView = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createApplication", node_createApplication); 

})(packageObj);
