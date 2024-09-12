package com.blackduck.integration.bdio.utility;

import com.blackduck.integration.bdio.graph.builder.LazyId;

import java.util.*;

public class LazyIdTestUtil {
    public static Set<LazyId> asSet(final LazyId... lazyIds) {
        return new HashSet<>(Arrays.asList(lazyIds));
    }

    public static List<LazyId> asList(final LazyId... lazyIds) {
        return new ArrayList<>(Arrays.asList(lazyIds));
    }

}
