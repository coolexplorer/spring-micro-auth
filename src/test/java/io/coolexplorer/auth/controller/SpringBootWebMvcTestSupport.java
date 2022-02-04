package io.coolexplorer.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.Validator;

public class SpringBootWebMvcTestSupport {
    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public Validator validator;

    @Autowired
    public MessageSourceAccessor validationMessageSourceAccessor;

    @Autowired
    public MessageSourceAccessor errorMessageSourceAccessor;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public ModelMapper modelMapper;
}
