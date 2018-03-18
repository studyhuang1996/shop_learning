package com.studyhuang.controller.protal;

import com.github.pagehelper.PageInfo;
import com.studyhuang.common.ServerResponse;
import com.studyhuang.service.ProductService;
import com.studyhuang.vo.ProductDetailVO;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 前台获取商品信息
 * Created by huang on 2018/3/19.
 */
@Controller
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @RequestMapping("details.do")
    @ResponseBody
    public ServerResponse<ProductDetailVO> getProductDetails(Integer productId){

        return  productService.getProductDetails(productId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false)String keyword,
                                         @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        return productService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }

}
