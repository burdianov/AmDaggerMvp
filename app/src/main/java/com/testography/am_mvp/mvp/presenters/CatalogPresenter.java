package com.testography.am_mvp.mvp.presenters;

import com.testography.am_mvp.data.storage.dto.ProductDto;
import com.testography.am_mvp.di.DaggerService;
import com.testography.am_mvp.di.scopes.CatalogScope;
import com.testography.am_mvp.mvp.models.CatalogModel;
import com.testography.am_mvp.mvp.views.ICatalogView;

import java.util.List;

import javax.inject.Inject;

import dagger.Provides;

public class CatalogPresenter extends AbstractPresenter<ICatalogView> implements
        ICatalogPresenter {

    @Inject
    CatalogModel mCatalogModel;
    private List<ProductDto> mProductDtoList;

    public CatalogPresenter() {
        Component component = DaggerService.getComponent(Component.class);
        if (component == null) {
            component = createDaggerComponent();
            DaggerService.registerComponent(Component.class, component);
        }
        component.inject(this);
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

    //region ==================== DI ===================

    private Component createDaggerComponent() {
        return DaggerCatalogPresenter_Component.builder()
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module {
        @Provides
        @CatalogScope
        CatalogModel provideCatalogModel() {
            return new CatalogModel();
        }
    }

    @dagger.Component(modules = Module.class)
    @CatalogScope
    interface Component {
        void inject(CatalogPresenter presenter);
    }

    //endregion


}
