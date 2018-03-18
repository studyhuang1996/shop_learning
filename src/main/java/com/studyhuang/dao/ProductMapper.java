package com.studyhuang.dao;

import com.studyhuang.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    List<Product> searchProduct(@Param("productId") Integer productId,@Param("productName") String productName);

    List<Product> selectByNameAndCategoryIds(@Param("s") String s,@Param("integers") List<Integer> integers);
}