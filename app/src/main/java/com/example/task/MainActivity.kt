package com.example.task

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.BufferedReader
import java.io.File

class MainActivity : AppCompatActivity() {
    /*宣言*/
    var main_flg = true;
    var main_text = "" //メインリスト
    var sub_text = "" //フォルダ内データ
    var task_txt = "" //タスクデータ
    var folder_txt = "" //フォルダーデータ
    private var listView: ListView? = null //リストビューの宣言
    var btn_flg = false;
    var transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
    var list_fragment: ListFragment = ListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val task_add_btn = findViewById<FloatingActionButton>(R.id.task_add_btn) //タスク追加ボタン
        val folder_add_btn = findViewById<FloatingActionButton>(R.id.folder_add_btn)//フォルダー追加ボタン
        //未使用分のボタンを非表示
        task_add_btn.setVisibility(View.INVISIBLE)
        folder_add_btn.setVisibility(View.INVISIBLE)
            if(read_txt("CREATE_CHECK.txt") == "OK"){
                main_text = read_txt("MAIN_TEXT.txt")
                if(main_flg){
                    if(main_text != ""){
                        list(main_text,"LOAD")
                    }
                }
            }else{
                write_txt("MAIN_TEXT.txt","")
                write_txt("FOLDER_TEXT.txt","")
                write_txt("CREATE_CHECK.txt","OK")
            }

        /*データ追加画面へ遷移するボタン*/
        findViewById<FloatingActionButton>(R.id.add_btn).setOnClickListener{
            //タスク追加ボタンを活性化
            if(!btn_flg){
                task_add_btn.setVisibility(View.VISIBLE)
                folder_add_btn.setVisibility(View.VISIBLE)
                btn_flg = true
            }else{
                task_add_btn.setVisibility(View.INVISIBLE)
                folder_add_btn.setVisibility(View.INVISIBLE)
                btn_flg = false;
            }
        }

        /*タスクを追加する際の処理*/
        findViewById<FloatingActionButton>(R.id.task_add_btn).setOnClickListener{
            show_input_dialog(true,"タスク名を入力","タスクを作成しました")
        }

        /*フォルダを追加する際の処理*/
        findViewById<FloatingActionButton>(R.id.folder_add_btn).setOnClickListener{
            show_input_dialog(false,"フォルダ名を入力","フォルダを作成しました")
        }

        list("","INFO")


        /*データが追加画面から渡された場合の処理*/

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater
        //メニューのリソース選択
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            //作成ボタンを押したとき
            R.id.home -> {
                return true
            }
            //削除ボタンを押したとき
            R.id.reset -> {
                show_info_dialog("RESET","すべてのデータをリセットします","確定するには「OK」を押してください")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /*リスト関連*/
    fun list(text: String, type:String) {
        val arr = text.split(",")
        this.listView = findViewById(R.id.list)
        if(type == "LOAD"){
            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arr)
            listView!!.adapter = adapter

        }else if(type=="RESET"){
            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
            listView!!.adapter = adapter
        }else if(type=="INFO"){
            listView?.setOnItemClickListener { adapterView, _, position, _ ->
                val read_name = adapterView.getItemAtPosition(position) as String

            }
        }
    }

    /*フォルダーかファイルかを格納*/
    fun type(type:String,name:String){
        val txt_name = name +"_" + String  + ".txt"
    }

    /*タスク情報の作成*/
    fun task(text:String,type:String){
        if(type == "TXT_READ") { //読み込み
            task_txt = read_txt("TASK_TEXT.txt")
        }else if(type == "WRITE"){
            if(task_txt != "") {
                task_txt += "," + text
            }else{
                task_txt += text
            }
            write_txt("FOLDER_TEXT.txt",folder_txt)
        }else if(type == "SEARCH"){
        }
    }

    /*フォルダー情報の作成*/
    fun folder(text:String,type:String){
        if(type == "TXT_READ") { //読み込み
            folder_txt = read_txt("FOLDER_TEXT.txt")
        }else if(type == "WRITE"){
            if(folder_txt != "") {
                folder_txt += "," + text
            }else{
                folder_txt += text
            }
            write_txt("FOLDER_TEXT.txt",folder_txt)
        }else if(type == "SEARCH"){
        }

    }

    /*txtファイルで端末内に書き込む*/
    fun write_txt(file_name: String,data: String?){
        val str = data
        File(applicationContext.filesDir, file_name).writer().use {
            it.write(str)
        }
    }

    /*txtファイルを読み込む*/
    fun read_txt(read_name: String): String {
        val readFile = File(applicationContext.filesDir, read_name)
        if(readFile.exists()){
            return readFile.bufferedReader().use(BufferedReader::readText)
        }
        return ""
    }

    /*txtファイルをリセットする*/
    fun reset_txt(file_name: String){
        val str = ""
        File(applicationContext.filesDir, file_name).writer().use {
            it.write(str)
        }
    }

    /*入力用ダイアログ(type:true=タスク false=フォルダ,dialog_title:タイトル,submit_message:入力後に出力するメッセージ)*/
    fun show_input_dialog(type:Boolean,dialog_title:String,submit_message:String){
            val myedit = EditText(this)
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle(dialog_title)
            dialog.setView(myedit)
            dialog.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                // OKボタン押したときの処理
                val work = myedit.getText().toString()
                val check_work = Regex(work)
                if(type){
                    //タスクとして追加する
                        if(main_text != "") {
                            main_text += ",📚 " + work
                        }else{
                            main_text += "📚 " + work
                        }

                }else{
                    //フォルダとして追加
                        if(main_text != ""){
                            main_text += ",📁 " + work
                        }else{
                            main_text += "📁 " + work
                        }
                        folder(work,"WRITE")
                }

                    write_txt("MAIN_TEXT.txt",main_text)
                    list(main_text,"LOAD")
                    transaction.replace(R.id.include, list_fragment)
                    Toast.makeText(applicationContext, submit_message, Toast.LENGTH_SHORT).show() //完了メッセージの表示
            })
            dialog.setNegativeButton("キャンセル",null)
            dialog.show()
    }

    /*案内用ダイアログ(dialog_title:タイトル,dialog_text:メッセージ)*/
    fun show_info_dialog(type:String,dialog_title: String, dialog_text: String){
        AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                .setTitle(dialog_title)
                .setMessage(dialog_text)
                .setPositiveButton("OK", { dialog, which ->
                    if(type == "RESET"){
                        if(main_flg){
                            reset()
                        }
                    }
                })
                .setNegativeButton("キャンセル",null)
                .show()
    }

    /*すべてのデータをリセットする*/
    fun reset(){
        main_text = ""
        sub_text = ""
        reset_txt("MAIN_TEXT.txt")
        reset_txt("FOLDER_TEXT.txt")
        list("","RESET")
        transaction.replace(R.id.include, list_fragment)
    }






}


