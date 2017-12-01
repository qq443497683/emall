package com.emall.service.impl;

import com.emall.common.ServerResponse;
import com.emall.pojo.Category;
import com.emall.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/11/30 0030.
 */
@Service(value = "categoryService")
public class CategoryServiceimpl implements CategoryService {
    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        return null;
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        return null;
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        return null;
    }

    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        return null;
    }

    @Override
    public ServerResponse<categoryIdList> selectCategoryAndChildreById(Integer id) {
        return null;
    }
}
