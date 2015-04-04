package com.pasdam.opensearch.description;

import java.security.InvalidParameterException;
import java.text.ParseException;

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

	public static final String ELEMENT_NAME = "Parameter";
	
	public static final String ATTR_MAXIMUM = "maximum";

	public static final String ATTR_MINIMUM = "minimum";

	public static final String ATTR_VALUE = "value";

	public static final String ATTR_NAME = "name";

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
	 * Indicates whether this parameter has its own tag or is included in template.<br/>
	 * Default: true;
	 */
	public boolean hasOwnTag = true;
	
	/**
	 * Empty constructor
	 */
	public Parameter() {
	}

	/**
	 * Constructor
	 * @param name - parameter's name
	 * @param value - parameter's value
	 * @param hasOwnTag - indicates that this element has its own tag
	 */
	public Parameter(String name, String value, boolean hasOwnTag) {
		this.name = name;
		try {
			this.valueType = TemplateParameter.fromString(value);
		} catch (InvalidParameterException e) {
			this.value = value;
		}
		this.hasOwnTag = hasOwnTag;
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
				param.value = "";
			} catch (Exception e) {}
		} catch (Exception e) {
			param.value = "";
		}
		try {
			param.minimum = Integer.parseInt(attributes.getNamedItem(ATTR_MINIMUM).getNodeValue().trim());
			if (param.minimum < 0) {
				param.minimum = 0;
			}
		} catch (Exception e) {}
		try {
			param.maximum = Integer.parseInt(attributes.getNamedItem(ATTR_MAXIMUM).getNodeValue().trim());
			if (param.maximum < param.minimum) {
				param.maximum = param.minimum;
			}
		} catch (Exception e) {}
		return null;
	}

	/**
	 * Returns the xml tag of this parameter
	 * @return the xml tag of this parameter
	 */
	@Override
	public String toString() {
		return "<" + ELEMENT_NAME + " " + ATTR_NAME + "=\"" + name + "\" " + ATTR_VALUE + "=\"" + value + "\" " + ATTR_MINIMUM + "=\""
				+ minimum + "\" " + ATTR_MAXIMUM + "=\"" + maximum + "\" />";
	}
}
