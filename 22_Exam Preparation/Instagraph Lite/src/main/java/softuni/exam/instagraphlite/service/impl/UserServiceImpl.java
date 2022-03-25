package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.UserSeedDto;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.models.entity.User;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {

    public static final String USERS_FILE_PATH = "src/main/resources/files/users.json";

    private final UserRepository userRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final PictureService pictureService;

    public UserServiceImpl(UserRepository userRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil, PictureService pictureService) {
        this.userRepository = userRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.pictureService = pictureService;
    }

    @Override
    public boolean areImported() {
        return userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(USERS_FILE_PATH));
    }

    @Override
    public String importUsers() throws IOException {
        StringBuilder sb = new StringBuilder();

        UserSeedDto[] userSeedDtos = gson.fromJson(readFromFileContent(), UserSeedDto[].class);

        Arrays.stream(userSeedDtos)
                .filter(userSeedDto -> {
                    boolean isValid = isValid(userSeedDto);

                    sb
                            .append(isValid
                                    ? String.format("Successfully imported User: %s", userSeedDto.getUsername())
                                    : "Invalid User")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(userSeedDto -> {
                    User user = modelMapper.map(userSeedDto, User.class);
                    Picture picture = pictureService.findPictureByPath(userSeedDto.getProfilePicture());
                    user.setProfilePicture(picture);

                    return user;
                })
                .forEach(userRepository::save);

        return sb.toString();
    }

    private boolean isValid(UserSeedDto userSeedDto) {
        boolean isValid = validationUtil.isValid(userSeedDto)
                //&& userSeedDto.getUsername() != null
                //&& userSeedDto.getPassword() != null
                //&& userSeedDto.getProfilePicture() != null
                && pictureService.isEntityExists(userSeedDto.getProfilePicture())
                && !isEntityExist(userSeedDto.getUsername());
        return isValid;
    }

    @Override
    public boolean isEntityExist(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public String exportUsersWithTheirPosts() {
        return null;
    }

    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findUserByUserName(userName).orElse(null);
    }
}
