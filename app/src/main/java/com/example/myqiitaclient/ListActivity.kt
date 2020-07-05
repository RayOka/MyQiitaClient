package com.example.myqiitaclient

import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.getSystemService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class ListActivity : AppCompatActivity() {
    private lateinit var mAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        mAdapter = ListAdapter(this, R.layout.list_item)

        val listView = findViewById<ListView>(R.id.list_view)
        listView.adapter = mAdapter

        val editText = findViewById<EditText>(R.id.edit_text) as EditText
        editText.setOnKeyListener(OnKeyListener())

        listView.onItemClickListener = OnItemClickListener()
    }

    private inner class ListAdapter(context: Context, resource: Int): ArrayAdapter<Item>(context, resource) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_item, null)
            }

            val imageView = convertView?.findViewById<ImageView>(R.id.imageView) as ImageView
            val itemTitleView = convertView.findViewById<TextView>(R.id.textView) as TextView
            val userNameView = convertView.findViewById<TextView>(R.id.textView2) as TextView

            imageView.setImageBitmap(null)

            val result = getItem(position)

            imageView?.let {
                if (result != null) {
                    Picasso.get().load(result.user?.profile_image_url).into(it)
                }
            }
            itemTitleView.text = result?.title
            userNameView.text = result?.user?.name

            return convertView
        }
    }

    private inner class OnKeyListener: View.OnKeyListener {

        override fun onKey(view: View, keyCode: Int, keyEvent: KeyEvent): Boolean {
            if (keyEvent.action != KeyEvent.ACTION_UP || keyCode != KeyEvent.KEYCODE_ENTER) {
                return false
            }

            val editText = view as EditText
            // キーボードを閉じる
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)

            var text = editText.text.toString()
            try {
                text = URLEncoder.encode(text, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                Log.e("", e.toString(), e)
                return true
            }

            if (!TextUtils.isEmpty(text)) {
                val request = QiitaClient.create().items(text)
                Log.d("RequestURL", request.request().url().toString())
                val item = object : retrofit2.Callback<List<Item>> {
                    override fun onResponse(call: Call<List<Item>>?, response: Response<List<Item>>?) {
                        mAdapter.clear()
                        response?.body()?.forEach {
                            mAdapter.add(it)
                            Log.d("responseData", it.body.toString())
                        }
                    }

                    override fun onFailure(call: Call<List<Item>>?, t: Throwable?) {
                        Log.d("TagFailure", t.toString())
                    }
                }
                request.enqueue(item)
            }
            return true
        }
    }

    private inner class OnItemClickListener: AdapterView.OnItemClickListener {
        override fun onItemClick(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val intent = Intent(this@ListActivity, DetailActivity::class.java)
            val result = mAdapter.getItem(position)
            intent.putExtra("URL", result?.url)
            startActivity(intent)
        }
    }
}