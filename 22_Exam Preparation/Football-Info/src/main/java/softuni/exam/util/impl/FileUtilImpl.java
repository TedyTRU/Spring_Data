package softuni.exam.util.impl;

import softuni.exam.util.FileUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtilImpl implements FileUtil {

    @Override
    public String readFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }
}
