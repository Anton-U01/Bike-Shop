package softuni.bg.bikeshop.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import softuni.bg.bikeshop.exceptions.UserNotFoundException;
import softuni.bg.bikeshop.models.Bike;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.dto.AddBikeDto;
import softuni.bg.bikeshop.models.dto.EditBikeDto;
import softuni.bg.bikeshop.repository.BikeRepository;
import softuni.bg.bikeshop.repository.PictureRepository;
import softuni.bg.bikeshop.repository.ProductRepository;
import softuni.bg.bikeshop.repository.UserRepository;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BikeServiceImplTest {
    @InjectMocks
    private BikeServiceImpl bikeService;
    @Mock
    private  ProductRepository productRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  ModelMapper modelMapper;
    @Mock
    private  BikeRepository bikeRepository;
    @Mock
    private  PictureRepository pictureRepository;
    @Captor
    private ArgumentCaptor<Bike> bikeCapture;
    private List<MultipartFile> files;
    private AddBikeDto addBikeDto;
    private Bike bike;
    private EditBikeDto editBikeDto;


    private Principal principal;

    @BeforeEach
    void setUp() {
        files = List.of(new MockMultipartFile("file", "file", "image/png", "file".getBytes()));
        principal = ()-> "testUser";
        addBikeDto = new AddBikeDto();
        addBikeDto.setName("test");
        addBikeDto.setFrame("test");
        addBikeDto.setType("ROAD");
        addBikeDto.setBrakes("test");
        addBikeDto.setPrice(156);
        addBikeDto.setDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        addBikeDto.setWheelsSize(18);

        bike = new Bike();

        editBikeDto = new EditBikeDto();
        editBikeDto.setId(1L);
        editBikeDto.setName("test1");
        editBikeDto.setFrame("test1");
        editBikeDto.setType("ROAD");
        editBikeDto.setBrakes("test1");
        editBikeDto.setPrice(250);
        editBikeDto.setDescription("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        editBikeDto.setWheelsSize(22);
    }

    @Test
    void  addBikeUserNotFoundTest() throws IOException {
        when(modelMapper.map(addBikeDto, Bike.class))
                .thenReturn(bike);
        when(userRepository.findByUsername(principal.getName()))
                .thenReturn(Optional.empty());

        try {
            this.bikeService.add(addBikeDto, principal,files);
        } catch (UserNotFoundException e){
            Assertions.assertEquals("User with username testUser is not found!", e.getMessage());
        }

    }
    @Test
    void  addBikeSuccessTest() throws IOException {
        User user = new User();
        when(modelMapper.map(addBikeDto, Bike.class))
                .thenReturn(bike);
        when(userRepository.findByUsername(principal.getName()))
                .thenReturn(Optional.of(user));


        boolean success = this.bikeService.add(addBikeDto, principal, files);

        Assertions.assertTrue(success);
        verify(productRepository).saveAndFlush(bikeCapture.capture());
        Bike actual = bikeCapture.getValue();
        Assertions.assertEquals(bike.getName(),actual.getName());
        Assertions.assertEquals(bike.getWheelsSize(),actual.getWheelsSize());
        Assertions.assertEquals(bike.getName(),actual.getBrakes());
    }
    @Test
    void  editBikeSuccessTest() throws IOException {

        when(bikeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(bike));


        boolean success = this.bikeService.edit(editBikeDto,1L);

        Assertions.assertTrue(success);
        verify(bikeRepository).saveAndFlush(bikeCapture.capture());
        Bike actual = bikeCapture.getValue();
        Assertions.assertEquals(bike.getName(),actual.getName());
        Assertions.assertEquals(bike.getWheelsSize(),actual.getWheelsSize());
        Assertions.assertEquals(bike.getBrakes(),actual.getBrakes());

    }
}
