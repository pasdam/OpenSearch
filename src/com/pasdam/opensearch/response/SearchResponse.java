package com.pasdam.opensearch.response;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.pasdam.opensearch.description.Query;

/**
 * The OpenSearch response elements can be used by search engines to augment existing XML 
 * formats with search-related metadata.OpenSearch response elements are typically found 
 * augmenting search results returned in list-based XML syndication formats, such as RSS 
 * 2.0 and Atom 1.0, but may be used in other contexts without restriction.
 * @author Paco
 * @version 1.0
 */
public class SearchResponse {
	
	public static final String RSS_CHILD_ITEM_DESCRIPTION = "description";

	public static final String RSS_CHILD_ITEM_LINK = "link";

	public static final String RSS_CHILD_ITEM_TITLE = "title";

	public static final String RSS_CHILD_ITEM = "item";

	public static final String RESULT_TITLE = "title";

	public static final String RESULT_LINK = RSS_CHILD_ITEM_LINK;

	public static final String RESULT_DESCRIPTION = "desc";
	
	public static final String ATOM_CHILD_ENTRY = "entry";

	public static final String ATOM_CHILD_ENTRY_TITLE = RSS_CHILD_ITEM_TITLE;

	public static final String ATOM_CHILD_ENTRY_LINK = RSS_CHILD_ITEM_LINK;

	public static final String ATOM_CHILD_ENTRY_CONTENT = "content";

	/**
	 * The number of search results available for the current search. If the totalResults 
	 * element does not appear on the page then the search client should consider the 
	 * current page to be the last page of search results.<br/>
	 * Restrictions: The value must be a non-negative integer.<br/>
	 * Default: The default value is equal to the offset index of the last search result 
	 * on the current page.
	 */
	public int totalResults;
	
	/**
	 * The index of the first search result in the current set of search results. If the 
	 * startIndex element does not appear on the page then the search client should 
	 * consider the current page to be the first page of search results.<br/>
	 * Restrictions: The value must an integer.<br/>
	 * Default: The default value is equal to the value of the "indexOffset" attribute 
	 * of the "Url" element" in the OpenSearch description document.
	 */
	public int startIndex;
	
	/**
	 * The number of search results returned per page. If the itemsPerPage element does 
	 * not appear on the page then the search client should use the number of items of 
	 * the current page as the default page size.<br/>
	 * Restrictions: The value must a non-negative integer.<br/>
	 * Default: The default value is equal to the number of search results on the 
	 * current page.<br/>
	 * Requirements: The element may appear zero or one time.
	 */
	public int itemsPerPage;
	
	/**
	 * Defines a search query that can be performed by search clients, see {@link Query}.
	 * Search results should include a Query element of type="request" that can be used 
	 * to recreate the search request that generate the current search response.<br/>
	 * Requirements: The element may appear zero or more times.
	 */
	public List<Query> queries;
	
	public List<HashMap<String, String>> resultsList;
	
	/**
	 * Parse the content of the given URL as an XML document and return a new SearchResponse object.
	 * @param documentUrl - The location of the content to be parsed
	 * @return a new SearchResponse object or null in case of errors
	 */
	public static SearchResponse parse(URL documentUrl){
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
	 * Parse the content of the given URL as an XML document and return a new SearchResponse object.
	 * @param xml - the string containing xml source
	 * @return a new SearchResponse object or null in case of errors
	 */
	public static SearchResponse parse(String xml){
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
	 * Parse the content of the given URL as an XML document and return a new SearchResponse object.
	 * @param file - The file containing the XML to parse. 
	 * @return a new SearchResponse object or null in case of errors
	 */
	public static SearchResponse parse(File file){
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
	 * Parse the content of the given URL as an XML document and return a new SearchResponse object.
	 * @param inputStream - InputStream containing the content to be parsed. 
	 * @return a new SearchResponse object or null in case of errors
	 */
	public static SearchResponse parse(InputStream inputStream){
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
	 * Parse the content of the given URL as an XML document and return a new SearchResponse object.
	 * @param document - The document containing xml elements
	 * @return a new SearchResponse object or null in case of errors
	 * @throws ParseException if input document isn't valid
	 */
	public static SearchResponse parse(Document document) throws ParseException {
		SearchResponse response = new SearchResponse();
		String rootNode = document.getFirstChild().getNodeName();
		if (rootNode.equalsIgnoreCase("rss")) {
			// Parse RSS results
			response.resultsList = new ArrayList<HashMap<String,String>>();
			NodeList items = document.getElementsByTagName(RSS_CHILD_ITEM);
			HashMap<String, String> currentItem = null;
			NodeList currentNodeList = null;
			Node currentElement = null;
			String currentElementName = null;
			for (int i = 0; i < items.getLength(); i++) {
				currentNodeList = items.item(i).getChildNodes();
				currentItem = new HashMap<String, String>();
				for (int j = 0; j < currentNodeList.getLength(); j++) {
					currentElement = currentNodeList.item(j);
					currentElementName = currentElement.getNodeName();
					if (currentElementName.equals(RSS_CHILD_ITEM_TITLE)) {
						currentItem.put(RESULT_TITLE, currentElement.getFirstChild().getNodeValue());
					} else if (currentElementName.equals(RSS_CHILD_ITEM_LINK)) {
						currentItem.put(RESULT_LINK, currentElement.getFirstChild().getNodeValue());
					} else if (currentElementName.equals(RSS_CHILD_ITEM_DESCRIPTION)) {
						currentItem.put(RESULT_DESCRIPTION, currentElement.getFirstChild().getNodeValue());
					}
				}
				if (currentItem.containsKey(RESULT_TITLE) && currentItem.containsKey(RESULT_LINK) && currentItem.containsKey(RESULT_DESCRIPTION)) {
					response.resultsList.add(currentItem);
				}
			}
		} else if (rootNode.equalsIgnoreCase("feed")) {
			// Parse Atom results
			response.resultsList = new ArrayList<HashMap<String,String>>();
			NodeList items = document.getElementsByTagName(ATOM_CHILD_ENTRY);
			HashMap<String, String> currentItem = null;
			NodeList currentNodeList = null;
			Node currentElement = null;
			String currentElementName = null;
			for (int i = 0; i < items.getLength(); i++) {
				currentNodeList = items.item(i).getChildNodes();
				currentItem = new HashMap<String, String>();
				for (int j = 0; j < currentNodeList.getLength(); j++) {
					currentElement = currentNodeList.item(j);
					currentElementName = currentElement.getNodeName();
					if (currentElementName.equals(ATOM_CHILD_ENTRY_TITLE)) {
						currentItem.put(RESULT_TITLE, currentElement.getFirstChild().getNodeValue());
					} else if (currentElementName.equals(ATOM_CHILD_ENTRY_LINK)) {
						currentItem.put(RESULT_LINK, currentElement.getFirstChild().getNodeValue());
					} else if (currentElementName.equals(ATOM_CHILD_ENTRY_CONTENT)) {
						currentItem.put(RESULT_DESCRIPTION, currentElement.getFirstChild().getNodeValue());
					}
				}
				if (currentItem.containsKey(RESULT_TITLE) && currentItem.containsKey(RESULT_LINK) && currentItem.containsKey(RESULT_DESCRIPTION)) {
					response.resultsList.add(currentItem);
				}
			}
		} else {
			throw new ParseException("Invalid root element: " + rootNode, 0);
		}
		// parsing other elements
		try {
			response.totalResults = Integer.parseInt(document.getElementsByTagName("opensearch:totalResults").item(0).getFirstChild().getNodeValue().trim());
		} catch (Exception e) {
			response.totalResults = response.resultsList.size();
		}
		try {
			response.startIndex = Integer.parseInt(document.getElementsByTagName("opensearch:startIndex").item(0).getFirstChild().getNodeValue().trim());
		} catch (Exception e) {}
		try {
			response.itemsPerPage = Integer.parseInt(document.getElementsByTagName("opensearch:itemsPerPage").item(0).getFirstChild().getNodeValue().trim());
		} catch (Exception e) {}
		NodeList queryElements = document.getElementsByTagName("opensearch:Query");
		response.queries = new ArrayList<Query>();
		for (int i = 0; i < queryElements.getLength(); i++) {
			try {
				response.queries.add(Query.parse(queryElements.item(i)));
			} catch (Exception e) {}
		}
		return response;
	}

}
