package com.lee.library.view.tabswitchwidget;

import android.content.res.TypedArray;
import android.view.View;
import android.widget.TextView;

import com.lee.library.R;

/**
 * Created by lee on 2018/4/2.
 */

public class TwoLineTextTabStyle implements TabLayoutView.TabStyle {

    @Override
    public void parseAttributes(TypedArray typedArray) {

    }

    @Override
    public int getDefaultTabLayoutResId() {
        return R.layout.view_tab_twolinetext;
    }

    @Override
    public void initSingleTabView(View view, TabLayoutView.Tab tab, TabLayoutView baseTabLayoutView) {
        if (tab instanceof TwoLineTextTab) {
            TextView topTextView = (TextView) view.findViewById(R.id.tab_top_textview);
            TextView bottomTextView = (TextView) view.findViewById(R.id.tab_bottom_textview);
            topTextView.setText(((TwoLineTextTab) tab).topText);
            bottomTextView.setText(((TwoLineTextTab) tab).bottomText);
            if (tab.isActive) {
                topTextView.setTextColor(baseTabLayoutView.activeTextColor);
                bottomTextView.setTextColor(baseTabLayoutView.activeTextColor);
            } else {
                topTextView.setTextColor(baseTabLayoutView.inactiveTextColor);
                bottomTextView.setTextColor(baseTabLayoutView.inactiveTextColor);
            }
        } else {
            throw new IllegalArgumentException("the parameter tab not match the TwoLineTextTab");
        }
    }

    public static class TwoLineTextTab extends TabLayoutView.Tab {
        public String topText;
        public String bottomText;
    }

    public static class TabFactory {
        public static TabLayoutView.Tab getTwoLineTextTab(String topText, String bottomText) {
            TwoLineTextTab twoLineTextTab = new TwoLineTextTab();
            twoLineTextTab.tabViewLayoutId = R.layout.view_tab_twolinetext;
            twoLineTextTab.topText = topText;
            twoLineTextTab.bottomText = bottomText;
            return twoLineTextTab;
        }
    }
}
