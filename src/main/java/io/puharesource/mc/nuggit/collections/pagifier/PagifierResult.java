package io.puharesource.mc.nuggit.collections.pagifier;

import lombok.Getter;

import java.text.MessageFormat;
import java.util.List;

public class PagifierResult<E> {
    private @Getter final List<E> list;
    private @Getter final int page;
    private @Getter final int pageSize;
    private @Getter final int maxPages;

    public PagifierResult(List<E> list, final int page, final int pageSize, final int maxPages) {
        this.list = list;
        this.page = page + 1;
        this.pageSize = pageSize;
        this.maxPages = maxPages;
    }

    public String getHeader(String message) {
        return MessageFormat.format(message, page, maxPages);
    }
}
