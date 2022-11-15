package hr.algebra.codenames.repository.interfaces;

import java.io.IOException;
import java.util.List;

public interface Repository {
    public List<String> GetWords() throws IOException;
    public void writeReflectionDocs(String ldocs) throws IOException;
}
