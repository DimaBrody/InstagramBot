package com.bot.insta.internal.conditions

import android.content.Context
import android.os.Build
import android.view.accessibility.AccessibilityNodeInfo
import com.bot.insta.data.variables.AppValues

class ButtonCondition(
    private val context: Context,
    private val node: AccessibilityNodeInfo
) : Condition<Int> {

    private val language: String
        get() = context.locale.language

    override fun invoke(ID: Int): Boolean {
        val text = node.text?.toString()
        val viewID = node.viewIdResourceName?.toString()
        val contentDesc = node.contentDescription?.toString()
        val className = node.className?.toString()

        return when {
            ID == BOTTOM_TAB && contentDesc.containsSafe(tabViewText) -> true
            ID == PROFILE_TAB && contentDesc.containsSafe(profileViewText) -> true
            ID == FOLLOWING_BUTTON && viewID.containsSafe(followingButtonText) -> true
            ID == SEARCH_EDITTEXT && viewID.containsSafe(searchEditText) -> true
            ID == SEARCH_ACTIONBAR && viewID.containsSafe(actionBarEditText) -> true
            ID == CURRENT_ACCOUNT && className.containsSafe(currentAccountText)
                    && text.containsSafe(AppValues.currentAccount) -> true
            ID == TITLE_HEADER && className.containsSafe(titleClassName) &&
                    viewID.containsSafe(titleViewID) -> true
            ID == UNFOLLOW_BUTTON && viewID.containsSafe(unSubscribeViewID)
                    && text.containsSafe(unSubscribeText) -> true
            ID == SUBSCRIBERS_BUTTON && viewID.containsSafe(subsribersButtonText) -> true
            ID == SUBSCRIBE_BUTTON && text.equalsSafe(subscribeButtonText) &&
                    className.containsSafe(currentAccountText) && viewID.containsSafe(
                unSubscribeViewID
            ) -> true
            ID == SCROLLABLE_VIEW && className.containsSafe(listViewScroll) -> true
            else -> false
        }

    }


    private val tabViewText: Array<String>
        get() = arrayOf("Поиск и интересное", "Search")

    private val unSubscribeViewID: String
        get() = "com.instagram.android:id/button"

    private val unSubscribeText: Array<String>
        get() = arrayOf("Подписки","Following")

    private val profileViewText: Array<String>
        get() = arrayOf("Профиль", "Profile")

    private val searchEditText: String
        get() = "com.instagram.android:id/action_bar_search_edit_text"

    private val actionBarEditText: String
        get() = "com"

    private val titleViewID: String
        get() = "com.instagram.android:id/action_bar_textview_title"

    private val titleClassName: String
        get() = "android.widget.TextView"

    private val currentAccountText: String
        get() = "TextView"

    private val subsribersButtonText: String
        get() = "com.instagram.android:id/row_profile_header_textview_followers_title"

    private val followingButtonText: String
        get() = "com.instagram.android:id/row_profile_header_textview_following_count"

    private val subscribeButtonText: Array<String>
        get() = arrayOf("Подписаться", "Follow")

    private val listViewScroll: String
        get() = "ListView"

    private fun Array<String>.checkNode(nodeText: String) = any {
        it.equals(nodeText, true)
    }

    private fun CharSequence?.containsSafe(other: CharSequence, ignoreCase: Boolean = true) =
        this?.contains(other, ignoreCase) ?: false

    private fun CharSequence?.containsSafe(other: Array<String>, ignoreCase: Boolean = true) =
        other.any {
            this.containsSafe(it, ignoreCase)
        }

    private fun CharSequence?.equalsSafe(other: Array<String>, ignoreCase: Boolean = true) =
        other.any {
            this.toString().toLowerCase() == it.toLowerCase()
        }

    private val Context.locale
        get() = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N)
            resources.configuration.locales.get(0)
        else
            resources.configuration.locale
}