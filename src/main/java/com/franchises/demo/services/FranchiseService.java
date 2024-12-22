package com.franchises.demo.services;

import com.franchises.demo.dto.FranchiseDTO;
import com.franchises.demo.entities.Branch;
import com.franchises.demo.entities.Franchise;
import com.franchises.demo.entities.Product;
import com.franchises.demo.repository.FranchiseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class FranchiseService {
    private final FranchiseRepository franchiseRepository;

    public FranchiseService(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Franchise createFranchise(FranchiseDTO franchiseDTO) {
        Franchise franchise = new Franchise();
        franchise.setName(franchiseDTO.getName());
        franchise.setBranches(
                franchiseDTO.getBranches().stream().map(branchDTO -> {
                    Branch branch = new Branch();
                    branch.setName(branchDTO.getName());
                    branch.setProducts(
                            branchDTO.getProducts().stream().map(productDTO -> {
                                Product product = new Product();
                                product.setName(productDTO.getName());
                                product.setStock(productDTO.getStock());
                                return product;
                            }).collect(Collectors.toList())
                    );
                    return branch;
                }).collect(Collectors.toList())
        );

        return franchiseRepository.save(franchise);
    }

    public ResponseEntity<Object> addBranchsToFranchise(String franchiseId, Branch newBranch) {

        Optional<Franchise> franchiseOptional = franchiseRepository.findById(franchiseId);
        if (franchiseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Franchise franchise = franchiseOptional.get();
        franchise.getBranches().add(newBranch);
        Franchise updatedFranchise = franchiseRepository.save(franchise);

        return new ResponseEntity<>(updatedFranchise, HttpStatus.OK);
    }

    public ResponseEntity<Object> addProductToBranch(String franchiseId, String branchId, Product newProduct) {

        Optional<Franchise> franchiseOptional = franchiseRepository.findById(franchiseId);
        if (franchiseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Franchise franchise = franchiseOptional.get();

        Branch targetBranch = franchise.getBranches().stream()
                .filter(branch -> branch.getId().equals(branchId))
                .findFirst()
                .orElse(null);

        if (targetBranch == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        targetBranch.getProducts().add(newProduct);

        Franchise updatedFranchise = franchiseRepository.save(franchise);

        return new ResponseEntity<>(updatedFranchise, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteProductToBranch(String franchiseId, String branchId, String productId) {

        Optional<Franchise> franchiseOptional = franchiseRepository.findById(franchiseId);
        if (franchiseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Franchise franchise = franchiseOptional.get();

        Branch targetBranch = franchise.getBranches().stream()
                .filter(branch -> branch.getId().equals(branchId))
                .findFirst()
                .orElse(null);

        if (targetBranch == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        boolean productRemoved = targetBranch.getProducts().removeIf(product -> product.getId().equals(productId));

        if (!productRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Franchise updatedFranchise = franchiseRepository.save(franchise);
        return new ResponseEntity<>(updatedFranchise, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateStok(String franchiseId, String branchId, String productId, Map<String, Integer> stockUpdate) {

        Optional<Franchise> franchiseOptional = franchiseRepository.findById(franchiseId);
        if (franchiseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Franchise franchise = franchiseOptional.get();

        Branch targetBranch = franchise.getBranches().stream()
                .filter(branch -> branch.getId().equals(branchId))
                .findFirst()
                .orElse(null);

        if (targetBranch == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Product targetProduct = targetBranch.getProducts().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (targetProduct == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Integer newStock = stockUpdate.get("stock");
        if (newStock == null || newStock < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        targetProduct.setStock(newStock);
        Franchise updatedFranchise = franchiseRepository.save(franchise);

        return new ResponseEntity<>(updatedFranchise, HttpStatus.OK);
    }

    public ResponseEntity<Object> topProductStock(String franchiseId) {

        Optional<Franchise> franchiseOptional = franchiseRepository.findById(franchiseId);
        if (franchiseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Franchise franchise = franchiseOptional.get();

        List<Map<String, Object>> result = new ArrayList<>();

        for (Branch branch : franchise.getBranches()) {
            Product topProduct = branch.getProducts().stream()
                    .max(Comparator.comparingInt(Product::getStock))
                    .orElse(null);

            if (topProduct != null) {
                Map<String, Object> branchProductInfo = new HashMap<>();
                branchProductInfo.put("branchId", branch.getId());
                branchProductInfo.put("branchName", branch.getName());
                branchProductInfo.put("productId", topProduct.getId());
                branchProductInfo.put("productName", topProduct.getName());
                branchProductInfo.put("stock", topProduct.getStock());
                result.add(branchProductInfo);
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

}