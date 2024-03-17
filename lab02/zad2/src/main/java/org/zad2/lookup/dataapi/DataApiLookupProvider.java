package org.zad2.lookup.dataapi;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.zad2.lookup.LookupData;
import org.zad2.lookup.common.AbstractLookupProvider;
import org.zad2.lookup.common.LookupRegistry;
import reactor.core.publisher.Flux;

@Component
public class DataApiLookupProvider extends AbstractLookupProvider<DataApiLookupModelWrapper> {

    private final DataApiLookupConverter converter;

    public DataApiLookupProvider(LookupRegistry registry, DataApiLookupProperties properties) {
        super(registry, properties);
        this.converter = new DataApiLookupConverter();
    }

    @Override
    protected Flux<LookupData> map(WebClient.ResponseSpec responseSpec) {
        return responseSpec.bodyToMono(DataApiLookupModelWrapper.class)
                .map(DataApiLookupModelWrapper::data)
                .flatMapMany(Flux::fromIterable)
                .map(converter::convert);
    }
}
