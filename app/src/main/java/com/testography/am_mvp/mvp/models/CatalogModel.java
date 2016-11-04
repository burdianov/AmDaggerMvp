package com.testography.am_mvp.mvp.models;

import com.testography.am_mvp.data.storage.dto.ProductDto;

import java.util.List;

public class CatalogModel extends AbstractModel {
//    DataManager mDataManager = DataManager.getInstance();

    public CatalogModel() {

    }

    public List<ProductDto> getProductList() {
        return mDataManager.getProductList();
    }

    public boolean isUserAuth() {
        return mDataManager.isAuthUser();
    }
}
