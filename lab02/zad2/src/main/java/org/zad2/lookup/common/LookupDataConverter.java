package org.zad2.lookup.common;

import org.zad2.lookup.LookupData;

public interface LookupDataConverter<T> {
    LookupData convert(T data);
}
