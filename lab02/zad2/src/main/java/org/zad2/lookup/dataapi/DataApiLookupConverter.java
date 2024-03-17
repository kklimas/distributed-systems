package org.zad2.lookup.dataapi;

import org.zad2.lookup.LookupData;
import org.zad2.lookup.common.LookupDataConverter;

public class DataApiLookupConverter implements LookupDataConverter<DataApiLookupModel> {
    @Override
    public LookupData convert(DataApiLookupModel data) {
        return LookupData.builder()
                .title(data.title())
                .description(data.description())
                .source(data.url())
                .categories(data.categories())
                .imageUrl(data.imageUrl())
                .build();
    }
}
