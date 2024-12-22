package com.franchises.demo.repository;

import com.franchises.demo.entities.Franchise;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface  FranchiseRepository extends MongoRepository<Franchise, String> {
}
