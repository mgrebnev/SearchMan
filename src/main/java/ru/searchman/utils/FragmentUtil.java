package ru.searchman.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentUtil {
    public static List<String> getFragmentBySentence(List<String> currentPaper, int startIndex, int sizeFragment){
        List<String> resultList = new ArrayList<>();
        if (startIndex < currentPaper.size() && startIndex >= 0){
            int count = 0;
            for (int i = startIndex - 1; i >= 0; i--){
                if (count == sizeFragment)
                    break;
                else
                    resultList.add(currentPaper.get(i));
                ++count;
            }

            Collections.reverse(resultList);
            String currentSentence = currentPaper.get(startIndex);
            currentSentence = "[" + currentSentence + "]";
            resultList.add(currentSentence);

            if (startIndex < currentPaper.size()){
                for (int i = startIndex + 1; i < startIndex + sizeFragment + 1; i++){
                    if (i < currentPaper.size())
                        resultList.add(currentPaper.get(i));
                    else
                        break;
                }
            }
        }
        return resultList;
    }
}
