package org.zad2.lookup.dataio;

import org.zad2.lookup.LookupData;
import org.zad2.lookup.common.LookupDataConverter;

public class DataIOLookupConverter implements LookupDataConverter<DataIOLookupModel> {
    @Override
    public LookupData convert(DataIOLookupModel data) {
        return LookupData.builder()
                .title(data.title())
                .description(data.description())
                .source(data.sourceUrl())
                .categories(data.categories())
                .imageUrl(data.imageUrl())
                .build();
    }
}
