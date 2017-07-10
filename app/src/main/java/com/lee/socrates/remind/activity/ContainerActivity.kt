package com.lee.socrates.remind.activity

import android.support.v4.app.Fragment
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.lee.socrates.remind.R
import com.lee.socrates.remind.util.Constant
import kotlinx.android.synthetic.main.activity_container.*

@Route(path = Constant.activityContainer)
class ContainerActivity : BaseActivity() {

    @Autowired
    @JvmField
    var title: String? = null
    @Autowired
    @JvmField
    var fragmentName: String? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_container
    }

    override fun init() {
        ARouter.getInstance().inject(this)
        initToolbar()
        loadFragment()
    }

    fun initToolbar() {
        if (title.isNullOrEmpty()) {
            toolbar.visibility=View.GONE
        } else{
            toolbar.visibility = View.VISIBLE
            toolbar.title = title
        }
    }

    fun loadFragment() {
        fragmentName ?: throw IllegalArgumentException("field fragmentName must not be null")
        fragmentName?.let {
            var f: Fragment = ARouter.getInstance().build("/remain/fragment/$fragmentName").navigation() as Fragment
            openFragment(f, fragmentName, false)
        }

    }

}
