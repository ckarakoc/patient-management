package nl.ckarakoc.authservice.service;

import java.util.Optional;
import nl.ckarakoc.authservice.model.User;
import nl.ckarakoc.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

   public UserService(UserRepository userRepository) {
     this.userRepository = userRepository;
   }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}
