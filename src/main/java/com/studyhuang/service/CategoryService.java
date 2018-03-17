package com.studyhuang.service;

import com.studyhuang.common.ServerResponse;
import com.studyhuang.pojo.Category;

import java.util.List;

/**
 * Created by huang on 2018/3/17.
 */
public interface CategoryService {

    /**
     * 添加分类
     * @param category
     * @return
     */
    public ServerResponse<String> save(Category category);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getCategory(Integer categoryId);

    ServerResponse selectCategoryById(Integer id);
}
