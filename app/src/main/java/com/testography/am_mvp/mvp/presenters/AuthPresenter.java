package com.testography.am_mvp.mvp.presenters;

import android.os.Handler;
import android.util.Log;

import com.testography.am_mvp.di.DaggerService;
import com.testography.am_mvp.di.scopes.AuthScope;
import com.testography.am_mvp.mvp.models.AuthModel;
import com.testography.am_mvp.mvp.views.IAuthView;
import com.testography.am_mvp.ui.custom_views.AuthPanel;
import com.testography.am_mvp.utils.CredentialsValidator;

import javax.inject.Inject;

import dagger.Provides;

public class AuthPresenter extends AbstractPresenter<IAuthView> implements
        IAuthPresenter {
//    private static Context sAppContext = DataManager.getInstance().getAppContext();

    public static final String TAG = "AuthPresenter";

    @Inject
    AuthModel mAuthModel;

    public AuthPresenter() {
        mAuthModel = new AuthModel();
        Component component = DaggerService.getComponent(Component.class);
        if (component == null) {
            component = createDaggerComponent();
            DaggerService.registerComponent(Component.class, component);
        }
        component.inject(this);
        Log.e(TAG, "AuthPresenter: inject complete");
    }

    @Override
    public void initView() {
        if (checkUserAuth()) {
            getView().showCatalogScreen();
        }
        if (getView() != null) {
            if (checkUserAuth()) {
                getView().hideLoginBtn();
            } else {
                getView().showLoginBtn();
            }
            getView().animateSocialButtons();
            getView().addChangeTextListeners();
            getView().setTypeface();
        }
    }

    @Override
    public void clickOnLogin() {
        if (getView() != null && getView().getAuthPanel() != null) {
            if (getView().getAuthPanel().isIdle()) {
                getView().getAuthPanel().setCustomState(AuthPanel.LOGIN_STATE);
            } else {
                String email = getView().getAuthPanel().getUserEmail();
                String password = getView().getAuthPanel().getUserPassword();

                if (!CredentialsValidator.isValidEmail(email)) {
                    getView().requestEmailFocus();
//                    getView().showMessage(sAppContext.getString(R.string.err_msg_email));
                    return;
                }
                if (!CredentialsValidator.isValidPassword(password)) {
                    getView().requestPasswordFocus();
//                    getView().showMessage(sAppContext.getString(R.string.err_msg_password));
                    return;
                }
                mAuthModel.loginUser(email, password);
                getView().showLoad();
                getView().showMessage("request for user auth");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        getView().hideLoad();
                    }
                }, 3000);
            }
        }
    }

    @Override
    public void clickOnFb() {
        if (getView() != null) {
            getView().showMessage("clickOnFb");
        }
    }

    @Override
    public void clickOnVk() {
        if (getView() != null) {
            getView().showMessage("clickOnVk");
        }
    }

    @Override
    public void clickOnTwitter() {
        if (getView() != null) {
            getView().showMessage("clickOnTwitter");
        }
    }

    @Override
    public void clickOnShowCatalog() {
        if (getView() != null) {
            // TODO: 28-Oct-16 start catalog screen if data updating is complete
            getView().showCatalogScreen();
        }
    }

    @Override
    public boolean checkUserAuth() {
        return mAuthModel.isAuthUser();
    }

    //region ==================== DI ===================

    @dagger.Module
    public class Module {

        @Provides
        @AuthScope
        AuthModel provideAuthModel() {
            return new AuthModel();
        }
    }

    @dagger.Component(modules = Module.class)
    @AuthScope
    interface Component {
        void inject(AuthPresenter presenter);
    }

    private Component createDaggerComponent() {
        return DaggerAuthPresenter_Component.builder()
                .module(new Module())
                .build();
    }

    //endregion


}
