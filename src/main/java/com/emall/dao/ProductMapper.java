package com.emall.dao;

import com.emall.pojo.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(Product record);
    int insertSelective(Product record);
    Product selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(Product record);
    int updateByPrimaryKey(Product record);

    List<Product> selectAllProducts();
    List<Product> selectProductsByNameAndCategoryid(@Param("keyword")String keyword,@Param("categoryIdList")List<Integer> categoryIdList);
    List<Product> selectProductsByNameAndId(@Param("productName")String productName,@Param("productId") Integer id);
}