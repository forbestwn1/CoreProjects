//get/create package
var packageObj = library.getChildPackage();    


(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_getComponentLifecycleInterface;
	var node_getComponentInterface;
	var node_getEntityTreeNodeInterface;
	var node_createServiceRequestInfoSimple;
	var node_requestServiceProcessor;
	var node_getComponentManagementInterface;
	var node_componentUtility;
	var node_getStateMachineDefinition;
	var node_getComplexEntityObjectInterface;
	var node_complexEntityUtility;
	var node_uiDataOperationServiceUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_createComponentVariableInspection = function(){
	var loc_vueComponent = {
		data : function(){
			return {
				variableId : "",
				variableValue : "",
			};
		},
		methods : {
			getVariableValue : function(event){
				var that = this;
				var varInfo = nosliw.runtime.getUIVariableManager().getVariableInfo(that.variableId);
				if(varInfo!=undefined){
					var request = varInfo.variable.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(), {
						success : function(request, data){
							that.variableValue = JSON.stringify(data.value, null, 4);
						}
					});
					node_requestServiceProcessor.processRequest(request, false);
					
				}
			},
		},
		props : ['data'],
		template : `
			<div>
				<div>
				  <input v-model="variableId" placeholder="variable id"/>
				  <button v-on:click="getVariableValue">GetValue</button>
				  <textarea v-model="variableValue"></textarea>
				</div>
		    </div>
		`
	};
	return loc_vueComponent;
};



var node_createVariableDebugView = function(view){
	
	var loc_view = $('<div></div>');
	loc_view = view;

	var loc_componentData = {
	};
	
	var loc_vue;
	
	var loc_init = function(){
		Vue.component('variable-inspection', loc_createComponentVariableInspection());

		loc_vue = new Vue({
			el: document.getElementById("variableDebug"),
//    		  root: "#complextreeDebug",
//			el: loc_view,
			data: loc_componentData,
			methods : {
			},
			watch: {
			},
			computed : {
			},
			template : `
				<div>
					<variable-inspection/>
				</div>
			`
		});
	};

	var loc_out = {
		
		getView : function(){   return loc_view;   },
		
	};
	
	loc_init();
	return loc_out;
};
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentManagementInterface", function(){node_getComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.getStateMachineDefinition", function(){node_getStateMachineDefinition = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getComplexEntityObjectInterface", function(){node_getComplexEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){  node_complexEntityUtility = this.getData();});

nosliw.registerSetNodeDataEvent("uidata.context.createContextVariablesGroup", function(){  node_createContextVariablesGroup = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){  node_createContextVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){  node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createVariableDebugView", node_createVariableDebugView); 

})(packageObj);
