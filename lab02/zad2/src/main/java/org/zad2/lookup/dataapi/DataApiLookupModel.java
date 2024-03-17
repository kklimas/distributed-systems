package org.zad2.lookup.dataapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DataApiLookupModel(
        String title,
        String description,
        String url,
        List<String> categories,
        @JsonProperty("image_url")
        String imageUrl
) {
}
