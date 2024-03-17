package org.zad2.lookup;

import lombok.Builder;

import java.util.List;

@Builder
public record LookupData(
        String title,
        String description,
        String source,
        List<String> categories,
        String imageUrl
) {
}
