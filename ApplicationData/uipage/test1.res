<!DOCTYPE html>
<html>
<body>
		<span class="red1">HHHHHHHHHHHHHHHHHHHHHHHHHHHHHH</span>
		<div>
		<span class="red1">HHHHHHHHHHHHHHHHHHHHHHHHHHHHHH</span>
		<br>
		</div>
		
		<br>
		Content:<%=?(baseVarNormal)?.value + '   6666 ' %>
		<br>
  	<br>
	Attribute:<span style="color:<%=?(attr1)?.value=='1234'?'red':'blue'%>">Phone Number : </span> 
	<br>
		<br>
		<br><a href='' nosliw-event="click:newElementInLoop:">New</a><br>
		<br>
  
</body>

	<script>
	{
		newElementInLoop : function(data, info, env){
			console.log(data);
		},
		
		textInputValueChanged : function(info, env){
			env.trigueEvent("changeInputText", info.eventData);
		},
		
		command_Start :function(data, env){
			return data.data + "   Start";
		},
	}
	</script>
	
	<valuecontext>
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
					
					"attr1": {
						"definition":{
							"criteria": "test.string;1.0.0"
						},
						"defaultValue": {
							"dataTypeId": "test.string;1.0.0",
							"value": "1234"
						}
					}
				}
			}
		]
	}
	</valuecontext>
	
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
