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


class INSERTUSER(val cid: Context, val cpw:Context) : AsyncTask<Void?, Void?, Void?>() {

    var id : TextView? = null
    var pw : TextView? = null

    companion object{
        const val insertSQL ="insert into exUser (user_id,user_pw) values(?,?)"
    }

    override fun onPreExecute() {
        id = (cid as Activity).findViewById(gteamproject.shere.R.id.userName2)
        pw = (cpw as Activity).findViewById(gteamproject.shere.R.id.password2)
    }

    override fun doInBackground(vararg params: Void?): Void? {

        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            conn = JDBCUtil.makeConn()
            pstmt = conn!!.prepareStatement(insertSQL)
            pstmt.setString(1, id?.text.toString())
            pstmt.setString(2, pw?.text.toString())
            // pstmt.setString(2, emp.getFname())

            var cnt = pstmt.executeUpdate()
        } catch (se: SQLException) {
            println("insertEmp 에서 오류발생!!")
            se.printStackTrace()
        }
        // println(id?.text.toString())

        return null
    }
}