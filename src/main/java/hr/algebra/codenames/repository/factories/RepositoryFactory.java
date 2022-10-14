package hr.algebra.codenames.repository.factories;

import hr.algebra.codenames.repository.implementations.MemoryWordRepository;
import hr.algebra.codenames.repository.interfaces.WordRepository;

public class RepositoryFactory {
    public static WordRepository getWordRepository(){
        return new MemoryWordRepository();
    }
}
