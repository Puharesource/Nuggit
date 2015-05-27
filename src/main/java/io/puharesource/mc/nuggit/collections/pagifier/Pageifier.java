package io.puharesource.mc.nuggit.collections.pagifier;

import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pageifier<E> {
    private final List<E> list;
    private @Setter int pageSize = 8;

    public Pageifier(List<E> collection) {
        this.list = collection;
    }

    public Pageifier(E[] array) {
        this.list = Arrays.asList(array);
    }

    public PagifierResult<E> getPage(int page) {
        final int totalPages = list.size() / pageSize + (list.size() % pageSize > 0 ? 1 : 0);

        page = page < 1 ? 0 : page - 1;
        page = page < totalPages ? page : totalPages - 1;

        final int from = page * pageSize;
        final int to = from + pageSize;

        return new PagifierResult<>(getSpecificObjects(from, to), page, pageSize, totalPages);
    }

    @SuppressWarnings("unchecked")
    public List<E> getSpecificObjects(int from, int to) {
        if (list.size() < to)
            to = list.size();

        final List<E> newList = new ArrayList<>();
        for (int i = from; to > i && list.size() > i; i++)
            newList.add(list.get(i));
        return newList;
    }
}