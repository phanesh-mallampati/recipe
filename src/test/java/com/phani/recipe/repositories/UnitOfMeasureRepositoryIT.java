package com.phani.recipe.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    /*@Before
    public void setUp() throws Exception {
    }*/

    @Test
    public void findByDescription() {
        String expected = "Teaspoon";
        String actual = unitOfMeasureRepository.findByDescription("Teaspoon").get().getDescription();
        assertEquals(expected, actual);
    }

    @Test
    public void findById(){
        String expected = "Pinch";
        String actual = unitOfMeasureRepository.findById(4l).get().getDescription();
        assertEquals(expected, actual);
    }
}