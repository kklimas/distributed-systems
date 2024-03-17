package org.zad2.lookup.dataio;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.zad2.lookup.LookupData;
import org.zad2.lookup.common.AbstractLookupProvider;
import org.zad2.lookup.common.LookupRegistry;
import reactor.core.publisher.Flux;

@Component
public class DataIOLookupProvider extends AbstractLookupProvider<DataIOLookupModelWrapper> {

    private final DataIOLookupConverter converter;

    public DataIOLookupProvider(LookupRegistry registry, DataIOLookupProperties properties) {
        super(registry, properties);
        this.converter = new DataIOLookupConverter();
    }


    @Override
    protected Flux<LookupData> map(WebClient.ResponseSpec responseSpec) {
        return responseSpec.bodyToMono(DataIOLookupModelWrapper.class)
                .map(DataIOLookupModelWrapper::results)
                .flatMapMany(Flux::fromIterable)
                .map(converter::convert);
    }

}
