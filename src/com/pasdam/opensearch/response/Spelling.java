package com.pasdam.opensearch.response;

import java.net.URL;
import java.text.ParseException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The OpenSearch Spelling Extension allows search engines to include information 
 * about a spelling suggestion in the search results.
 * @author Paco
 * @version 1.0
 *
 */
public class Spelling {
	
	private static final String CHILD_LINK_ATTR_HREF = "href";
	public static final String CHILD_SUGGESTION = "spell:suggestion";
	public static final String CHILD_PROMPT = "spell:prompt";
	public static final String CHILD_LINK = "atom:link";
	
	/**
	 * The tag name of this element
	 */
	public static final String TAG_NAME = "spell:spelling";
	
	/**
	 * The suggestion sub-element is an atom:text construct that specifies the 
	 * suggested spelling correction for the query.</br>
	 * Requirements: This element must appear one time.
	 */
	public String suggestion;
	
	/**
	 * The prompt sub-element is an atom:text construct that specifies the prompt 
	 * text displayed near the spelling suggestion.</br>
	 * Requirements: This element must appear one time.
	 */
	public String prompt;
	
	/**
	 * The link sub-element of spelling is an Atom Link element whose link targets 
	 * search requests for the suggested spell-corrected query.</br>
	 * Requirements: This element must appear one time.
	 */
	public URL link;
	
	/**
	 * Parse an xml node and return an object representing the Spelling element.
	 * @param spellingElement - the xml element to parse
	 * @return the parsed object
	 * @throws ParseException if the input element is not a valid Spelling node
	 */
	public static Spelling parse(Node spellingElement) throws ParseException{
		Spelling spelling = new Spelling();
		NodeList childNodes = spellingElement.getChildNodes();
		Node currentNode = null;
		for (int i = 0; i < childNodes.getLength(); i++) {
			try {
				currentNode = childNodes.item(i);
				if (currentNode.getNodeName().equals(CHILD_SUGGESTION)) {
					spelling.suggestion = currentNode.getFirstChild().getNodeValue();
				} else if (currentNode.getNodeName().equals(CHILD_PROMPT)) {
					spelling.prompt = currentNode.getFirstChild().getNodeValue();
				} else if (currentNode.getNodeName().equals(CHILD_LINK)) {
					spelling.link = new URL(currentNode.getAttributes().getNamedItem(CHILD_LINK_ATTR_HREF).getNodeValue());
				}
			} catch (Exception e) {}
		}
		if (spelling.suggestion==null || spelling.prompt==null || spelling.link==null) {
			throw new ParseException("This element is not a valid " + TAG_NAME + " node", 0);
		}
		return spelling;
	}

	/**
	 * Returns the xml tag of this element
	 * @return the xml tag of this element
	 */
	@Override
	public String toString() {
		StringBuilder tag = new StringBuilder();
		tag.append("<");tag.append(TAG_NAME);tag.append(">");
		tag.append("\n\t<");tag.append(CHILD_SUGGESTION);tag.append(">");tag.append(this.suggestion);tag.append("</");tag.append(CHILD_SUGGESTION);tag.append(">");
		tag.append("\n\t<");tag.append(CHILD_PROMPT);tag.append(">");tag.append(this.prompt);tag.append("</");tag.append(CHILD_PROMPT);tag.append(">");
		tag.append("\n\t<");tag.append(CHILD_LINK);tag.append(">");tag.append(this.link.toString());tag.append("</");tag.append(CHILD_LINK);tag.append(">");
		tag.append("</");tag.append(TAG_NAME);tag.append(">");
		return tag.toString();
	}

}
