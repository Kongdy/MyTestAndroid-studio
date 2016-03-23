package com.example.hmyd.mytestandroid_studio.widgets;

/**
 * @author kongdy
 *         on 2016/3/23
 */
public interface GifAction {


    /**
     * gifDecoder 观察者
     * @param parseStatus 解码状态，是否成功
     * @param frameIndex 当前解码的帧数，当解码全部成功后，这里为-1
     */
    public void parseOk(boolean parseStatus,int frameIndex);
}
