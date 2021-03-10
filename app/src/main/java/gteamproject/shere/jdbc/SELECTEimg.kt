package gteamproject.shere.jdbc

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.TextView
import gteamproject.shere.R
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class SELECTEimg(val context: Context) : AsyncTask<String?, Void?, Void?>() {

    var result = ""
    var tv : TextView? = null

    companion object{
        const val selectSQL ="select employee_id, first_name, last_name from EMPLOYEES"
        const val selectOneSQL ="select * from EMPLOYEES where employee_id = ?"
        const val selectimgSQL ="select MAX(file_num) from exPlus where user_id = ?"
    }

    override fun onPreExecute() {
        tv = (context as Activity).findViewById(R.id.img_num)
    }
    override fun doInBackground(vararg params: String?): Void? {
        var uid = params[0].toString()

        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        var rs: ResultSet? = null
        var sb = StringBuilder()

        try{
            conn = JDBCUtil.makeConn()
            pstmt = conn!!.prepareStatement(selectimgSQL)
            pstmt.setString(1, uid)
            rs = pstmt.executeQuery()

            while(rs.next()){
                sb.append(rs.getString("MAX(file_num)"))
            }
            Log.d("jdbc", sb.toString())
        } catch (ex: Exception){
            ex.printStackTrace()
        }finally {
            JDBCUtil.destoryConn(conn, pstmt, rs)
        }
        result = sb.toString()

        return null
    }

    override fun onPostExecute(params: Void?) {
        tv?.setText(result)
    }
}