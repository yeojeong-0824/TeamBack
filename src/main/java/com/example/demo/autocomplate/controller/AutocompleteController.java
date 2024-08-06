package com.example.demo.autocomplate.controller;

import com.example.demo.autocomplate.controller.dto.AutocompleteResponse;
import com.example.demo.autocomplate.application.AutocompleteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autocomplete")
public class AutocompleteController {

    private final AutocompleteService autocompleteService;

    public AutocompleteController(AutocompleteService autocompleteService) {
        this.autocompleteService = autocompleteService;
    }

    @GetMapping("/{searchWord}")
    @ResponseBody
    public ResponseEntity<AutocompleteResponse> getAutocompleteList(@PathVariable("searchWord") String searchWord) {
        return ResponseEntity.ok(autocompleteService.getAutocomplete(searchWord));
    }
}
