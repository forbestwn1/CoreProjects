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
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_createComponentNodeRuntime = function(){
	var loc_vueComponent = {
		data : function(){
			return {};
		},
		watch: {
		},
		methods : {
			
			getDataType : function(){	return this.data==undefined?undefined:node_getComponentInterface(this.data.getCoreEntity()).getDataType();	},
			
			getCoreEntity : function(){   return this.data==undefined?undefined:this.data.getCoreEntity();   },

			getCoreChildrenName : function(){   
				var out = this.data==undefined?undefined:node_getEntityTreeNodeInterface(this.data.getCoreEntity()).getChildrenName();
				return out;
			},
			
			getChild : function(childName){   
				var out = this.data==undefined?undefined:node_getEntityTreeNodeInterface(this.data.getCoreEntity()).getChild(childName).getChildValue();
				return out;
			},
			
			onSelectEntity : function(){
				this.$emit("selectEntity", this.data.getCoreEntity());
			},

			onChildSelectEntity : function(entity){
				this.$emit("selectEntity", entity);
			},
			
		},
		props : ['data'],
		template : `
			<div class="treeview-item">
				    <a class="treeview-item-root treeview-item-selectable" v-on:click.prevent="onSelectEntity">
					      <div class="treeview-item-content">
								<i class="icon f7-icons">doc</i>
								<div class="treeview-item-label">{{getDataType()}}</div>
					      </div>
				    </a>
			        <div class="treeview-toggle"></div>
			    <div class="treeview-item-children">
					Decoration
			    </div>
			    <div class="treeview-item-children">
			    	Core
					<div class="treeview-item">
						<complextree-runtime
							v-for="childName in getCoreChildrenName()"
							v-bind:key="childName"
					  		v-bind:data="getChild(childName)"
							v-on:selectEntity="onChildSelectEntity"
						/>
					</div>
			    </div>
		    </div>
		`
	};
	return loc_vueComponent;
};
	
var loc_createComponentInfoPackage = function(){
	var loc_vueComponent = {
		data : function(){
			return {};
		},
		methods : {
		},
		props : ['data'],
		template : `
			<div>
				Package
		    </div>
		`
	};
	return loc_vueComponent;
};
	
var loc_createComponentInfoBundle = function(){
	var loc_vueComponent = {
		data : function(){
			return {};
		},
		methods : {
		},
		props : ['data'],
		template : `
			<div>
				Bundle
		    </div>
		`
	};
	return loc_vueComponent;
};
	
var loc_createComponentInfoComplexEntity = function(){
	var loc_vueComponent = {
		data : function(){
			return {};
		},
		methods : {
			getValueContext : function(){
				return node_getComplexEntityObjectInterface(this.data).getValueContext();
			},
		},
		props : ['data'],
		template : `
			<div>
				<div>
					ComplexEntity
			    </div>
				<div v-if="data!=undefined">
					<complexinfo-valuecontext v-bind:data="getValueContext()"/>
			    </div>
		    </div>
		`
	};
	return loc_vueComponent;
};

var loc_createComponentValueContext = function(){
	var loc_vueComponent = {
		data : function(){
			return {};
		},
		methods : {
		},
		props : ['data'],
		template : `
			<div>
				<div>
					SolidValueStructure
					<complexinfo-valuestructure 
						v-for="vsId in data.getValueStructureRuntimeIds().solid" 
						v-bind:key="vsId"
						v-bind:data="data.getValueStructure(vsId)"
					/>
			    </div>
				<div>
					SoftValueStructure
					<complexinfo-valuestructure 
						v-for="vs in data.getValueStructureRuntimeIds().soft" 
						v-bind:key="vsId"
						v-bind:data="data.getValueStructure(vsId)"
					/>
			    </div>
		    </div>
		`
	};
	return loc_vueComponent;
};

var loc_createComponentValueStructure = function(){
	
	var loc_contextVariableGroup;
	
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

	var loc_updateView = function(thisContext, requestInfo){
		//variable tree
		var varTree = {};
		var eleVars = loc_contextVariableGroup.getVariables();
		_.each(eleVars, function(eleVar, eleName){
			varTree[eleName] = loc_getVariableTreeInfo(eleVar.prv_getVariable());
		});
		thisContext.dataStr = JSON.stringify(varTree, null, 4);
	};

	var loc_updateView1 = function(thisContext, requestInfo){
		//context data
		var contextContent = {};
		var setRequest = node_createServiceRequestInfoSet({}, {
			success : function(requestInfo, result){
				_.each(result.getResults(), function(contextData, name){
					contextContent[name] = contextData!=undefined?node_dataUtility.getValueOfData(contextData):"EMPTY VARIABLE";
				});
				loc_viewData.val(JSON.stringify(contextContent, null, 4));
			}
		}, requestInfo);
		var eleVars = loc_contextVariableGroup.getVariables();
		_.each(eleVars, function(eleVar, eleName){
			setRequest.addRequest(eleName, loc_env.getDataOperationRequestGet(eleVar));
		});
		node_requestProcessor.processRequest(setRequest, false);
	};

	var loc_vueComponent = {
		data : function(){
			return {
				dataStr : "",
			};
		},
		props : ['data'],
		methods : {
			isSolid : function(){	return this.data.isSolid(); },
			runtimeId : function(){    return this.data.getRuntimeId();   },
		},
		watch: {
		},
		mounted : function(){
			var valueStructure = this.data.getValueStructure();
			loc_contextVariableGroup = node_createContextVariablesGroup(valueStructure, undefined, function(request){
				loc_updateView(this, request);
			});
			_.each(valueStructure.getElementsName(), function(eleName, index){
				loc_contextVariableGroup.addVariable(node_createContextVariableInfo(eleName));
			});
			loc_updateView(this);
		},
		
		template : `
			<div>
				ValueStructure
				<p>isSolid : {{isSolid()}}</p>
				<p>runtimeId : {{runtimeId()}}</p>
				<p>data : {{dataStr}}</p>
				<textarea v-model="dataStr"></textarea>
		    </div>
		`
	};
	return loc_vueComponent;
};


var loc_createComponentVariable = function(){
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
				var varInfo = nosliw.runtime.getUIVariableManager().getVariableInfo(eleVar.prv_id);
				if(varInfo!=undefined){
					var request = varInfo.variable.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(), {
						success : function(request, data){
							that.variableValue = JSON.stringify(data, null, 4);
						}
					});
					node_requestProcessor.processRequest(setRequest, false);
					
				}
			},
		},
		props : ['data'],
		template : `
			<div>
				<div">
				  <input v-model="variableId" placeholder="variable id">
				  <button v-on:click="getVariableValue">GetValue</button>
				  <textarea v-model="variableValue"></textarea>
				</div>
		    </div>
		`
	};
	return loc_vueComponent;
};



var node_createComplexTreeDebugView = function(view){
	
	var loc_view = $('<div></div>');
	loc_view = view;

	var loc_componentData = {
		application : {},
		currentEntityType : "",
		currentEntity : ""
	};
	
	var loc_vue;
	
	var loc_init = function(){
		Vue.component('complextree-runtime', loc_createComponentNodeRuntime());

		Vue.component('complexinfo-package', loc_createComponentInfoPackage());
		Vue.component('complexinfo-bundle', loc_createComponentInfoBundle());
		Vue.component('complexinfo-complexentity', loc_createComponentInfoComplexEntity());
		Vue.component('complexinfo-valuecontext', loc_createComponentValueContext());
		Vue.component('complexinfo-valuestructure', loc_createComponentValueStructure());

		loc_vue = new Vue({
			el: document.getElementById("complextreeDebug"),
//    		  root: "#complextreeDebug",
//			el: loc_view,
			data: loc_componentData,
			methods : {
				getPackageRuntime : function(){
					return this.application.getPackageRuntime==undefined?undefined:this.application.getPackageRuntime();    
				},
				
				onSelectEntity : function(entity) {
					this.currentEntity = entity;
					if(entity!=undefined){
						this.currentEntityType = node_getComponentInterface(entity).getDataType();
					}
					else{
						this.currentEntityType = undefined;
					}
				},
				
			},
			watch: {
			},
			computed : {
				isPackage : function(){
					return "package"== this.currentEntityType;
				},
				isBundle : function(){
					return "bundle"== this.currentEntityType;
				},
				isComplexEntity : function(){
					return this.currentEntityType!=""&&this.currentEntityType!="package"&&this.currentEntityType!="bundle"
				},
			},
			template : `
				<div class="row">
				    <!-- Each "cell" has col-[width in percents] class -->
				    <div class="col col-50 resizable">
						<div class="treeview" style="overflow-y: scroll; height:400px;">
						  	<complextree-runtime 
						  		v-bind:data="getPackageRuntime()"
								v-on:selectEntity="onSelectEntity"
						  	/>
						</div>
						<span class="resize-handler"></span>
				    </div>
				    <div id="infoDiv" class="col col-50 resizable">
				    	<div>
				    		<complexinfo-package v-if="isPackage" v-bind:data="currentEntity"/>
				    		<complexinfo-bundle v-else-if="isBundle" v-bind:data="currentEntity"/>
				    		<complexinfo-complexentity v-else-if="isComplexEntity" v-bind:data="currentEntity"/>
				    	</div>
				    </div>
				</div>
			`
		});
	};

	var loc_setup = function(entity){
		loc_entity = entity;
		
		loc_createEntityView(loc_entity);
	};
	
	var loc_out = {
		
		getView : function(){   return loc_view;   },
		
		setApplication : function(application){
			loc_componentData.application = application;
		}
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

//Register Node by Name
packageObj.createChildNode("createComplexTreeDebugView", node_createComplexTreeDebugView); 

})(packageObj);
