package ru.searchman.async.resolvers;

import ru.searchman.models.BookFragment;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FragmentIntegrityResolver {
    private AtomicInteger currentCountProcessedFragments;
    private Integer maxCountProcessedFragments;
    private BooksIntegrityResolver booksIntegrityResolver;

    private Integer idBook;
    private File currentBookFile;

    private AtomicInteger countFoundFragments;

    public FragmentIntegrityResolver(AtomicInteger currentCountProcessedFragments,
                                     Integer maxCountProcessedFragments,
                                     BooksIntegrityResolver booksIntegrityResolver,
                                     Integer idBook,
                                     File currentBookFile) {
        this.currentCountProcessedFragments = currentCountProcessedFragments;
        this.maxCountProcessedFragments = maxCountProcessedFragments;
        this.booksIntegrityResolver = booksIntegrityResolver;
        this.idBook = idBook;
        this.currentBookFile = currentBookFile;
        this.countFoundFragments = new AtomicInteger(0);
    }


    public void addProcessedFragment(){
        if (currentCountProcessedFragments.addAndGet(1) >= maxCountProcessedFragments){
            booksIntegrityResolver.addProcessedBook();
        }
    }

    public void addFragment(String keyWord, List<String> foundFragment){
        BookFragment foundBookFragment = new BookFragment(
                this.countFoundFragments.addAndGet(1),
                foundFragment,
                this.currentBookFile,
                keyWord
        );
        booksIntegrityResolver.addFragment(foundBookFragment);
    }

    public Integer getIdBook() {
        return idBook;
    }
}
