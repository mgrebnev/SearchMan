package ru.searchman.async.methods;

import ru.searchman.controllers.SettingsFormController;
import ru.searchman.models.BookFragment;

import java.util.List;

public class BooksFinishedSearchMethod implements FinishedMethod{
    @Override
    public void finish(long firstTime, List<BookFragment> data) {
        long endTime = System.currentTimeMillis();
        System.out.println("Время выполнения: " + (Double.valueOf(endTime - firstTime) / 1000) + " сек.");
        System.out.println("Количество найденных фрагментов: " + data.size());
        for (BookFragment fragment: data){
            System.out.println("Фрагмент #" + fragment.getId() + ", книга - " + fragment.getFile().getName());
            List<String> text = fragment.getTextFragment();
            for (String sentence: text){
                if (sentence.contains(fragment.getFindWord())){
                    sentence = sentence.replace(fragment.getFindWord()," {" + fragment.getFindWord() + "} ");
                }
                System.out.println(sentence);
            }
        }
        SettingsFormController.service.getSearchButton().setDisable(false);
    }
}
