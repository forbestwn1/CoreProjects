/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_createComponentQuestionItemService;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionItem = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		props : ['data'],
		components : {
			'question-item-service': node_createComponentQuestionItemService()
		},
		methods : {
			onSelectMiniApp : function(miniAppId) {
				this.$emit("selectMiniApp", miniAppId);
			},
		},
		template : `
			<div>
				<question-item-service v-if="data.element.type=='service'" v-bind:data="data"/>
			
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

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItem", node_createComponentQuestionItem); 

})(packageObj);
