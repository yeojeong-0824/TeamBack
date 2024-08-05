package com.example.demo.autoComplate.controller;

import com.example.demo.autoComplate.controller.dto.AutocompleteResponse;
import com.example.demo.autoComplate.application.AutocompleteService;
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
