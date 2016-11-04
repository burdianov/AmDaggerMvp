package com.testography.am_mvp.mvp.models;

import com.testography.am_mvp.data.storage.dto.ProductDto;

public class ProductModel extends AbstractModel {

    public ProductDto getProductById(int productId) {
        // TODO: 28-Oct-16 get product from datamanager
        return mDataManager.getProductById(productId);
    }

    public void updateProduct(ProductDto product) {
        mDataManager.updateProduct(product);
    }
}
