package com.pasdam.opensearch.description;

import java.security.InvalidParameterException;

/**
 * This enumeration contains all possible url role values:
 * <ul>
 * <li>"results" (default) - Represents a request for search results in the specified format.</li>
 * <li>"suggestions" - Represents a request for search suggestions in the specified format.</li>
 * <li>"self" - Represents the canonical URL of this description document.</li>
 * <li>"collection" - Represents a request for a set of resources.</li>
 * </ul>
 * 
 * @author Paco
 * @version 1.0
 */
public enum UrlRole {
	
	RESULTS, SUGGESTIONS, SELF, COLLECTION;
	
	public static final String ROLE_COLLECTION = "collection";
	public static final String ROLE_SELF = "self";
	public static final String ROLE_SUGGESTIONS = "suggestions";
	public static final String ROLE_RESULTS = "results";

	/**
	 * @param role - the int role
	 * @return a string representing the role
	 */
	public String toString(){
		switch (this) {
		case RESULTS:
			return ROLE_RESULTS;
		case SUGGESTIONS:
			return ROLE_SUGGESTIONS;
		case SELF:
			return ROLE_SELF;
		case COLLECTION:
			return ROLE_COLLECTION;
		default:
			return "";
		}
	}
	
	/**
	 * This method return an UrlRole by parsing an input string.
	 * @param role - the input role
	 * @return a int representing the role
	 * @throws InvalidParameterException - if role is an unknown value
	 */
	public static UrlRole fromString(String role) throws InvalidParameterException {
		if(role.equalsIgnoreCase(ROLE_RESULTS)) {
			return RESULTS;
		} else if(role.equalsIgnoreCase(ROLE_SUGGESTIONS)) {
			return SUGGESTIONS;
		} else if(role.equalsIgnoreCase(ROLE_SELF)) {
			return SELF;
		} else if(role.equalsIgnoreCase(ROLE_COLLECTION)) {
			return COLLECTION;
		} else {
			throw new InvalidParameterException("Invalid Url role: " + role);
		}
	}
}
