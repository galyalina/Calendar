package com.iotta.mydays;

/**
 * MVP model interface
 */
public interface BaseView<T> {

    void setPresenter(T presenter);

}
