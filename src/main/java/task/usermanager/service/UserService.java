package task.usermanager.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import task.usermanager.dto.PageRequestDTO;
import task.usermanager.dto.UserDTO;
import task.usermanager.model.User;
import task.usermanager.repository.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder encoder;

    public UserDTO create(UserDTO userDTO) {
        userDTO.setId(null);
        validateLoginUniqueness(userDTO.getLogin());
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(encoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO update(UserDTO userDTO) {
        User existingUser = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        User userModifications = modelMapper.map(userDTO, User.class);
        if (userModifications.getLogin() != null) {
            validateLoginUniqueness(userDTO.getLogin());
            existingUser.setLogin(userModifications.getLogin());
        }
        if (userModifications.getPassword() != null) {
            existingUser.setPassword(encoder.encode(userModifications.getPassword()));
        }
        if (userModifications.getFullName() != null) {
            existingUser.setFullName(userModifications.getFullName());
        }
        if (userModifications.getGender() != null) {
            existingUser.setGender(userModifications.getGender());
        }

        User updatedUser = userRepository.save(existingUser);

        return modelMapper.map(updatedUser, UserDTO.class);
    }

    public void delete(Long id, String requesterLogin) {
        User requesterUser = userRepository.findByLogin(requesterLogin);
        if (requesterUser.getId().equals(id)) {
            throw new IllegalArgumentException("User can't delete their account");
        }
        userRepository.deleteById(id);
    }

    public void deleteRange(Long startId, Long endId, String requesterLogin) {
        User requesterUser = userRepository.findByLogin(requesterLogin);
        if (isIdInRange(startId, endId, requesterUser.getId())) {
            throw new IllegalArgumentException("User can't delete their account");
        }
        userRepository.deleteRange(startId, endId);
    }

    public Page<UserDTO> find(PageRequestDTO pageRequestDTO) {
        Page<User> foundPage = userRepository.findAll(PageRequest.of(pageRequestDTO.getPageNumber(), pageRequestDTO.getPageSize()));
        List<UserDTO> userDTOs = foundPage.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
        return new PageImpl<>(userDTOs, foundPage.getPageable(), foundPage.getTotalElements());
    }

    private void validateLoginUniqueness(String login) {
        if (userRepository.existsByLogin(login)) {
            throw new EntityExistsException("User '" + login + "' already exists");
        }
    }

    private boolean isIdInRange(long startId, long endId, long id) {
        if (startId > endId) {
            throw new IllegalArgumentException("startId must be less than or equal to endId");
        }

        return id >= startId && id <= endId;
    }
}
