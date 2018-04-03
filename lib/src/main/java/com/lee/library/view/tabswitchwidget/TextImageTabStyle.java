package com.lee.library.view.tabswitchwidget;

import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lee.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 2018/4/2.
 */

public class TextImageTabStyle implements BaseTabLayoutView.TabStyle {


    @Override
    public void parseAttributes(TypedArray typedArray) {

    }

    @Override
    public int getDefaultTabLayoutResId() {
        return R.layout.view_tab_textimage;
    }

    @Override
    public void initSingleTabView(View view, BaseTabLayoutView.Tab tab, BaseTabLayoutView baseTabLayoutView) {
        if (tab instanceof TextImageTab) {

            TextView name = (TextView) view.findViewById(R.id.tab_name);
            ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
            name.setText(((TextImageTab) tab).name);
            if (tab.isActive) {
                name.setTextColor(baseTabLayoutView.activeTextColor);
                icon.setBackgroundResource(((TextImageTab) tab).activeIconResId);
            } else {
                name.setTextColor(baseTabLayoutView.inactiveTextColor);
                icon.setBackgroundResource(((TextImageTab) tab).inactiveIconResId);
            }
        } else {
            throw new IllegalArgumentException("the parameter tab not match the TextImageTab");
        }
    }


    public static class TextImageTab extends BaseTabLayoutView.Tab {
        public String name;
        public int activeIconResId;
        public int inactiveIconResId;
        public int textSize;
    }

    public static class TabFactory {
        /**
         * @param tabLayoutId       the layout id of tabview, could be 0, if set value 0, will use the default layout,
         *                          the layout include a Textview(id must be tab_name) and a Imageview(id must be tab_icon)
         * @param tabText           the text of the tab show
         * @param activeIconResId   the image when the tab is choiced
         * @param inactiveIconResId the image when the tab is unchoiced
         * @return
         */
        public static BaseTabLayoutView.Tab getTextImageTab(int tabLayoutId, String tabText, int activeIconResId, int inactiveIconResId) {
            return getTextImageTab(tabLayoutId, tabText, activeIconResId, inactiveIconResId, 0);
        }

        public static BaseTabLayoutView.Tab getTextImageTab(int tabLayoutId, String tabText, int activeIconResId, int inactiveIconResId, int textSize) {
            TextImageTab textImageTab = new TextImageTab();
            textImageTab.name = tabText;
            textImageTab.activeIconResId = activeIconResId;
            textImageTab.inactiveIconResId = inactiveIconResId;
            textImageTab.tabViewLayoutId = tabLayoutId;
            textImageTab.textSize = textSize;
            return textImageTab;
        }

        public static List<BaseTabLayoutView.Tab> getTabList(@NonNull String[] tabTexts, @Nullable int[] activeIconResIds,
                                                             @Nullable  int[] inactiveIconResIds, @Nullable int tabLayoutId) {
            List<BaseTabLayoutView.Tab> tabList = new ArrayList<>();
            for (int i = 0; i < tabTexts.length; i++) {
                TextImageTab textImageTab = new TextImageTab();
                textImageTab.name = tabTexts[i];
                if (activeIconResIds != null && activeIconResIds.length > i) {
                    textImageTab.activeIconResId = activeIconResIds[i];
                }
                if (inactiveIconResIds != null && inactiveIconResIds.length > i) {
                    textImageTab.inactiveIconResId = inactiveIconResIds[i];
                }
                textImageTab.tabViewLayoutId = tabLayoutId;
                tabList.add(textImageTab);
            }
            return tabList;
        }
    }
}
