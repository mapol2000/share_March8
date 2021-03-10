package gteamproject.shere.jdbc

import android.R
import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.widget.EditText
import android.widget.TextView
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class INSERTimg(val context: Context) : AsyncTask<String?, Void?, Void?>() {

    companion object{
        const val insertSQL ="insert into exPlus (user_id,file_num) values(?,?)"
    }

    override fun doInBackground(vararg params: String?): Void? {

        var uid = params[0].toString()
        var fnum = params[1].toString()

        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            conn = JDBCUtil.makeConn()
            pstmt = conn!!.prepareStatement(insertSQL)
            pstmt.setString(1, uid)
            pstmt.setString(2, fnum)

            var cnt = pstmt.executeUpdate()
        } catch (se: SQLException) {
            println("insertEmp 에서 오류발생!!")
            se.printStackTrace()
        }
        // println(id?.text.toString())

        return null
    }
}