//get/create package
var packageObj = library.getChildPackage();    


(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_getComponentLifecycleInterface;
	var node_getComponentManagementInterface;
	var node_createServiceRequestInfoSimple;
	var node_requestServiceProcessor;
	var node_getComponentManagementInterface;
	var node_componentUtility;
	var node_getStateMachineDefinition;
	
//*******************************************   Start Node Definition  ************************************** 	

	
var loc_createComponentRuntime = function(entity){
	var loc_vueComponent = {
		data : function(){
			return {};
		},
		methods : {
			getDataType : function(){   return this.data==undefined?undefined:this.data.getCoreEntity().getDataType();  },
			
			getCoreEntity : function(){   return this.data==undefined?undefined:this.data.getCoreEntity();   },
		
		},
		props : ['data'],
		template : `
			<div class="treeview-item">
				    <a class="treeview-item-root treeview-item-selectable">
					      <div class="treeview-item-content">
								<i class="icon f7-icons">doc</i>
								<div class="treeview-item-label">{{getDataType()}}</div>
					      </div>
				    </a>
			    <div class="treeview-item-children">
					<complextree-core
						v-bind:data="getCoreEntity()"
					/>
			    </div>
		    </div>
		`
	};
	return loc_vueComponent;
};

var loc_createComponentCore = function(){
	var loc_vueComponent = {
			data : function(){
				return {};
			},
			methods : {
				getDataType : function(){   return this.data==undefined?undefined:this.data.getCoreEntity().getDataType();  },
				
				getCoreEntity : function(){   return this.data==undefined?undefined:this.data.getCoreEntity();   },
			
			},
			props : ['data'],
			template : `
				<div class="treeview-item">
					    <a class="treeview-item-root treeview-item-selectable">
						      <div class="treeview-item-content">
									<i class="icon f7-icons">doc</i>
									<div class="treeview-item-label">{{getDataType()}}</div>
						      </div>
					    </a>
			    </div>
			`
		};
		return loc_vueComponent;
};

var loc_createComponentDecoration = function(){
	
};



var loc_createComponentPackage = function(){
	
};

var loc_createComponentBundle = function(){
	
};

var loc_createComponentComplexEntity = function(){
	
};



//component lifecycle 
var node_createComplexTreeDebugView1 = function(view){
	
	var loc_view = $('<div></div>');
	loc_view = view;

	var loc_componentData = {
		application : {},
		info : {}
	};
	
	var loc_vue;
	
	var loc_init = function(){
		
		loc_vue = new Vue({
			el: document.getElementById("complextreeDebug"),
//    		  root: "#complextreeDebug",
//			el: loc_view,
			data: loc_componentData,
			methods : {
			},
			template : `
				<div>
					Hello Candada
				</div>
			`
		});
	};

	
	var loc_out = {
		
		getView : function(){   return loc_view;   },
		
		setEntity : function(entity){
		}
	};
	
	loc_init();
	return loc_out;
};
	
var node_createComplexTreeDebugView = function(view){
	
	var loc_view = $('<div></div>');
	loc_view = view;

	var loc_componentData = {
		application : {},
		info : {}
	};
	
	var loc_vue;
	
	var loc_init = function(){
		Vue.component('complextree-runtime', loc_createComponentRuntime());
		Vue.component('complextree-core', loc_createComponentCore());
		Vue.component('complextree-decoration', loc_createComponentDecoration());
		Vue.component('complextree-package', loc_createComponentPackage());
		Vue.component('complextree-bundle', loc_createComponentBundle());
		Vue.component('complextree-complexentity', loc_createComponentComplexEntity());
		
		loc_vue = new Vue({
			el: document.getElementById("complextreeDebug"),
//    		  root: "#complextreeDebug",
//			el: loc_view,
			data: loc_componentData,
			methods : {
				getPackageRuntime : function(){
					return this.application.getPackageRuntime==undefined?undefined:this.application.getPackageRuntime();    
				},
				
				onSelectResource : function(resourceInfo) {
					loc_componentData.info = resourceInfo;
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
								v-on:selectResource="onSelectResource"
						  	/>
						</div>
						<span class="resize-handler"></span>
				    </div>
				    <div id="infoDiv" class="col col-50 resizable">
				    	<div>
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
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentManagementInterface", function(){node_getComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.getStateMachineDefinition", function(){node_getStateMachineDefinition = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComplexTreeDebugView", node_createComplexTreeDebugView); 

})(packageObj);
