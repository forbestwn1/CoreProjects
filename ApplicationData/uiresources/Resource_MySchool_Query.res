<!DOCTYPE html>
<html>
<body>
	<br>
	<br>
	SchoolRating:<nosliw-floatinput data="criteria.schoolRating"/>  
	<br>
	<br>
	SchoolRating:<nosliw-floatinput data="criteria.schoolRating"/>  
	<br>

	<br>
	SchoolType:<nosliw-options id="schoolType" data="criteria.schoolType"/>
	<br>

</body>

	<context>
	{
		public : {
			criteria : {
				definition: {
					schoolType : "test.options;1.0.0",
					schoolRating : "test.float;1.0.0",
				},
				default: {
					schoolType : {
						dataTypeId: "test.options;1.0.0",
						value: {
							value : "Public",
							optionsId : "schoolType"
						}
					},
					schoolRating : {
						dataTypeId: "test.float;1.0.0",
						value: 9.0
					},
				}
			},
		}
	}
	</context>

	<event>
	[
	]
	</event>
	
	<service>
	[
	]
	</service>

	<command>
	[
	]
	</command>
	
	
	<script>
	{	
	}
	</script>
	
	<constants>
	{
	}
	</constants>
	

		<!-- This part can be used to define expressions
		-->
	<expressions>
	{	
	}
	</expressions>
	
</html>

