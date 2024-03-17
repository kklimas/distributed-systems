package org.zad2.lookup.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.zad2.lookup.LookupData;
import org.zad2.lookup.LookupQuery;
import org.zad2.lookup.configuration.LookupProperties;
import reactor.core.publisher.Flux;

@Slf4j
public abstract class AbstractLookupProvider<T> implements LookupProvider {

    private final WebClient webClient;
    private final LookupProperties properties;

    public AbstractLookupProvider(LookupRegistry registry, LookupProperties properties) {
        this.properties = properties;
        this.webClient = WebClient.create(properties.getUrl());
        registry.register(this);
    }

    @Override
    public Flux<LookupData> lookup(LookupQuery query) {
        return map(webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam(properties.getApiKeyName(), properties.getApiKeyValue())
                        .queryParam(properties.getSearchParameter(), query.getSearch())
                        .build())
                .retrieve())
                .onErrorResume(err -> {
                    log.error("Cannot retrieve data from {}. Exception with following message was observed: {}.", properties.getName(), err.getMessage());
                    return Flux.empty();
                });
    }

    protected abstract Flux<LookupData> map(WebClient.ResponseSpec responseSpec);

}
