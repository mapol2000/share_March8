package gteamproject.shere

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment


class Frag_Login : Fragment() {


    lateinit var userName : EditText
    lateinit var password : EditText
    lateinit var btnSignin : Button
    lateinit var chkRemember : CheckBox
    lateinit var textForgot : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_login, container, false)

        super.onCreate(savedInstanceState)
        userName = view.findViewById(R.id.userName)
        password = view.findViewById(R.id.password)
        btnSignin = view.findViewById(R.id.btnSignin)
        chkRemember = view.findViewById(R.id.chkRemember)
        textForgot = view.findViewById(R.id.textForgot)

        var prefs = requireActivity().getPreferences(0);
        val editor : SharedPreferences.Editor = prefs.edit()

        if(prefs.getBoolean("chkRemember",true)){
            userName.setText(prefs.getString("userName",""))
            chkRemember.isChecked = true
        }

       /* btnSignin.setOnClickListener {
            val inputName = userName.text.trim().toString()
            val inputPassword = password.text.trim().toString()

            if(inputName.equals(userName) && inputPassword.equals(password)) {
                val intent = Intent(activity, Main::class.java)
                startActivity(intent)
            }
            else {
                if (inputName.isNullOrEmpty()) {
                    Toast.makeText(activity, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    userName.requestFocus()
                    return@setOnClickListener
                }

                if(inputPassword.isNullOrEmpty()){
                    Toast.makeText(activity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    password.requestFocus()
                    return@setOnClickListener
                }
                // TODO: 체크박스 체크하고 로그인 성공 시 아이디 저장
                if(chkRemember.isChecked){
                    editor.putString("userName",inputName)
                    editor.putBoolean("chkRemember", chkRemember.isChecked)
                    editor.commit()
                }else{
                    editor.putString("userName","")
                    editor.putBoolean("chkRemember",false)
                    editor.commit()
                }

                // TODO: DB 연결 후 활성화
                //else {
                //    Toast.makeText(getActivity(), "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                //}
            }
        }*/

        return view
    }

}

