package com.testography.am_mvp.di.components;

import com.squareup.picasso.Picasso;
import com.testography.am_mvp.di.modules.PicassoCacheModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = PicassoCacheModule.class)
@Singleton
public interface PicassoComponent {
    Picasso getPicasso();
}
