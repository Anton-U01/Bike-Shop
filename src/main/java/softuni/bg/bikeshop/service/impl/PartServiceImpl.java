package softuni.bg.bikeshop.service.impl;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.dto.parts.AddPartDto;
import softuni.bg.bikeshop.models.dto.parts.EditPartDto;
import softuni.bg.bikeshop.models.parts.*;
import softuni.bg.bikeshop.repository.PartRepository;
import softuni.bg.bikeshop.repository.ProductRepository;
import softuni.bg.bikeshop.repository.UserRepository;
import softuni.bg.bikeshop.service.PartService;

import java.security.Principal;
import java.util.Optional;

@Service
public class PartServiceImpl implements PartService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PartRepository partRepository;

    public PartServiceImpl(ProductRepository productRepository, UserRepository userRepository, PartRepository partRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.partRepository = partRepository;
    }

    @Override
    public boolean add(AddPartDto addPartDto, Principal principal) {
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

        Optional<User> optional = userRepository.findByUsername(principal.getName());
        if (optional.isEmpty()) {
            return false;
        }
        User seller = optional.get();
        setCommonPartFields(part,addPartDto,seller,partType);

        productRepository.saveAndFlush(part);
        return true;
    }
    private void setCommonPartFields(Part part, AddPartDto addPartDto,User seller,PartType partType) {
        part.setName(addPartDto.getName());
        part.setDescription(addPartDto.getDescription());
        part.setPrice(addPartDto.getPrice());
        part.setManufacturer(addPartDto.getManufacturer());
        part.setSeller(seller);
        part.setType(partType);
    }

    @Override
    public boolean edit(@Valid EditPartDto editPart) {
        Optional<Part> optionalPart = partRepository.findById(editPart.getId());
        if(optionalPart.isEmpty()){
            return false;
        }
        Part part = optionalPart.get();

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
        part.setPrice(editPart.getPrice());
        part.setManufacturer(editPart.getManufacturer());

        partRepository.saveAndFlush(part);

        return true;
    }


}
