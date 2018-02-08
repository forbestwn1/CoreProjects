{
	name : "debug",
	description : "",
	attributes : [
	],
	context: {
		inherit : true,
		public : {
			
		},
		private : {
		}
	},
	events : {
		
	},
	requires:{
	},
	script : function(context, parentResourceView, uiTagResource, attributes, env){

		var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
		var node_requestProcessor = nosliw.getNodeData("request.requestServiceProcessor");
		var node_createContextVariablesGroup = nosliw.getNodeData("uidata.context.createContextVariablesGroup");
		var node_createContextVariableInfo = nosliw.getNodeData("uidata.context.createContextVariableInfo");
		
		var loc_env = env;
	
		var loc_view;
		
		var loc_contextVariableGroup = {};
		
		var loc_updateView = function(){
			var contextContent = {};
			var setRequest = node_createServiceRequestInfoSet({}, {
				success : function(requestInfo, result){
					_.each(result.getResults(), function(contextData, name){
						contextContent[name] = contextData.value;
					});
					loc_view.val(JSON.stringify(contextContent, null, 4));
				}
			});
			var eleVars = loc_contextVariableGroup.getVariables();
			_.each(eleVars, function(eleVar, eleName){
				setRequest.addRequest(eleName, loc_env.getDataOperationRequestGet(eleVar));
			});
			node_requestProcessor.processRequest(setRequest, false);
		};

		var loc_out = 
		{
			ovr_preInit : function(){
				loc_contextVariableGroup = node_createContextVariablesGroup(loc_env.getContext(), undefined, function(){
					loc_updateView();
				});
				_.each(loc_env.getContext().getElementsName(), function(eleName, index){
					loc_contextVariableGroup.addVariable(node_createContextVariableInfo(eleName));
				});
			},
				
			ovr_initViews : function(startEle, endEle, requestInfo){
				loc_view = $('<textarea rows="15" cols="150" id="aboutDescription" style="resize: none;" data-role="none"></textarea>');
				return loc_view;
			},
				
			ovr_postInit : function(){
				loc_updateView();
			},

			ovr_processAttribute : function(name, value){},

			ovr_handleDataEvent : function(name, event, path, data, requestInfo){	},
		};
		return loc_out;
	}
}
