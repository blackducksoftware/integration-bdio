/*
 * integration-bdio
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.bdio.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.synopsys.integration.util.IntegrationEscapeUtil;

public class BdioIdEscaper {
    private IntegrationEscapeUtil integrationEscapeUtil;

    public BdioIdEscaper() {
        this(new IntegrationEscapeUtil());
    }

    public BdioIdEscaper(IntegrationEscapeUtil integrationEscapeUtil) {
        this.integrationEscapeUtil = integrationEscapeUtil;
    }

    public List<String> escapePiecesForUri(final List<String> pieces) {
        final List<String> escapedPieces = new ArrayList<>(pieces.size());
        for (final String piece : pieces) {
            final String escaped = escapeForUri(piece);
            escapedPieces.add(escaped);
        }

        return escapedPieces;
    }

    public String escapeForUri(final String s) {
        if (s == null) {
            return null;
        }

        try {
            return URLEncoder.encode(s, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return poorManEscaping(s);
        }
    }

    private String poorManEscaping(String s) {
        return integrationEscapeUtil.replaceWithUnderscore(s);
    }

}
