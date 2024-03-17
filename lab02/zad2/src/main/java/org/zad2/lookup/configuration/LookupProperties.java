package org.zad2.lookup.configuration;

import lombok.Data;

@Data
public class LookupProperties {
    private String url;
    private String name;
    private String apiKeyName;
    private String apiKeyValue;
    private String searchParameter;
}
