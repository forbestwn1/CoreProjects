<!DOCTYPE html>
<html>
<body>
		<span class="red1">HHHHHHHHHHHHHHHHHHHHHHHHHHHHHH</span>
		<div>
		<span class="red1">HHHHHHHHHHHHHHHHHHHHHHHHHHHHHH</span>
		<br>
		</div>
		
		<nosliw-test data="business.a.cc" element="ele" index="index">  
			<span class="intag">BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB</span>
		</nosliw-test>
  
</body>

	<scripts>
	{
		newElementInLoop : function(data, info, env){

		},
		
		textInputValueChanged : function(info, env){
			env.trigueEvent("changeInputText", info.eventData);
		},
		
		command_Start :function(data, env){
			return data.data + "   Start";
		},
	}
	</scripts>
	
	<valueContext>
	{
		"entity": [
			{
				"groupType" : "public",
				"valueStructure" : {
					"baseVarNormal": {
						"definition":{
							"criteria": "test.string;1.0.0"
						},
						"defaultValue": {
							"dataTypeId": "test.string;1.0.0",
							"value": "default value of baseVarNormal"
						}
					},
					"baseVarMatcher": {
						"definition":{
							"criteria": "test.url;1.0.0"
						},
						"defaultValue": {
							"dataTypeId": "test.url;1.0.0",
							"value": "default value of testvar3"
						}
					},
					"parm1" : {
						"definition": {
							"criteria" : "test.integer"
						},
						"defaultValue": {
							"dataTypeId": "test.integer;1.0.0",
							"value": 5
						}
					},
					"parm2" : {
						"definition": {
							"criteria": "test.integer"
						},
						"defaultValue": {
							"dataTypeId": "test.integer;1.0.0",
							"value": 7
						}
					}
				}
			}
		]
	}
	</valueContext>
	
	<attachment>
	{
		"extra": {
			"status": "disabled1",
			"name": "parent attachment" 
		},
		"entity": {
			"data" : [
				{
					"name" : "constantBase",
					"entity": {
						"dataTypeId": "test.string;1.0.0",
						"value": "012345678901234567890"
					}
				},
				{
					"name" : "constantFrom",
					"entity": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 5
					}
				},
				{
					"name" : "constantTo",
					"entity": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 7
					}
				}
			]
		}
	}
	</attachment>

</html>
