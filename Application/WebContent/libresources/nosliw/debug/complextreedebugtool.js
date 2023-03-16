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
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_createComponentNodeRuntime = function(){
	var loc_vueComponent = {
		data : function(){
			return {};
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
				this.$emit("selectEntity", this.data);
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
		},
		props : ['data'],
		template : `
			<div>
				ComplexEntity
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
						this.currentEntityType = node_getComponentInterface(entity.getCoreEntity()).getDataType();
					}
					else{
						this.currentEntityType = undefined;
					}
				},
				
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
					Hello Candada
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
				    		<complexinfo-complexentity v-else-if v-show="isComplexEntity" v-bind:data="currentEntity"/>
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

//Register Node by Name
packageObj.createChildNode("createComplexTreeDebugView", node_createComplexTreeDebugView); 

})(packageObj);
