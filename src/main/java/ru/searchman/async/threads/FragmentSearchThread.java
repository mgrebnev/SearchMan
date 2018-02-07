package ru.searchman.async.threads;

import ru.searchman.async.resolvers.FragmentIntegrityResolver;
import ru.searchman.utils.FragmentUtil;

import java.util.List;

public class FragmentSearchThread extends Thread {
    private List<String> fragment;
    private FragmentIntegrityResolver fragmentIntegrityResolver;
    private List<String> keyWords;
    private Integer sizeFragment;

    public FragmentSearchThread(List<String> fragment,
                                FragmentIntegrityResolver fragmentIntegrityResolver,
                                List<String> keyWords,
                                Integer sizeFragment) {
        this.fragment = fragment;
        this.fragmentIntegrityResolver = fragmentIntegrityResolver;
        this.keyWords = keyWords;
        this.sizeFragment = sizeFragment;
    }

    @Override
    public void run() {
        for (int i = 0; i < fragment.size(); i++){
            String currentSentence = fragment.get(i);
            for (String findWord: keyWords) {
                if (currentSentence.contains(findWord)) {
                    fragmentIntegrityResolver.addFragment(
                            findWord,
                            FragmentUtil.getFragmentBySentence(fragment, i, sizeFragment)
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
