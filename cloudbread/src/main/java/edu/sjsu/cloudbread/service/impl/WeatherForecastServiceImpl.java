package edu.sjsu.cloudbread.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import edu.sjsu.cloudbread.service.WeatherForecastService;

/**
* 
* @author Ashwini Shankar Narayan
*
**/
@Service
public class WeatherForecastServiceImpl implements WeatherForecastService {
	
	@Override
	public List<String> getForecast()
	{
        /// Forecast weather for next 5 days 
		
        //inline will store the JSON data streamed in string format
		String inline = "";
	
		try
		{
			URL url = new URL("https://api.openweathermap.org/data/2.5/forecast?id=5392171&APPID=37939ebe28a91336fa1422066657dcbd");
			//Parse URL into HttpURLConnection in order to open the connection in order to get the JSON data
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//Set the request to GET or POST as per the requirements
			conn.setRequestMethod("GET");
			//Use the connect method to create the connection bridge
			conn.connect();
			//Get the response status of the Rest API
			int responsecode = conn.getResponseCode();
			
			//Iterating condition to if response code is not 200 then throw a runtime exception
			//else continue the actual process of getting the JSON data
			if(responsecode != 200)
				throw new RuntimeException("HttpResponseCode: " +responsecode);
			else
			{
				//Scanner functionality will read the JSON data from the stream
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					inline+=sc.nextLine();
				}
				//Close the stream when reading the data has been finished
				sc.close();
			}
			
			//JSONParser reads the data from string object and break each data into key value pairs
			JSONParser parse = new JSONParser();
			//Type caste the parsed json data in json object
			JSONObject jobj = (JSONObject)parse.parse(inline);
			//Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
			JSONArray jsonarr_1 = (JSONArray) jobj.get("list");

			List<String> result = new ArrayList<String>();  

			//Get data for Results array
			for(int i=0;i<jsonarr_1.size();i++)
			{
				//Store the JSON objects in an array
				//Get the index of the JSON object and print the values as per the index
				JSONObject jsonobj_1 = (JSONObject)jsonarr_1.get(i);
			
				String date1 = (String) jsonobj_1.get("dt_txt");
				String[] splited = date1.split(" ");
				if(splited[1].equals("12:00:00")) {
					String date = splited[0];
					JSONArray weather_arr = (JSONArray)jsonobj_1.get("weather");
					JSONObject weather =(JSONObject)weather_arr.get(0);
					String weather_main = (String)weather.get("main");
					result.add(weather_main);
				}    					
			}
			//Disconnect the HttpURLConnection stream
			conn.disconnect();
			 return result;
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;        	
	}
	
	@Override
	public String getTodaysWeather()
	{
		/// Find Today's weather
		
		//inline will store the JSON data streamed in string format
		String inline1 = "";
	
		try
		{
			URL url = new URL("http://api.openweathermap.org/data/2.5/weather?id=5392171&APPID=37939ebe28a91336fa1422066657dcbd");
			//Parse URL into HttpURLConnection in order to open the connection in order to get the JSON data
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//Set the request to GET or POST as per the requirements
			conn.setRequestMethod("GET");
			//Use the connect method to create the connection bridge
			conn.connect();
			//Get the response status of the Rest API
			int responsecode = conn.getResponseCode();
			
			//Iterating condition to if response code is not 200 then throw a runtime exception
			//else continue the actual process of getting the JSON data
			if(responsecode != 200)
				throw new RuntimeException("HttpResponseCode: " +responsecode);
			else
			{
				
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					inline1+=sc.nextLine();
				}
				
				sc.close();
			}
				
			JSONParser parse1 = new JSONParser();
			
			JSONObject jobj1 = (JSONObject)parse1.parse(inline1);

			JSONArray jsonarr_2 = (JSONArray) jobj1.get("weather");
			JSONObject weather =(JSONObject)jsonarr_2.get(0);
			String weather_main = (String)weather.get("main");
			System.out.println("\nToday's Weather type: " +weather_main);
            
			//Disconnect the HttpURLConnection stream
			conn.disconnect();
			return weather_main;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	

}
