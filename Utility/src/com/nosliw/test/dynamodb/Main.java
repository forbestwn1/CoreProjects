package com.nosliw.test.dynamodb;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

public class Main {

	public static void main(String[] args) {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "nosliw")).build();
		DynamoDB docClient = new DynamoDB(client);
		
		Table table = docClient.getTable("UserAppData");
		
		Item item = new Item();
		item.withString("User", "1111");
		item.withString("Application", "2222");
		
		table.putItem(item);
		
	}
	
}
