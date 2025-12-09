function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_createValuePortElementInfo = nosliw.getNodeData("valueport.createValuePortElementInfo");
	var node_complexEntityUtility = nosliw.getNodeData("complexentity.complexEntityUtility");
	var node_taskExecuteUtility = nosliw.getNodeData("task.taskExecuteUtility");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_getWithValuePortInterface = nosliw.getNodeData("valueport.getWithValuePortInterface");
	var node_getEntityObjectInterface = nosliw.getNodeData("complexentity.getEntityObjectInterface");
	var node_utilityNamedVariable = nosliw.getNodeData("valueport.utilityNamedVariable");
	var node_makeObjectWithApplicationInterface = nosliw.getNodeData("component.makeObjectWithApplicationInterface");
	var node_interactiveUtility = nosliw.getNodeData("task.interactiveUtility");
	var node_createTaskCore = nosliw.getNodeData("task.createTaskCore");

	var loc_envInterface = {};
	
	var loc_taskCore;
	
	var loc_facadeTaskCore = {
		//return a task
		getTaskCore : function(){
			return loc_taskCore;
		},
	};

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		loc_taskCore = node_createTaskCore(loc_out, loc_out);
	};

	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(){
		},
		
		updateView : function(view){
			var rootView =  $('<div>' + '</div>');
			var trigueView = $('<button>Check variable value</button>');
			var intputView = $('<textarea rows="1" cols="150" style="resize: none; border:solid 1px;" data-role="none" placeholder="input variable name"></textarea>');
			var resultView = $('<textarea rows="5" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
			
			rootView.append(trigueView);
			rootView.append(intputView);
			rootView.append(resultView);
			$(view).append(rootView);

			trigueView.click(function() {
				var variableName = intputView.val();
				var valuePortContainer = node_getEntityObjectInterface(loc_out).getInternalValuePortContainer();
                var variable = valuePortContainer.getVariableByName(node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_EVENT, node_COMMONCONSTANT.VALUEPORT_TYPE_EVENT, variableName);
                var request = variable.getGetValueRequest({
					success : function(request, value){
						resultView.val(JSON.stringify(value));
					}
				});
				node_requestServiceProcessor.processRequest(request);
			});
		},

		getTaskExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var valuePortContainer = node_getEntityObjectInterface(loc_out).getInternalValuePortContainer();
			var withValuePort = loc_envInterface[node_CONSTANT.INTERFACE_WITHVALUEPORT];
			
			out.addRequest(node_interactiveUtility.getGetTaskRequestValuesFromValuePortRequest(valuePortContainer, {
				success : function(request, requestValues){
					return {
					    "resultName": "success"
					};
				}
			}));
			

			return out;
		},
		
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	loc_out = node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK, loc_facadeTaskCore);
	return loc_out;
}
