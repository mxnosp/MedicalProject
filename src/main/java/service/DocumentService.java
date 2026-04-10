package service;

import repository.DocumentRepository;

public class DocumentService {
    private final DocumentRepository repo;

    public DocumentService(){
        this.repo=new DocumentRepository();
    }


}
