package org.zad2.lookup.dataapi;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.zad2.lookup.configuration.LookupProperties;

@ConfigurationProperties(prefix = "api.data-api")
public class DataApiLookupProperties extends LookupProperties {
}
