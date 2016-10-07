package com.nosliw.datasource.realtor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.datasource.HAPDataSource;

public class HAPDataSoruceRealtor  implements HAPDataSource{

	private HAPDataTypeManager m_dataTypeMan;
	
	public HAPDataSoruceRealtor(HAPDataTypeManager dataTypeMan){
		this.m_dataTypeMan = dataTypeMan;
	}

	public static void main(String[] args) throws Exception{
		sendPost();
	}
	
	@Override
	public HAPData getData() {
		return null;
	}

	// HTTP POST request
		private static void sendPost() throws Exception {

			String url = "https://api2.realtor.ca/Listing.svc/PropertySearch_Post";
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			//add reuqest header
			con.setRequestMethod("POST");
//			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("PropertyTypeGroupID", "application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
			con.setRequestProperty("Accept", "*/*");

			String urlParameters = "CultureId=1&ApplicationId=1&RecordsPerPage=90&MaximumResults=90&PropertySearchTypeId=1&TransactionTypeId=2&StoreyRange=0-0&BedRange=0-0&BathRange=0-0&LongitudeMin=-80.444052&LongitudeMax=-78.570883&LatitudeMin=43.168231&LatitudeMax=44.107412&SortOrder=A&SortBy=1&viewState=m&Longitude=-79.312311&Latitude=43.755543&ZoomLevel=5&CurrentPage=1&PropertyTypeGroupID=1";

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			System.out.println(response.toString());

		}	
	
}
