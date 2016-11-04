package com.testography.am_mvp.ui.custom_views;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.testography.am_mvp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthPanel extends LinearLayout {

    public static final String TAG = "AuthPanel";
    public static final int LOGIN_STATE = 0;
    public static final int IDLE_STATE = 1;
    private int mCustomState = 1;

    @BindView(R.id.auth_card)
    CardView mAuthCard;

    @BindView(R.id.login_email_et)
    EditText mEmailEt;

    @BindView(R.id.login_password_et)
    EditText mPasswordEt;

    @BindView(R.id.login_btn)
    Button mLoginBtn;

    @BindView(R.id.show_catalog_btn)
    Button mShowCatalogBtn;

    public AuthPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        showViewFromState();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.state = mCustomState;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setCustomState(savedState.state);
    }

    public void setCustomState(int state) {
        mCustomState = state;
        showViewFromState();
    }

    private void showLoginState() {
        Animation animation = new ScaleAnimation(
                1.0f, 1.0f,
                0.3f, 1.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 1.0f
        );
        animation.setDuration(500);
        mAuthCard.startAnimation(animation);

        mAuthCard.setVisibility(VISIBLE);
        mShowCatalogBtn.setVisibility(GONE);
    }

    private void showIdleState() {
        Animation animation = new ScaleAnimation(
                1.0f, 1.0f,
                1.0f, 0.3f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 1.0f
        );
        animation.setDuration(500);
        mAuthCard.setAnimation(animation);

        mAuthCard.setVisibility(GONE);
        mShowCatalogBtn.setVisibility(VISIBLE);
    }

    private void showViewFromState() {
        if (mCustomState == LOGIN_STATE) {
            showLoginState();
        } else {
            showIdleState();
        }
    }

    public String getUserEmail() {
        return String.valueOf(mEmailEt.getText());
    }

    public String getUserPassword() {
        return String.valueOf(mPasswordEt.getText());
    }

    public boolean isIdle() {
        return mCustomState == IDLE_STATE;
    }

    static class SavedState extends BaseSavedState {

        private int state;

        private static final Parcelable.Creator<SavedState> CREATOR = new
                Parcelable.Creator<SavedState>() {

                    @Override
                    public SavedState createFromParcel(Parcel parcel) {
                        return new SavedState(parcel);
                    }

                    @Override
                    public SavedState[] newArray(int i) {
                        return new SavedState[i];
                    }
                };

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            state = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(state);
        }
    }
}
