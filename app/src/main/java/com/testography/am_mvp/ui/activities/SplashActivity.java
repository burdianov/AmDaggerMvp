package com.testography.am_mvp.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.testography.am_mvp.BuildConfig;
import com.testography.am_mvp.R;
import com.testography.am_mvp.di.DaggerService;
import com.testography.am_mvp.di.scopes.AuthScope;
import com.testography.am_mvp.mvp.presenters.AuthPresenter;
import com.testography.am_mvp.mvp.presenters.IAuthPresenter;
import com.testography.am_mvp.mvp.views.IAuthView;
import com.testography.am_mvp.ui.custom_views.AuthPanel;
import com.testography.am_mvp.utils.ConstantsManager;
import com.testography.am_mvp.utils.CustomTextWatcher;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Provides;

public class SplashActivity extends AppCompatActivity implements IAuthView, View.OnClickListener {

    @Inject
    AuthPresenter mPresenter;

    @BindView(R.id.coordinator_container)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.auth_wrapper)
    AuthPanel mAuthPanel;

    @BindView(R.id.show_catalog_btn)
    Button mShowCatalogBtn;

    @BindView(R.id.login_btn)
    Button mLoginBtn;

    @BindView(R.id.app_name_txt)
    TextView mAppNameTxt;

    @BindView(R.id.login_email_et)
    EditText mEmailEt;

    @BindView(R.id.login_password_et)
    EditText mPasswordEt;

    @BindView(R.id.facebook_ib)
    ImageButton mFacebook;

    @BindView(R.id.twitter_ib)
    ImageButton mTwitter;

    @BindView(R.id.vk_ib)
    ImageButton mVK;

    protected ProgressDialog mProgressDialog;

    //region ========== Life cycle ==========

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Component component = DaggerService.getComponent(Component.class);
        if (component == null) {
            component = createDaggerComponent();
            DaggerService.registerComponent(Component.class, component);
        }
        component.inject(this);

        mPresenter.takeView(this);
        mPresenter.initView();

        mLoginBtn.setOnClickListener(this);
        mShowCatalogBtn.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        mPresenter.dropView();
        super.onDestroy();
    }

    //endregion

    //region ========== IAuthView ==========

    @Override
    public void showMessage(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(Throwable e) {
        if (BuildConfig.DEBUG) {
            showMessage(e.getMessage());
            e.printStackTrace();
        } else {
            showMessage("Something went wrong, try again later");
            // TODO: 22-Oct-16 send error stacktrace to crashlytics
        }
    }

    @Override
    public void showLoad() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, R.style.custom_dialog);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable
                    (Color.TRANSPARENT));
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        } else {
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        }
    }

    @Override
    public void hideLoad() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.hide();
            }
        }
    }

    @Override
    public IAuthPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void showLoginBtn() {
        mLoginBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginBtn() {
        mLoginBtn.setVisibility(View.GONE);
    }

    @Override
    public AuthPanel getAuthPanel() {
        return mAuthPanel;
    }

    @Override
    public void animateSocialButtons() {
        setSocialButtonsAnimation(mFacebook);
        setSocialButtonsAnimation(mTwitter);
        setSocialButtonsAnimation(mVK);
    }

    @Override
    public void setTypeface() {
        String customFont = ConstantsManager.CUSTOM_FONTS_ROOT + ConstantsManager
                .CUSTOM_FONT_NAME;
        mAppNameTxt.setTypeface(Typeface.createFromAsset(getAssets(), customFont));
    }

    @Override
    public void addChangeTextListeners() {
        mEmailEt.addTextChangedListener(new CustomTextWatcher(this, mEmailEt,
                mLoginBtn));
        mPasswordEt.addTextChangedListener(new CustomTextWatcher(this,
                mPasswordEt, mLoginBtn));
    }

    @Override
    public void showCatalogScreen() {
        Intent intent = new Intent(this, RootActivity.class);
        startActivity(intent);
        finish();
    }

    private void setSocialButtonsAnimation(ImageButton button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.animate().setDuration(200).scaleX(1.1f).scaleY(1.1f);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.animate().setDuration(200).scaleX(1.0f).scaleY(1.0f);
                        break;
                }
                return false;
            }
        });
    }

    //endregion

    @Override
    public void onBackPressed() {
        if (!mAuthPanel.isIdle()) {
            mAuthPanel.setCustomState(AuthPanel.IDLE_STATE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void requestEmailFocus() {
        if (mEmailEt.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            mEmailEt.setSelection(mEmailEt.length());
        }
    }

    @Override
    public void requestPasswordFocus() {
        if (mPasswordEt.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            mPasswordEt.setSelection(mPasswordEt.length());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_catalog_btn:
                mPresenter.clickOnShowCatalog();
                break;
            case R.id.login_btn:
                mPresenter.clickOnLogin();
                break;
        }
    }

    //region ==================== DI ===================

    @dagger.Module
    public class Module {

        @Provides
        @AuthScope
        AuthPresenter provideAuthPresenter() {
            return new AuthPresenter();
        }
    }

    @dagger.Component(modules = Module.class)
    @AuthScope
    interface Component {
        void inject(SplashActivity activity);
    }

    private Component createDaggerComponent() {
        return DaggerSplashActivity_Component.builder()
                .module(new Module())
                .build();
    }

    //endregion


}