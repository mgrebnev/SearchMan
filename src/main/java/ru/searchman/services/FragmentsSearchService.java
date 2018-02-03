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
                            this.countThreads,
                            booksIntegrityResolver,
                            counter,
                            file
                    );
                    //Divide mainFragment and create thread
                    Integer sizeSubList = this.countThreads;
                    Integer onePart = mainFragment.size() / sizeSubList;
                    for (int i = 0; i < sizeSubList; i++){
                        if (i == sizeSubList - 1){
                            List<String> current = mainFragment.subList(i*onePart,mainFragment.size());
                            System.out.println("id: " + counter + " size: " + current.size());
                            createFragmentSearchThread(
                                    mainFragment.subList(i*onePart,mainFragment.size()),
                                    fragmentIntegrityResolver
                            ).start();
                        }else{
                            List<String> current = mainFragment.subList(i*onePart,(i+1) * onePart);
                            System.out.println("id: " + counter + " size: " + current.size());
                            createFragmentSearchThread(
                                    mainFragment.subList(i*onePart,(i+1) * onePart),
                                    fragmentIntegrityResolver
                            ).start();
                        }
                    }
                    ++counter;
                }
            }catch (Exception ex){
                System.out.println("Ex: "+ ex.getMessage());
            }
        });
        mainThread.start();
    }

    private FragmentSearchThread createFragmentSearchThread(List<String> subFragment, FragmentIntegrityResolver resolver){
        return new FragmentSearchThread(subFragment,resolver,keyWords);
    }
}
