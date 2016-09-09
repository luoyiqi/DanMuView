package com.licrafter.mylibrary;

/**
 * author: shell
 * date 16/9/4 下午5:37
 **/
public interface Proxy {
    public void prepareDraw(IDanMuItem danMuItem);
    public void releaseResource(IDanMuItem danMuItem);
}
