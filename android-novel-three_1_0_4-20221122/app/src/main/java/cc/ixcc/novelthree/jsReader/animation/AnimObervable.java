package cc.ixcc.novelthree.jsReader.animation;

import java.util.Observable;

public class AnimObervable extends Observable {

    public void post(int status) {
        notifyObservers(status);// 通知观察
    }
}
