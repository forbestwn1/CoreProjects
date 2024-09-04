
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"script",
"version":"1.0.0"
},
"id":"*complextask_test_basic"
},
"children":[],
"dependency":{},
"info":{}
}, {"script":function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_createValuePortElementInfo = nosliw.getNodeData("valueport.createValuePortElementInfo");
	var node_makeObjectWithApplicationInterface = nosliw.getNodeData("component.makeObjectWithApplicationInterface");

	var loc_valueContext;

	var loc_envInterface = {};

	var loc_taskContext;
	var loc_taskResult;

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		var varDomain = bundleCore.getVariableDomain();
		loc_valueContext = varDomain.creatValuePortContainer(valueContextId);
	};



	var loc_facadeTaskFactory = {
		//return a task
		createTask : function(taskContext){
			loc_taskContext = taskContext;
			return loc_out;
		},
	};

	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(){
		},
		
		updateView : function(view){
			var rootView =  $('<div>Task' + '</div>');
			$(view).append(rootView);
		},
		
		getTaskInitRequest : function(handlers, request){
			return loc_taskContext.getInitTaskRequest(loc_out, handlers, request);
		},
		
		getTaskExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				loc_taskResult = {
					aaa : "bbbb"
				}
			}));
			return out;
		},
		
		getTaskResult : function(){   return loc_taskResult;    }
		
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	loc_out = node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASKFACTORY, loc_facadeTaskFactory);
	return loc_out;
}

}, {"loadPattern":"file"
});

