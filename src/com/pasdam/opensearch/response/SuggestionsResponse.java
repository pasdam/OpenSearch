package com.pasdam.opensearch.response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import com.pasdam.opensearch.description.TemplateParameter;
import com.pasdam.opensearch.description.Url;

/**
 * The response body should be returned in JavaScript Object Notation as a JavaScript array of arrays. 
 * Search suggestions are returned as an ordered collection of values. <br/>
 * The four values are returned in the following order: 
 * <ul><li>Query String</li>
 * 	<li>Completions</li>
 * 	<li>Descriptions</li>
 * 	<li>Query URLs</li></ul>
 * @author Paco
 * @version 1.0
 */
public class SuggestionsResponse {
	
	private static final Pattern PATTERN_QUERY_RESULTS_SEPARATOR = Pattern.compile(",\\[");
	private static final Pattern PATTERN_RESULTS_SEPARATOR = Pattern.compile("\",\"");
	
	private Url url;

	public SuggestionsResponse(Url url) {
		this.url = url;
	}
	
	public String[][] getSuggestions(String query, int maxResults) {
		url.setParameterValue(TemplateParameter.SEARCH_TERM, query);
		url.setParameterValue(TemplateParameter.COUNT, "" + maxResults);
		
		try {
			URL suggestionsUrl = new URL(url.getUrl());
			BufferedReader reader = new BufferedReader(new InputStreamReader(suggestionsUrl.openStream()));
			
			// read response
			String line;
			StringBuilder builder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			
			url.clearParametersValues();
			
			return parseJsonResponse(builder.toString());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		url.clearParametersValues();
		return null;
	}
	
	/**
	 * This allow to parse a JSON response
	 * @param jsonResponse
	 * @return an array of array containing the suggestions. The first array contains only one element, 
	 * the suggestion prefix. The second array contains the completions. Other arrays depend on the
	 * specific search engine.
	 */
	public static String[][] parseJsonResponse(String jsonResponse){
		// delete the outer square brackets  
		String response = jsonResponse.substring(1, jsonResponse.length()-1);
		// splits the content
		String[] tokens = PATTERN_QUERY_RESULTS_SEPARATOR.split(response);
		String[][] parsedValues = new String[tokens.length][];
		// retrieve the suggestion prefix (query string)
		parsedValues[0] = new String[]{tokens[0].substring(1, tokens[0].length()-1)};
		// retrieve other contents
		for (int i = 1; i < tokens.length; i++) {
			parsedValues[i]= PATTERN_RESULTS_SEPARATOR.split(tokens[i].substring(1, tokens[i].length()-2));
		}
		return parsedValues;
	}
}
