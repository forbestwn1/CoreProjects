<!DOCTYPE html>
<html>
	<body>
	
	<br>
	<nosliw-submit title="Submit" datasource="school" parms="schoolType:criteria.schoolType;schoolRating:criteria.schoolRating"  output="result"/>  
	<br>

	<div>
	Result
	<br>
	<nosliw-include source="Example_App_Query_School"/> 
	</div>
	
	<div>
	Result
	<br>
	<nosliw-include source="Example_App_Result_School"/> 
	</div>

	<nosliw-debug/>
	
	<br>
	</body>

	<script>
	{
	}
	</script>
	
	<constants>
	{
	}
	</constants>
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<context>
	{
	}
	</context>
	
		<!-- This part can be used to define expressions
		-->
	<expressions>
	{
		
	
	}
	</expressions>
	
</html>

