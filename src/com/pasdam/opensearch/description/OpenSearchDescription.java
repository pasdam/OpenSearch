package com.pasdam.opensearch.description;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * The root node of the OpenSearch description document.
 * 
 * @author Paco
 * @version 1.0
 */
public class OpenSearchDescription {
	
	public static final String ELEMENT_NAME = "OpenSearchDescription";
	public static final String CHILD_LANGUAGE = "Language";
	public static final String CHILD_ADULT_CONTENT = "AdultContent";
	public static final String CHILD_SYNDICATION_RIGHT = "SyndicationRight";
	public static final String CHILD_ATTRIBUTION = "Attribution";
	public static final String CHILD_DEVELOPER = "Developer";
	public static final String CHILD_QUERY = Query.TAG_NAME;
	public static final String CHILD_IMAGE = Image.TAG_NAME;
	public static final String CHILD_URL = Url.TAG_NAME;
	public static final String CHILD_LONG_NAME = "LongName";
	public static final String CHILD_TAGS = "Tags";
	public static final String CHILD_CONTACT = "Contact";
	public static final String CHILD_DESCRIPTION = "Description";
	public static final String CHILD_SHORT_NAME = "ShortName";
	public static final String CHILD_OUTPUT_ENCODING = "OutputEncoding";
	public static final String CHILD_INPUT_ENCODING = "InputEncoding";
	public static final int SYNDICATION_RIGHT_OPEN = 0;
	public static final int SYNDICATION_RIGHT_LIMITED = 1;
	public static final int SYNDICATION_RIGHT_PRIVATE = 2;
	public static final int SYNDICATION_RIGHT_CLOSED = 3;

	/**
	 * Contains a brief human-readable title that identifies this search engine. <br/>
	 * Restrictions: The value must contain 16 or fewer characters of plain text. 
	 * The value must not contain HTML or other markup.<br/>
	 * Requirements: This element must appear exactly once.
	 */
	public String shortName;
	
	/**
	 * Contains a human-readable text description of the search engine.<br/>
	 * Restrictions: The value must contain 1024 or fewer characters of plain text. 
	 * The value must not contain HTML or other markup.<br/>
	 * Requirements: This element must appear exactly once.
	 */
	public String description;
	
	/**
	 * Contains a URL that identifies the location of an image that can be used in 
	 * association with this search content.<br/>
	 * Requirements: This element may appear zero, one, or more times.
	 */
	public List<Image> images;
	
	/**
	 * Describes an interface by which a client can make requests for an external 
	 * resource, such as search results, search suggestions, or additional description 
	 * documents.<br/>
	 * Requirements: This element must appear one or more times.
	 */
	public List<Url> urls;
	
	/**
	 * Defines a search query that can be performed by search clients. Please see the 
	 * OpenSearch Query element specification for more information.<br/>
	 * OpenSearch description documents should include at least one Query element of 
	 * role="example" that is expected to return search results. Search clients may 
	 * use this example query to validate that the search engine is working properly.<br/>
	 * Requirements: This element may appear zero or more times.
	 */
	public List<Query> queries;
	
	/**
	 * Contains the human-readable name or identifier of the creator or maintainer of 
	 * the description document.<br/>
	 * The developer is the person or entity that created the description document, 
	 * and may or may not be the owner, author, or copyright holder of the source of 
	 * the content itself.<br/>
	 * Restrictions: The value must contain 64 or fewer characters of plain text. The 
	 * value must not contain HTML or other markup.<br/>
	 * Requirements: This element may appear zero or one time.
	 */
	public String developer;
	
	/**
	 * Contains an email address at which the maintainer of the description document can 
	 * be reached.<br/>
	 * Restrictions: The value must conform to the requirements of Section 3.4.1 "Addr-spec 
	 * specification" in RFC 2822<br/>
	 * Requirements: This element may appear zero or one time.
	 */
	public String contact;
	
	/**
	 * Contains a set of words that are used as keywords to identify and categorize this 
	 * search content. Tags must be a single word and are delimited by the space character 
	 * (' ').<br/>
	 * Restrictions: The value must contain 256 or fewer characters of plain text. The 
	 * value must not contain HTML or other markup.<br/>
	 * Requirements: This element may appear zero or one time.
	 */
	public String[] tags;
	
	/**
	 * Contains an extended human-readable title that identifies this search engine.<br/>
	 * Search clients should use the value of the ShortName element if this element is 
	 * not available.<br/>
	 * Restrictions: The value must contain 48 or fewer characters of plain text. 
	 * The value must not contain HTML or other markup.<br/>
	 * Requirements: This element may appear zero or one time.
	 */
	public String longName;
	
	/**
	 * Contains a list of all sources or entities that should be credited for the content 
	 * contained in the search feed.<br/>
	 * Restrictions: The value must contain 256 or fewer characters of plain text. The 
	 * value must not contain HTML or other markup.<br/>
	 * Requirements: This element may appear zero or one time.
	 */
	public String attribution;
	
	/**
	 * Contains a value that indicates the degree to which the search results provided by this 
	 * search engine can be queried, displayed, and redistributed.
	 * Default: "open"
	 */
	public SyndicationRight syndicationRight = SyndicationRight.OPEN;
	
	/**
	 * Contains a boolean value that should be set to true if the search results may contain 
	 * material intended only for adults.<br/>
	 * Default: "false"
	 */
	public boolean adultContent = false;
	
	/**
	 * Contains a string that indicates that the search engine supports search results in the 
	 * specified language.<br/>
	 * An OpenSearch description document should include one "Language" element for each language 
	 * that the search engine supports. If the search engine also supports queries for any arbitrary 
	 * language then the OpenSearch description document should include a Language element with a 
	 * value of "*". The "language" template parameter in the OpenSearch URL template can be used to 
	 * allow the search client to choose among the available languages.<br/>
	 * Restrictions: The value must conform to the XML 1.0 Language Identification, as specified by 
	 * RFC 3066. In addition, the value of "*" will signify that the search engine does not restrict 
	 * search results to any particular language.<br/>
	 * Default: "*".<br/>
	 * Requirements: This element may appear zero, one, or more times.
	 */
	public String[] languages;
	
	/**
	 * Contains a string that indicates that the search engine supports search requests encoded with 
	 * the specified character encoding.<br/>
	 * An OpenSearch description document should include one "InputEncoding" element for each character 
	 * encoding that can be used to encode search requests. The "inputEncoding" template parameter in 
	 * the OpenSearch URL template can be used to require the search client to identify which encoding 
	 * is being used to encode the current search request.<br/>
	 * Restrictions: The value must conform to the XML 1.0 Character Encodings, as specified by the 
	 * IANA Character Set Assignments.<br/>
	 * Default: "UTF-8".<br/>
	 * Requirements: This element may appear zero, one, or more times.
	 */
	public String[] inputEncoding;
	
	/**
	 * Contains a string that indicates that the search engine supports search responses encoded with 
	 * the specified character encoding.<br/>
	 * An OpenSearch description document should include one "OutputEncoding" element for each character 
	 * encoding that can be used to encode search responses. The "OutputEncoding" template parameter in 
	 * the OpenSearch URL template can be used to allow the search client to choose a character encoding 
	 * in the search response.<br/>
	 * Restrictions: The value must conform to the XML 1.0 Character Encodings, as specified by the 
	 * IANA Character Set Assignments.<br/>
	 * Default: "UTF-8".<br/>
	 * Requirements: This element may appear zero, one, or more times.
	 */
	public String[] outputEncoding;
	
	/**
	 * Parse the content of the given URL as an XML document and return a new OpenSearchDescription object.
	 * @param documentUrl - The location of the content to be parsed
	 * @return a new OpenSearchDescription object or null in case of errors
	 */
	public static OpenSearchDescription parse(URL documentUrl){
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parsing input document
			return parse(db.parse(documentUrl.toString()));
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Parse the content of the given URL as an XML document and return a new OpenSearchDescription object.
	 * @param xml - the string containing xml source
	 * @return a new OpenSearchDescription object or null in case of errors
	 */
	public static OpenSearchDescription parse(String xml){
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parsing input document
			return parse(db.parse(new InputSource(new StringReader(xml))));
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Parse the content of the given URL as an XML document and return a new OpenSearchDescription object.
	 * @param file - The file containing the XML to parse. 
	 * @return a new OpenSearchDescription object or null in case of errors
	 */
	public static OpenSearchDescription parse(File file){
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parsing input document
			return parse(db.parse(file));
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Parse the content of the given URL as an XML document and return a new OpenSearchDescription object.
	 * @param inputStream - InputStream containing the content to be parsed. 
	 * @return a new OpenSearchDescription object or null in case of errors
	 */
	public static OpenSearchDescription parse(InputStream inputStream){
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parsing input document
			return parse(db.parse(inputStream));
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Parse the content of the given URL as an XML document and return a new OpenSearchDescription object.
	 * @param document - The document containing xml elements
	 * @return a new OpenSearchDescription object or null in case of errors
	 * @throws ParseException if input document isn't valid
	 */
	private static OpenSearchDescription parse(Document document) throws ParseException{
		OpenSearchDescription openSearchObject = new OpenSearchDescription();
		// setting object properties
		NodeList currentElements = null;
		// parsing required elements
		currentElements = document.getElementsByTagName(CHILD_SHORT_NAME);
		if (currentElements != null) {
			openSearchObject.shortName = currentElements.item(0).getChildNodes().item(0).getNodeValue().trim();
		} else {
			throw new ParseException("Element \""+ CHILD_SHORT_NAME + "\" not found!", 0);
		}
		currentElements = document.getElementsByTagName(CHILD_DESCRIPTION);
		if (currentElements != null) {
			openSearchObject.description = currentElements.item(0).getChildNodes().item(0).getNodeValue().trim();
		} else {
			throw new ParseException("Element \"" + CHILD_DESCRIPTION + "\" not found!", 0);
		}
		currentElements = document.getElementsByTagName(CHILD_URL);
		if (currentElements != null) {
			openSearchObject.urls = new ArrayList<Url>();
			for (int i = 0; i < currentElements.getLength(); i++) {
				try {
					openSearchObject.urls.add(Url.parse(currentElements.item(i)));
				} catch (Exception e) {}
			}
		} else {
			throw new ParseException("Element \"" + CHILD_URL + "\" not found!", 0);
		}
		// parsing optional elements
		currentElements = document.getElementsByTagName(CHILD_CONTACT);
		try {
			openSearchObject.contact = currentElements.item(0).getChildNodes().item(0).getNodeValue().trim();
		} catch (Exception e) {}
		currentElements = document.getElementsByTagName(CHILD_TAGS);
		try {
			openSearchObject.tags = currentElements.item(0).getChildNodes().item(0).getNodeValue().trim().split(" ");
		} catch (Exception e) {}
		currentElements = document.getElementsByTagName(CHILD_LONG_NAME);
		try {
			openSearchObject.longName = currentElements.item(0).getChildNodes().item(0).getNodeValue().trim();
		} catch (Exception e) {
			openSearchObject.longName = openSearchObject.shortName;
		}
		currentElements = document.getElementsByTagName(CHILD_IMAGE);
		if (currentElements != null) {
			openSearchObject.images = new ArrayList<Image>();
			for (int i = 0; i < currentElements.getLength(); i++) {
				try {
					openSearchObject.images.add(Image.parse(currentElements.item(i)));
				} catch (Exception e) {}
			}
		}
		currentElements = document.getElementsByTagName(CHILD_QUERY);
		openSearchObject.queries = new ArrayList<Query>();
		for (int i = 0; i < currentElements.getLength(); i++) {
			try {
				openSearchObject.queries.add(Query.parse(currentElements.item(i)));
			} catch (Exception e) {}
		}
		currentElements = document.getElementsByTagName(CHILD_DEVELOPER);
		try {
			openSearchObject.developer = currentElements.item(0).getChildNodes().item(0).getNodeValue().trim();
		} catch (Exception e) {}
		currentElements = document.getElementsByTagName(CHILD_ATTRIBUTION);
		try {
			openSearchObject.attribution = currentElements.item(0).getChildNodes().item(0).getNodeValue().trim();
		} catch (Exception e) {}
		currentElements = document.getElementsByTagName(CHILD_SYNDICATION_RIGHT);
		try {
			openSearchObject.syndicationRight = SyndicationRight.fromString(currentElements.item(0).getChildNodes().item(0).getNodeValue().trim());
		} catch (Exception e) {}
		currentElements = document.getElementsByTagName(CHILD_ADULT_CONTENT);
		try {
			String adultContent = currentElements.item(0).getChildNodes().item(0).getNodeValue().trim();
			if (adultContent.equals("false") || 
					adultContent.equals("FALSE") || 
					adultContent.equals("0") || 
					adultContent.equals("no") || 
					adultContent.equals("NO")) {
				openSearchObject.adultContent = false;
			} else {
				openSearchObject.adultContent = true;
			}
		} catch (Exception e) {
			openSearchObject.adultContent = false;
		}
		currentElements = document.getElementsByTagName(CHILD_LANGUAGE);
		if (currentElements != null) {
			int langCount = 0;
			openSearchObject.languages = new String[currentElements.getLength()];
			for (int i = 0; i < openSearchObject.languages.length; i++) {
				try {
					openSearchObject.languages[i] = currentElements.item(i).getChildNodes().item(0).getNodeValue().trim();
					if (openSearchObject.languages[i] != null) {
						langCount++;
					}
				} catch (Exception e) {}
			}
			if (langCount == 0) {
				openSearchObject.languages = new String[]{"*"};
			}
		} else {
			openSearchObject.languages = new String[]{"*"};
		}
		currentElements = document.getElementsByTagName(CHILD_INPUT_ENCODING);
		if (currentElements != null) {
			int encCount = 0;
			openSearchObject.inputEncoding = new String[currentElements.getLength()];
			for (int i = 0; i < openSearchObject.inputEncoding.length; i++) {
				try {
					openSearchObject.inputEncoding[i] = currentElements.item(i).getChildNodes().item(0).getNodeValue().trim();
					if (openSearchObject.inputEncoding[i] != null) {
						encCount++;
					}
				} catch (Exception e) {}
			}
			if (encCount == 0) {
				openSearchObject.inputEncoding = new String[]{"UTF-8"};
			}
		} else {
			openSearchObject.inputEncoding = new String[]{"UTF-8"};
		}
		currentElements = document.getElementsByTagName(CHILD_OUTPUT_ENCODING);
		if (currentElements != null) {
			int encCount = 0;
			openSearchObject.outputEncoding = new String[currentElements.getLength()];
			for (int i = 0; i < openSearchObject.outputEncoding.length; i++) {
				try {
					openSearchObject.outputEncoding[i] = currentElements.item(i).getChildNodes().item(0).getNodeValue().trim();
					if (openSearchObject.outputEncoding[i] != null) {
						encCount++;
					}
				} catch (Exception e) {}
			}
			if (encCount == 0) {
				openSearchObject.outputEncoding = new String[]{"UTF-8"};
			}
		} else {
			openSearchObject.outputEncoding = new String[]{"UTF-8"};
		}
		// return object, if it has all required fields
		return openSearchObject;
	}
	
	/**
	 * Tags must be a single word and are delimited by the space character (' ').
	 * @return the value of the Tags element
	 */
	public StringBuilder getTags(){
		if (this.tags != null) {
			StringBuilder tags = new StringBuilder(this.tags.length*10);
			tags.append(tags.append(this.tags[0]));
			for (int i = 1; i < this.tags.length; i++) {
				tags.append(" ");
				tags.append(this.tags[i]);
			}
			return tags;
		} else {
			return null;
		}
	}
	
	/** 
	 * Returns the xml source of this element
	 * @return the xml source of this element
	 */
	@Override
	public String toString() {
		StringBuilder tag = new StringBuilder();
		tag.append("<" + ELEMENT_NAME + ">");
		tag.append(shortName != null ? "\n\t" + createTag(CHILD_SHORT_NAME, shortName) : "");
		tag.append(description != null ? "\n\t" + createTag(CHILD_DESCRIPTION, description) : "");
		tag.append(developer != null ? "\n\t" + createTag(CHILD_DEVELOPER, developer) : "");
		tag.append(contact != null ? "\n\t" + createTag(CHILD_CONTACT, contact) : "");
		tag.append(tags != null ? "\n\t" + createTag(CHILD_TAGS, getTags()) : "");
		tag.append("\n\t" + createTag(CHILD_LONG_NAME, (longName != null ? longName : shortName)));
		tag.append(attribution != null ? "\n\t" + createTag(CHILD_ATTRIBUTION, attribution) : "");
		tag.append("\n\t" + createTag(CHILD_SYNDICATION_RIGHT, syndicationRight.toString()));
		tag.append("\n\t" + createTag(CHILD_ADULT_CONTENT, (adultContent == true ? "true" : "false")));
		if (images != null) {
			for (Image img : images) {
				tag.append("\n\t");
				tag.append(img.toString());
			}
		}
		if (urls != null) {
			for (Url url : urls) {
				tag.append("\n\t");
				tag.append(url.toString());
			}
		}
		if (queries != null) {
			for (Query query : queries) {
				tag.append("\n\t");
				tag.append(query.toString());
			}
		}
		if (languages != null) {
			for (String lang : languages) {
				tag.append("\n\t");
				tag.append(createTag(CHILD_LANGUAGE, lang));
			}
		}
		if (inputEncoding != null) {
			for (String enc : inputEncoding) {
				tag.append("\n\t");
				tag.append(createTag(CHILD_INPUT_ENCODING, enc));
			}
		}
		if (outputEncoding != null) {
			for (String enc : outputEncoding) {
				tag.append("\n\t");
				tag.append(createTag(CHILD_OUTPUT_ENCODING, enc));
			}
		}
		tag.append(" \n</" + ELEMENT_NAME + ">");
		return tag.toString();
	}
	
	/**
	 * Generate an xml tag from input values
	 * @param name - the tag name
	 * @param value - tha tag value
	 * @return a string containing the correspondent xml tag
	 */
	private StringBuilder createTag(String name, CharSequence value){
		StringBuilder buffer = new StringBuilder();
		buffer.append("<");
		buffer.append(name);
		buffer.append(">");
		buffer.append(value);
		buffer.append("</");
		buffer.append(name);
		buffer.append(">");
		return buffer;
	}
}
