package br.com.capsistema.marvel.ui.custom

import android.content.Context
import androidx.core.view.ViewCompat.dispatchNestedPreFling
import androidx.core.view.ViewCompat.dispatchNestedFling
import androidx.core.view.ViewCompat.isNestedScrollingEnabled
import androidx.core.view.ViewCompat.setNestedScrollingEnabled
import androidx.core.view.ViewParentCompat.onNestedFling
import androidx.core.view.ViewParentCompat.onNestedPreFling
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.NestedScrollingChild
import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View

/**
 * Custom CoordinatorLayout that works as a child for another CoordinatorLayout.
 * Copied from https://stackoverflow.com/a/37660246/2240976
 */
class NestedCoordinatorLayout : CoordinatorLayout, NestedScrollingChild {

    private var mChildHelper: NestedScrollingChildHelper? = null

    constructor(context: Context) : super(context) {
        mChildHelper = NestedScrollingChildHelper(this)
        isNestedScrollingEnabled = true
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mChildHelper = NestedScrollingChildHelper(this)
        isNestedScrollingEnabled = true
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mChildHelper = NestedScrollingChildHelper(this)
        isNestedScrollingEnabled = true
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        /* Enable the scrolling behavior of our own children */
        val tHandled = super.onStartNestedScroll(child, target, nestedScrollAxes)
        /* Enable the scrolling behavior of the parent's other children  */
        return startNestedScroll(nestedScrollAxes) || tHandled
    }

    override fun onStopNestedScroll(target: View) {
        /* Disable the scrolling behavior of our own children */
        super.onStopNestedScroll(target)
        /* Disable the scrolling behavior of the parent's other children  */
        stopNestedScroll()
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        val tConsumed = Array(2) { IntArray(2) }
        super.onNestedPreScroll(target, dx, dy, tConsumed[0])
        dispatchNestedPreScroll(dx, dy, tConsumed[1], null)
        consumed[0] = tConsumed[0][0] + tConsumed[1][0]
        consumed[1] = tConsumed[0][1] + tConsumed[1][1]
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int,
                       dxUnconsumed: Int, dyUnconsumed: Int) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null)
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        val tHandled = super.onNestedPreFling(target, velocityX, velocityY)
        return dispatchNestedPreFling(velocityX, velocityY) || tHandled
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        val tHandled = super.onNestedFling(target, velocityX, velocityY, consumed)
        return dispatchNestedFling(velocityX, velocityY, consumed) || tHandled
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper!!.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mChildHelper!!.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mChildHelper!!.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        mChildHelper!!.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mChildHelper!!.hasNestedScrollingParent()
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
                                      dyUnconsumed: Int, offsetInWindow: IntArray?): Boolean {
        return mChildHelper!!.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed, offsetInWindow)
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean {
        return mChildHelper!!.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return mChildHelper!!.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mChildHelper!!.dispatchNestedPreFling(velocityX, velocityY)
    }
}