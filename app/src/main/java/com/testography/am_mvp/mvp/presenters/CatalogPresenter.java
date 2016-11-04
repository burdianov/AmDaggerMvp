package com.testography.am_mvp.mvp.presenters;

import com.testography.am_mvp.data.storage.dto.ProductDto;
import com.testography.am_mvp.mvp.models.CatalogModel;
import com.testography.am_mvp.mvp.views.ICatalogView;

import java.util.List;

public class CatalogPresenter extends AbstractPresenter<ICatalogView> implements
        ICatalogPresenter {
    private static CatalogPresenter ourInstance = new CatalogPresenter();
    private CatalogModel mCatalogModel;
    private List<ProductDto> mProductDtoList;

    public static CatalogPresenter getInstance() {
        return ourInstance;
    }

    private CatalogPresenter() {
        mCatalogModel = new CatalogModel();
    }

    @Override
    public void initView() {
        if (mProductDtoList == null) {
            mProductDtoList = mCatalogModel.getProductList();
        }

        if (getView() != null) {
            getView().showCatalogView(mProductDtoList);
        }
    }

    @Override
    public void clickOnBuyButton(int position) {
        if (getView() != null) {
            if (checkUserAuth()) {
                getView().showAddToCartMessage(mProductDtoList.get(position));
            } else {
                getView().showAuthScreen();
            }
        }
    }

    @Override
    public boolean checkUserAuth() {
        return mCatalogModel.isUserAuth();
    }
}
