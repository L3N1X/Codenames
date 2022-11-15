package hr.algebra.codenames.repository.implementations;

import hr.algebra.codenames.repository.interfaces.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileRepository implements Repository {

    //region Private static variables
    private static final String WORDS_FILE = "words.txt";
    private static final String WORDS_DIR = "Words";
    private static final Path WORDS_PATH = Paths.get(WORDS_DIR + File.separator + WORDS_FILE);

    private static final String DOCS_FILE = "CodenamesDocs.html";
    private static final String DOCS_DIR = "Documentation";
    private static final Path DOCS_DIR_PATH = Paths.get(DOCS_DIR);
    private static final Path DOCS_PATH = Paths.get(DOCS_DIR + File.separator + DOCS_FILE);

    //endregion
    @Override
    public List<String> GetWords() throws IOException {
        return Files.readAllLines(WORDS_PATH);
    }

    @Override
    public void writeReflectionDocs(String ldocs) throws IOException {
        if(!Files.exists(DOCS_PATH)){
            Files.createDirectories(DOCS_DIR_PATH);
            Files.createFile(DOCS_PATH);
        }
        Files.writeString(DOCS_PATH, ldocs);
    }
}
