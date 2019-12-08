package pl.app.epublibrary.services.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.repositories.publisher.PublisherRepository;

@Service
public class PublisherService {

    private PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }


//    public void saveEntity(Publisher entity) {
//        if (getExistingEntity(entity) == null)
//            publisherRepository.save(entity);
//    }
//
//    public void updateEntity(Publisher entity) {
//
//    }
//
//    public Publisher getExistingEntity(Publisher entity) {
//        return publisherRepository.findByPublisherName(entity.getPublisherName());
//    }
}
