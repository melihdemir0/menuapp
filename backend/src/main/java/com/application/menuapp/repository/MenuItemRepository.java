package com.application.menuapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.application.menuapp.entity.MenuItem;

//MenuItemRepository.java
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

 @EntityGraph(attributePaths = "translations")
 List<MenuItem> findAll();
}

