package com.studyhuang.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.studyhuang.common.Const;
import com.studyhuang.common.ResponseCode;
import com.studyhuang.common.ServerResponse;
import com.studyhuang.dao.CategoryMapper;
import com.studyhuang.dao.ProductMapper;
import com.studyhuang.pojo.Category;
import com.studyhuang.pojo.Product;
import com.studyhuang.service.CategoryService;
import com.studyhuang.service.ProductService;
import com.studyhuang.utils.DateTimeUtils;
import com.studyhuang.utils.PropertiesUtil;
import com.studyhuang.vo.ProductDetailVO;
import com.studyhuang.vo.ProductListVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang on 2018/3/18.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;

    @Override
    public ServerResponse saveOrUpdate(Product product) {
        if (null == product) {
            return ServerResponse.createError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        } else {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImages = product.getSubImages().split(",");
                if (subImages.length > 0) {
                    product.setMainImage(subImages[0]);
                }
            }
            if (product.getId() != null) {
                int i = productMapper.updateByPrimaryKey(product);
                if (i > 0) {
                    return ServerResponse.createSuccessByMsg("更新商品成功");
                }
                return ServerResponse.createErrorByMsg("更新商品失败");
            } else {
                int i = productMapper.insert(product);
                if (i > 0) {
                    return ServerResponse.createSuccessByMsg("save更新商品成功");
                }
                return ServerResponse.createErrorByMsg("save商品失败");
            }
        }
    }


    /**
     * 上下架
     *
     * @param productId
     * @param status
     * @return
     */
    @Override
    public ServerResponse<String> setSaleAndStatus(Integer productId, Integer status) {
        if (null == productId || null == status) {
            return ServerResponse.createError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int i = productMapper.updateByPrimaryKey(product);
        if (i > 0) {
            return ServerResponse.createSuccessByMsg("更新商品成功");
        }
        return ServerResponse.createErrorByMsg("更新商品失败");

    }

    @Override
    public ServerResponse<ProductDetailVO> getProductInfo(Integer productId) {
        if (null == productId) {
            return ServerResponse.createError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createErrorByMsg("该商品不存在");
        }
        ProductDetailVO productDetailVO = assemProduct(product);

        return ServerResponse.createSuccess(productDetailVO);
    }


    public ProductDetailVO assemProduct(Product product) {

        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setName(product.getName());
        productDetailVO.setId(product.getId());
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());

        productDetailVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVO.setParentCategoryId(0);
        }
        productDetailVO.setParentCategoryId(category.getParentId());

        productDetailVO.setCreateTime(DateTimeUtils.dateToStr(product.getCreateTime()));
        productDetailVO.setUpdateTime(DateTimeUtils.dateToStr(product.getUpdateTime()));
        return productDetailVO;
    }


    public ServerResponse<PageInfo> listProducts(Integer pageNum, Integer pageSize) {
       //分页
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products =  productMapper.selectList();
        List<ProductListVO> productListVO = Lists.newArrayList();
        for (Product product:products) {
            ProductListVO  listvo = assembleProductList(product);
            productListVO.add(listvo);
        }
        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(productListVO);
        return  ServerResponse.createSuccess(pageResult);
    }


    private ProductListVO assembleProductList(Product product){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setSubtitle(product.getSubtitle());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setStatus(product.getStatus());
        productListVO.setId(product.getId());
        productListVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        return  productListVO;
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(Integer productId, String productName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products =  productMapper.searchProduct(productId,productName);
        List<ProductListVO> productListVO = Lists.newArrayList();
        for (Product product:products) {
            ProductListVO  listvo = assembleProductList(product);
            productListVO.add(listvo);
        }
        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(productListVO);
        return  ServerResponse.createSuccess(pageResult);
    }


    @Override
    public ServerResponse<ProductDetailVO> getProductDetails(Integer productId) {
        if (productId == null){
            return ServerResponse.createError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createErrorByMsg("SHANGPIN XIA JIA");
        }
        //?yingbianma 枚举
        if (product.getStatus() != 1){
            return ServerResponse.createErrorByMsg("商品已经下架或者删除");
        }
        ProductDetailVO productDetails = assemProduct(product);
        return ServerResponse.createSuccess(productDetails);
     }

    @Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if(StringUtils.isBlank(keyword) && categoryId == null){
            return ServerResponse.createError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<Integer>();

        if(categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category == null && StringUtils.isBlank(keyword)){
                //没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVO> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createSuccess(pageInfo);
            }
            categoryIdList = categoryService.selectCategoryById(category.getId()).getData();
        }
        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum,pageSize);
        //排序处理
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);

        List<ProductListVO> productListVoList = Lists.newArrayList();
        for(Product product : productList){
            ProductListVO productListVo = assembleProductList(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createSuccess(pageInfo);
    }



}