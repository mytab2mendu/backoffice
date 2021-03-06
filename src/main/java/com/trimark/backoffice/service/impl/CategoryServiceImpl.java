package com.trimark.backoffice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trimark.backoffice.framework.data.BaseJPAServiceImpl;
import com.trimark.backoffice.framework.exception.NotFoundException;
import com.trimark.backoffice.model.entity.Category;
import com.trimark.backoffice.model.repository.CategoryRepository;
import com.trimark.backoffice.service.CategoryService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl extends BaseJPAServiceImpl<Category, Long> implements CategoryService {
    private static Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private @Autowired
    CategoryRepository categoryRepository;

    @PostConstruct
    public void setupService() {
        LOG.info("setting up categoryService...");
        this.baseJpaRepository = categoryRepository;
        this.entityClass = Category.class;
        this.baseJpaRepository.setupEntityClass(Category.class);
        LOG.info("categoryService created...");
    }


    @Override
    public boolean isCategoryPresent(String categoryName) {
        if (categoryRepository.findByCategoryName(categoryName) != null) {
            return true;
        } else
            return false;
    }

    @Override
    public boolean isPriorityPresent(Integer categoryPriority) {
        if (categoryRepository.findByCategoryPriority(categoryPriority) != null) {
            return true;
        } else
            return false;
    }

    @Override
    public Category findByCategoryName(String categoryName) throws NotFoundException {
        Category c = categoryRepository.findByCategoryName(categoryName);

        if(c==null) {
            throw new NotFoundException("Category: "+categoryName + " not found");
        }

        return c;
    }

    @Override
    public List<Category> findSubCategories(Category parentCategory) throws NotFoundException {
        List<Category> categories = categoryRepository.findSubCategories(parentCategory);

        if(categories==null || categories.size()==0) {
            throw new NotFoundException("Subcategories for category: "+parentCategory.getName()+ " not found");
        }

        return categories;
    }
}
