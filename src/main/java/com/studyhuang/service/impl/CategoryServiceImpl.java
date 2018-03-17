package com.studyhuang.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.studyhuang.common.ServerResponse;
import com.studyhuang.dao.CategoryMapper;
import com.studyhuang.pojo.Category;
import com.studyhuang.service.CategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


/**
 * Created by huang on 2018/3/17.
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse<String>  save(Category category){
        int row = categoryMapper.insert(category);
        if (row > 0){
            return ServerResponse.createSuccessByMsg("添加商品分类成功");
        }
        return  ServerResponse.createErrorByMsg("添加商品分类失败");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
        return  ServerResponse.createErrorByMsg("参数出错");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int i = categoryMapper.updateByPrimaryKey(category);
        if ( i > 0){
            return  ServerResponse.createSuccessByMsg("更新名称成功");
        }
        return ServerResponse.createErrorByMsg("更新名称失败");
    }

    @Override
    public ServerResponse<List<Category>> getCategory(Integer categoryId) {
        List<Category> categories=  categoryMapper.selectCategories(categoryId);
        if (CollectionUtils.isEmpty(categories)) {
         logger.error("没有子节点");
        }
        return ServerResponse.createSuccess(categories) ;
    }

    @Override
    public ServerResponse selectCategoryById(Integer id) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,id);
        List<Integer> lists = Lists.newArrayList();
        if ( id != null){
            for ( Category categoryItem :categorySet ) {
                 lists.add(categoryItem.getId());
            }
        }
        return ServerResponse.createSuccess(lists);
    }

    public Set<Category> findChildCategory(Set<Category> setList,Integer categoryId){
        Category category1 = categoryMapper.selectByPrimaryKey(categoryId);
        if (category1 != null){
            setList.add(category1);
        }

        List<Category> categories = categoryMapper.selectCategories(categoryId);
        for ( Category category: categories) {
            findChildCategory(setList,category.getId());
        }
       return setList;
    }
}
