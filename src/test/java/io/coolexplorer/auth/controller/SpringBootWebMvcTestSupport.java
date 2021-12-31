package io.coolexplorer.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.web.servlet.MockMvc;

import javax.xml.validation.Validator;

public class SpringBootWebMvcTestSupport {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    Validator validator;

    @Autowired
    MessageSourceAccessor validationMessageSourceAccessor;

    @Autowired
    MessageSourceAccessor errorMessageSourceAccessor;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;
}
