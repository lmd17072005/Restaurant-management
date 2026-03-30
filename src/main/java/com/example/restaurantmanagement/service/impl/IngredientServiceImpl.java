package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.IngredientRequest;
import com.example.restaurantmanagement.dto.response.IngredientResponse;
import com.example.restaurantmanagement.dto.response.PageResponse;
import com.example.restaurantmanagement.entity.Ingredient;
import com.example.restaurantmanagement.entity.enums.IngredientStatus;
import com.example.restaurantmanagement.exception.DuplicateResourceException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.IngredientMapper;
import com.example.restaurantmanagement.repository.IngredientRepository;
import com.example.restaurantmanagement.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    @Override
    @Transactional
    public List<IngredientResponse> getAllIngredients() {
        return ingredientMapper.toResponseList(ingredientRepository.findAll());
    }

    @Override
    @Transactional
    public IngredientResponse getIngredientById(Integer id) {
        return ingredientMapper.toResponse(ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "id", id)));
    }

    @Override
    @Transactional
    public IngredientResponse createIngredient(IngredientRequest request) {
        if (ingredientRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Ingredient already exists: " + request.getName());
        }
        Ingredient ingredient = ingredientMapper.toEntity(request);
        if (request.getStatus() == null) {
            ingredient.setStatus(IngredientStatus.hoat_dong);
        }
        return ingredientMapper.toResponse(ingredientRepository.save(ingredient));
    }

    @Override
    @Transactional
    public IngredientResponse updateIngredient(Integer id, IngredientRequest request) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "id", id));
        ingredientRepository.findByName(request.getName()).ifPresent(e -> {
            if (!e.getId().equals(id)) {
                throw new DuplicateResourceException("Ingredient name exists: " + request.getName());
            }
        });
        ingredientMapper.updateEntity(request, ingredient);
        return ingredientMapper.toResponse(ingredientRepository.save(ingredient));
    }

    @Override
    @Transactional
    public void deleteIngredient(Integer id) {
        if (!ingredientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ingredient", "id", id);
        }
        ingredientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<IngredientResponse> getAllIngredients(Pageable pageable) {
        Page<Ingredient> page = ingredientRepository.findAll(pageable);
        List<IngredientResponse> content = ingredientMapper.toResponseList(page.getContent());
        return PageResponse.of(page, content);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<IngredientResponse> searchIngredients(String keyword, Pageable pageable) {
        Page<Ingredient> page = ingredientRepository.findByNameContainingIgnoreCase(keyword, pageable);
        List<IngredientResponse> content = ingredientMapper.toResponseList(page.getContent());
        return PageResponse.of(page, content);
    }
}

