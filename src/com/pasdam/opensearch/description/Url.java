package com.pasdam.opensearch.description;

import java.text.ParseException;
import java.util.ArrayList;

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
	
	public static final String ELEMENT_NAME = "Url";

	public static final String ATTR_PARAMETERS_ENCTYPE = "parameters:enctype";

	public static final String ATTR_PARAMETERS_METHOD = "parameters:method";

	public static final String ATTR_PAGE_OFFSET = "pageOffset";

	public static final String ATTR_INDEX_OFFSET = "indexOffset";

	public static final String ATTR_REL = "rel";

	public static final String ATTR_TYPE = "type";

	public static final String ATTR_TEMPLATE = "template";

	/**
	 * The tag name of this element
	 */
	public static final String TAG_NAME = ELEMENT_NAME;
	
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
	public String rel;
	
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
	public ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	
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
			url.template = attributes.getNamedItem(ATTR_TEMPLATE).getNodeValue().trim();
			parseTemplate(url.template, url.parameters);
		} catch (Exception e) {
			throw new ParseException("Invalid attribute \"" + ATTR_TEMPLATE + "\"!", 0);
		}
		try {
			url.type = attributes.getNamedItem(ATTR_TYPE).getNodeValue().trim();
		} catch (Exception e) {}
		if (url.type != null && !url.type.matches("^[-\\w]+/[-\\w\\+]+$")) {
			throw new ParseException("Invalid attribute \"" + ATTR_TYPE + "\"!", 0);
		}
		try {
			url.rel = attributes.getNamedItem(ATTR_REL).getNodeValue().trim();
		} catch (Exception e1) {
			throw new ParseException("Invalid attribute \"" + ATTR_REL + "\"!", 0);
		}
		try {
			url.indexOffset = Integer.parseInt(attributes.getNamedItem(ATTR_INDEX_OFFSET).getNodeValue().trim());
		} catch (Exception e) {
			url.indexOffset = 1;
		}
		try {
			url.pageOffset = Integer.parseInt(attributes.getNamedItem(ATTR_PAGE_OFFSET).getNodeValue().trim());
		} catch (Exception e) {
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
			} catch (Exception e) {}
		}
		return url;
	}
	
	/**
	 * @return a string representing the url, with all parameters
	 */
	public String getUrl() throws IllegalStateException {
		String url = this.template;
		if (this.parameters.size()>0) {
			Parameter currentParam = null;
			String queryString = "";
			for (int i = 0; i < this.parameters.size(); i++) {
				currentParam = this.parameters.get(i);
				if (currentParam.valueType != null) {
					if (currentParam.hasOwnTag) {
						if (currentParam.equals("")) {
							if (currentParam.minimum > 0) {
								// error
								throw new IllegalStateException("Please set all parameter. Invalid value of parameter: " + currentParam.valueType.toString());
							}
						} else {
							for (int j = currentParam.minimum; j <= currentParam.maximum; j++) {
								// insert
								queryString += "&" + currentParam.name + "="
										+ currentParam.value;
							}
						}
					} else {
						// substitute
						url = url.replaceAll(TemplateParameter
								.toString(currentParam.valueType),
								currentParam.value);
					}
				} else if (currentParam.hasOwnTag) {
					for (int j = currentParam.minimum; j <= currentParam.maximum; j++) {
						// insert
						queryString += "&" + currentParam.name + "="
								+ currentParam.value;
					}
				}
			}
			if (queryString.length() > 1) {
				url = url + "?" + queryString.substring(1);
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
				currentParameter.value = TemplateParameter.toString(currentParameter.valueType);
			}
		}
	}
	
	/**
	 * This method parse the template to find possible parameters
	 * @param template - the template to parse
	 * @param parameterList - ArrayList containing all parameters
	 */
	public static void parseTemplate(String template, ArrayList<Parameter> parameterList) {
		if (template.contains(TemplateParameter.PARAM_SEARCH_TERMS)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_SEARCH_TERMS, false));
		} else if (template.contains(TemplateParameter.PARAM_COUNT)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_COUNT, false));
		} else if (template.contains(TemplateParameter.PARAM_START_INDEX)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_START_INDEX, false));
		} else if (template.contains(TemplateParameter.PARAM_START_PAGE)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_START_PAGE, false));
		} else if (template.contains(TemplateParameter.PARAM_LANGUAGE)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_LANGUAGE, false));
		} else if (template.contains(TemplateParameter.PARAM_INPUT_ENCODING)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_INPUT_ENCODING, false));
		} else if (template.contains(TemplateParameter.PARAM_OUTPUT_ENCODING)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_OUTPUT_ENCODING, false));
		} else if (template.contains(TemplateParameter.PARAM_TIME_START)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_TIME_START, false));
		} else if (template.contains(TemplateParameter.PARAM_TIME_END)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_TIME_END, false));
		} else if (template.contains(TemplateParameter.PARAM_GEO_NAME) || template.contains(TemplateParameter.PARAM_GEO_LOCATION_STRING)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_GEO_NAME, false));
		} else if (template.contains(TemplateParameter.PARAM_GEO_LAT)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_GEO_LAT, false));
		} else if (template.contains(TemplateParameter.PARAM_GEO_LON)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_GEO_LON, false));
		} else if (template.contains(TemplateParameter.PARAM_GEO_RADIUS)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_GEO_RADIUS, false));
		} else if (template.contains(TemplateParameter.PARAM_GEO_BOX)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_GEO_BOX, false));
		} else if (template.contains(TemplateParameter.PARAM_GEO_GEOMETRY) || template.contains(TemplateParameter.PARAM_GEO_POLYGON)) {
			parameterList.add(new Parameter(null, TemplateParameter.PARAM_GEO_GEOMETRY, false));
		}
	}
	
	/**
	 * Returns the xml tag of this element
	 * @return the xml tag of this element
	 */
	@Override
	public String toString() {
		StringBuilder tag = new StringBuilder();
		tag.append("<" + ELEMENT_NAME + " ");
		tag.append("template");tag.append("=\"");tag.append(template);tag.append("\" ");
		tag.append(ATTR_TYPE);tag.append("=\"");tag.append(type);tag.append("\" ");
		tag.append(ATTR_REL);tag.append("=\"");tag.append(rel);tag.append("\" ");
		tag.append(ATTR_INDEX_OFFSET);tag.append("=\"");tag.append(indexOffset);tag.append("\" ");
		tag.append(ATTR_PAGE_OFFSET);tag.append("=\"");tag.append(pageOffset);tag.append("\" ");
		tag.append(ATTR_PARAMETERS_METHOD);tag.append("=\"");tag.append(method);tag.append("\" ");
		tag.append(ATTR_PARAMETERS_ENCTYPE);tag.append("=\"");tag.append(enctype);tag.append("\"");
		if (parameters != null && parameters.size() > 0) {
			tag.append(">");
			for (int i = 0; i < parameters.size(); i++) {
				tag.append("\n\t");tag.append(parameters.get(i).toString());
			}
			tag.append("\n\t</" + ELEMENT_NAME + ">");
		} else {
			tag.append(" />");
		}
		return tag.toString();
	}
}