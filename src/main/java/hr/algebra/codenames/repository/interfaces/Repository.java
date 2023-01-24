package hr.algebra.codenames.repository.interfaces;

import hr.algebra.codenames.xml.model.XmlTurnLog;

import java.io.IOException;
import java.util.List;

public interface Repository {
     List<String> GetWords() throws IOException;
     void writeReflectionDocs(String ldocs) throws IOException;
     void writeGameLogs(String xmlTurns) throws IOException;
     String readGameLogs() throws IOException;
}
