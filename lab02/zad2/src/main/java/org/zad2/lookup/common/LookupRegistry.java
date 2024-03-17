package org.zad2.lookup.common;

import java.util.Collection;

public interface LookupRegistry {
    void register(LookupProvider provider);
    Collection<LookupProvider> listProviders();
}
