package com.studyhuang.service;

import com.github.pagehelper.PageInfo;
import com.studyhuang.common.ServerResponse;
import com.studyhuang.pojo.Product;
import com.studyhuang.pojo.User;
import com.studyhuang.vo.ProductDetailVO;

import java.util.List;

/**
 * Created by huang on 2018/3/18.
 */
public interface ProductService {

    /**
     * 保存更新
     * @param product
     * @return
     */
    ServerResponse saveOrUpdate(Product product);

    /**
     * 详情
     * @param productId
     * @return
     */
    ServerResponse<ProductDetailVO> getProductInfo(Integer productId);

    ServerResponse<String> setSaleAndStatus(Integer productId, Integer status);
    /**
         liebaio
    */
 //   ServerResponse<List<Product>> listProduct();

    /**
     * 列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> listProducts(Integer pageNum, Integer pageSize);

    ServerResponse<PageInfo> searchProduct(Integer productId, String productName, Integer pageNum, Integer pageSize);

    /**
     * 前台获取商品详情
     * @param productId
     * @return
     */
    ServerResponse<ProductDetailVO> getProductDetails(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
