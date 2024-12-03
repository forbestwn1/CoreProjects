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
	var node_createComponentQuestionItemUIData;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionItem = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		props : ['question', 'story'],
		components : {
			'question-item-service': node_createComponentQuestionItemService(),
			'question-item-switch': node_createComponentQuestionItemSwitch(),
			'question-item-constant': node_createComponentQuestionItemConstant(),
			'question-item-uidata': node_createComponentQuestionItemUIData(),
		},
		methods : {
			onAnswerChange : function(answerData) {
				this.$emit("answerChange");
				if(answerData==undefined)	this.question.answered = false;
				else  this.question.answered = true;
			},
		},
		template : `
			<div v-if="question.element.enable==true">
<!--				{{question.question}}<span v-if="question.isMandatory==true">*</span> :  -->
				<span>
				<question-item-service v-if="question.element.type=='service'" v-bind:question="question" v-bind:story="story" v-on:answerChange="onAnswerChange"/>
				<question-item-switch v-if="question.element.type=='switch'" v-bind:question="question" v-bind:story="story" v-on:answerChange="onAnswerChange"/>
				<question-item-constant v-if="question.element.type=='constant'" v-bind:question="question" v-bind:story="story" v-on:answerChange="onAnswerChange"/>
				<question-item-uidata v-if="question.element.type=='UI_tagData'" v-bind:question="question" v-bind:story="story" v-on:answerChange="onAnswerChange"/>
				</span>
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
nosliw.registerSetNodeDataEvent("application.story.module.builder.createComponentQuestionItemUIData", function(){node_createComponentQuestionItemUIData = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItem", node_createComponentQuestionItem); 

})(packageObj);
