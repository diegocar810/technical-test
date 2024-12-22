package com.franchises.demo.controllers;

import com.franchises.demo.dto.FranchiseDTO;
import com.franchises.demo.entities.Branch;
import com.franchises.demo.entities.Franchise;
import com.franchises.demo.services.FranchiseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class FranchiseController {
    private final FranchiseService franchiseService;

    public FranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    @PostMapping("/franchises")
    public ResponseEntity<Franchise> createFranchise(@RequestBody FranchiseDTO franchiseDTO) {
        Franchise franchise = franchiseService.createFranchise(franchiseDTO);
        return new ResponseEntity<>(franchise, HttpStatus.CREATED);
    }

    @PostMapping("/{franchiseId}/branches")
    public ResponseEntity<Object> addBranchsToFranchise(@PathVariable String franchiseId, @RequestBody Branch newBranch) {
        return franchiseService.addBranchsToFranchise(franchiseId, newBranch);
    }


}