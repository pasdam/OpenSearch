package com.pasdam.opensearch.description;

import java.net.URI;
import java.text.ParseException;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Contains a URL that identifies the location of an image that can be used in 
 * association with this search content.<br/>
 * Image sizes are offered as a hint to the search client. The search client will 
 * choose the most appropriate image for the available space and should give 
 * preference to those listed first in the OpenSearch description document. Square 
 * aspect ratios are recommended. When possible, search engines should offer a 16x16 
 * image of type "image/x-icon" or "image/vnd.microsoft.icon" (the Microsoft ICON 
 * format) and a 64x64 image of type "image/jpeg" or "image/png".<br/>
 * @author Paco
 * @version 1.0
 */
public class Image {
	
	public static final String ELEMENT_NAME = "Image";
	
	public static final String ATTR_TYPE = "type";

	public static final String ATTR_WIDTH = "width";

	public static final String ATTR_HEIGHT = "height";

	/**
	 * The tag name of this element
	 */
	public static final String TAG_NAME = "Image";
	
	/**
	 * Contains the height, in pixels, of this image.<br/>
	 * Restrictions: The value must be a non-negative integer.<br/>
	 * Requirements: This attribute is optional.<br/>
	 */
	public int height;
	
	/**
	 * Contains the width, in pixels, of this image.<br/>
	 * Restrictions: The value must be a non-negative integer.<br/>
	 * Requirements: This attribute is optional.<br/>
	 */
	public int width;
	
	/**
	 * Contains the the MIME type of this image.<br/>
	 * Restrictions: The value must be a valid MIME type.<br/>
	 * Requirements: This attribute is optional.<br/>
	 */
	public String type;
	
	/**
	 * Restrictions: The value must be a URI.<br/>
	 */
	public String value = "";
	
	/**
	 * @param imageElement - the element containing parameter's attributes
	 * @return an Image object or null, in case of errors
	 * @throws ParseException if element is invalid
	 */
	public static Image parse(Node imageElement) throws ParseException{
		Image img = new Image();
		try {
			img.value = URI.create(imageElement.getChildNodes().item(0).getNodeValue().trim()).toString();
		} catch (Exception e) {
			String value = null;
			try {
				value = imageElement.getChildNodes().item(0).getNodeValue().trim();
			} catch (DOMException e1) {
				value = "";
			}
			throw new ParseException("Image element's value isn't a valid URI: " + value, 0);
		}
		NamedNodeMap attr = imageElement.getAttributes();
		try {
			img.height = Integer.parseInt(attr.getNamedItem(ATTR_HEIGHT).getNodeValue().trim());
			if (img.height < 0) {
				img.height = 0;
			}
		} catch (Exception e) {}
		try {
			img.width = Integer.parseInt(attr.getNamedItem(ATTR_WIDTH).getNodeValue().trim());
			if (img.width < 0) {
				img.width = 0;
			}
		} catch (Exception e) {}
		try {
			img.type = attr.getNamedItem(ATTR_TYPE).getNodeValue().trim();
		} catch (Exception e) {}
		if (img.type!=null && !img.type.matches("^[-\\w]+/[-\\w\\+]+$")) {
			throw new ParseException("Invalid attribute \"" + ATTR_TYPE + "\"!", 0);
		}
		return img;
	}

	/**
	 * Returns the xml tag of this element
	 * @return the xml tag of this element
	 */
	@Override
	public String toString() {
		StringBuilder tag = new StringBuilder("<" + ELEMENT_NAME);
		if (this.height > 0) {
			tag.append(" " + ATTR_HEIGHT + "=\"" + this.height + "\"");
		}
		if (this.width > 0) {
			tag.append(" " + ATTR_WIDTH + "=\"" + this.width + "\"");
		}
		if (this.type!= null && !this.type.equals("")) {
			tag.append(" " + ATTR_TYPE + "=\"" + this.type + "\"");
		}
		tag.append(">" + this.value + "</" + ELEMENT_NAME + ">");
		return tag.toString();
	}
}
