/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_createComponentQuestionItemService;
	var node_createComponentQuestionItemSwitch;
	var node_createComponentQuestionItemConstant;
	var node_createComponentQuestionItemVariable;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionItem = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		props : ['data'],
		components : {
			'question-item-service': node_createComponentQuestionItemService(),
			'question-item-switch': node_createComponentQuestionItemSwitch(),
			'question-item-constant': node_createComponentQuestionItemConstant(),
			'question-item-variable': node_createComponentQuestionItemVariable(),
		},
		methods : {
		},
		template : `
			<div>
				<question-item-service v-if="data.element.type=='service' && data.element.enable==true" v-bind:data="data"/>
				<question-item-switch v-if="data.element.type=='switch' && data.element.enable==true" v-bind:data="data"/>
				<question-item-constant v-if="data.element.type=='constant' && data.element.enable==true" v-bind:data="data"/>
				<question-item-variable v-if="data.element.type=='variable' && data.element.enable==true" v-bind:data="data"/>
			
				QuestionItem
			</div>
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.builder.createComponentQuestionItemService", function(){node_createComponentQuestionItemService = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.builder.createComponentQuestionItemSwitch", function(){node_createComponentQuestionItemSwitch = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.builder.createComponentQuestionItemConstant", function(){node_createComponentQuestionItemConstant = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.builder.createComponentQuestionItemVariable", function(){node_createComponentQuestionItemVariable = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItem", node_createComponentQuestionItem); 

})(packageObj);
