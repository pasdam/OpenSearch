package com.pasdam.opensearch.description;

import java.text.ParseException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Describes a specific search request that can be made by the search client.
 * @author Paco
 * @version 1.0
 */
public class Query {
	
	public static final String ATTR_OUTPUT_ENCODING = "outputEncoding";

	public static final String ATTR_INPUT_ENCODING = "inputEncoding";

	public static final String ATTR_LANGUAGE = "language";

	public static final String ATTR_START_PAGE = "startPage";

	public static final String ATTR_START_INDEX = "startIndex";

	public static final String ATTR_COUNT = "count";

	public static final String ATTR_SEARCH_TERMS = "searchTerms";

	public static final String ATTR_TOTAL_RESULTS = "totalResults";

	public static final String ATTR_TITLE = "title";

	public static final String ATTR_ROLE = "role";

	/**
	 * The tag name of this element
	 */
	public static final String TAG_NAME = "Query";
	
	/**
	 * Required. Contains a string identifying how the search client should interpret the 
	 * search request defined by this Query element.<br/>
	 * A role value consists of an optional prefix followed by the local role value. 
	 * If the prefix is present it will be separated from the local role value with the 
	 * ":" character. All role values are associated with a namespace, either implicitly 
	 * in the case of local role values, or explicitly via a prefix in the case of fully 
	 * qualified role values.<br/>
	 * Local role values:
	 * <ul><li>"request" - Represents the search query that can be performed to retrieve the 
	 * 		same set of search results.</li>
	 *  <li>"example" - Represents a search query that can be performed to demonstrate the 
	 * 		search engine.</li>
	 *  <li>"related" - Represents a search query that can be performed to retrieve similar 
	 *  	but different search results.</li>
	 *  <li>"correction" - Represents a search query that can be performed to improve the 
	 *  	result set, such as with a spelling correction.</li>
	 *  <li>"subset" - Represents a search query that will narrow the current set of search 
	 *  	results.</li>
	 *  <li>"superset" - Represents a search query that will broaden the current set of 
	 *  	search results.</li></ul>
	 */
	public String role;
	
	/**
	 * Contains a human-readable plain text string describing the search request.<br/>
	 * Restrictions: The value must contain 256 or fewer characters of plain text. The 
	 * value must not contain HTML or other markup.<br/>
	 * Requirements: This attribute is optional.
	 */
	public String title;
	
	/**
	 * Contains the expected number of results to be found if the search request were made.<br/>
	 * Restrictions: The value is a non-negative integer.<br/>
	 * Requirements: This attribute is optional.
	 */
	public int totalResults;
	
	/**
	 * Contains the value representing the "searchTerms" as an OpenSearch 1.1 parameter.<br/>
	 * Requirements: This attribute is optional.
	 */
	public String searchTerms;
	
	/**
	 * Contains the value representing the "count" as a OpenSearch 1.1 parameter.<br/>
	 * Requirements: This attribute is optional.
	 */
	public int count;
	
	/**
	 * startIndex - Contains the value representing the "startIndex" as an OpenSearch 1.1 parameter.<br/>
	 * Requirements: This attribute is optional.
	 */
	public int startIndex;
	
	/**
	 * Contains the value representing the "startPage" as an OpenSearch 1.1 parameter.<br/>
	 * Requirements: This attribute is optional.
	 */
	public int startPage;
	
	/**
	 * Contains the value representing the "language" as an OpenSearch 1.1 parameter.<br/>
	 * Requirements: This attribute is optional.
	 */
	public String language;
	
	/**
	 * Contains the value representing the "inputEncoding" as an OpenSearch 1.1 parameter.<br/>
	 * Requirements: This attribute is optional.
	 */
	public String inputEncoding;
	
	/**
	 * Contains the value representing the "outputEncoding" as an OpenSearch 1.1 parameter.<br/>
	 * Requirements: This attribute is optional.
	 */
	public String outputEncoding;
	
	/**
	 * @param queryElement - the element containing parameter's attributes
	 * @return an Image object or null, in case of errors
	 * @throws ParseException if the element is not valid
	 */
	public static Query parse(Node queryElement) throws ParseException{
		NamedNodeMap attributes = queryElement.getAttributes();
		Query query = new Query();
		try {
			query.role = attributes.getNamedItem(ATTR_ROLE).getNodeValue().trim();
		} catch (Exception e) {
			throw new ParseException("Attribute \"" + ATTR_ROLE + "\" not found!", 0);
		}
		try {
			query.title = attributes.getNamedItem(ATTR_TITLE).getNodeValue().trim();
		} catch (Exception e) {
			query.title = null;
		}
		try {
			query.totalResults = Integer.parseInt(attributes.getNamedItem(ATTR_TOTAL_RESULTS).getNodeValue().trim());
			if (query.totalResults < 0) {
				query.totalResults = 0;
			}
		} catch (Exception e) {
			query.totalResults = 0;
		}
		try {
			query.searchTerms = attributes.getNamedItem(ATTR_SEARCH_TERMS).getNodeValue().trim();
		} catch (Exception e) {
			query.searchTerms = null;
		}
		try {
			query.count = Integer.parseInt(attributes.getNamedItem(ATTR_COUNT).getNodeValue().trim());
			if (query.count < 0) {
				query.count = 0;
			}
		} catch (Exception e) {
			query.count = 0;
		}
		try {
			query.startIndex = Integer.parseInt(attributes.getNamedItem(ATTR_START_INDEX).getNodeValue().trim());
		} catch (Exception e) {
			query.startIndex = 0;
		}
		try {
			query.startPage = Integer.parseInt(attributes.getNamedItem(ATTR_START_PAGE).getNodeValue().trim());
		} catch (Exception e) {
			query.startPage = 0;
		}
		try {
			query.language = attributes.getNamedItem(ATTR_LANGUAGE).getNodeValue().trim();
		} catch (Exception e) {
			query.language = "*";
		}
		try {
			query.inputEncoding = attributes.getNamedItem(ATTR_INPUT_ENCODING).getNodeValue().trim();
		} catch (Exception e) {
			query.inputEncoding = "UTF-8";
		}
		try {
			query.outputEncoding = attributes.getNamedItem(ATTR_OUTPUT_ENCODING).getNodeValue().trim();
		} catch (Exception e) {
			query.outputEncoding = "UTF-8";
		}
		return query;
	}

	/**
	 * Returns the xml tag of this element
	 * @return the xml tag of this element
	 */
	@Override
	public String toString() {
		StringBuilder tag = new StringBuilder();
		tag.append("<" + TAG_NAME + " role=\"" + role + "\"");
		if (this.title != null) {
			tag.append(" ");tag.append(ATTR_TITLE);tag.append("=\"");tag.append(this.title);tag.append("\"");
		}
		if (this.totalResults > 0) {
			tag.append(" ");tag.append(ATTR_TOTAL_RESULTS);tag.append("=\"");tag.append(this.totalResults);tag.append("\"");
		}
		if (this.searchTerms != null) {
			tag.append(" ");tag.append(ATTR_SEARCH_TERMS);tag.append("=\"");tag.append(this.searchTerms);tag.append("\"");
		}
		if (this.count > 0) {
			tag.append(" ");tag.append(ATTR_COUNT);tag.append("=\"");tag.append(this.count);tag.append("\"");
		}
		if (this.startIndex > 0) {
			tag.append(" ");tag.append(ATTR_START_INDEX);tag.append("=\"");tag.append(this.startIndex);tag.append("\"");
		}
		if (this.startPage > 0) {
			tag.append(" ");tag.append(ATTR_START_PAGE);tag.append("=\"");tag.append(this.startPage);tag.append("\"");
		}
		if (this.language != null) {
			tag.append(" ");tag.append(ATTR_LANGUAGE);tag.append("=\"");tag.append(this.language);tag.append("\"");
		}
		if (this.inputEncoding != null) {
			tag.append(" ");tag.append(ATTR_INPUT_ENCODING);tag.append("=\"");tag.append(this.inputEncoding);tag.append("\"");
		}
		if (this.outputEncoding != null) {
			tag.append(" ");tag.append(ATTR_OUTPUT_ENCODING);tag.append("=\"");tag.append(this.outputEncoding);tag.append("\"");
		}
		tag.append(" />");
		return tag.toString();
	}
}
