package com.pasdam.opensearch.description;

import java.security.InvalidParameterException;

/**
 * This enumeration contains all possible template parameter, listed before.<br/>
 * 
 * <ul>
 * <li><b>{searchTerms}</b> - Replaced with the keyword or keywords desired by the search client. <br/>
 * Restrictions: The value must be URL-encoded.
 * </li>
 * <li><b>{count}</b> - Replaced with the number of search results per page desired by the search client.<br/>
 * Restrictions: The value must be a non-negative integer.
 * </li>
 * <li><b>{startIndex}</b> - Replaced with the index of the first search result desired by the search 
 * client.<br/>
 * Restrictions: The value must be an integer.<br/>
 * Default: The value specified by the "indexOffset" attribute of the containing Url element.
 * </li>
 * <li><b>{startPage}</b> - Replaced with the page number of the set of search results desired by the 
 * search client.<br/>
 * Restrictions: The value must be an integer.<br/>
 * Default: The value specified by the "pageOffset" attribute of the containing Url element.
 * </li>
 * <li><b>{language}</b> - Replaced with a string that indicates that the search client desires search 
 * results in the specified language.<br/>
 * Restrictions: The value must conform to the XML 1.0 Language Identification, as specified 
 * by RFC 3066. In addition, a value of "*" will signify that the search client desires search 
 * results in any language.<br/>
 * Default: "*"
 * </li>
 * <li><b>{inputEncoding}</b> - Replaced with a string that indicates that the search client is performing 
 * the search request encoded with the specified character encoding.<br/>
 * Restrictions: The value must conform to the XML 1.0 Character Encodings, as specified by the 
 * IANA Character Set Assignments.<br/>
 * Default: "UTF-8"
 * </li>
 * <li><b>{outputEncoding}</b> - Replaced with a string that indicates that the search client desires a 
 * search response encoding with the specified character encoding.<br/>
 * Restrictions: The value must conform to the XML 1.0 Character Encodings, as specified by 
 * the IANA Character Set Assignments.
 * Default: "UTF-8"
 * </li>
 * <li><b>{time:start}</b> - Replaced with a string of the beginning of the time slice of the search 
 * query. This string should match the RFC-3339 - Date and Time on the Internet: Timestamps, 
 * which is also used by the Atom syndication format.<br/>
 * It is of the format: YYYY-MM-DDTHH:mm:ssZ or 1996-12-19T16:39:57-08:00.<br/>
 * 'Z' is the time-offset, where 'Z' signifies UTC or an actual offset can be used.
 * </li>
 * <li><b>{time:end}</b> - Replaced with a string of the ending of the time slice of the search 
 * query.
 * </li>
 * <li><b>{geo:name}</b> or {geo:locationString}(deprecated) - Replaced with a string describing the 
 * location (place name) to perform the search. This location string will be parsed and 
 * geocoded by the search service.
 * </li>
 * <li><b>{geo:lat}</b> - Replaced with the "latitude", in decimal degrees in EPSG:4326 (typical WGS84 
 * coordinates as returned by a GPS receiver). This parameter should also include a "radius" 
 * parameter that specifies the search radius, in meters. If no radius is supplied, then the 
 * search service is free to use a default radius but should specify this radius in the 
 * returned result.
 * </li>
 * <li><b>{geo:lon}</b> - Replaced with the "longitude".
 * </li>
 * <li><b>{geo:radius}</b> - used with the lat and lon parameters, specifies the search distance from 
 * this point. The distance is in meters along the Earth's surface.
 * </li>
 * <li><b>{geo:box}</b> - Replaced with the bounding box to search for geospatial results within. 
 * The box is defined by "west, south, east, north" coordinates of longitude, latitude, in 
 * a EPSG:4326 decimal degrees. This is also commonly referred to by minX, minY, maxX, maxY 
 * (where longitude is the X-axis, and latitude is the Y-axis), or also SouthWest corner 
 * and NorthEast corner.
 * </li>
 * <li><b>{geo:geometry}</b> - or {geo:polygon}(deprecated) - Replaced with a geometry defined using the Well Known Text (WKT) standard.<br/>
 * The following 2D geometric objects can be described:
 * <ul><li>POINT</li>
 * <li>LINESTRING</li>
 * <li>POLYGON </li>
 * <li>MULTIPOINT </li>
 * <li>MULTILINESTRING </li>
 * <li>MULTIPOLYGON</li></ul>
 * Note that the WKT coordinate pairs are in lon, lat order; opposit to GeoRSS.
 * Polygons are a collection of linearrings. The outer ring is expressed in counter-clockwise order. Internal rings have the opposite orientation, appearing as clockwise (see 6.1.11.1 in OGC Simple Features Specification v. 1.2.0).
 * Spaces must be URL encoded ('%20' or '+') in the request.
 * Example geometries:
 * <ul><li>POINT(6 10)</li>
 * <li>LINESTRING(3 4,10 50,20 25)</li>
 * <li>POLYGON((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2))</li>
 * <li>MULTIPOINT(3.5 5.6, 4.8 10.5)</li>
 * <li>MULTILINESTRING((3 4,10 50,20 25),(-5 -8,-10 -8,-15 -4))</li>
 * <li>MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2)),((6 3,9 2,9 4,6 3)))</li></ul>
 * </li>
 * </ul>
 * @author Paco
 * @version 1.0
 */
public enum TemplateParameter {

	SEARCH_TERM, 
	COUNT, 
	START_INDEX, 
	START_PAGE, 
	LANGUAGE, 
	INPUT_ENCODING, 
	OUTPUT_ENCODING, 
	TIME_START, 
	TIME_END, 
	GEO_NAME, 
	GEO_LAT, 
	GEO_LON, 
	GEO_RADIUS, 
	GEO_BOX, 
	GEO_GEOMETRY;

	public static final String PARAM_GEO_POLYGON = "{geo:polygon}";
	public static final String PARAM_GEO_GEOMETRY = "{geo:geometry}";
	public static final String PARAM_GEO_BOX = "{geo:box}";
	public static final String PARAM_GEO_RADIUS = "{geo:radius}";
	public static final String PARAM_GEO_LON = "{geo:lon}";
	public static final String PARAM_GEO_LAT = "{geo:lat}";
	public static final String PARAM_GEO_LOCATION_STRING = "{geo:locationString}";
	public static final String PARAM_GEO_NAME = "{geo:name}";
	public static final String PARAM_TIME_END = "{time:end}";
	public static final String PARAM_TIME_START = "{time:start}";
	public static final String PARAM_OUTPUT_ENCODING = "{outputEncoding}";
	public static final String PARAM_INPUT_ENCODING = "{inputEncoding}";
	public static final String PARAM_LANGUAGE = "{language}";
	public static final String PARAM_START_PAGE = "{startPage}";
	public static final String PARAM_START_INDEX = "{startIndex}";
	public static final String PARAM_COUNT = "{count}";
	public static final String PARAM_SEARCH_TERMS = "{searchTerms}";

	/**
	 * Returns the type of the input value
	 * @param value - the "value" attribute of this parameter
	 * @return the type of the input value
	 * @throws InvalidParameterException if the input value is invalid
	 */
	public static TemplateParameter fromString(String value) throws InvalidParameterException{
		if (value != null) {
			String type = value.trim().replaceAll("?", "");
			if (type.equals(PARAM_SEARCH_TERMS)) {
				return SEARCH_TERM;
			} else if (type.equals(PARAM_COUNT)) {
				return COUNT;
			} else if (type.equals(PARAM_START_INDEX)) {
				return START_INDEX;
			} else if (type.equals(PARAM_START_PAGE)) {
				return START_PAGE;
			} else if (type.equals(PARAM_LANGUAGE)) {
				return LANGUAGE;
			} else if (type.equals(PARAM_INPUT_ENCODING)) {
				return INPUT_ENCODING;
			} else if (type.equals(PARAM_OUTPUT_ENCODING)) {
				return OUTPUT_ENCODING;
			} else if (type.equals(PARAM_TIME_START)) {
				return TIME_START;
			} else if (type.equals(PARAM_TIME_END)) {
				return TIME_END;
			} else if (type.equals(PARAM_GEO_NAME)
					|| type.equals(PARAM_GEO_LOCATION_STRING)) {
				return GEO_NAME;
			} else if (type.equals(PARAM_GEO_LAT)) {
				return GEO_LAT;
			} else if (type.equals(PARAM_GEO_LON)) {
				return GEO_LON;
			} else if (type.equals(PARAM_GEO_RADIUS)) {
				return GEO_RADIUS;
			} else if (type.equals(PARAM_GEO_BOX)) {
				return GEO_BOX;
			} else if (type.equals(PARAM_GEO_GEOMETRY)
					|| type.equals(PARAM_GEO_POLYGON)) {
				return GEO_GEOMETRY;
			}
		}
		throw new InvalidParameterException("Invalid value: " + value);
	}
	
	/**
	 * Returns the OpenSearch string parameter<br/>
	 * 
	 * Examples: {searchTerms}, {language}
	 * @param type - the int type of the value
	 * @return the OpenSearch string parameter
	 */
	public static String toString(TemplateParameter type) {
		if (type != null) {
			switch (type) {
			case SEARCH_TERM:
				return PARAM_SEARCH_TERMS;
			case COUNT:
				return PARAM_COUNT;
			case START_INDEX:
				return PARAM_START_INDEX;
			case START_PAGE:
				return PARAM_START_PAGE;
			case LANGUAGE:
				return PARAM_LANGUAGE;
			case INPUT_ENCODING:
				return PARAM_INPUT_ENCODING;
			case OUTPUT_ENCODING:
				return PARAM_OUTPUT_ENCODING;
			case TIME_START:
				return PARAM_TIME_START;
			case TIME_END:
				return PARAM_TIME_END;
			case GEO_NAME:
				return PARAM_GEO_NAME;
			case GEO_LAT:
				return PARAM_GEO_LAT;
			case GEO_LON:
				return PARAM_GEO_LON;
			case GEO_RADIUS:
				return PARAM_GEO_RADIUS;
			case GEO_BOX:
				return PARAM_GEO_BOX;
			case GEO_GEOMETRY:
				return PARAM_GEO_GEOMETRY;
			}
		}
		return null;
	}
	
	/**
	 * Returns the OpenSearch string parameter<br/>
	 * @return the OpenSearch string parameter
	 */
	@Override
	public String toString() {
		return toString(this);
	}
}
