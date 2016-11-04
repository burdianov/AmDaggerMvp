package com.testography.am_mvp.ui.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.testography.am_mvp.BuildConfig;
import com.testography.am_mvp.R;
import com.testography.am_mvp.mvp.views.IView;
import com.testography.am_mvp.ui.fragments.AccountFragment;
import com.testography.am_mvp.ui.fragments.CatalogFragment;
import com.testography.am_mvp.ui.fragments.FavoritesFragment;
import com.testography.am_mvp.ui.fragments.NotificationsFragment;
import com.testography.am_mvp.ui.fragments.OrdersFragment;
import com.testography.am_mvp.utils.RoundedAvatarDrawable;

import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RootActivity extends AppCompatActivity implements IView, NavigationView
        .OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.coordinator_container)
    CoordinatorLayout mCoordinatorContainer;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;

    protected ProgressDialog mProgressDialog;

    FragmentManager mFragmentManager;

    private ArrayList<Integer> mNavSet = new ArrayList<>();

    private int mActiveNavItem = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        ButterKnife.bind(this);

        initToolbar();
        initDrawer();

        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new CatalogFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (mFragmentManager.getBackStackEntryCount() == 0) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.close_app)
                        .setMessage(R.string.are_you_sure)
                        .setPositiveButton(R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        finish();
                                    }
                                })
                        .setNegativeButton(R.string.no,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                }).show();
            } else {
                super.onBackPressed();
                int activeItem = 0;
                mNavSet.remove(mNavSet.size() - 1);
                if (mNavSet.size() > 0) {
                    activeItem = mNavSet.get(mNavSet.size() - 1);
                } else if (mNavSet.size() == 0) {
                    activeItem = 1;
                }
                mNavigationView.getMenu().getItem(activeItem).setChecked(true);
            }
        }
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        setupRoundedAvatar();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_account:
                fragment = new AccountFragment();
                mActiveNavItem = 0;
                break;
            case R.id.nav_catalog:
                fragment = new CatalogFragment();
                mActiveNavItem = 1;
                break;
            case R.id.nav_favorites:
                fragment = new FavoritesFragment();
                mActiveNavItem = 2;
                break;
            case R.id.nav_orders:
                fragment = new OrdersFragment();
                mActiveNavItem = 3;
                break;
            case R.id.nav_notifications:
                fragment = new NotificationsFragment();
                mActiveNavItem = 4;
                break;
        }
        if (fragment != null) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(String.valueOf(mActiveNavItem))
                    .commit();
        }
        mDrawer.closeDrawer(GravityCompat.START);
        mNavSet.add(mActiveNavItem);
        return true;
    }

    private void setupRoundedAvatar() {
        ImageView avatar = (ImageView) mNavigationView.getHeaderView(0)
                .findViewById(R.id.user_avatar_iv);
        InputStream resource = getResources().openRawResource(R.raw.user_avatar);
        Bitmap bitmap = BitmapFactory.decodeStream(resource);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            avatar.setBackgroundDrawable(new RoundedAvatarDrawable(bitmap));
        } else {
            avatar.setBackground(new RoundedAvatarDrawable(bitmap));
        }
    }

    //region ==================== IView ===================

    @Override
    public void showMessage(String message) {
        Snackbar.make(mCoordinatorContainer, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(Throwable e) {
        if (BuildConfig.DEBUG) {
            showMessage(e.getMessage());
            e.printStackTrace();
        } else {
            showMessage(getString(R.string.error_message));
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

    //endregion
}
