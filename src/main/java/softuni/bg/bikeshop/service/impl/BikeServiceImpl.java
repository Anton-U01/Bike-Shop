package softuni.bg.bikeshop.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.bg.bikeshop.exceptions.ProductNotFoundException;
import softuni.bg.bikeshop.exceptions.UserNotFoundException;
import softuni.bg.bikeshop.models.Bike;
import softuni.bg.bikeshop.models.BikeType;
import softuni.bg.bikeshop.models.Picture;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.dto.AddBikeDto;
import softuni.bg.bikeshop.models.dto.parts.EditBikeDto;
import softuni.bg.bikeshop.models.parts.Part;
import softuni.bg.bikeshop.repository.BikeRepository;
import softuni.bg.bikeshop.repository.PictureRepository;
import softuni.bg.bikeshop.repository.ProductRepository;
import softuni.bg.bikeshop.repository.UserRepository;
import softuni.bg.bikeshop.service.BikeService;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class BikeServiceImpl implements BikeService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BikeRepository bikeRepository;
    private final PictureRepository pictureRepository;
    public BikeServiceImpl(ProductRepository productRepository, UserRepository userRepository, ModelMapper modelMapper, BikeRepository bikeRepository, PictureRepository pictureRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bikeRepository = bikeRepository;
        this.pictureRepository = pictureRepository;
    }


    @Override
    public boolean add(AddBikeDto addBikeDto, Principal principal, List<MultipartFile> files) throws IOException {

        Bike bike = modelMapper.map(addBikeDto, Bike.class);
        BikeType bikeType = BikeType.valueOf(addBikeDto.getType());
        bike.setType(bikeType);
        User seller = userRepository.findByUsername(principal.getName())
                .orElseThrow(()-> new UserNotFoundException("User with username " + principal.getName() + "is not found!"));
        bike.setSeller(seller);

        List<Picture> pictureList = new ArrayList<>();
        for (MultipartFile file : files) {
            byte[] bytes = file.getBytes();
            String image = Base64.getEncoder().encodeToString(bytes);

            Picture picture = new Picture();
            picture.setTitle(file.getName());
            picture.setUrl(image);
            picture.setAuthor(seller);
            pictureRepository.saveAndFlush(picture);
            pictureList.add(picture);
        }

        bike.setPictures(pictureList);

        productRepository.saveAndFlush(bike);
        for (Picture picture : pictureList) {
            picture.setProduct(bike);
            pictureRepository.saveAndFlush(picture);
        }

        return true;
    }

    @Override
    public boolean edit(EditBikeDto editBike, Long id) {
        Bike bike = bikeRepository.findById(editBike.getId())
                .orElseThrow(()-> new ProductNotFoundException("Part with id " + editBike.getId() + " was not found!"));

        bike.setName(editBike.getName());
        bike.setDescription(editBike.getDescription());
        bike.setPrice(editBike.getPrice());
        bike.setBrakes(editBike.getBrakes());
        bike.setFrame(editBike.getFrame());
        bike.setWheelsSize(editBike.getWheelsSize());
        bikeRepository.saveAndFlush(bike);
        return true;
    }

}
