package com.pasdam.opensearch.description;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.regex.Pattern;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * The OpenSearch parameter extension is an enhancement to the OpenSearch description document 
 * that enables an augmented query parameter mechanism via structured XML. The extension also 
 * includes a mechanism for specifying alternate HTTP request methods, such as POST, and a 
 * mechanism for specifying alternate request encodings, such as " multipart/form-data". <br/>
 * 
 * Specified in: OpenSearch Parameter extension.
 * @author Paco
 * @version 1.0
 */
public class Parameter {

	public static final String ATTR_MAXIMUM = "maximum";

	public static final String ATTR_MINIMUM = "minimum";

	public static final String ATTR_VALUE = "value";

	public static final String ATTR_NAME = "name";
	
	private static final Pattern PATTERN_QUERY_STRING_PARAMETER = Pattern.compile(".+=.+"); // TODO Use a more precise pattern to match query parameter
	private static final Pattern PATTERN_QUERY_STRING_VALUE_SEPARATOR = Pattern.compile("=");
	
	/**
	 * The tag name of this element
	 */
	public static final String TAG_NAME = "parameters:Parameter";
	
	/**
	 * Contains the name of the parameter
	 * Required.
	 */
	public String name;
	
	/**
	 * Contains a string that will be processed according to the rules of the 
	 * OpenSearch URL template syntax
	 */
	public String value = "";
	
	/**
	 * Contains a string that identifies the minimum number of times this parameter 
	 * must be included in the search request
	 */
	public int minimum = 1;
	
	/**
	 * Contains a string that identifies the maximim number of times this parameter 
	 * must be included in the search request. The literal string "*" will be 
	 * interpreted to mean that the parameter may repeat an arbitrary number of times
	 */
	public int maximum = 1;
	
	/**
	 * A value that identifies the type of the parameter
	 */
	public TemplateParameter valueType;
	
	/**
	 * Empty constructor
	 */
	public Parameter() {
	}

	/**
	 * Constructor
	 * @param name - parameter's name
	 * @param value - parameter's value
	 */
	public Parameter(String name, String value) {
		this.name = name;
		try {
			this.valueType = TemplateParameter.fromString(value);
		} catch (InvalidParameterException e) {
			this.value = value;
		}
	}

	/**
	 * @param parameterElement - the element containing parameter's attributes
	 * @return a Parameter object or null, in case of errors
	 * @throws ParseException if element is invalid
	 */
	public static Parameter parse(Node parameterElement) throws ParseException{
		NamedNodeMap attributes = parameterElement.getAttributes();
		Parameter param = new Parameter();
		try {
			param.name = attributes.getNamedItem(ATTR_NAME).getNodeValue().trim();
		} catch (Exception e) {
			throw new ParseException("Attribute \"" + ATTR_NAME + "\" not found!", 0);
		}
		try {
			param.value = attributes.getNamedItem(ATTR_VALUE).getNodeValue().trim();
			if (param.value.endsWith("?}")) {
				param.minimum = 0;
				param.value = param.value.replaceAll("?}", "}");
			}
			try {
				param.valueType = TemplateParameter.fromString(param.value);
				if (param.valueType != null) {
					// if valueType is valid (so this object indicates a template parameter) the value
					// must be inserted before perform the query
					param.value = "";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			param.value = "";
		}
		
		Node currentElement;
		
		try {
			currentElement = attributes.getNamedItem(ATTR_MINIMUM);
			if (currentElement != null) {
				param.minimum = Integer.parseInt(currentElement.getNodeValue().trim());
			}
			if (param.minimum < 0) {
				param.minimum = 0;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			currentElement = attributes.getNamedItem(ATTR_MAXIMUM);
			if (currentElement != null) {
				param.maximum = Integer.parseInt(currentElement.getNodeValue().trim());
			}
			if (param.maximum < param.minimum) {
				param.maximum = param.minimum;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return param;
	}
	
	/**
	 * It parses a part of a query string that contains the parameter's name and
	 * value, in the format "&lt;name&gt;=&lt;value&gt;"
	 * 
	 * @param queryStringParameter
	 *            the part of the query string that contains the parameter's
	 *            name and value
	 * @return the parsed parameter
	 * @throws IllegalArgumentException
	 *             if queryStringParameter is empty/null or if it has an invalid
	 *             format
	 */
	public static Parameter parse(String queryStringParameter) throws IllegalArgumentException {
		if (queryStringParameter != null && !queryStringParameter.isEmpty()) {
			if (PATTERN_QUERY_STRING_PARAMETER.matcher(queryStringParameter).matches()) {
				String[] parts = PATTERN_QUERY_STRING_VALUE_SEPARATOR.split(queryStringParameter);
				Parameter parameter = new Parameter();
				
				parameter.name = parts[0];
				
				parameter.maximum = 1;
				if (parts[1].endsWith("?}")) {
					parameter.minimum = 0;
				} else {
					parameter.minimum = 1;
				}
				
				parameter.valueType = TemplateParameter.fromString(parts[1]);
				if (parameter.valueType == null) {
					parameter.value = parts[1];
				}
				
				return parameter;
			} else {
				throw new IllegalArgumentException("Illegal format for query string parameter: " + queryStringParameter);
			}
		} else {
			throw new IllegalArgumentException("Query string parameter cannot be null or empty.");
		}
	}

	/**
	 * Returns the xml tag of this parameter
	 * @return the xml tag of this parameter
	 */
	@Override
	public String toString() {
		return "<" + TAG_NAME + " " + ATTR_NAME + "=\"" + name + "\" " + ATTR_VALUE + "=\"" + (valueType == null ? value : valueType) + "\" " + ATTR_MINIMUM + "=\""
				+ minimum + "\" " + ATTR_MAXIMUM + "=\"" + maximum + "\" />";
	}
	
	public String encodeAsQueryStringElement() {
		return encodeAsQueryString(this.value);
	}
	
	public String encodeAsQueryString(String value) {
		try {
			return name + "=" + URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
