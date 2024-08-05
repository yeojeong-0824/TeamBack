package com.example.demo.autocomplate;

import com.example.demo.autocomplate.application.AutocompleteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AutocompleteService autocompleteService;

    public DataInitializer(AutocompleteService autocompleteService) {
        this.autocompleteService = autocompleteService;
    }

    @Override
    public void run(String... args) {

        autocompleteService.addAutocomplete("사과");
        autocompleteService.addAutocomplete("바나나");
        autocompleteService.addAutocomplete("오렌지");
    }
}