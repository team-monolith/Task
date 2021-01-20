package com.example.task

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private var listView: ListView? = null
    private var cnt = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var text = ""
        load_data() //メイン起動時にデータを読み込む

        /*データ追加画面へ遷移するボタン*/
        findViewById<Button>(R.id.add_btn).setOnClickListener{
            //val intent = Intent(this, List::class.java)
            //startActivity
            if(cnt != 0){
                text += "," +"テスト" + cnt.toString();
            }else{
                text = "テスト" + cnt.toString();
            }

            create_list(text)
            cnt++
        }

    }

    fun create_list(text:String) {
        val arr = text.split(",")
        this.listView = findViewById(R.id.list)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arr)
        listView!!.adapter = adapter
    }

    fun load_data(){

    }


}