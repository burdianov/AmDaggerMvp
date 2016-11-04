package com.testography.am_mvp.mvp.views;

public interface IView {
    void showMessage(String message);

    void showError(Throwable e);

    void showLoad();

    void hideLoad();
}
