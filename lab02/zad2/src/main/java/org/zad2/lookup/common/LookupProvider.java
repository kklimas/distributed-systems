package org.zad2.lookup.common;

import org.zad2.lookup.LookupData;
import org.zad2.lookup.LookupQuery;
import reactor.core.publisher.Flux;

public interface LookupProvider {
    Flux<LookupData> lookup(LookupQuery query);
}
