{
	name : "contexttree",
	description : "",
	attributes : [
	],
	context: {
	},
	events : {
		
	},
	requires:{
	},
	script : function(env){

		var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
		var node_requestProcessor = nosliw.getNodeData("request.requestServiceProcessor");
		var node_createContextVariablesGroup = nosliw.getNodeData("uidata.context.createContextVariablesGroup");
		var node_createContextVariableInfo = nosliw.getNodeData("uidata.context.createContextVariableInfo");
		var node_dataUtility = nosliw.getNodeData("uidata.data.utility");

		var loc_env = env;
	
		var loc_view;
		var loc_viewVariableTree;
		
		var loc_contextVariableGroup = {};
		
		var loc_getVariableTreeInfo = function(eleVar, childInfo){
			var out = {};
			out.id = eleVar.prv_id;
			out.usage = nosliw.runtime.getUIVariableManager().getVariableInfo(eleVar.prv_id).usage;

			out.wrapperId = eleVar.prv_wrapper!=undefined ? eleVar.prv_wrapper.prv_id : "NO WRAPPER"; 
			if(childInfo!=undefined){
				out.path = childInfo.path;
				out.normal = childInfo.isNormal;
			}
			
			out.children = [];
			var childrenInfo = eleVar.prv_getChildren();
			if(childrenInfo!=undefined){
				_.each(childrenInfo, function(childVarInfo, id){
					out.children.push(loc_getVariableTreeInfo(childVarInfo.variable, childVarInfo));
				});
			}
			return out;
		};
		
		
		var loc_updateView = function(requestInfo){
			//variable tree
			var varTree = {};
			var eleVars = loc_contextVariableGroup.getVariables();
			_.each(eleVars, function(eleVar, eleName){
				varTree[eleName] = loc_getVariableTreeInfo(eleVar.prv_getVariable());
			});
			loc_viewVariableTree.val(JSON.stringify(varTree, null, 4));
		};

		var loc_out = 
		{
			preInit : function(requestInfo){
				loc_contextVariableGroup = node_createContextVariablesGroup(loc_env.getContext(), undefined, function(request){
					loc_updateView(request);
				});
				_.each(loc_env.getContext().getElementsName(), function(eleName, index){
					loc_contextVariableGroup.addVariable(node_createContextVariableInfo(eleName));
				});
			},
				
			initViews : function(requestInfo){
				loc_view = $('<div/>');
				loc_viewVariableTree = $('<textarea rows="15" cols="150" id="aboutDescription" style="resize: none;" data-role="none"></textarea>');
				loc_view.append(loc_viewVariableTree);
				return loc_view;
			},
				
			postInit : function(requestInfo){
				loc_updateView(requestInfo);
			},
		};
		return loc_out;
	}
}
