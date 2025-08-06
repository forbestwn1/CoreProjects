<!DOCTYPE html>
<html>
<body>
	<br>
	SchoolRating:<nosliw-float data="schoolRating"/>  
	<br>

	<br>
	SchoolType:<nosliw-options id="schoolType" data="schoolType"/>
	<br>

</body>

	<context>
	{
		public : {
			schoolType : {
				definition : "test.options;1.0.0",
				default : {
					dataTypeId: "test.options;1.0.0",
					value: {
						value : "Public",
						optionsId : "schoolType"
					}
				}
			},
			schoolRating : {
				definition : "test.float;1.0.0",
				default : {
					schoolRating : {
						dataTypeId: "test.float;1.0.0",
						value: 9.0
					},
				}
			},
		}
	}
	</context>

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
