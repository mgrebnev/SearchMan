package ru.searchman.async.threads;

import ru.searchman.async.resolvers.FragmentIntegrityResolver;
import ru.searchman.utils.FragmentUtil;

import java.util.List;

public class FragmentSearchThread extends Thread {
    private List<String> fragment;
    private FragmentIntegrityResolver fragmentIntegrityResolver;
    private List<String> keyWords;

    public FragmentSearchThread(List<String> fragment, FragmentIntegrityResolver fragmentIntegrityResolver,List<String> keyWords) {
        //System.out.println("Create thread book " + fragmentIntegrityResolver.getIdBook());
        System.out.println(fragment.size());
        this.fragment = fragment;
        this.fragmentIntegrityResolver = fragmentIntegrityResolver;
        this.keyWords = keyWords;
    }

    @Override
    public void run() {
        for (int i = 0; i < fragment.size(); i++){
            String currentSentence = fragment.get(i);
            for (String findWord: keyWords) {
                if (isContainsSubString(currentSentence, findWord)) {
                    fragmentIntegrityResolver.addFragment(
                            findWord,
                            FragmentUtil.getFragmentBySentence(fragment, i, 5)
                    );
                }
            }
        }
        fragmentIntegrityResolver.addProcessedFragment();
    }

    public boolean isContainsSubString(String text, String findText){
        for (int i = 0; i <= text.length() - findText.length(); i++){
            String currentSubString = text.substring(i,i+findText.length());
            if (currentSubString.equals(findText)) return true;
        }
        return false;
    }
}
