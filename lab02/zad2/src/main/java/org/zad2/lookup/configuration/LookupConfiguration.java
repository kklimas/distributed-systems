package org.zad2.lookup.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.zad2.lookup.dataapi.DataApiLookupProperties;
import org.zad2.lookup.dataio.DataIOLookupProperties;

@Configuration
@EnableConfigurationProperties({DataApiLookupProperties.class, DataIOLookupProperties.class})
public class LookupConfiguration {
}
