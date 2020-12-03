<!DOCTYPE html>
<html>
<body>
		<nosliw-style>
			.red1 {
			  color: red;
			}
			.intag {
			  color: red;
			}
			
		</nosliw-style>

		<div>
		<span class="red1">HHHHHHHHHHHHHHHHHHHHHHHHHHHHHH</span>
		<br>
		</div>
		
		<nosliw-loop data="business.a.cc" element="ele" index="index">  

			<nosliw-style>
				.intag {
				  color: blue;
				}
			</nosliw-style>
	
			<span class="intag">BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB</span>
		</nosliw-loop>
  

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
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<contexts>
	{
		group : {
			public : {
				element : {
					business : {
						definition: {
							child : {
								a : {
									child : {
										aa : {criteria:"test.string;1.0.0"},
										cc : {criteria:"test.array;1.0.0%||element:test.string;1.0.0||%"},
									}
								}
							}
						},
						defaultValue: {
							a : {
								aa : {
									dataTypeId: "test.string;1.0.0",
									value: "This is my world!"
								},
								cc : [
									{
										dataTypeId: "test.string;1.0.0",
										value: "This is my world 1111!"
									},
									{
										dataTypeId: "test.string;1.0.0",
										value: "This is my world 2222!"
									}
								],
								ee : {
									dataTypeId: "test.array;1.0.0",
									value: [
										{
											dataTypeId: "test.string;1.0.0",
											value: "This is my world 1111!"
										},
										{
											dataTypeId: "test.string;1.0.0",
											value: "This is my world 2222!"
										}
									]
								}
							}
						}
					},
					aaaa : {
						definition: {
							value : "<%=5+6+7%>",
						}
					},
					bbbb : {
						definition: {
							value : "<%=(5+6+7)>5%>"
						}
					},
					cccc : {
						definition: {
							value : {
								a : 12345,
								b : true,
								c : "good",
								d : "<%=5+6+7%>"
							}
						}
					},
					dddd : {
						definition : {
							value : "<%=&(cccc)&.a+6%>"
						}
					},
					ffff : {
						definition: {
							value : "<%=#|&(#test##string___Thisismyworldabcdef)&|#%>"
						}
					},
					eeee : {
						definition: {
							value : "<%=#|&(ffff)&.subString(from:&(#test##integer___3)&,to:&(#test##integer___7)&)|#%>"
						}
					},
					base: {
						definition : {
							value : {
								dataTypeId: "test.string",
								value: "This is my world!"
							}
						}
					},
					from: {
						definition : {
							value : {
								dataTypeId: "test.integer",
								value: 3
							}
						}
					},
					to: {
						definition:{
							value : {
								dataTypeId: "test.integer",
								value: 7
							}
						}
					}
				}
			}
		}
	}
	</contexts>
	
	<events>
	[
	]
	</events>
	
	<commands>
	[
	]
	</commands>
	
	<attachment>
	{
		"expression" : [
		],
		
		"data": [
					
		]
	}
	</attachment>

</html>
