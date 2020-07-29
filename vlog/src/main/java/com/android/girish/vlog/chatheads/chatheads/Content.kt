package com.android.girish.vlog.chatheads.chatheads

import android.content.Context
import android.content.pm.PackageManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.girish.vlog.R
import com.facebook.rebound.SimpleSpringListener
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringSystem

class Content(context: Context): LinearLayout(context) {
    private val springSystem = SpringSystem.create()
    private val scaleSpring = springSystem.createSpring()

    var messagesView: RecyclerView
    var layoutManager = LinearLayoutManager(context)

    lateinit var messagesAdapter: ChatAdapter
    var vLogAdapter: VLogAdapter

    init {
        inflate(context, R.layout.chat_head_content, this)

        val logPrioritDropDown: Spinner = findViewById(R.id.log_priority_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                context,
                R.array.log_priority_names,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            logPrioritDropDown.adapter = adapter
        }

        logPrioritDropDown.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("AVINASH", "AVINASH: Nothing has been selected in spinner");
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("AVINASH", String.format("AVINASH: %s has been selected in spinner.",
                        parent?.getItemAtPosition(position).toString()))
            }
        }

        messagesView = findViewById(R.id.events)
        messagesView.layoutManager = layoutManager

        //vLogAdapter = VLogAdapter(GenreDataFactory.generateLogs())
        vLogAdapter = VLogAdapter(mutableListOf())

        messagesView.adapter = vLogAdapter

        val editText: EditText = findViewById(R.id.editText)
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(constraint: CharSequence?, start: Int, before: Int, count: Int) {
                vLogAdapter.setFilteringOn(VLogAdapter.FILTERING_ON_TAG_KEYWORD)
                vLogAdapter.filter.filter(constraint)
            }

        })

        scaleSpring.addListener(object : SimpleSpringListener() {
            override fun onSpringUpdate(spring: Spring) {
                scaleX = spring.currentValue.toFloat()
                scaleY = spring.currentValue.toFloat()
            }
        })
        scaleSpring.springConfig = SpringConfigs.CONTENT_SCALE

        scaleSpring.currentValue = 0.0
    }

    fun setInfo(chatHead: ChatHead) {
        val list = ArrayList<String>()
        list.add("new list")
        list.add("girish")
        messagesAdapter.messages = list
        messagesAdapter.notifyDataSetChanged()
        messagesView.scrollToPosition(messagesAdapter.messages.lastIndex)
    }

    private fun isAppInstalled(context: Context, packageName: String): Boolean {
        val pm = context.packageManager
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            return true
        } catch (ignored: PackageManager.NameNotFoundException) {
        }

        return false
    }

    private fun isAppEnabled(context: Context, packageName: String): Boolean {
        var appStatus = false
        try {
            val ai = context.packageManager.getApplicationInfo(packageName, 0)
            if (ai != null) {
                appStatus = ai.enabled
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return appStatus
    }

    fun hideContent() {
        OverlayService.instance.chatHeads.handler.removeCallbacks(
            OverlayService.instance.chatHeads.showContentRunnable)

        scaleSpring.endValue = 0.0

        val anim = AlphaAnimation(1.0f, 0.0f)
        anim.duration = 200
        anim.repeatMode = Animation.RELATIVE_TO_SELF
        startAnimation(anim)
    }

    fun showContent() {
        scaleSpring.endValue = 1.0

        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 100
        anim.repeatMode = Animation.RELATIVE_TO_SELF
        startAnimation(anim)
    }

    fun addLog(vlog: VLogModel) {
        vLogAdapter.addLog(vlog)
    }
}