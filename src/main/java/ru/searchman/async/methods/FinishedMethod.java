package ru.searchman.async.methods;

import ru.searchman.models.BookFragment;

import java.util.List;

public interface FinishedMethod {
    void finish(long firstTime, List<BookFragment> data);
}
