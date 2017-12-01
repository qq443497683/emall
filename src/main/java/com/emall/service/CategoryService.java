package com.emall.service;

import com.emall.common.ServerResponse;
import com.emall.pojo.Category;

import java.util.List;

/**
 * Created by Administrator on 2017/11/30 0030.
 */
public interface CategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategoryName(Integer categoryId,String categoryName);
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

    ServerResponse<categoryIdList> selectCategoryAndChildreById(Integer id);
}
