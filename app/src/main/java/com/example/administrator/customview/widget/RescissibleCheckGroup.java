package com.example.administrator.customview.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.administrator.customview.util.IdGenerator;

/**
 * Author:liuqi
 * Date:2017/9/7 15:40
 * Detail:可取消选中的单选列表
 */
public class RescissibleCheckGroup extends LinearLayout {
    private int mCheckedId = -1;
    private int mOldCheckedId = -1;
    private CheckedStateTracker checkedStateTracker;
    private PassThroughHierarchyChangeListener mPassThroughListener;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public RescissibleCheckGroup(Context context) {
        super(context);
        init();
    }

    public RescissibleCheckGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RescissibleCheckGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        checkedStateTracker = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    public void check(int id) {
        View view = findViewById(id);
        if (view instanceof CheckBox) {
            ((CheckBox) view).setChecked(true);
        }
    }

    public void clearCheck() {
        if (mCheckedId != -1) {
            mOldCheckedId = mCheckedId;
            CheckBox view = (CheckBox) findViewById(mCheckedId);
            view.setChecked(false);
            mCheckedId = -1;
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * <p>Interface definition for a callback to be invoked when the checked
     * radio button changed in this group.</p>
     */
    public interface OnCheckedChangeListener {
        /**
         * <p>Called when the checked radio button has changed. When the
         * selection is cleared, checkedId is -1.</p>
         *
         * @param group     the group in which the checked radio button has changed
         * @param checkedId the unique identifier of the newly checked radio button
         */
        public void onCheckedChanged(RescissibleCheckGroup group, @IdRes int checkedId);
    }

    public class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                mOldCheckedId = mCheckedId;
                mCheckedId = buttonView.getId();
                View oldCheckedView = findViewById(mOldCheckedId);
                if (oldCheckedView != null && oldCheckedView instanceof CheckBox) {
                    ((CheckBox) oldCheckedView).setChecked(false);
                }
                if (mOnCheckedChangeListener != null)
                    mOnCheckedChangeListener.onCheckedChanged(RescissibleCheckGroup.this, buttonView.getId());
            } else {
                if (buttonView.getId() == mOldCheckedId) {
                    mOldCheckedId = -1;
                    return;
                }
                mCheckedId = -1;
                if (mOnCheckedChangeListener != null)
                    mOnCheckedChangeListener.onCheckedChanged(RescissibleCheckGroup.this, buttonView.getId());
            }
        }
    }

    /**
     * <p>A pass-through listener acts upon the events and dispatches them
     * to another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.</p>
     */
    private class PassThroughHierarchyChangeListener implements
            OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        public void onChildViewAdded(View parent, View child) {
            if (parent == RescissibleCheckGroup.this && child instanceof CheckBox) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = IdGenerator.generate();
                    child.setId(id);
                }
                ((CheckBox) child).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                                  @Override
                                                                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                      checkedStateTracker.onCheckedChanged(buttonView, isChecked);
                                                                  }
                                                              }
                );
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void onChildViewRemoved(View parent, View child) {

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}
