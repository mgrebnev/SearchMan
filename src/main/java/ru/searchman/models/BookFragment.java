package ru.searchman.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookFragment {
    private Integer id;
    private List<String> textFragment;
    private File file;
    private String findWord;

    public String toString(){
        return "Фрагмент #" + id + ", книга - " + file.getName();
    }
}
