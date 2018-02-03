package ru.searchman.services;

import ru.searchman.async.methods.FinishedMethod;
import ru.searchman.async.resolvers.BooksIntegrityResolver;
import ru.searchman.async.resolvers.FragmentIntegrityResolver;
import ru.searchman.async.threads.FragmentSearchThread;
import ru.searchman.models.BookFragment;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FragmentsSearchService {
    private List<String> keyWords;
    private Integer lengthFragment;
    private Integer countThreads;
    private File mainDirectory;
    private FinishedMethod finishedMethod;

    public FragmentsSearchService(List<String> keyWords,
                                  Integer lengthFragment,
                                  Integer countThreads,
                                  File mainDirectory,
                                  FinishedMethod finishedMethod) {
        this.keyWords = keyWords;
        this.lengthFragment = lengthFragment;
        this.countThreads = countThreads;
        this.finishedMethod = finishedMethod;
        this.mainDirectory = mainDirectory;
    }

    public void startSearch() {
        final long firstTime = System.currentTimeMillis();
        final BooksIntegrityResolver booksIntegrityResolver = new BooksIntegrityResolver(
                new AtomicInteger(0),
                this.finishedMethod,
                this.mainDirectory.listFiles().length,
                firstTime
        );
        System.out.println("Количество файлов в папке: " + mainDirectory.listFiles().length);
        Thread mainThread = new Thread(() -> {
            try{
                int counter = 1;
                for (File file: mainDirectory.listFiles()) {
                    List<String> mainFragment = Files.readAllLines(Paths.get(file.getAbsolutePath()), Charset.forName("UTF-8"));
                    FragmentIntegrityResolver fragmentIntegrityResolver = new FragmentIntegrityResolver(
                            new AtomicInteger(0),
                            2,
                            booksIntegrityResolver,
                            counter,
                            file
                    );
                    FragmentSearchThread fragmentSearchThread1 = new FragmentSearchThread(
                            mainFragment.subList(0,mainFragment.size() / 2),
                            fragmentIntegrityResolver,
                            this.keyWords
                    );
                    FragmentSearchThread fragmentSearchThread2 = new FragmentSearchThread(
                            mainFragment.subList((mainFragment.size() / 2) + 1,mainFragment.size()),
                            fragmentIntegrityResolver,
                            this.keyWords
                    );
                    fragmentSearchThread1.start();
                    fragmentSearchThread2.start();
                    ++counter;
                }
            }catch (Exception ex){
                System.out.println("Ex: "+ ex.getMessage());
            }
        });
        mainThread.start();
    }
}
