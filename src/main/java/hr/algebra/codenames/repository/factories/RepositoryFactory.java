package hr.algebra.codenames.repository.factories;

import hr.algebra.codenames.repository.implementations.FileRepository;
import hr.algebra.codenames.repository.interfaces.Repository;

public class RepositoryFactory {
    public static Repository getRepository() {
        return new FileRepository();
    }
}
