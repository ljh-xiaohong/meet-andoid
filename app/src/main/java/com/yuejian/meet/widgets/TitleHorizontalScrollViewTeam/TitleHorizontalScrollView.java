package com.yuejian.meet.widgets.TitleHorizontalScrollViewTeam;

import android.content.Context;
import android.text.BoringLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;

public class TitleHorizontalScrollView extends HorizontalScrollView {

    private Observable<View> observable;

    private SelectAdapter adapter;
    private LinearLayout ll;


    public TitleHorizontalScrollView(Context context) {
        this(context, null);
    }

    public TitleHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        observable = new Observable<View>();
        initViews();
        ll=new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        addView(ll);
    }

    private void initViews() {
        if (adapter != null) {
            for (int i = 0; i < adapter.getcount(); i++) {
                View view = adapter.getView(i, this);
                ll.addView(view,i);
                if(view instanceof Observer){
                    observable.register((Observer) view);
                }

                if (view instanceof ItemTextView) {
                    ((ItemTextView) view).setObservable(observable);
                }
            }
        }
    }



    public void setAdapter(SelectAdapter adapter) {
        this.adapter = adapter;
        initViews();
    }



    public static abstract class SelectAdapter {
        public abstract int getcount();

        public abstract Object getItem(int i);

        public abstract long getItemId(int i);

        public abstract View getView(int i, ViewGroup viewGroup);
    }


    public class Observable<T> {
        List<Observer<T>> mObservers = new ArrayList<>();

        public void register(Observer<T> observer) {
            if (observer == null) {
                throw new NullPointerException("observer == null");
            }
            synchronized (this) {
                if (!mObservers.contains(observer))
                    mObservers.add(observer);
            }
        }

        public synchronized void unregister(Observer<T> observer) {
            mObservers.remove(observer);
        }

        public void notifyObservers(T data) {
            for (Observer<T> observer : mObservers) {
                observer.onUpdate(this, data);
            }
        }

    }

    public interface Observer<T> {
        void onUpdate(Observable<T> observable, T id);
    }

    ;
}


