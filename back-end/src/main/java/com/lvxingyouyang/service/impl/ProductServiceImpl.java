package com.lvxingyouyang.service.impl;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Product;
import com.lvxingyouyang.repository.ProductRepository;
import com.lvxingyouyang.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 商品服务实现类
 * 实现商品搜索、查询、推荐等功能
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;

    /**
     * 获取商品列表
     * 支持按分类或关键词搜索
     */
    @Override
    public PageResponse<Product> getProducts(String category, String keyword, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Product> result;

        if (category != null && !category.isEmpty()) {
            // 按分类搜索
            result = productRepository.findByCategory(category, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            // 按关键词搜索
            result = productRepository.findByNameContaining(keyword, pageable);
        } else {
            // 获取所有商品
            result = productRepository.findAll(pageable);
        }

        return PageResponse.<Product>builder()
                .list(result.getContent())
                .total(result.getTotalElements())
                .page(page)
                .pageSize(pageSize)
                .build();
    }

    /**
     * 获取商品详情
     */
    @Override
    public Product getProductDetail(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
    }

    /**
     * 获取热门商品
     * 按销售量排序
     */
    @Override
    public List<Product> getPopularProducts(Integer limit) {
        return productRepository.findTop10ByOrderBySalesDesc().stream()
                .limit(limit)
                .toList();
    }

    /**
     * 获取推荐商品
     * 与热门商品相同
     */
    @Override
    public List<Product> getRecommendedProducts(Integer limit) {
        return getPopularProducts(limit);
    }
}
