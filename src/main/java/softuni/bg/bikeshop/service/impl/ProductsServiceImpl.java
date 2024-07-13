package softuni.bg.bikeshop.service.impl;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.bg.bikeshop.models.Bike;
import softuni.bg.bikeshop.models.BikeType;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.UserDetailEntity;
import softuni.bg.bikeshop.models.dto.AddBikeDto;
import softuni.bg.bikeshop.models.dto.parts.AddChainPartDto;
import softuni.bg.bikeshop.models.dto.parts.AddFramePartDto;
import softuni.bg.bikeshop.models.dto.parts.AddPartDto;
import softuni.bg.bikeshop.models.dto.parts.AddTiresPartDto;
import softuni.bg.bikeshop.models.parts.*;
import softuni.bg.bikeshop.repository.ProductRepository;
import softuni.bg.bikeshop.repository.UserRepository;
import softuni.bg.bikeshop.service.ProductsService;

import java.security.Principal;
import java.util.Optional;

@Service
public class ProductsServiceImpl implements ProductsService {


}
