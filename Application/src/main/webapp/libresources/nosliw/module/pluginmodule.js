//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_basicUtility;
    var node_createCoreEntityPackage;
    var node_createReferenceCoreEntity;
    var node_createAdapterInfo;
	var node_complexEntityUtility;
	var node_taskExecuteUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createModulePlugin = function(){
	
	var loc_out = {

		getCreateEntityCoreRequest : function(complexEntityDef, internalValuePortContainerId, externalValuePortContainerId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createModuleCore(complexEntityDef, internalValuePortContainerId, bundleCore, configure);
			}, handlers, request);
		}
	};

	return loc_out;
};

var loc_createModuleCore = function(complexEntityDef, valueContextId, bundleCore, configure){
	var loc_complexEntityDef = complexEntityDef;
	var loc_envInterface = {};
	var loc_tasks;
	var loc_pages;
	
	
	var loc_lifecycleInit = function(){
				
	};
	
	var loc_presentPage = function(){
		
	};
	
	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},

		getEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createAttributeRequest(node_COMMONATRIBUTECONSTANT.WITHBRICKTASKS_TASK, undefined, {
				success : function(request, node){
					loc_tasks = node_complexEntityUtility.getBrickNode(node).getChildValue().getCoreEntity();
				}
			}));

			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createAttributeRequest(node_COMMONATRIBUTECONSTANT.BLOCKMODULE_PAGE, undefined, {
				success : function(request, node){
					loc_pages = node_complexEntityUtility.getBrickNode(node).getChildValue().getCoreEntity();
				}
			}));
			return out;
		},

		updateView : function(view){
			loc_tasks.updateView(view);

			//register pages
            var pages = {};
            _.each(loc_pages.getChildrenEntity(), function(childEntity, name){
				var pageCoreEntity = childEntity.getCoreEntity().getBrickCoreEntity();
				pages[name] = pageCoreEntity;
			});

            nosliw.runtime.runtimeEnv.getValue("ui.registerAllPages", {
				"pageCoreEntitys" : pages
			});

			return view;
		},
		
		getPostInitRequest : function(handlers, request){
			var taskCoreEntity = loc_tasks.getChildCoreEntity("nosliw_init");
			return node_taskExecuteUtility.getExecuteInteractiveBrickPackageRequest(node_createCoreEntityPackage(node_createReferenceCoreEntity(taskCoreEntity.getBrickCoreEntity()), node_createAdapterInfo(undefined, false)), undefined, handlers, request);
		}
		
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
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.entity.createCoreEntityPackage", function(){node_createCoreEntityPackage = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.entity.createReferenceCoreEntity", function(){node_createReferenceCoreEntity = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.entity.createAdapterInfo", function(){node_createAdapterInfo = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("task.taskExecuteUtility", function(){node_taskExecuteUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createModulePlugin", node_createModulePlugin); 

})(packageObj);
