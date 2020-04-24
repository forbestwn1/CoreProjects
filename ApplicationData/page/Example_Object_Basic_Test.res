<!DOCTYPE html>
<html>
<body>


	<br>
	Content:<%=#|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value + ' 6666 ' %>

	<br>
<!--	TextInput:<nosliw-textinput data="business.a.aa"/>

	<br>
	<nosliw-contexttree></nosliw-contexttree>
	<br>
	<nosliw-contextvalue></nosliw-contextvalue>
  -->
</body>

	<scripts>
	{
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
