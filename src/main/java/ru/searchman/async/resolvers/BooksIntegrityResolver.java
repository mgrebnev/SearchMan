package ru.searchman.async.resolvers;

import ru.searchman.async.methods.FinishedMethod;
import ru.searchman.models.BookFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BooksIntegrityResolver {
    private AtomicInteger currentCountProcessedBooks;
    private FinishedMethod finishedMethod;
    private Integer maxCountProcessedBooks;
    private Long startTime;

    private List<BookFragment> foundFragments;

    public BooksIntegrityResolver(AtomicInteger currentCountProcessedBooks, FinishedMethod finishedMethod, Integer maxCountProcessedBooks, Long startTime) {
        this.currentCountProcessedBooks = currentCountProcessedBooks;
        this.finishedMethod = finishedMethod;
        this.maxCountProcessedBooks = maxCountProcessedBooks;
        this.startTime = startTime;
        this.foundFragments = new ArrayList<>();
    }

    public void addProcessedBook(){
        if (currentCountProcessedBooks.addAndGet(1) >= maxCountProcessedBooks){
            this.finishedMethod.finish(startTime,foundFragments);
        }
    }

    public void addFragment(BookFragment bookFragment){
        this.foundFragments.add(bookFragment);
    }
}
