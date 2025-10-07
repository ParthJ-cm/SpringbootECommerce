package com.shop.user_service.service;

import com.shop.user_service.DTO.DemoDto;
import com.shop.user_service.Entity.DemoEntity;
import com.shop.user_service.Entity.User;
import com.shop.user_service.repository.DemoRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DemoService {

    private final DemoRepo demoRepo;
    private final ModelMapper modelMapper;


    public DemoDto createNewDemo(DemoDto demoDto) {
        DemoEntity demo = modelMapper.map(demoDto,DemoEntity.class);

        DemoEntity savedDemo = demoRepo.save(demo);

        return modelMapper.map(savedDemo,DemoDto.class);
    }

    public List<DemoDto> getAllDemo() {
        return demoRepo.findAll()
                .stream()
                .map(demoEntity ->
                        modelMapper.map(demoEntity,DemoDto.class))
                .collect(Collectors.toList());
    }


    public DemoDto getDemoById(Long id) {

        //here if you want then we can get the authentication from the spring security context holder
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal(); // this principal contains the user details only because in filter we use the 'user' object directly

        log.info("user {}",user); //logging our logged in user here

        DemoEntity demo = demoRepo.findById(id)
                .orElseThrow();
        return modelMapper.map(demo,DemoDto.class);
    }
}
