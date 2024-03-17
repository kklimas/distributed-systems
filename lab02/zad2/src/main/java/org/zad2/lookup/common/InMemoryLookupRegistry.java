package org.zad2.lookup.common;

import org.springframework.stereotype.Component;
import org.zad2.lookup.common.LookupProvider;
import org.zad2.lookup.common.LookupRegistry;

import java.util.Collection;
import java.util.HashSet;

@Component
public class InMemoryLookupRegistry implements LookupRegistry {

    private final HashSet<LookupProvider> providers = new HashSet<>();

    @Override
    public void register(LookupProvider provider) {
        providers.add(provider);
    }

    @Override
    public Collection<LookupProvider> listProviders() {
        return providers.stream().toList();
    }

}
