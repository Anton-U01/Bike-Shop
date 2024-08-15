package softuni.bg.bikeshop.service.impl;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.bg.bikeshop.exceptions.ProductNotFoundException;
import softuni.bg.bikeshop.exceptions.UserNotFoundException;
import softuni.bg.bikeshop.models.Picture;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.dto.parts.AddPartDto;
import softuni.bg.bikeshop.models.dto.parts.EditPartDto;
import softuni.bg.bikeshop.models.parts.*;
import softuni.bg.bikeshop.repository.PartRepository;
import softuni.bg.bikeshop.repository.PictureRepository;
import softuni.bg.bikeshop.repository.ProductRepository;
import softuni.bg.bikeshop.repository.UserRepository;
import softuni.bg.bikeshop.service.PartService;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartServiceImpl implements PartService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PartRepository partRepository;
    private final PictureRepository pictureRepository;

    public PartServiceImpl(ProductRepository productRepository, UserRepository userRepository, PartRepository partRepository, PictureRepository pictureRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.partRepository = partRepository;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public boolean add(AddPartDto addPartDto, Principal principal, List<MultipartFile> files) throws IOException {
        Part part = new Part();
        PartType partType = PartType.valueOf(addPartDto.getType());


        switch (partType) {
            case TIRES -> {
                TiresPart tiresPart = new TiresPart();
                tiresPart.setSize(Integer.parseInt(addPartDto.getDynamicFields().get("tireSize").toString()));
                part = tiresPart;
            }
            case CHAIN -> {
                ChainPart chainPart = new ChainPart();
                chainPart.setChainLinks(Integer.parseInt( addPartDto.getDynamicFields().get("chainLinks").toString()));
                chainPart.setSpeedsCount(Integer.parseInt( addPartDto.getDynamicFields().get("speedsCount").toString()));
                part = chainPart;
            }
            case FRAME -> {
                FramePart framePart = new FramePart();
                framePart.setWeight(Integer.parseInt( addPartDto.getDynamicFields().get("weight").toString()));
                framePart.setMaterial((String) addPartDto.getDynamicFields().get("material"));
                part = framePart;
            }
        }


        User seller = userRepository.findByUsername(principal.getName())
                        .orElseThrow(()->new UserNotFoundException("User with username " + principal.getName() + " was not found!"));

        setCommonPartFields(part,addPartDto,seller,partType);


        List<Picture> pictureList = new ArrayList<>();
        for (MultipartFile file : files) {
            byte[] bytes = file.getBytes();
            String image = Base64.getEncoder().encodeToString(bytes);

            Picture picture = new Picture();
            picture.setTitle(file.getName());
            picture.setUrl(image);
            pictureRepository.saveAndFlush(picture);
            pictureList.add(picture);
        }

        part.setPictures(pictureList);

        productRepository.saveAndFlush(part);
        for (Picture picture : pictureList) {
            picture.setProduct(part);
            pictureRepository.saveAndFlush(picture);
        }

        return true;
    }
    private void setCommonPartFields(Part part, AddPartDto addPartDto,User seller,PartType partType) {
        part.setName(addPartDto.getName());
        part.setDescription(addPartDto.getDescription());
        part.setPrice(addPartDto.getPrice());
        part.setQuantity(addPartDto.getQuantity());
        part.setManufacturer(addPartDto.getManufacturer());
        part.setSeller(seller);
        part.setCreatedOn(LocalDateTime.now());
        part.setType(partType);
    }

    @Override
    public boolean edit(@Valid EditPartDto editPart) {
        Part part = partRepository.findById(editPart.getId())
                .orElseThrow(()-> new ProductNotFoundException("Part with id " + editPart.getId() + " was not found!"));

        if(part instanceof FramePart framePart){
            framePart.setMaterial(editPart.getDynamicFields().get("material").toString());
            framePart.setWeight(Double.parseDouble(editPart.getDynamicFields().get("weight").toString()));
        } else if(part instanceof ChainPart chainPart){
            chainPart.setSpeedsCount(Integer.parseInt(editPart.getDynamicFields().get("speedsCount").toString()));
            chainPart.setChainLinks(Integer.parseInt(editPart.getDynamicFields().get("chainLinks").toString()));
        } else if(part instanceof TiresPart tiresPart){
            tiresPart.setSize(Integer.parseInt(editPart.getDynamicFields().get("tireSize").toString()));
        }
        part.setName(editPart.getName());
        part.setType(PartType.valueOf(editPart.getType()));
        part.setDescription(editPart.getDescription());
        part.setQuantity(editPart.getQuantity());
        part.setPrice(editPart.getPrice());
        part.setManufacturer(editPart.getManufacturer());

        partRepository.saveAndFlush(part);

        return true;
    }



}
