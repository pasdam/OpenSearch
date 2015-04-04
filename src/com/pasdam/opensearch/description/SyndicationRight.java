package com.pasdam.opensearch.description;

import java.security.InvalidParameterException;

/**
 * Contains a value that indicates the degree to which the search results provided by this 
 * search engine can be queried, displayed, and redistributed.
 * Values: The value must be one of the following:<br/>
 * <ul>
 * 	<li>"open": <br/>
 * 		<ul>
 * 			<li>The search client may request search results.</li>
 * 			<li>The search client may display the search results to end users.</li>
 * 			<li>The search client may send the search results to other search clients.</li>
 * 		</ul>
 * 	</li>
 * 	<li>"limited": <br/>
 * 		<ul>
 * 			<li>The search client may request search results.</li>
 * 			<li>The search client may display the search results to end users.</li>
 * 			<li>The search client may not send the search results to other search clients.</li>
 * 		</ul>
 * 	</li>
 * 	<li>"private": <br/>
 * 		<ul>
 * 			<li>The search client may request search results.</li>
 * 			<li>The search client may not display the search results to end users.</li>
 * 			<li>The search client may not send the search results to other search clients.</li>
 * 		</ul>
 * 	</li>
 * 	<li>"closed": <br/>
 * 		<ul>
 * 			<li>The search client may not request search results.</li>
 * 		</ul>
 * 	</li>
 * </ul>
 * @author Paco
 * @version 1.0
 */
public enum SyndicationRight {
	
	OPEN, LIMITED, PRIVATE, CLOSED;

	/**
	 * Returns the type of the input value
	 * @param value - the String value to convert
	 * @return the type of the input value
	 * @throws InvalidParameterException - if the input string is unknown
	 */
	public static SyndicationRight fromString(String value) throws InvalidParameterException{
		if (value.equals("open")) {
			return OPEN;
		} else if (value.equals("limited")) {
			return LIMITED;
		} else if (value.equals("private")) {
			return PRIVATE;
		} else if (value.equals("closed")) {
			return CLOSED;
		} else {
			throw new InvalidParameterException("Invalid SyndicationRight value: " + value);
		}
	}
	
	/**
	 * Returns the OpenSearch value<br/>
	 * 
	 * @param type - the int type of the value
	 * @return the OpenSearch string parameter
	 */
	public static String toString(SyndicationRight type) {
		if (type != null) {
			switch (type) {
			case OPEN:
				return "open";
			case LIMITED:
				return "limited";
			case PRIVATE:
				return "private";
			case CLOSED:
				return "closed";
			}
		}
		return null;
	}
	
	/**
	 * Returns the OpenSearch value<br/>
	 * @return the OpenSearch value
	 */
	@Override
	public String toString() {
		return toString(this);
	}
}
