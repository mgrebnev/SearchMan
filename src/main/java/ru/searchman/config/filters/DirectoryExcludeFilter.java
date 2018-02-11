package ru.searchman.config.filters;

import java.io.File;
import java.io.FileFilter;

public class DirectoryExcludeFilter implements FileFilter{
    @Override
    public boolean accept(File file) {
        if (file.isDirectory())
            return false;
        return true;
    }
}
