package org.zad2.lookup.dataio;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DataIOLookupModel(
        String title,
        String description,
        @JsonProperty("source_url")
        String sourceUrl,
        @JsonProperty("category")
        List<String> categories,
        @JsonProperty("image_url")
        String imageUrl
) {
}
