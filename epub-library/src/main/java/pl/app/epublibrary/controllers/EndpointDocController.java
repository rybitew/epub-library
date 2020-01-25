package pl.app.epublibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@RestController
public class EndpointDocController {
    private final RequestMappingHandlerMapping handlerMapping;

    @Autowired
    public EndpointDocController(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @RequestMapping(value="endpointdoc", method=RequestMethod.GET)
    public void show(Model model) throws IOException {
        model.addAttribute("handlerMethods", this.handlerMapping.getHandlerMethods());
        BufferedWriter writer = new BufferedWriter(new FileWriter("xd.txt"));
        writer.write(model.toString());

        writer.close();
    }
}