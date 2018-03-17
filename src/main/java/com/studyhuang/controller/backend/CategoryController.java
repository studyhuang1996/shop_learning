package com.studyhuang.controller.backend;

import com.studyhuang.common.Const;
import com.studyhuang.common.ResponseCode;
import com.studyhuang.common.ServerResponse;
import com.studyhuang.pojo.Category;
import com.studyhuang.pojo.User;
import com.studyhuang.service.CategoryService;
import com.studyhuang.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 分类管理
 * Created by huang on 2018/3/17.
 */
@RequestMapping("manager/category/")
@Controller
public class CategoryController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private CategoryService categoryService;


    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse<String> saveCategory(HttpSession session , String categoryName,
                                               @RequestParam(value = "parentId",defaultValue ="0" ) Integer parentId ){
         User user = (User) session.getAttribute(Const.CURRENT_USER);
        if ( null == user){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,强制登录");
        }
        if (user.getRole().intValue() == Const.Role.Role_ADMIN){
          //管理员权限
            Category category = new Category();
            category.setName(categoryName);
            category.setParentId(parentId);
             category.setStatus(true);
            return categoryService.save(category);
        }else{
            return ServerResponse.createErrorByMsg("非管理员没有权限");
        }

    }


    @RequestMapping("updateName.do")
    @ResponseBody
    public  ServerResponse updateCategoryName(HttpSession session,Integer categoryId,String categoryName){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if ( null == user){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,强制登录");
        }

        if (iUserService.checkAdmin(user).isSuccess()){
             return  categoryService.updateCategoryName(categoryId,categoryName);
        }else {
            return ServerResponse.createErrorByMsg("无权限操作");
        }
    }

    /**
     * 通过父节点获取子节点
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get.do")
    @ResponseBody
    public ServerResponse  getCategory(HttpSession session,
                                            @RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if ( null == user){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,强制登录");
        }

        if (iUserService.checkAdmin(user).isSuccess()){
            return (ServerResponse) categoryService.getCategory(categoryId);
        }else {
            return ServerResponse.createErrorByMsg("无权限操作");
        }
    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public  ServerResponse getDeepCategory(HttpSession session,
                                           @RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if ( null == user){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,强制登录");
        }

        if (iUserService.checkAdmin(user).isSuccess()){
            //查询节点id和递归节点id
            return (ServerResponse) categoryService.selectCategoryById(categoryId);
        }else {
            return ServerResponse.createErrorByMsg("无权限操作");
        }

    }
}
