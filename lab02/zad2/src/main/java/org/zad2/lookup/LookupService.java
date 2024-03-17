package org.zad2.lookup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zad2.lookup.common.LookupRegistry;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class LookupService {

    private final LookupRegistry lookupRegistry;

    public Flux<LookupData> lookup(LookupQuery query) {
        return Flux.fromStream(lookupRegistry.listProviders()
                        .parallelStream()
                        .map(provider -> provider.lookup(query)))
                .flatMap(x -> x);
    }

}
