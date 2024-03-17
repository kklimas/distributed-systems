package org.zad2.lookup.dataio;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.zad2.lookup.configuration.LookupProperties;

@ConfigurationProperties(prefix = "api.data-io")
public class DataIOLookupProperties extends LookupProperties {
}
