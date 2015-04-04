package com.pasdam.opensearch.response;

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
public class SuggestResponse {
	
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
		String[] tokens = response.split(",[");
		String[][] parsedValues = new String[tokens.length][];
		// retrieve the suggestion prefix (query string)
		parsedValues[0] = new String[]{tokens[0].substring(1, tokens[0].length()-1)};
		// retrieve other contents
		for (int i = 1; i < tokens.length; i++) {
			parsedValues[i]= tokens[i].substring(1, tokens[i].length()-2).split("\",\"");
		}
		return parsedValues;
	}
}
