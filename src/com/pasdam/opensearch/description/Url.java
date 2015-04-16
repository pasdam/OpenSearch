package com.pasdam.opensearch.description;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Describes an interface by which a client can make requests for an external resource, such as 
 * search results, search suggestions, or additional description documents.
 * @author Paco
 * @version 1.0
 */
public class Url {
	
	public static final String ATTR_PARAMETERS_ENCTYPE = "parameters:enctype";

	public static final String ATTR_PARAMETERS_METHOD = "parameters:method";

	public static final String ATTR_PAGE_OFFSET = "pageOffset";

	public static final String ATTR_INDEX_OFFSET = "indexOffset";

	public static final String ATTR_REL = "rel";

	public static final String ATTR_TYPE = "type";

	public static final String ATTR_TEMPLATE = "template";
	
	private static final Pattern PATTERN_REL_SEPARATOR = Pattern.compile(" ");
	private static final Pattern PATTERN_QUERY_STRING_SEPARATOR = Pattern.compile("\\?");
	private static final Pattern PATTERN_QUERY_STRING_PARAMETERS_SEPARATOR = Pattern.compile("&");

	/**
	 * The tag name of this element
	 */
	public static final String TAG_NAME = "Url";
	
	/**
	 * The URL template to be processed according to the OpenSearch URL template syntax
	 */
	public String template;
	
	/**
	 * The MIME type of the resource being described<br/>
	 * Examples: text/html, application/rss+xml, application/json, application/opensearchdescription+xml,
	 * application/x-suggestions+json
	 */
	public String type;
	
	/**
	 * The role of the resource being described in relation to the description document.<br/>
	 * Possible values: REL_RESULTS (default), REL_SUGGESTIONS, REL_SELF, REL_COLLECTION.
	 */
	public List<UrlRole> rel;
	
	/**
	 * The index number of the first search result
	 */
	public int indexOffset = 1;
	
	/**
	 * The page number of the first set of search results
	 */
	public int pageOffset = 1;
	
	/**
	 * Contains a case-insensitive string that identifies the HTTP request method that the search 
	 * client should use to perform the search request.<br/>
	 * 
	 * Restrictions: Must be a valid HTTP request method, as specified in RFC 2616.<br/>
	 * Default: "get"
	 * 
	 * Specified in: OpenSearch Parameter extension.
	 */
	public String method = "get";
	
	/**
	 * Contains a string that identifies the content encoding the search client should use to 
	 * perform the search request.<br/>
	 * 
	 * Default: "application/x-www-form-urlencoded"<br/>
	 * Requirements: This attribute is optional.
	 * 
	 * Specified in: OpenSearch Parameter extension.
	 */
	public String enctype = "application/x-www-form-urlencoded";
	
	/**
	 * List of query url's parameters
	 */
	public List<Parameter> parameters = new ArrayList<Parameter>();
	
	/**
	 * This method parse an xml Url element to create an object of this class
	 * @param urlNode - the url element
	 * @return an object of this class, with parsed attributes, or null, in case of errors
	 * @throws ParseException If element is invalid
	 */
	public static Url parse(Node urlNode) throws ParseException{
		Url url = new Url();
		NamedNodeMap attributes = urlNode.getAttributes();
		
		try {
			String[] templateParts = PATTERN_QUERY_STRING_SEPARATOR.split(attributes.getNamedItem(ATTR_TEMPLATE).getNodeValue().trim());
			url.template = templateParts[0];
			if (templateParts.length > 1) {
				url.parameters = parseQueryString(templateParts[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParseException("Invalid attribute \"" + ATTR_TEMPLATE + "\"!", 0);
		}
		
		try {
			url.type = attributes.getNamedItem(ATTR_TYPE).getNodeValue().trim();
		} catch (Exception e) {}
		if (url.type != null && !url.type.matches("^[-\\w]+/[-\\w\\+]+$")) {
			throw new ParseException("Invalid attribute \"" + ATTR_TYPE + "\"!", 0);
		}
			
		Node attribute = attributes.getNamedItem(ATTR_REL);
		url.rel = new ArrayList<UrlRole>();
		if (attribute != null) {
			
			String relText = attribute.getNodeValue();
			if (relText != null) {
				
				relText = relText.trim();
				if (relText.length() > 0) {
			
					String[] relValues = PATTERN_REL_SEPARATOR.split(relText);
					for (String relValue : relValues) {
						try {
							url.rel.add(UrlRole.fromString(relValue));
						} catch (InvalidParameterException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		if (url.rel.size() == 0) {
			url.rel.add(UrlRole.RESULTS); // default value
		}
		
		attribute = attributes.getNamedItem(ATTR_INDEX_OFFSET);
		if (attribute != null) {
			try {
				url.indexOffset = Integer.parseInt(attribute.getNodeValue().trim());
			} catch (Exception e) {
				e.printStackTrace();
				url.indexOffset = 1;
			}
		} else {
			url.indexOffset = 1;
		}
		
		attribute = attributes.getNamedItem(ATTR_PAGE_OFFSET);
		if (attribute != null) {
			try {
				url.pageOffset = Integer.parseInt(attribute.getNodeValue().trim());
				
			} catch (Exception e) {
				url.pageOffset = 1;
				e.printStackTrace();
			}
			
		} else {
			url.pageOffset = 1;
		}
			
		try {
			url.method = attributes.getNamedItem(ATTR_PARAMETERS_METHOD).getNodeValue().trim();
			if (!url.method.equalsIgnoreCase("get") || !url.method.equalsIgnoreCase("post")) {
				url.method = "get";
			}
		} catch (Exception e) {
			url.method = "get";
		}
		
		try {
			url.enctype = attributes.getNamedItem(ATTR_PARAMETERS_ENCTYPE).getNodeValue().trim();
		} catch (Exception e) {
			url.enctype = "application/x-www-form-urlencoded";
		}
		if (!url.enctype.matches("^[-\\w]+/[-\\w\\+]+$")) {
			throw new ParseException("Invalid attribute \"" + ATTR_PARAMETERS_ENCTYPE + "\"!", 0);
		}
		
		NodeList parameters = urlNode.getChildNodes();
		for (int i = 0; i < parameters.getLength(); i++) {
			try {
				if (parameters.item(i).getNodeName().equals(Parameter.TAG_NAME)) {
					url.parameters.add(Parameter.parse(parameters.item(i)));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return url;
	}
	
	/**
	 * This method parses a query string and extracts all parameters from it
	 * 
	 * @param queryString query string to parse
	 * @return a list of all query parameters
	 */
	public static List<Parameter> parseQueryString(String queryString) {
		String[] queryParts = PATTERN_QUERY_STRING_PARAMETERS_SEPARATOR.split(queryString);
		List<Parameter> parameters = new ArrayList<Parameter>(queryParts.length);
		for (String part: queryParts) {
			parameters.add(Parameter.parse(part));
		}
		return parameters;
	}
	
	/**
	 * @return a string representing the url, with all parameters
	 */
	public String getUrl() throws IllegalStateException {
		String url = this.template;
		if (this.parameters.size() > 0) {
			StringBuilder queryString = new StringBuilder();
			for (Parameter currentParam : this.parameters) {
				for (int j = currentParam.minimum; j <= currentParam.maximum; j++) {
					// insert
					queryString.append("&" + currentParam.encodeAsQueryStringElement());
				}
			}
			if (queryString.length() > 1) {
				url = url + "?" + queryString.substring(1); // ignore the first "&"
			}
		} 
		return url;
	}
	
	/**
	 * Clear all parameters values
	 */
	public void clearParametersValues() {
		Parameter currentParameter = null;
		for (int i = 0; i < this.parameters.size(); i++) {
			currentParameter = this.parameters.get(i);
			if (currentParameter.valueType != null) {
				currentParameter.value = TemplateParameter.toString(currentParameter.valueType);
			}
		}
	}
	
	/**
	 * Sets the value of a specified parameter
	 * @param parameterType - the type of the parameter
	 * @param value - the value to set
	 */
	public void setParameterValue(TemplateParameter parameterType, String value) {
		Parameter currentParameter = null;
		for (int i = 0; i < this.parameters.size(); i++) {
			currentParameter = this.parameters.get(i);
			if (currentParameter.valueType == parameterType) {
				currentParameter.value = value;
			}
		}
	}
	
	/**
	 * Returns the xml tag of this element
	 * @return the xml tag of this element
	 */
	@Override
	public String toString() {
		StringBuilder tag = new StringBuilder();
		tag.append("<" + TAG_NAME + " ");
		tag.append("template");tag.append("=\"");tag.append(template);tag.append("\" ");
		tag.append(ATTR_TYPE);tag.append("=\"");tag.append(type);tag.append("\" ");
		
		tag.append(ATTR_REL);tag.append("=\"");tag.append(rel.get(0).toString());
		for (int i = 1; i < rel.size(); i++) {
			tag.append(" " + rel.get(i).toString());
		}
		tag.append("\" ");
		
		tag.append(ATTR_INDEX_OFFSET);tag.append("=\"");tag.append(indexOffset);tag.append("\" ");
		tag.append(ATTR_PAGE_OFFSET);tag.append("=\"");tag.append(pageOffset);tag.append("\" ");
		tag.append(ATTR_PARAMETERS_METHOD);tag.append("=\"");tag.append(method);tag.append("\" ");
		tag.append(ATTR_PARAMETERS_ENCTYPE);tag.append("=\"");tag.append(enctype);tag.append("\"");
		if (parameters != null && parameters.size() > 0) {
			tag.append(">");
			for (int i = 0; i < parameters.size(); i++) {
				tag.append("\n\t");tag.append(parameters.get(i).toString());
			}
			tag.append("\n\t</" + TAG_NAME + ">");
		} else {
			tag.append(" />");
		}
		return tag.toString();
	}
	
	public static String toStringTemplate(Url url) {
		if (url != null) {
			StringBuilder builder = new StringBuilder(url.template);
			if (!url.template.contains("?")) {
				builder.append("?");
			}
			for (Parameter parameter : url.parameters) {
				builder.append("&" + parameter.name + "=" + parameter.value);
			}
		}
		return null;
	}
}