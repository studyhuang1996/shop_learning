package com.studyhuang.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.studyhuang.common.Const;
import com.studyhuang.common.ResponseCode;
import com.studyhuang.common.ServerResponse;
import com.studyhuang.pojo.Product;
import com.studyhuang.pojo.User;
import com.studyhuang.service.IFileService;
import com.studyhuang.service.IUserService;
import com.studyhuang.service.ProductService;
import com.studyhuang.utils.PropertiesUtil;
import com.sun.javafx.collections.MappingChange;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**'
 * Created by huang on 2018/3/18.
 */

@Controller
@RequestMapping("/admin/product")
public class ProductController {
       @Autowired
        private IUserService iUserService;
        @Autowired
        private ProductService productService;
        @Autowired
       private IFileService iFileService;

       @RequestMapping("save.do")
       @ResponseBody
        public ServerResponse<String>  saveOrUpdate(HttpSession session, Product product){
            User user = (User) session.getAttribute(Const.CURRENT_USER);
            if ( null == user){
                return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,强制登录");
            }

            if (iUserService.checkAdmin(user).isSuccess()){
                return  productService.saveOrUpdate(product);
            }else {
                return ServerResponse.createErrorByMsg("无权限操作");
            }
        }

    @RequestMapping("sale_status.do")
    @ResponseBody
    public ServerResponse<String>  setSaleAndStatus(HttpSession session,Integer productId,Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if ( null == user){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,强制登录");
        }

        if (iUserService.checkAdmin(user).isSuccess()){
            return  productService.setSaleAndStatus(productId,status);
        }else {
            return ServerResponse.createErrorByMsg("无权限操作");
        }
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<String>  getDetails(HttpSession session,Integer productId,Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if ( null == user){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,强制登录");
        }

        if (iUserService.checkAdmin(user).isSuccess()){
            return  productService.setSaleAndStatus(productId,status);
        }else {
            return ServerResponse.createErrorByMsg("无权限操作");
        }
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo>  listProducts(HttpSession session,
                                                  @RequestParam(value = "pageNum",defaultValue = "0") Integer pageNum,
                                                  @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if ( null == user){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,强制登录");
        }

        if (iUserService.checkAdmin(user).isSuccess()){
            return  productService.listProducts(pageNum,pageSize);
        }else {
            return ServerResponse.createErrorByMsg("无权限操作");
        }
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo>  searchProducts(HttpSession session,Integer productId,String productName,
                                                  @RequestParam(value = "pageNum",defaultValue = "0") Integer pageNum,
                                                  @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if ( null == user){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,强制登录");
        }

        if (iUserService.checkAdmin(user).isSuccess()){
            return  productService.searchProduct(productId,productName,pageNum,pageSize);
        }else {
            return ServerResponse.createErrorByMsg("无权限操作");
        }
    }

    //上传文件
    @RequestMapping("upload.do")
    @ResponseBody
    public  ServerResponse upload(HttpSession session,@RequestParam(value="upload_file",required = false) MultipartFile multipartFile ,
                                  HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if ( null == user){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,强制登录");
        }

        if (iUserService.checkAdmin(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFile = iFileService.upload(multipartFile, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFile;
            Map<String,String> maps =new HashMap<>();
            maps.put("filename",targetFile);
            maps.put("url",url);
            return  ServerResponse.createSuccess(maps);
        }else {
            return ServerResponse.createErrorByMsg("无权限操作");
        }

    }

    //富文本上传文件
    @RequestMapping("richtext_upload.do")
    @ResponseBody
    public  Map richtextUpload(HttpSession session, @RequestParam(value="upload_file",required = false) MultipartFile multipartFile ,
                                          HttpServletRequest request, HttpServletResponse response){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        Map resultMap = Maps.newHashMap();
        if ( null == user){
            resultMap.put("success",false);
            resultMap.put("msg","用户未登录");
            return resultMap;
        }

        if (iUserService.checkAdmin(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFile = iFileService.upload(multipartFile, path);
            if (StringUtils.isBlank(targetFile)) {
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFile;
            resultMap.put("success",true);
            resultMap.put("msg","富文本上传文件成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return  resultMap;
        }else {
          resultMap.put("success",false);
          resultMap.put("msg","无权限操作");
          return resultMap;
        }

    }



}
