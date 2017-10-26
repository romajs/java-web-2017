package com.romajs.rest.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractRS {

	final Logger logger = LoggerFactory.getLogger(getClass());

	String fixOptionalPathParam(String group) {
		return group != null && !group.trim().isEmpty() ? group.split("/")[2] : null;
	}

}
