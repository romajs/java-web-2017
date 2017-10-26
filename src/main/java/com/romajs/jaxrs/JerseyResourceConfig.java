package com.romajs.jaxrs;

import com.romajs.jaxrs.feature.JerseyJacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class JerseyResourceConfig extends ResourceConfig {

	{
		register(JerseyJacksonFeature.class);
	}
}
